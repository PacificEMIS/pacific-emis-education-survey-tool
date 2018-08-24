package fm.doe.national.data.data_source.models.db.wrappers;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;

public class OrmLiteStandard implements Standard {

    private OrmLiteSurveyItem surveyItem;

    private OrmLiteGroupStandard groupStandard;

    private List<OrmLiteCriteria> criterias;

    public OrmLiteStandard(OrmLiteSurveyItem surveyItem, OrmLiteGroupStandard groupStandard) {
        this.surveyItem = surveyItem;
        this.groupStandard = groupStandard;
    }

    @Override
    public GroupStandard getGroupStandard() {
        return groupStandard;
    }

    @Override
    public Long getId() {
        return surveyItem.getId();
    }

    @NonNull
    @Override
    public String getName() {
        return surveyItem.getName();
    }

    @Override
    public List<? extends Criteria> getCriterias() {
        if (criterias == null) {
            criterias = new ArrayList<>();
            for (OrmLiteSurveyItem surveyItem : surveyItem.getChildrenItems()) {
                criterias.add(new OrmLiteCriteria(surveyItem, this));
            }
        }

        return criterias;
    }

}

