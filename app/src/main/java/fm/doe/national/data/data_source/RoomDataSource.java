package fm.doe.national.data.data_source;

import android.content.Context;

import androidx.room.Room;

import java.util.List;

import fm.doe.national.data.model.Answer;
import fm.doe.national.data.model.Photo;
import fm.doe.national.data.model.School;
import fm.doe.national.data.model.Survey;
import fm.doe.national.data.model.mutable.MutableAnswer;
import fm.doe.national.data.model.mutable.MutablePhoto;
import fm.doe.national.data.model.mutable.MutableSurvey;
import fm.doe.national.data.persistence.AppDatabase;
import fm.doe.national.data.persistence.dao.AnswerDao;
import fm.doe.national.data.persistence.dao.PhotoDao;
import fm.doe.national.data.persistence.dao.SchoolDao;
import fm.doe.national.data.persistence.dao.SurveyDao;
import fm.doe.national.data.persistence.entity.PersistenceAnswer;
import fm.doe.national.data.persistence.entity.PersistencePhoto;
import fm.doe.national.data.persistence.entity.PersistenceSchool;
import fm.doe.national.data.persistence.entity.relative.RelativePersistenceSurvey;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class RoomDataSource implements DataSource {

    private static final String DATABASE_NAME = "fm.doe.national.database";

    private final SurveyDao surveyDao;
    private final SchoolDao schoolDao;
    private final AnswerDao answerDao;
    private final PhotoDao photoDao;

    public RoomDataSource(Context applicationContext) {
        AppDatabase database = Room.databaseBuilder(applicationContext, AppDatabase.class, DATABASE_NAME).build();
        surveyDao = database.getSurveyDao();
        schoolDao = database.getSchoolDao();
        answerDao = database.getAnswerDao();
        photoDao = database.getPhotoDao();
    }

    @Override
    public Single<List<PersistenceSchool>> loadSchools() {
        return Single.fromCallable(schoolDao::getAll);
    }

    @Override
    public Completable rewriteSchools(List<School> schools) {
        return Completable.fromAction(() -> {
            // TODO: this might become CCE if another School impl appear, but for now on it is OK
            for (School school : schools) {
                schoolDao.insert((PersistenceSchool) school);
            }
        });
    }

    @Override
    public Single<MutableSurvey> loadFullSurvey(long surveyId) {
        return Single.fromCallable(() -> {
            RelativePersistenceSurvey relativePersistenceSurvey = surveyDao.getFilledById(surveyId);
            if (relativePersistenceSurvey == null) {
                return new MutableSurvey();
            }
            return new MutableSurvey(relativePersistenceSurvey);
        });
    }

    @Override
    public Single<List<MutableSurvey>> loadAllSurveys() {
        return Single.fromCallable(surveyDao::getAll)
                .flatMap(persistenceSurveys -> Observable.fromIterable(surveyDao.getAll())
                        .map(MutableSurvey::new)
                        .toList());
    }

    @Override
    public Single<MutableSurvey> createSurvey(Survey survey) {
        return null;
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
