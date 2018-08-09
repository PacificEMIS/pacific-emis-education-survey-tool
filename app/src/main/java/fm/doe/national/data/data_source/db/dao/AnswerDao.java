package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import fm.doe.national.data.models.survey.Answer;
import fm.doe.national.data.models.survey.SubCriteria;
import fm.doe.national.data.models.survey.Survey;
import io.reactivex.Completable;
import io.reactivex.Single;

public class AnswerDao extends BaseRxDao<Answer, Long> {

    AnswerDao(ConnectionSource connectionSource, Class<Answer> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<Answer> createAnswer(boolean answer, SubCriteria subCriteria, Survey survey) {
        return Single.fromCallable(() -> {
            Answer ormLiteAnswer = new Answer(answer, subCriteria, survey);
            create(ormLiteAnswer);
            return ormLiteAnswer;
        });
    }

    public Completable updateAnswer(Answer answer) {
        return Completable.fromAction(() -> createOrUpdate(answer));
    }

    public Single<List<Answer>> getAnswers(Survey survey) {
        return Single.fromCallable(() -> queryBuilder()
                .where()
                .eq(Answer.Column.SURVEY, survey)
                .query());
    }

}
