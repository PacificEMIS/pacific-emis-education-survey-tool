package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import fm.doe.national.data.data_source.models.survey.db.OrmLiteAnswer;
import fm.doe.national.data.data_source.models.survey.db.OrmLiteSubCriteria;
import fm.doe.national.data.data_source.models.survey.db.OrmLiteSurvey;
import io.reactivex.Completable;
import io.reactivex.Single;

public class AnswerDao extends BaseRxDao<OrmLiteAnswer, Long> {

    AnswerDao(ConnectionSource connectionSource, Class<OrmLiteAnswer> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteAnswer> createAnswer(boolean answer, OrmLiteSubCriteria subCriteria, OrmLiteSurvey survey) {
        return Single.fromCallable(() -> {
            OrmLiteAnswer ormLiteAnswer = new OrmLiteAnswer(answer, subCriteria, survey);
            create(ormLiteAnswer);
            return ormLiteAnswer;
        });
    }

    public Completable updateAnswer(OrmLiteAnswer answer) {
        return Completable.fromAction(() -> createOrUpdate(answer));
    }

    public Single<List<OrmLiteAnswer>> getAnswers(OrmLiteSurvey survey) {
        return Single.fromCallable(() -> queryBuilder()
                .where()
                .eq(OrmLiteAnswer.Column.SURVEY_RESULT, survey)
                .query());
    }

}
