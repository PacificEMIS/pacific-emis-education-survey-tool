package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import fm.doe.national.data.data_source.models.db.OrmLiteAnswer;
import fm.doe.national.data.data_source.models.db.OrmLiteBaseSurvey;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyResult;
import io.reactivex.Completable;
import io.reactivex.Single;

public class AnswerDao extends BaseRxDao<OrmLiteAnswer, Long> {

    AnswerDao(ConnectionSource connectionSource, Class<OrmLiteAnswer> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteAnswer> createAnswer(boolean answer, OrmLiteSurveyItem surveyItem, OrmLiteSurveyResult surveyResult) {
        return Single.fromCallable(() -> {
            OrmLiteAnswer ormLiteAnswer = new OrmLiteAnswer(answer, surveyItem, surveyResult);
            create(ormLiteAnswer);
            return ormLiteAnswer;
        });
    }

    public Completable updateAnswer(OrmLiteAnswer answer) {
        return Completable.fromAction(() -> createOrUpdate(answer));
    }

    public Single<List<OrmLiteAnswer>> getAnswers(OrmLiteBaseSurvey survey) {
        return Single.fromCallable(() -> queryBuilder()
                .where()
                .eq(OrmLiteAnswer.Column.SURVEY_RESULT, survey)
                .query());
    }

}
