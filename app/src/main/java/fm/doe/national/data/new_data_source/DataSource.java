package fm.doe.national.data.new_data_source;

import java.util.List;

import fm.doe.national.data.model.Answer;
import fm.doe.national.data.model.Photo;
import fm.doe.national.data.model.School;
import fm.doe.national.data.model.Survey;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface DataSource {

    Single<List<School>> loadSchools();

    Completable createSchools(List<School> schools);

    Single<Survey> loadFullSurvey(Survey survey);

    Single<List<Survey>> loadAllSurveys();

    Single<Survey> createSurvey(Survey survey);

    Completable deleteSurvey(Survey survey);

    Single<Answer> createOrUpdateAnswer(Answer answer);

    Single<Photo> createOrUpdatePhoto(Photo photo);

    Completable deletePhoto(Photo photo);

}
