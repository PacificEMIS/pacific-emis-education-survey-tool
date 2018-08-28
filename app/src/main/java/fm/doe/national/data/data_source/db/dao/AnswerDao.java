package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.db.OrmLiteAnswer;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyPassing;
import io.reactivex.Completable;
import io.reactivex.Single;

public class AnswerDao extends BaseRxDao<OrmLiteAnswer, Long> {

    AnswerDao(ConnectionSource connectionSource, Class<OrmLiteAnswer> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteAnswer> createAnswer(
            Answer.State state,
            OrmLiteSurveyItem parentSurveyItem,
            OrmLiteSurveyPassing surveyPassing) {
        return Single.fromCallable(() -> {
            OrmLiteAnswer ormLiteAnswer = new OrmLiteAnswer(state, parentSurveyItem, surveyPassing);
            create(ormLiteAnswer);
            return ormLiteAnswer;
        });
    }

    public Single<OrmLiteAnswer> requestAnswer(OrmLiteSurveyItem parentSurveyItem, OrmLiteSurveyPassing surveyPassing) {
        return Single.fromCallable(() -> {
                    OrmLiteAnswer answer = queryBuilder()
                            .where()
                            .eq(OrmLiteAnswer.Column.SURVEY_PASSING, surveyPassing)
                            .and()
                            .eq(OrmLiteAnswer.Column.PARENT_ITEM, parentSurveyItem)
                            .queryForFirst();
                    return answer != null ? answer : new OrmLiteAnswer(Answer.State.NOT_ANSWERED, parentSurveyItem, surveyPassing);
                }

        );
    }

    public Single<OrmLiteAnswer> updateAnswer(OrmLiteAnswer answer) {
        return Single.fromCallable(() -> {
            createOrUpdate(answer);
            return answer;
        });
    }

}
