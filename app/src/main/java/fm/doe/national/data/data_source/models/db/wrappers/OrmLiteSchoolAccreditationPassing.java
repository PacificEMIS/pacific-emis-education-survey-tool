package fm.doe.national.data.data_source.models.db.wrappers;

import java.util.Date;

import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyPassing;

public class OrmLiteSchoolAccreditationPassing implements SchoolAccreditationPassing {

    private OrmLiteSurveyPassing surveyPassing;

    private OrmLiteSchoolAccreditation schoolAccreditation;

    public OrmLiteSchoolAccreditationPassing(OrmLiteSurveyPassing surveyPassing, OrmLiteSchoolAccreditation schoolAccreditation) {
        this.surveyPassing = surveyPassing;
        this.schoolAccreditation = schoolAccreditation;
    }

    @Override
    public Long getId() {
        return surveyPassing.getId();
    }

    @Override
    public int getYear() {
        return surveyPassing.getYear();
    }

    @Override
    public School getSchool() {
        return surveyPassing.getSchool();
    }

    @Override
    public SchoolAccreditation getSchoolAccreditation() {
        /*if (schoolAccreditation == null) {
            schoolAccreditation = new OrmLiteSchoolAccreditation(surveyPassing.getSurvey(), surveyProgress);
        }*/
        return schoolAccreditation;
    }

    @Override
    public Date getStartDate() {
        return surveyPassing.getStartDate();
    }

    @Override
    public Date getEndDate() {
        return surveyPassing.getEndDate();
    }
}
