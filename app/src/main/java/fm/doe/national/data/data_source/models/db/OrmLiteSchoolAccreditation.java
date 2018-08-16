package fm.doe.national.data.data_source.models.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.SchoolAccreditation;

public class OrmLiteSchoolAccreditation implements SchoolAccreditation {

    private OrmLiteBaseSurvey baseSurvey;

    public OrmLiteSchoolAccreditation(OrmLiteBaseSurvey baseSurvey) {
        this.baseSurvey = baseSurvey;
    }

    @Override
    public Collection<? extends GroupStandard> getGroupStandards() {
        List<GroupStandard> groupStandardList = new ArrayList<>();
        for (OrmLiteSurveyItem surveyItem : baseSurvey.getSurveyItems()) {
            groupStandardList.add(new OrmLiteGroupStandard(surveyItem));
        }
        return groupStandardList;
    }

    @Override
    public int getVersion() {
        return baseSurvey.getVersion();
    }

    @Override
    public String getType() {
        return baseSurvey.getType();
    }
}
