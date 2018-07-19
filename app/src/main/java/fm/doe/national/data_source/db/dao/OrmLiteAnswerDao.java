package fm.doe.national.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import fm.doe.national.data_source.db.models.*;
import fm.doe.national.models.survey.Survey;
import io.reactivex.Completable;
import io.reactivex.Single;

public class OrmLiteAnswerDao extends BaseRxDao<OrmLiteAnswer, Long> {

    OrmLiteAnswerDao(ConnectionSource connectionSource, Class<OrmLiteAnswer> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteAnswer> createAnswer(boolean answer, OrmLiteSubCriteria subCriteria, OrmLiteSurvey survey) {
        return Single.fromCallable(() -> {
            OrmLiteAnswer ormLiteAnswer = new OrmLiteAnswer(answer, subCriteria, survey);
            int id = create(ormLiteAnswer);
            return ormLiteAnswer;
        });
    }

    public Completable updateAnswer(OrmLiteAnswer answer) {
        return Completable.fromAction(() -> createOrUpdate(answer));
    }

    public Single<List<OrmLiteAnswer>> getAnswers(Survey survey) {
        return Single.fromCallable(() -> queryBuilder()
                .where()
                .eq(OrmLiteAnswer.Column.SURVEY, survey)
                .query());
    }

}
