package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.db.OrmLiteAnswer;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyPassing;
import io.reactivex.Single;

public class AnswerDao extends BaseRxDao<OrmLiteAnswer, Long> {

    AnswerDao(ConnectionSource connectionSource, Class<OrmLiteAnswer> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteAnswer> requestAnswer(OrmLiteSurveyItem surveyItem, OrmLiteSurveyPassing surveyPassing) {
        return Single.fromCallable(() -> {
            OrmLiteAnswer answer = queryBuilder()
                    .where()
                    .eq(OrmLiteAnswer.Column.SURVEY_PASSING, surveyPassing)
                    .and()
                    .eq(OrmLiteAnswer.Column.SURVEY_ITEM, surveyItem)
                    .queryForFirst();
            return answer != null ? answer : createIfNotExists(new OrmLiteAnswer(Answer.State.NOT_ANSWERED, surveyItem, surveyPassing));
        });
    }

    public Single<OrmLiteAnswer> updateAnswer(OrmLiteSurveyItem surveyItem, OrmLiteSurveyPassing surveyPassing, Answer.State state) {
        return requestAnswer(surveyItem, surveyPassing)
                .doOnSuccess(answer -> {
                    answer.setState(state);
                    update(answer);
                });
    }

}
