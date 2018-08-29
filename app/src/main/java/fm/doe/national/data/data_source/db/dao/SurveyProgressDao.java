package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Collection;

import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyPassing;
import fm.doe.national.data.data_source.models.db.OrmLiteCategoryProgress;
import io.reactivex.Observable;
import io.reactivex.Single;

public class SurveyProgressDao extends BaseRxDao<OrmLiteCategoryProgress, Long> {
    SurveyProgressDao(ConnectionSource connectionSource, Class<OrmLiteCategoryProgress> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteCategoryProgress> requestSurveyProgress(OrmLiteSurveyPassing passing, OrmLiteSurveyItem item) {
        return Single.fromCallable(() -> {
            OrmLiteCategoryProgress progress = queryBuilder()
                    .where()
                    .eq(OrmLiteCategoryProgress.Column.SURVEY_PASSING, passing)
                    .and()
                    .eq(OrmLiteCategoryProgress.Column.SURVEY_ITEM, item)
                    .queryForFirst();
            return progress == null ? createIfNotExists(new OrmLiteCategoryProgress(0, item, passing)) : progress;
        });
    }

    public Single<? extends CategoryProgress> requestSurveyProgress(OrmLiteSurveyPassing passing, Collection<OrmLiteSurveyItem>
            items) {
        return Observable.fromIterable(items)
                .flatMap(surveyItem -> requestSurveyProgress(passing, surveyItem).toObservable())
                .reduce((accumulator, current) -> {
                    accumulator.setCompletedItemsCount(accumulator.getCompletedItemsCount() + current.getCompletedItemsCount());
                    accumulator.setTotalItemsCount(accumulator.getTotalItemsCount() + current.getTotalItemsCount());
                    return accumulator;
                })
                .toSingle();
    }
}
