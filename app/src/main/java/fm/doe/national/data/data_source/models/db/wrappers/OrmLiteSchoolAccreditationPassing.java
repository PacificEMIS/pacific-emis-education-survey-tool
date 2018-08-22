package fm.doe.national.data.data_source.models.db.wrappers;

import java.util.Date;

import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyPassing;

public class OrmLiteSchoolAccreditationPassing implements SchoolAccreditationPassing {

    private OrmLiteSurveyPassing surveyPassing;

    public OrmLiteSchoolAccreditationPassing(OrmLiteSurveyPassing surveyPassing) {
        this.surveyPassing = surveyPassing;
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
        return new OrmLiteSchoolAccreditation(surveyPassing.getSurvey());
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
