package fm.doe.national.data.data_source.models.db.wrappers;

import java.util.Date;

import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.data.data_source.models.db.OrmLiteBaseSurveyResult;

public class OrmLiteSchoolAccreditationPassing implements SchoolAccreditationPassing {

    private OrmLiteBaseSurveyResult surveyResult;

    public OrmLiteSchoolAccreditationPassing(OrmLiteBaseSurveyResult surveyResult) {
        this.surveyResult = surveyResult;
    }

    @Override
    public int getYear() {
        return surveyResult.getYear();
    }

    @Override
    public School getSchool() {
        return surveyResult.getSchool();
    }

    @Override
    public SchoolAccreditation getSchoolAccreditation() {
        return new OrmLiteSchoolAccreditation(surveyResult.getSurvey());
    }

    @Override
    public Date getStartDate() {
        return surveyResult.getStartDate();
    }

    @Override
    public Date getEndDate() {
        return surveyResult.getEndDate();
    }
}
