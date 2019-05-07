package fm.doe.national.data.data_source;

import android.content.Context;

import androidx.room.Room;

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
            saveSurvey(staticDatabase, survey);
        });
    }

    private long saveSurvey(AppDatabase database, Survey survey) {
        PersistenceSurvey persistenceSurvey = new PersistenceSurvey(survey);
        long id = database.getSurveyDao().insert(persistenceSurvey);
        if (survey.getCategories() != null) {
            saveCategories(database, survey.getCategories(), id);
        }
        return id;
    }

    private void saveCategories(AppDatabase database, List<? extends Category> categories, long surveyId) {
        for (Category category : categories) {
            PersistenceCategory persistenceCategory = new PersistenceCategory(category);
            persistenceCategory.surveyId = surveyId;
            long id = database.getCategoryDao().insert(persistenceCategory);
            if (category.getStandards() != null) {
                saveStandards(database, category.getStandards(), id);
            }
        }
    }

    private void saveStandards(AppDatabase database, List<? extends Standard> standards, long categoryId) {
        for (Standard standard : standards) {
            PersistenceStandard persistenceStandard = new PersistenceStandard(standard);
            persistenceStandard.categoryId = categoryId;
            long id = database.getStandardDao().insert(persistenceStandard);
            if (standard.getCriterias() != null) {
                saveCriterias(database, standard.getCriterias(), id);
            }
        }
    }

    private void saveCriterias(AppDatabase database, List<? extends Criteria> criterias, long standardId) {
        for (Criteria criteria : criterias) {
            PersistenceCriteria persistenceCriteria = new PersistenceCriteria(criteria);
            persistenceCriteria.standardId = standardId;
            long id = database.getCriteriaDao().insert(persistenceCriteria);
            if (criteria.getSubCriterias() != null) {
                saveSubCriterias(database, criteria.getSubCriterias(), id);
            }
        }
    }

    private void saveSubCriterias(AppDatabase database, List<? extends SubCriteria> subCriterias, long criteriaId) {
        for (SubCriteria subCriteria : subCriterias) {
            PersistenceSubCriteria persistenceSubCriteria = new PersistenceSubCriteria(subCriteria);
            persistenceSubCriteria.criteriaId = criteriaId;
            database.getSubcriteriaDao().insert(persistenceSubCriteria);
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
        return Single.fromCallable(surveyDao::getAll)
                .flatMap(persistenceSurveys -> Observable.fromIterable(surveyDao.getAll())
                        .map(MutableSurvey::new)
                        .toList());
    }

    @Override
    public Single<MutableSurvey> createSurvey(String schoolId, String schoolName, Date date) {
        return getStaticSurvey()
                .flatMap(mutableSurvey -> {
                    mutableSurvey.setSchoolName(schoolName);
                    mutableSurvey.setSchoolId(schoolId);
                    mutableSurvey.setDate(date);
                    long id = saveSurvey(dynamicDatabase, mutableSurvey);
                    return loadFullSurvey(id);
                });
    }

    @Override
    public Completable deleteSurvey(Survey survey) {
        return Completable.fromAction(() -> surveyDao.deleteById(survey.getId()));
    }

    @Override
    public Single<MutableAnswer> createOrUpdateAnswer(Answer answer, long subCriteriaId) {
        return Single.fromCallable(() -> {
            PersistenceAnswer existingAnswer = answerDao.getAllForSubCriteriaWithId(subCriteriaId).get(0);

            if (existingAnswer != null) {
                existingAnswer.comment = answer.getComment();
                existingAnswer.state = answer.getState();
                answerDao.update(existingAnswer);
                return new MutableAnswer(answerDao.getFilledById(existingAnswer.uid));
            }

            PersistenceAnswer persistenceAnswer = new PersistenceAnswer(answer);
            persistenceAnswer.subCriteriaId = subCriteriaId;
            long answerId = answerDao.insert(persistenceAnswer);
            return new MutableAnswer(answerDao.getFilledById(answerId));
        });
    }

    @Override
    public Single<MutablePhoto> createOrUpdatePhoto(Photo photo, long answerId) {
        return Single.fromCallable(() -> {
            PersistencePhoto existingPhoto = photoDao.getById(photo.getId());
            if (existingPhoto != null && existingPhoto.answerId == answerId) {
                existingPhoto.localUrl = photo.getLocalPath();
                existingPhoto.remoteUrl = photo.getRemotePath();
                photoDao.update(existingPhoto);
                return new MutablePhoto(photoDao.getById(existingPhoto.uid));
            }

            PersistencePhoto persistencePhoto = new PersistencePhoto(photo);
            persistencePhoto.answerId = answerId;
            long photoId = photoDao.insert(persistencePhoto);
            return new MutablePhoto(photoDao.getById(photoId));
        });
    }

    @Override
    public Completable deletePhoto(Photo photo) {
        return Completable.fromAction(() -> photoDao.deleteById(photo.getId()));
    }
}
