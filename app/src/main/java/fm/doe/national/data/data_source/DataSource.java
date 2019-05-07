package fm.doe.national.data.data_source;

import java.util.Date;
import java.util.List;

import fm.doe.national.data.model.Answer;
import fm.doe.national.data.model.Photo;
import fm.doe.national.data.model.School;
import fm.doe.national.data.model.Survey;
import fm.doe.national.data.model.mutable.MutableAnswer;
import fm.doe.national.data.model.mutable.MutablePhoto;
import fm.doe.national.data.model.mutable.MutableSurvey;
import fm.doe.national.data.persistence.entity.PersistenceSchool;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface DataSource {

    Single<List<PersistenceSchool>> loadSchools();

    Completable rewriteSchools(List<School> schools);

    Completable rewriteStaticSurvey(Survey survey);

    Single<MutableSurvey> getStaticSurvey();

    Single<MutableSurvey> loadFullSurvey(long surveyId);

    Single<List<MutableSurvey>> loadAllSurveys();

    Single<MutableSurvey> createSurvey(String schoolId, String schoolName, Date date);

    Completable deleteSurvey(Survey survey);

    Single<MutableAnswer> createOrUpdateAnswer(Answer answer, long subCriteriaId);

    Single<MutablePhoto> createOrUpdatePhoto(Photo photo, long answerId);

    Completable deletePhoto(Photo photo);

}
