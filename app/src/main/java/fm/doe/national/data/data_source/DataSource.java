package fm.doe.national.data.data_source;

import java.util.List;

import fm.doe.national.data.model.Answer;
import fm.doe.national.data.model.Photo;
import fm.doe.national.data.model.School;
import fm.doe.national.data.model.Survey;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface DataSource {

    Single<List<? extends School>> loadSchools();

    Completable createSchools(List<School> schools);

    Single<? extends Survey> loadFullSurvey(Survey survey);

    Single<List<? extends Survey>> loadAllSurveys();

    Single<? extends Survey> createSurvey(Survey survey);

    Completable deleteSurvey(Survey survey);

    Single<? extends Answer> createOrUpdateAnswer(Answer answer, long subCriteriaId);

    Single<? extends Photo> createOrUpdatePhoto(Photo photo, long answerId);

    Completable deletePhoto(Photo photo);

}
