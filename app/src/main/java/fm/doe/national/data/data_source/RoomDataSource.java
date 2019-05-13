package fm.doe.national.data.data_source;

import android.content.Context;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import fm.doe.national.data.persistence.entity.PersistenceAnswer;
import fm.doe.national.data.persistence.entity.PersistenceCategory;
import fm.doe.national.data.persistence.entity.PersistenceCriteria;
import fm.doe.national.data.persistence.entity.PersistencePhoto;
import fm.doe.national.data.persistence.entity.PersistenceSchool;
import fm.doe.national.data.persistence.entity.PersistenceStandard;
import fm.doe.national.data.persistence.entity.PersistenceSubCriteria;
import fm.doe.national.data.persistence.entity.PersistenceSurvey;
import fm.doe.national.utils.CollectionUtils;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class RoomDataSource implements DataSource {

    private static final String DATABASE_NAME = "fm.doe.national.database";
    private static final String STATIC_DATABASE_NAME = "fm.doe.national.static_database";

    private final SurveyDao surveyDao;
    private final AnswerDao answerDao;
    private final PhotoDao photoDao;

    private final AppDatabase staticDatabase;
    private final AppDatabase dynamicDatabase;

    public RoomDataSource(Context applicationContext) {
        dynamicDatabase = Room.databaseBuilder(applicationContext, AppDatabase.class, DATABASE_NAME).build();
        staticDatabase = Room.databaseBuilder(applicationContext, AppDatabase.class, STATIC_DATABASE_NAME).build();
        surveyDao = dynamicDatabase.getSurveyDao();
        answerDao = dynamicDatabase.getAnswerDao();
        photoDao = dynamicDatabase.getPhotoDao();
    }

    @Override
    public void closeConnections() {
        dynamicDatabase.close();
        staticDatabase.close();
    }

    @Override
    public Single<List<PersistenceSchool>> loadSchools() {
        return Single.fromCallable(staticDatabase.getSchoolDao()::getAll);
    }

    @Override
    public Completable rewriteSchools(List<School> schools) {
        return Completable.fromAction(() -> {
            staticDatabase.getSchoolDao().deleteAll();
            // WARNING: this might become CCE if another School impl appear, but for now on it is OK
            for (School school : schools) {
                staticDatabase.getSchoolDao().insert((PersistenceSchool) school);
            }
        });
    }

    @Override
    public Completable rewriteStaticSurvey(Survey survey) {
        return Completable.fromAction(() -> {
            staticDatabase.getSurveyDao().deleteAll();
            saveSurvey(staticDatabase, survey, false);
        });
    }

    private long saveSurvey(AppDatabase database, Survey survey, boolean shouldCreateAnswers) {
        PersistenceSurvey persistenceSurvey = new PersistenceSurvey(survey);
        persistenceSurvey.uid = 0;
        long id = database.getSurveyDao().insert(persistenceSurvey);
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
            PersistenceCategory persistenceCategory = new PersistenceCategory(category);
            persistenceCategory.uid = 0;
            persistenceCategory.surveyId = surveyId;
            long id = database.getCategoryDao().insert(persistenceCategory);
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
            PersistenceStandard persistenceStandard = new PersistenceStandard(standard);
            persistenceStandard.uid = 0;
            persistenceStandard.categoryId = categoryId;
            long id = database.getStandardDao().insert(persistenceStandard);
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
            PersistenceCriteria persistenceCriteria = new PersistenceCriteria(criteria);
            persistenceCriteria.uid = 0;
            persistenceCriteria.standardId = standardId;
            long id = database.getCriteriaDao().insert(persistenceCriteria);
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
            PersistenceSubCriteria persistenceSubCriteria = new PersistenceSubCriteria(subCriteria);
            persistenceSubCriteria.uid = 0;
            persistenceSubCriteria.criteriaId = criteriaId;
            long id = database.getSubcriteriaDao().insert(persistenceSubCriteria);
            if (shouldCreateAnswers) {
                database.getAnswerDao().insert(new PersistenceAnswer(id));
            }
        }
    }

    @Override
    public Single<MutableSurvey> getStaticSurvey() {
        return Single.fromCallable(() -> staticDatabase.getSurveyDao().getFirstFilled())
                .map(MutableSurvey::new);
    }

    @Override
    public Single<MutableSurvey> loadFullSurvey(long surveyId) {
        return Single.fromCallable(() -> surveyDao.getFilledById(surveyId))
                .map(MutableSurvey::new);
    }

    @Override
    public Single<List<MutableSurvey>> loadAllSurveys() {
        return Single.fromCallable(surveyDao::getAllFilled)
                .flatMapObservable(Observable::fromIterable)
                .map(MutableSurvey::new)
                .toList();
    }

    @Override
    public Single<MutableSurvey> createSurvey(String schoolId, String schoolName, Date date) {
        return getStaticSurvey()
                .flatMap(mutableSurvey -> {
                    mutableSurvey.setId(0);
                    mutableSurvey.setSchoolName(schoolName);
                    mutableSurvey.setSchoolId(schoolId);
                    mutableSurvey.setDate(date);
                    long id = saveSurvey(dynamicDatabase, mutableSurvey, true);
                    return loadFullSurvey(id);
                });
    }

    @Override
    public Completable deleteSurvey(long surveyId) {
        return Completable.fromAction(() -> surveyDao.deleteById(surveyId));
    }

    @Override
    public Single<MutableAnswer> updateAnswer(Answer answer, long subCriteriaId) {
        return Single.fromCallable(() -> {
            PersistenceAnswer existingAnswer = answerDao.getAllForSubCriteriaWithId(subCriteriaId).get(0);
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

                    return Observable.fromIterable(photosToUpdate)
                            .map(photo -> {
                                photoDao.update(new PersistencePhoto(photo));
                                return photo;
                            })
                            .toList()
                            .flatMap(updatedPhotos -> Observable.fromIterable(photosToDelete)
                                    .map(photo -> {
                                        photoDao.deleteById(photo.getId());
                                        return photo;
                                    })
                                    .toList()
                                    .flatMap(deletedPhotos -> Observable.fromIterable(photosToCreate)
                                            .map(photo -> {
                                                PersistencePhoto persistencePhoto = new PersistencePhoto(photo);
                                                persistencePhoto.answerId = answer.getId();
                                                photoDao.insert(persistencePhoto);
                                                return photo;
                                            })
                                            .toList()
                                    )
                            );
                })
                .flatMap(photos ->
                                Single.fromCallable(() -> new MutableAnswer(answerDao.getFilledById(answer.getId()))));
    }

    @Override
    public Single<MutablePhoto> createPhoto(Photo photo, long answerId) {
        return Single.fromCallable(() -> {
            PersistencePhoto persistencePhoto = new PersistencePhoto(photo);
            persistencePhoto.answerId = answerId;
            long photoId = photoDao.insert(persistencePhoto);
            return new MutablePhoto(photoDao.getById(photoId));
        });
    }

    @Override
    public Completable deletePhoto(long photoId) {
        return Completable.fromAction(() -> photoDao.deleteById(photoId));
    }

    @Override
    public Completable clearDynamicData() {
        return Completable.fromAction(surveyDao::deleteAll);
    }
}
