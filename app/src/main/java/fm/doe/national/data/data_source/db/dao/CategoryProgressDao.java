package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Collection;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.db.OrmLiteCategoryProgress;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyPassing;
import io.reactivex.Observable;
import io.reactivex.Single;

public class CategoryProgressDao extends BaseRxDao<OrmLiteCategoryProgress, Long> {

    CategoryProgressDao(ConnectionSource connectionSource, Class<OrmLiteCategoryProgress> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteCategoryProgress> requestCategoryProgress(OrmLiteSurveyPassing passing, OrmLiteSurveyItem item) {
        return Single.fromCallable(() -> {
            OrmLiteCategoryProgress progress = queryBuilder()
                    .where()
                    .eq(OrmLiteCategoryProgress.Column.SURVEY_PASSING, passing)
                    .and()
                    .eq(OrmLiteCategoryProgress.Column.SURVEY_ITEM, item)
                    .queryForFirst();
            return progress == null ? createIfNotExists(new OrmLiteCategoryProgress(item, passing)) : progress;
        });
    }

    public Single<? extends CategoryProgress> requestCategoryProgress(OrmLiteSurveyPassing passing, Collection<OrmLiteSurveyItem> items) {
        return Observable.fromIterable(items)
                .flatMap(surveyItem -> requestCategoryProgress(passing, surveyItem).toObservable())
                .reduce((accumulator, current) -> {
                    accumulator.setCompletedItemsCount(accumulator.getCompletedItemsCount() + current.getCompletedItemsCount());
                    accumulator.setTotalItemsCount(accumulator.getTotalItemsCount() + current.getTotalItemsCount());
                    return accumulator;
                })
                .toSingle();
    }

    public Single<OrmLiteCategoryProgress> updateCategoryProgress(OrmLiteSurveyItem surveyItem, OrmLiteSurveyPassing passing,  Answer.State previousState, Answer.State state) {
        Single<OrmLiteCategoryProgress> single = requestCategoryProgress(passing, surveyItem).doOnSuccess(progress -> {
            progress.recalculate(previousState, state);
            update(progress);
        });

        if (surveyItem.getParentItem() != null) {
            return single.flatMap(progress -> updateCategoryProgress(surveyItem.getParentItem(), passing, previousState, state));
        }
        return single;
    }
}
