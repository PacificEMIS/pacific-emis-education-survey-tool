package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import fm.doe.national.data.models.survey.School;
import fm.doe.national.data.models.survey.Survey;
import io.reactivex.Single;

public class SurveyDao extends BaseRxDao<Survey, Long> {

    SurveyDao(ConnectionSource connectionSource, Class<Survey> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<Survey> createSurvey(School school, int year) {
        return Single.fromCallable(() -> {
            Survey survey = new Survey(year, school);
            create(survey);
            return survey;
        });
    }


}
