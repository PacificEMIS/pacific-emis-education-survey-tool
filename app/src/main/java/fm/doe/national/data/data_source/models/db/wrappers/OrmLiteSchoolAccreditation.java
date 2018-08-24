package fm.doe.national.data.data_source.models.db.wrappers;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.data_source.models.db.OrmLiteSurvey;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;

public class OrmLiteSchoolAccreditation implements SchoolAccreditation {

    private OrmLiteSurvey survey;

    private List<OrmLiteGroupStandard> groupStandards;

    public  OrmLiteSchoolAccreditation(OrmLiteSurvey survey) {
        this.survey = survey;
    }

    @Override
    public List<? extends GroupStandard> getGroupStandards() {
        if (groupStandards == null) {
            groupStandards = new ArrayList<>();
            for (OrmLiteSurveyItem surveyItem : survey.getSurveyItems()) {
                groupStandards.add(new OrmLiteGroupStandard(surveyItem));
            }
        }

        return groupStandards;
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
