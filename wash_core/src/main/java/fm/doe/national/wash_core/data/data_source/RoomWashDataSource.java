package fm.doe.national.wash_core.data.data_source;

import android.content.Context;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fm.doe.national.core.data.data_source.DataSourceImpl;
import fm.doe.national.core.data.model.Photo;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.model.mutable.MutablePhoto;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.wash_core.BuildConfig;
import fm.doe.national.wash_core.data.model.Answer;
import fm.doe.national.wash_core.data.model.Group;
import fm.doe.national.wash_core.data.model.Question;
import fm.doe.national.wash_core.data.model.SubGroup;
import fm.doe.national.wash_core.data.model.WashSurvey;
import fm.doe.national.wash_core.data.model.mutable.MutableWashSurvey;
import fm.doe.national.wash_core.data.persistence.WashDatabase;
import fm.doe.national.wash_core.data.persistence.dao.AnswerDao;
import fm.doe.national.wash_core.data.persistence.dao.PhotoDao;
import fm.doe.national.wash_core.data.persistence.entity.RoomAnswer;
import fm.doe.national.wash_core.data.persistence.entity.RoomGroup;
import fm.doe.national.wash_core.data.persistence.entity.RoomPhoto;
import fm.doe.national.wash_core.data.persistence.entity.RoomQuestion;
import fm.doe.national.wash_core.data.persistence.entity.RoomSubGroup;
import fm.doe.national.wash_core.data.persistence.entity.RoomWashSurvey;
import fm.doe.national.wash_core.data.persistence.entity.relative.RelativeRoomSurvey;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class RoomWashDataSource extends DataSourceImpl implements WashDataSource {

    private static final String DATABASE_NAME = BuildConfig.APPLICATION_ID + ".database";
    private static final String TEMPLATE_DATABASE_NAME = BuildConfig.APPLICATION_ID + ".template_database";

    private final AnswerDao answerDao;
    private final PhotoDao photoDao;

    private final WashDatabase templateDatabase;
    private final WashDatabase database;

    private final GlobalPreferences globalPreferences;

    public RoomWashDataSource(Context applicationContext, GlobalPreferences globalPreferences) {
        super(applicationContext, globalPreferences);
        this.globalPreferences = globalPreferences;
        database = Room.databaseBuilder(applicationContext, WashDatabase.class, DATABASE_NAME).build();
        templateDatabase = Room.databaseBuilder(applicationContext, WashDatabase.class, TEMPLATE_DATABASE_NAME).build();
        answerDao = database.getAnswerDao();
        photoDao = database.getPhotoDao();
    }

    public void closeConnections() {
        super.closeConnections();
        database.close();
        templateDatabase.close();
    }

    @Override
    public Completable rewriteTemplateSurvey(Survey survey) {
        return Completable.fromAction(() -> {
            templateDatabase.getSurveyDao().deleteAllForAppRegion(survey.getAppRegion());
            saveSurvey(templateDatabase, (WashSurvey) survey, false);
        });
    }

    private long saveSurvey(WashDatabase database, WashSurvey washSurvey, boolean shouldCreateAnswers) {
        RoomWashSurvey roomSurvey = new RoomWashSurvey(washSurvey);
        roomSurvey.uid = 0;
        long id = database.getSurveyDao().insert(roomSurvey);
        if (washSurvey.getGroups() != null) {
            saveGroups(database, washSurvey.getGroups(), id, shouldCreateAnswers);
        }
        return id;
    }

    private void saveGroups(WashDatabase database,
                            List<? extends Group> groups,
                            long surveyId,
                            boolean shouldCreateAnswers) {
        groups.forEach(group -> {
            RoomGroup roomGroup = new RoomGroup(group);
            roomGroup.uid = 0;
            roomGroup.surveyId = surveyId;
            long id = database.getGroupDao().insert(roomGroup);

            if (group.getSubGroups() != null) {
                saveSubGroups(database, group.getSubGroups(), id, shouldCreateAnswers);
            }
        });
    }

    private void saveSubGroups(WashDatabase database,
                               List<? extends SubGroup> subGroups,
                               long groupId,
                               boolean shouldCreateAnswers) {
        subGroups.forEach(subGroup -> {
            RoomSubGroup roomSubGroup = new RoomSubGroup(subGroup);
            roomSubGroup.uid = 0;
            roomSubGroup.groupId = groupId;
            long id = database.getSubGroupDao().insert(roomSubGroup);

            if (subGroup.getQuestions() != null) {
                saveQuestions(database, subGroup.getQuestions(), id, shouldCreateAnswers);
            }
        });
    }

    private void saveQuestions(WashDatabase database,
                               List<? extends Question> questions,
                               long subGroupId,
                               boolean shouldCreateAnswers) {
        questions.forEach(question -> {
            RoomQuestion roomQuestion = new RoomQuestion(question);
            roomQuestion.uid = 0;
            roomQuestion.subGroupId = subGroupId;
            long id = database.getQuestionDao().insert(roomQuestion);
            Answer questionAnswer = question.getAnswer();

            if (questionAnswer != null) {
                saveAnswer(database, id, questionAnswer);
            } else if (shouldCreateAnswers) {
                database.getAnswerDao().insert(new RoomAnswer(id));
            }
        });
    }

    private void saveAnswer(WashDatabase database, long questionId, Answer answer) {
        RoomAnswer roomAnswer = new RoomAnswer(answer);
        roomAnswer.questionId = questionId;
        long answerId = database.getAnswerDao().insert(roomAnswer);

        if (answer.getPhotos() != null) {
            savePhotos(database, answer.getPhotos(), answerId);
        }
    }

    @Override
    public Single<Answer> createAnswer(Answer answer, long questionId) {
        return Single.fromCallable(() -> {
            saveAnswer(database, questionId, answer);
            return answer;
        });
    }

    private void savePhotos(WashDatabase database,
                            List<? extends Photo> photos,
                            long answerId) {
        photos.forEach(photo -> {
            RoomPhoto roomPhoto = new RoomPhoto(photo);
            roomPhoto.answerId = answerId;
            database.getPhotoDao().insert(roomPhoto);
        });
    }

    @Override
    public Single<Survey> getTemplateSurvey() {
        return Single.fromCallable(() -> templateDatabase.getSurveyDao().getFirstFilled(globalPreferences.getAppRegion()))
                .map(RelativeRoomSurvey::toMutable);
    }

    @Override
    public Single<Survey> loadSurvey(long surveyId) {
        return Single.fromCallable(() -> database.getSurveyDao().getFilledById(surveyId))
                .map(RelativeRoomSurvey::toMutable);
    }

    @Override
    public Single<List<Survey>> loadAllSurveys() {
        return Single.fromCallable(() -> database.getSurveyDao().getAllFilled(globalPreferences.getAppRegion()))
                .flatMapObservable(Observable::fromIterable)
                .map(RelativeRoomSurvey::toMutable)
                .toList()
                .map(list -> new ArrayList<>(list));
    }

    @Override
    public Single<List<Survey>> loadSurveys(String schoolId, AppRegion appRegion) {
        return Single.fromCallable(() -> database.getSurveyDao().getBySchoolIdAndRegion(schoolId, appRegion))
                .flatMapObservable(Observable::fromIterable)
                .map(MutableWashSurvey::new)
                .toList()
                .map(list -> new ArrayList<>(list));
    }

    @Override
    public Single<Survey> createSurvey(String schoolId, String schoolName, Date createDate, Date surveyDate) {
        return getTemplateSurvey()
                .flatMap(survey -> {
                    MutableWashSurvey mutableSurvey = new MutableWashSurvey((WashSurvey) survey);
                    mutableSurvey.setId(0);
                    mutableSurvey.setSchoolName(schoolName);
                    mutableSurvey.setSchoolId(schoolId);
                    mutableSurvey.setCreateDate(createDate);
                    mutableSurvey.setSurveyDate(surveyDate);
                    long id = saveSurvey(database, mutableSurvey, true);
                    return loadSurvey(id);
                });
    }

    @Override
    public Completable deleteSurvey(long surveyId) {
        return Completable.fromAction(() -> database.getSurveyDao().deleteById(surveyId));
    }

    @Override
    public Single<Answer> updateAnswer(Answer answer) {
        return Single.fromCallable(() -> {
            RoomAnswer existingAnswer = answerDao.getById(answer.getId());
            existingAnswer.comment = answer.getComment();
            existingAnswer.binaryAnswerState = answer.getBinaryAnswerState();
            existingAnswer.ternaryAnswerState = answer.getTernaryAnswerState();
            existingAnswer.items = answer.getItems();
            existingAnswer.location = answer.getLocation();
            existingAnswer.variants = answer.getVariants();
            existingAnswer.inputText = answer.getInputText();
            answerDao.update(existingAnswer);
            return answerDao.getFilledById(existingAnswer.uid).toMutable();
        })
                .flatMap(mutableAnswer -> {
                    List<MutablePhoto> existingPhotos = mutableAnswer.getPhotos();
                    List<MutablePhoto> expectedPhotos = answer.getPhotos() == null ? new ArrayList<>() : answer.getPhotos().stream()
                            .map(MutablePhoto::new).collect(Collectors.toList());
                    // Note: update photo === delete + create
                    List<MutablePhoto> photosToDelete = new ArrayList<>();
                    List<MutablePhoto> photosToCreate = new ArrayList<>();
                    List<MutablePhoto> photosToUpdate = new ArrayList<>();

                    for (MutablePhoto existingPhoto : existingPhotos) {
                        Optional<MutablePhoto> updatedPhoto = expectedPhotos.stream()
                                .filter(p -> p.getId() == existingPhoto.getId())
                                .findFirst();

                        if (!updatedPhoto.isPresent()) {
                            photosToDelete.add(existingPhoto);
                            continue;
                        }

                        expectedPhotos.remove(updatedPhoto.get());

                        if (!existingPhoto.equals(updatedPhoto.get())) {
                            photosToUpdate.add(updatedPhoto.get());
                        }
                    }

                    photosToCreate.addAll(expectedPhotos);

                    return updatePhotos(photosToUpdate)
                            .andThen(deletePhotos(photosToDelete))
                            .andThen(createPhotos(photosToCreate, mutableAnswer.getId()))
                            .andThen(Single.fromCallable(() -> answerDao.getFilledById(mutableAnswer.getId()).toMutable()));
                });
    }

    private Completable updatePhotos(List<MutablePhoto> photos) {
        return Observable.fromIterable(photos)
                .map(photo -> {
                    photoDao.update(new RoomPhoto(photo));
                    return photo;
                })
                .toList()
                .ignoreElement();
    }

    private Completable deletePhotos(List<MutablePhoto> photos) {
        return Observable.fromIterable(photos)
                .map(photo -> {
                    photoDao.deleteById(photo.getId());
                    return photo;
                })
                .toList()
                .ignoreElement();
    }

    private Completable createPhotos(List<MutablePhoto> photos, long answerId) {
        return Observable.fromIterable(photos)
                .map(photo -> {
                    RoomPhoto roomPhoto = new RoomPhoto(photo);
                    roomPhoto.answerId = answerId;
                    photoDao.insert(roomPhoto);
                    return photo;
                })
                .toList()
                .ignoreElement();
    }

    @Override
    public Single<Photo> createPhoto(Photo photo, long answerId) {
        return Single.fromCallable(() -> {
            RoomPhoto roomPhoto = new RoomPhoto(photo);
            roomPhoto.answerId = answerId;
            long photoId = photoDao.insert(roomPhoto);
            return new MutablePhoto(photoDao.getById(photoId));
        });
    }

    @Override
    public Completable deletePhoto(long photoId) {
        return Completable.fromAction(() -> photoDao.deleteById(photoId));
    }

    @Override
    public Completable deleteCreatedSurveys() {
        return Completable.fromAction(database.getSurveyDao()::deleteAll);
    }

    @Override
    public Completable createPartiallySavedSurvey(Survey survey) {
        return Completable.fromAction(() -> saveSurvey(database, (WashSurvey) survey, true));
    }

    @Override
    public void updateSurvey(Survey survey) {
        database.getSurveyDao().update(new RoomWashSurvey((WashSurvey) survey));
    }
}
