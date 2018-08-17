package fm.doe.national.data.data_source.models.db.wrappers;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;

public class OrmLiteStandard implements Standard {

    private OrmLiteSurveyItem surveyItem;

    public OrmLiteStandard(OrmLiteSurveyItem surveyItem) {
        this.surveyItem = surveyItem;
    }

    @Override
    public GroupStandard getGroupStandard() {
        return new OrmLiteGroupStandard(surveyItem.getParentItem());
    }

    @NonNull
    @Override
    public String getName() {
        return surveyItem.getName();
    }

    @Override
    public Collection<? extends Criteria> getCriterias() {
        List<Criteria> criterias = new ArrayList<>();
        for (OrmLiteSurveyItem surveyItem : surveyItem.getChildrenItems()) {
            criterias.add(new OrmLiteCriteria(surveyItem));
        }
        return criterias;
    }

}

