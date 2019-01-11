package fm.doe.national.data.data_source.db.dao;


import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import androidx.annotation.Nullable;
import fm.doe.national.data.data_source.models.db.OrmLiteSubCriteriaQuestion;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;
import io.reactivex.Single;

public class SubcriteriaQuestionDao extends BaseRxDao<OrmLiteSubCriteriaQuestion, Long> {

    SubcriteriaQuestionDao(ConnectionSource connectionSource,
                           Class<OrmLiteSubCriteriaQuestion> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public void create(@Nullable String interviewQuestion, @Nullable String hint, OrmLiteSurveyItem item) throws SQLException {
        createIfNotExists(new OrmLiteSubCriteriaQuestion(interviewQuestion, hint, item));
    }

    public Single<OrmLiteSubCriteriaQuestion> requestQuestion(OrmLiteSurveyItem item) {
        return Single.fromCallable(() -> {
            OrmLiteSubCriteriaQuestion progress = queryBuilder()
                    .where()
                    .eq(OrmLiteSubCriteriaQuestion.Column.SURVEY_ITEM, item)
                    .queryForFirst();
            return progress == null ?
                    createIfNotExists(new OrmLiteSubCriteriaQuestion(null, null, item)) : progress;
        });
    }
}
