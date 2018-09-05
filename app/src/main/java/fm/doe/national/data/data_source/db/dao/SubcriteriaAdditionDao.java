package fm.doe.national.data.data_source.db.dao;

import android.support.annotation.Nullable;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import fm.doe.national.data.data_source.models.db.OrmLiteSubCriteriaAddition;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;
import io.reactivex.Single;

public class SubcriteriaAdditionDao extends BaseRxDao<OrmLiteSubCriteriaAddition, Long> {

    SubcriteriaAdditionDao(ConnectionSource connectionSource,
                     Class<OrmLiteSubCriteriaAddition> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public void create(@Nullable String interviewQuestions, @Nullable String hint, OrmLiteSurveyItem item) throws SQLException {
        createIfNotExists(new OrmLiteSubCriteriaAddition(interviewQuestions, hint, item));
    }

    public Single<OrmLiteSubCriteriaAddition> requestAddition(OrmLiteSurveyItem item) {
        return Single.fromCallable(() -> {
            OrmLiteSubCriteriaAddition progress = queryBuilder()
                    .where()
                    .eq(OrmLiteSubCriteriaAddition.Column.SURVEY_ITEM, item)
                    .queryForFirst();
            return progress == null ?
                    createIfNotExists(new OrmLiteSubCriteriaAddition(null, null, item)) : progress;
        });
    }
}
