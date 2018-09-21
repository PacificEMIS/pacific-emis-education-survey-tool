package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.SubCriteriaQuestion;
import fm.doe.national.data.data_source.models.db.OrmLiteSurvey;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;
import fm.doe.national.data.data_source.models.serializable.LinkedCategory;
import fm.doe.national.data.data_source.models.serializable.LinkedStandard;

public class SurveyItemDao extends BaseRxDao<OrmLiteSurveyItem, Long> {

    private SubcriteriaQuestionDao subcriteriaQuestionDao;

    SurveyItemDao(SubcriteriaQuestionDao subcriteriaQuestionDao,
                  ConnectionSource connectionSource,
                  Class<OrmLiteSurveyItem> dataClass)
            throws SQLException {
        super(connectionSource, dataClass);
        this.subcriteriaQuestionDao = subcriteriaQuestionDao;
    }

    public List<OrmLiteSurveyItem> createFromCategories(
            List<? extends LinkedCategory> categories,
            OrmLiteSurvey survey) throws SQLException {
        List<OrmLiteSurveyItem> surveyItems = new ArrayList<>();

        for (LinkedCategory category : categories) {
            OrmLiteSurveyItem surveyItem = new OrmLiteSurveyItem(
                    category.getName(),
                    OrmLiteSurveyItem.Type.CATEGORY, null, survey,
                    null);
            create(surveyItem);
            surveyItem.addChildren(createFromStandards(category.getStandards(), surveyItem));

            surveyItems.add(surveyItem);
        }

        return surveyItems;
    }

    private List<OrmLiteSurveyItem> createFromStandards(
            List<? extends LinkedStandard> standards,
            OrmLiteSurveyItem parentItem) throws SQLException {
        List<OrmLiteSurveyItem> surveyItems = new ArrayList<>();

        for (LinkedStandard standard : standards) {
            OrmLiteSurveyItem surveyItem = new OrmLiteSurveyItem(
                    standard.getName(),
                    OrmLiteSurveyItem.Type.STANDARD,
                    standard.getIcon(),
                    null,
                    parentItem);
            create(surveyItem);

            List<? extends Criteria> criterias = standard.getCriterias();
            if (criterias != null) surveyItem.addChildren(createFromCriterias(criterias, surveyItem));

            surveyItems.add(surveyItem);
        }

        return surveyItems;
    }

    private List<OrmLiteSurveyItem> createFromCriterias(
            List<? extends Criteria> criterias,
            OrmLiteSurveyItem parentItem) throws SQLException {
        List<OrmLiteSurveyItem> surveyItems = new ArrayList<>();

        for (Criteria criteria : criterias) {
            OrmLiteSurveyItem surveyItem = new OrmLiteSurveyItem(
                    criteria.getName(),
                    OrmLiteSurveyItem.Type.CRITERIA,
                    null,
                    null,
                    parentItem);

            create(surveyItem);
            surveyItem.addChildren(createFromSubCriterias(criteria.getSubCriterias(), surveyItem));

            surveyItems.add(surveyItem);
        }

        return surveyItems;
    }

    private List<OrmLiteSurveyItem> createFromSubCriterias(
            List<? extends SubCriteria> subCriterias,
            OrmLiteSurveyItem parentItem) throws SQLException {
        List<OrmLiteSurveyItem> surveyItems = new ArrayList<>();

        for (SubCriteria subCriteria : subCriterias) {
            OrmLiteSurveyItem surveyItem = new OrmLiteSurveyItem(
                    subCriteria.getName(),
                    OrmLiteSurveyItem.Type.SUBCRITERIA,
                    null,
                    null,
                    parentItem);
            create(surveyItem);

            SubCriteriaQuestion question = subCriteria.getSubCriteriaQuestion();
            subcriteriaQuestionDao.create(question.getInterviewQuestion(), question.getHint(), surveyItem);

            surveyItems.add(surveyItem);
        }

        return surveyItems;
    }
}
