package fm.doe.national.data.data_source.models.db;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.Standard;

public class OrmLiteGroupStandard implements GroupStandard {

    private OrmLiteSurveyItem surveyItem;

    public OrmLiteGroupStandard(OrmLiteSurveyItem surveyItem) {
        this.surveyItem = surveyItem;
    }

    @NonNull
    @Override
    public String getName() {
       return surveyItem.getName();
    }

    @Override
    public Collection<? extends Standard> getStandards() {
        List<Standard> standards = new ArrayList<>();
        for (OrmLiteSurveyItem surveyItem : surveyItem.getChildrenItems()) {
            standards.add(new OrmLiteStandard(surveyItem));
        }
        return standards;
    }

}
