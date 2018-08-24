package fm.doe.national.data.data_source.models.db.wrappers;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;

public class OrmLiteGroupStandard implements GroupStandard {

    private OrmLiteSurveyItem surveyItem;

    private List<OrmLiteStandard> standards;

    public OrmLiteGroupStandard(OrmLiteSurveyItem surveyItem) {
        this.surveyItem = surveyItem;
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
    public List<? extends Standard> getStandards() {
        if (standards == null) {
            standards = new ArrayList<>();
            for (OrmLiteSurveyItem surveyItem : surveyItem.getChildrenItems()) {
                standards.add(new OrmLiteStandard(surveyItem, this));
            }
        }

        return standards;
    }

}
