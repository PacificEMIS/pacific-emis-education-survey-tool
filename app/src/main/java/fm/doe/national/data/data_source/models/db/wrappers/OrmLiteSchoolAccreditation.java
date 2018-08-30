package fm.doe.national.data.data_source.models.db.wrappers;

import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.data_source.models.db.OrmLiteSurvey;

public class OrmLiteSchoolAccreditation implements SchoolAccreditation {

    private OrmLiteSurvey survey;

    private CategoryProgress progress;

    public OrmLiteSchoolAccreditation(OrmLiteSurvey survey, CategoryProgress progress) {
        this.survey = survey;
        this.progress = progress;
    }

    @Override
    public CategoryProgress getProgress() {
        return progress;
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
