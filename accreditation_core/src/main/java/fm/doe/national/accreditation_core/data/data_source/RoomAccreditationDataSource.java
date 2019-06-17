package fm.doe.national.accreditation_core.data.data_source;

import android.content.Context;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.BuildConfig;
import fm.doe.national.accreditation_core.data.model.AccreditationSurvey;
import fm.doe.national.accreditation_core.data.model.Answer;
import fm.doe.national.accreditation_core.data.model.Category;
import fm.doe.national.accreditation_core.data.model.Criteria;
import fm.doe.national.accreditation_core.data.model.Standard;
import fm.doe.national.accreditation_core.data.model.SubCriteria;
import fm.doe.national.accreditation_core.data.model.mutable.MutableAccreditationSurvey;
import fm.doe.national.accreditation_core.data.persistence.AccreditationDatabase;
import fm.doe.national.accreditation_core.data.persistence.dao.AnswerDao;
import fm.doe.national.accreditation_core.data.persistence.dao.PhotoDao;
import fm.doe.national.accreditation_core.data.persistence.dao.SurveyDao;
import fm.doe.national.accreditation_core.data.persistence.entity.RoomAccreditationSurvey;
import fm.doe.national.accreditation_core.data.persistence.entity.RoomAnswer;
import fm.doe.national.accreditation_core.data.persistence.entity.RoomCategory;
import fm.doe.national.accreditation_core.data.persistence.entity.RoomCriteria;
import fm.doe.national.accreditation_core.data.persistence.entity.RoomPhoto;
import fm.doe.national.accreditation_core.data.persistence.entity.RoomStandard;
import fm.doe.national.accreditation_core.data.persistence.entity.RoomSubCriteria;
import fm.doe.national.accreditation_core.data.persistence.entity.relative.RelativeRoomSurvey;
import fm.doe.national.core.data.data_source.DataSourceImpl;
import fm.doe.national.core.data.model.Photo;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.model.mutable.MutablePhoto;
import fm.doe.national.core.preferences.GlobalPreferences;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class RoomAccreditationDataSource extends DataSourceImpl implements AccreditationDataSource {

    private static final String DATABASE_NAME = BuildConfig.APPLICATION_ID + ".database";
    private static final String TEMPLATE_DATABASE_NAME = BuildConfig.APPLICATION_ID + ".template_database";

    private final SurveyDao surveyDao;
    private final AnswerDao answerDao;
    private final PhotoDao photoDao;

    private final AccreditationDatabase templateDatabase;
    private final AccreditationDatabase database;

    private final GlobalPreferences globalPreferences;

    public RoomAccreditationDataSource(Context applicationContext, GlobalPreferences globalPreferences) {
        super(applicationContext, globalPreferences);

        this.globalPreferences = globalPreferences;
        database = Room.databaseBuilder(applicationContext, AccreditationDatabase.class, DATABASE_NAME).build();
        templateDatabase = Room.databaseBuilder(applicationContext, AccreditationDatabase.class, TEMPLATE_DATABASE_NAME).build();
        surveyDao = database.getSurveyDao();
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
            saveSurvey(templateDatabase, (AccreditationSurvey) survey, false);
        });
    }

    private long saveSurvey(AccreditationDatabase database, AccreditationSurvey accreditationSurvey, boolean shouldCreateAnswers) {
        RoomAccreditationSurvey roomSurvey = new RoomAccreditationSurvey(accreditationSurvey);
        roomSurvey.uid = 0;
        long id = database.getSurveyDao().insert(roomSurvey);
        if (accreditationSurvey.getCategories() != null) {
            saveCategories(database, accreditationSurvey.getCategories(), id, shouldCreateAnswers);
        }
        return id;
    }

    private void saveCategories(AccreditationDatabase database,
                                List<? extends Category> categories,
                                long surveyId,
                                boolean shouldCreateAnswers) {
        for (Category category : categories) {
            RoomCategory roomCategory = new RoomCategory(category);
            roomCategory.uid = 0;
            roomCategory.surveyId = surveyId;
            long id = database.getCategoryDao().insert(roomCategory);
            if (category.getStandards() != null) {
                saveStandards(database, category.getStandards(), id, shouldCreateAnswers);
            }
        }
    }

    private void saveStandards(AccreditationDatabase database,
                               List<? extends Standard> standards,
                               long categoryId,
                               boolean shouldCreateAnswers) {
        for (Standard standard : standards) {
            RoomStandard roomStandard = new RoomStandard(standard);
            roomStandard.uid = 0;
            roomStandard.categoryId = categoryId;
            long id = database.getStandardDao().insert(roomStandard);
            if (standard.getCriterias() != null) {
                saveCriterias(database, standard.getCriterias(), id, shouldCreateAnswers);
            }
        }
    }

    private void saveCriterias(AccreditationDatabase database,
                               List<? extends Criteria> criterias,
                               long standardId,
                               boolean shouldCreateAnswers) {
        for (Criteria criteria : criterias) {
            RoomCriteria roomCriteria = new RoomCriteria(criteria);
            roomCriteria.uid = 0;
            roomCriteria.standardId = standardId;
            long id = database.getCriteriaDao().insert(roomCriteria);
            if (criteria.getSubCriterias() != null) {
                saveSubCriterias(database, criteria.getSubCriterias(), id, shouldCreateAnswers);
            }
        }
    }

    private void saveSubCriterias(AccreditationDatabase database,
                                  List<? extends SubCriteria> subCriterias,
                                  long criteriaId,
                                  boolean shouldCreateAnswers) {
        for (SubCriteria subCriteria : subCriterias) {
            RoomSubCriteria roomSubCriteria = new RoomSubCriteria(subCriteria);
            roomSubCriteria.uid = 0;
            roomSubCriteria.criteriaId = criteriaId;
            long id = database.getSubcriteriaDao().insert(roomSubCriteria);
            Answer answer = subCriteria.getAnswer();

            if (answer != null) {
                RoomAnswer roomAnswer = new RoomAnswer(answer);
                roomAnswer.subCriteriaId = id;
                long answerId = database.getAnswerDao().insert(roomAnswer);

                if (answer.getPhotos() != null) {
                    savePhotos(database, answer.getPhotos(), answerId);
                }
            } else if (shouldCreateAnswers) {
                database.getAnswerDao().insert(new RoomAnswer(id));
            }
        }
    }

    private void savePhotos(AccreditationDatabase database,
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
                .map(RelativeRoomSurvey::toMutableSurvey);
    }

    @Override
    public Single<Survey> loadSurvey(long surveyId) {
        return Single.fromCallable(() -> surveyDao.getFilledById(surveyId))
                .map(RelativeRoomSurvey::toMutableSurvey);
    }

    @Override
    public Single<List<Survey>> loadAllSurveys() {
        return Single.fromCallable(() -> surveyDao.getAllFilled(globalPreferences.getAppRegion()))
                .flatMapObservable(Observable::fromIterable)
                .map(RelativeRoomSurvey::toMutableSurvey)
                .toList()
                .map(list -> new ArrayList<>(list));
    }

    @Override
    public Single<Survey> createSurvey(String schoolId, String schoolName, Date date) {
        return getTemplateSurvey()
                .flatMap(survey -> {
                    MutableAccreditationSurvey mutableSurvey = new MutableAccreditationSurvey((AccreditationSurvey) survey);
                    mutableSurvey.setId(0);
                    mutableSurvey.setSchoolName(schoolName);
                    mutableSurvey.setSchoolId(schoolId);
                    mutableSurvey.setDate(date);
                    long id = saveSurvey(database, mutableSurvey, true);
                    return loadSurvey(id);
                });
    }

    @Override
    public Completable deleteSurvey(long surveyId) {
        return Completable.fromAction(() -> surveyDao.deleteById(surveyId));
    }

    @Override
    public Single<Answer> updateAnswer(Answer answer, long subCriteriaId) {
        return Single.fromCallable(() -> {
            RoomAnswer existingAnswer = answerDao.getAllForSubCriteriaWithId(subCriteriaId).get(0);
            existingAnswer.comment = answer.getComment();
            existingAnswer.state = answer.getState();
            answerDao.update(existingAnswer);
            return answerDao.getFilledById(existingAnswer.uid).toMutableAnswer();
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
                            .andThen(Single.fromCallable(() -> answerDao.getFilledById(mutableAnswer.getId()).toMutableAnswer()));
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
        return Completable.fromAction(surveyDao::deleteAll);
    }

    @Override
    public Completable createPartiallySavedSurvey(Survey survey) {
        return Completable.fromAction(() -> saveSurvey(database, (AccreditationSurvey) survey, true));
    }
}
