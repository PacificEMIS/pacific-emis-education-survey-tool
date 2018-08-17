package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.db.OrmLiteBaseSurvey;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;
import io.reactivex.Single;

public class SurveyItemDao extends BaseRxDao<OrmLiteSurveyItem, Long> {

    SurveyItemDao(ConnectionSource connectionSource, Class<OrmLiteSurveyItem> dataClass)
            throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteSurveyItem> requestItemByName(String name) {
        return Single.fromCallable(() -> queryBuilder()
                .where()
                .eq(OrmLiteSurveyItem.Column.NAME, name)
                .queryForFirst());
    }

    public List<OrmLiteSurveyItem> createFromGroupStandards(
            Collection<? extends GroupStandard> groupStandards,
            OrmLiteBaseSurvey survey) throws SQLException {
        List<OrmLiteSurveyItem> surveyItems = new ArrayList<>();

        for (GroupStandard groupStandard : groupStandards) {
            OrmLiteSurveyItem surveyItem = new OrmLiteSurveyItem(
                    groupStandard.getName(),
                    OrmLiteSurveyItem.Type.GROUP_STANDARD, survey,
                    null);
            create(surveyItem);
            surveyItem.addChildren(createFromStandards(groupStandard.getStandards(), surveyItem));

            surveyItems.add(surveyItem);
        }

        return surveyItems;
    }

    private List<OrmLiteSurveyItem> createFromStandards(
            Collection<? extends Standard> standards,
            OrmLiteSurveyItem parentItem) throws SQLException {
        List<OrmLiteSurveyItem> surveyItems = new ArrayList<>();

        for (Standard standard : standards) {
            OrmLiteSurveyItem surveyItem = new OrmLiteSurveyItem(
                    standard.getName(),
                    OrmLiteSurveyItem.Type.STANDARD,
                    null,
                    parentItem);
            create(surveyItem);
            surveyItem.addChildren(createFromCriterias(standard.getCriterias(), surveyItem));

            surveyItems.add(surveyItem);
        }

        return surveyItems;
    }

    private List<OrmLiteSurveyItem> createFromCriterias(
            Collection<? extends Criteria> criterias,
            OrmLiteSurveyItem parentItem) throws SQLException {
        List<OrmLiteSurveyItem> surveyItems = new ArrayList<>();

        for (Criteria criteria : criterias) {
            OrmLiteSurveyItem surveyItem = new OrmLiteSurveyItem(
                    criteria.getName(),
                    OrmLiteSurveyItem.Type.CRITERIA,
                    null,
                    parentItem);
            create(surveyItem);
            surveyItem.addChildren(createFromSubCriterias(criteria.getSubCriterias(), surveyItem));

            surveyItems.add(surveyItem);
        }

        return surveyItems;
    }

    private List<OrmLiteSurveyItem> createFromSubCriterias(
            Collection<? extends SubCriteria> subCriterias,
            OrmLiteSurveyItem parentItem) throws SQLException {
        List<OrmLiteSurveyItem> surveyItems = new ArrayList<>();

        for (SubCriteria subCriteria : subCriterias) {
            OrmLiteSurveyItem surveyItem = new OrmLiteSurveyItem(
                    subCriteria.getName(),
                    OrmLiteSurveyItem.Type.SUBCRITERIA,
                    null,
                    parentItem);
            create(surveyItem);

            surveyItems.add(surveyItem);
        }

        return surveyItems;
    }
}
