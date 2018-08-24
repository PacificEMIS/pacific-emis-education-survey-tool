package fm.doe.national.data.data_source.models.db.wrappers;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.data_source.models.db.OrmLiteSurvey;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;

public class OrmLiteSchoolAccreditation implements SchoolAccreditation {

    private OrmLiteSurvey survey;

    public OrmLiteSchoolAccreditation(OrmLiteSurvey survey) {
        this.survey = survey;
    }

    @Override
    public List<? extends GroupStandard> getGroupStandards() {
        List<GroupStandard> groupStandardList = new ArrayList<>();
        for (OrmLiteSurveyItem surveyItem : survey.getSurveyItems()) {
            groupStandardList.add(new OrmLiteGroupStandard(surveyItem));
        }
        return groupStandardList;
    }

    @Override
    public int getVersion() {
        return survey.getVersion();
    }

    @Override
    public String getType() {
        return survey.getType();
    }
}
