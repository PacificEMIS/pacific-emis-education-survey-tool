package fm.doe.national.data.data_source;

import android.content.Context;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fm.doe.national.BuildConfig;
import fm.doe.national.data.model.Answer;
import fm.doe.national.data.model.Category;
import fm.doe.national.data.model.Criteria;
import fm.doe.national.data.model.Photo;
import fm.doe.national.data.model.School;
import fm.doe.national.data.model.Standard;
import fm.doe.national.data.model.SubCriteria;
import fm.doe.national.data.model.Survey;
import fm.doe.national.data.model.mutable.MutableAnswer;
import fm.doe.national.data.model.mutable.MutablePhoto;
import fm.doe.national.data.model.mutable.MutableSurvey;
import fm.doe.national.data.persistence.AppDatabase;
import fm.doe.national.data.persistence.dao.AnswerDao;
import fm.doe.national.data.persistence.dao.PhotoDao;
import fm.doe.national.data.persistence.dao.SurveyDao;
import fm.doe.national.data.persistence.entity.RoomAnswer;
import fm.doe.national.data.persistence.entity.RoomCategory;
import fm.doe.national.data.persistence.entity.RoomCriteria;
import fm.doe.national.data.persistence.entity.RoomPhoto;
import fm.doe.national.data.persistence.entity.RoomSchool;
import fm.doe.national.data.persistence.entity.RoomStandard;
import fm.doe.national.data.persistence.entity.RoomSubCriteria;
import fm.doe.national.data.persistence.entity.RoomSurvey;
import fm.doe.national.app_support.utils.CollectionUtils;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class RoomDataSource implements DataSource {

    private static final String DATABASE_NAME = BuildConfig.APPLICATION_ID + ".database";
    private static final String TEMPLATE_DATABASE_NAME = BuildConfig.APPLICATION_ID + ".template_database";

    private final SurveyDao surveyDao;
    private final AnswerDao answerDao;
    private final PhotoDao photoDao;

    private final AppDatabase templateDatabase;
    private final AppDatabase database;

    public RoomDataSource(Context applicationContext) {
        database = Room.databaseBuilder(applicationContext, AppDatabase.class, DATABASE_NAME).build();
        templateDatabase = Room.databaseBuilder(applicationContext, AppDatabase.class, TEMPLATE_DATABASE_NAME).build();
        surveyDao = database.getSurveyDao();
        answerDao = database.getAnswerDao();
        photoDao = database.getPhotoDao();
    }

    public void closeConnections() {
        database.close();
        templateDatabase.close();
    }

    @Override
    public Single<List<School>> loadSchools() {
        return Single.fromCallable(templateDatabase.getSchoolDao()::getAll)
                .map(roomSchools -> new ArrayList<>(roomSchools));
    }

    @Override
    public Completable rewriteAllSchools(List<School> schools) {
        return Observable.fromIterable(schools)
                .map(RoomSchool::new)
                .toList()
                .flatMapCompletable(roomSchools -> Completable.fromAction(() -> {
                    templateDatabase.getSchoolDao().deleteAll();
                    templateDatabase.getSchoolDao().insert(roomSchools);
                }));
    }

    @Override
    public Completable rewriteTemplateSurvey(Survey survey) {
        return Completable.fromAction(() -> {
            templateDatabase.getSurveyDao().deleteAll();
            saveSurvey(templateDatabase, survey, false);
        });
    }

    private long saveSurvey(AppDatabase database, Survey survey, boolean shouldCreateAnswers) {
        RoomSurvey roomSurvey = new RoomSurvey(survey);
        roomSurvey.uid = 0;
        long id = database.getSurveyDao().insert(roomSurvey);
        if (survey.getCategories() != null) {
            saveCategories(database, survey.getCategories(), id, shouldCreateAnswers);
        }
        return id;
    }

    private void saveCategories(AppDatabase database,
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

    private void saveStandards(AppDatabase database,
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

    private void saveCriterias(AppDatabase database,
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

    private void saveSubCriterias(AppDatabase database,
                                  List<? extends SubCriteria> subCriterias,
                                  long criteriaId,
                                  boolean shouldCreateAnswers) {
        for (SubCriteria subCriteria : subCriterias) {
            RoomSubCriteria roomSubCriteria = new RoomSubCriteria(subCriteria);
            roomSubCriteria.uid = 0;
            roomSubCriteria.criteriaId = criteriaId;
            long id = database.getSubcriteriaDao().insert(roomSubCriteria);
            if (shouldCreateAnswers) {
                database.getAnswerDao().insert(new RoomAnswer(id));
            }
        }
    }

    @Override
    public Single<Survey> getTemplateSurvey() {
        return Single.fromCallable(() -> templateDatabase.getSurveyDao().getFirstFilled())
                .map(MutableSurvey::new);
    }

    @Override
    public Single<Survey> loadSurvey(long surveyId) {
        return Single.fromCallable(() -> surveyDao.getFilledById(surveyId))
                .map(MutableSurvey::new);
    }

    @Override
    public Single<List<Survey>> loadAllSurveys() {
        return Single.fromCallable(surveyDao::getAllFilled)
                .flatMapObservable(Observable::fromIterable)
                .map(MutableSurvey::new)
                .toList()
                .map(list -> new ArrayList<>(list));
    }

    @Override
    public Single<Survey> createSurvey(String schoolId, String schoolName, Date date) {
        return getTemplateSurvey()
                .flatMap(survey -> {
                    MutableSurvey mutableSurvey = new MutableSurvey(survey);
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
            return new MutableAnswer(answerDao.getFilledById(existingAnswer.uid));
        })
                .flatMap(mutableAnswer -> {
                    List<MutablePhoto> existingPhotos = mutableAnswer.getPhotos();
                    List<MutablePhoto> expectedPhotos = CollectionUtils.map(answer.getPhotos(), MutablePhoto::new);
                    // Note: update photo === delete + create
                    List<MutablePhoto> photosToDelete = new ArrayList<>();
                    List<MutablePhoto> photosToCreate = new ArrayList<>();
                    List<MutablePhoto> photosToUpdate = new ArrayList<>();

                    for (MutablePhoto existingPhoto : existingPhotos) {
                        MutablePhoto updatedPhoto = CollectionUtils.firstWhere(
                                expectedPhotos,
                                p -> p.getId() == existingPhoto.getId()
                        );

                        if (updatedPhoto == null) {
                            photosToDelete.add(existingPhoto);
                            continue;
                        }

                        expectedPhotos.remove(updatedPhoto);

                        if (!existingPhoto.equals(updatedPhoto)) {
                            photosToUpdate.add(updatedPhoto);
                        }
                    }

                    photosToCreate.addAll(expectedPhotos);

                    return updatePhotos(photosToUpdate)
                            .andThen(deletePhotos(photosToDelete))
                            .andThen(createPhotos(photosToCreate, mutableAnswer.getId()))
                            .andThen(Single.fromCallable(() -> new MutableAnswer(answerDao.getFilledById(mutableAnswer.getId()))));
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
}
