package fm.doe.national.data.data_source.models.db.wrappers;

import java.util.List;

import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.db.OrmLiteSurvey;
import fm.doe.national.data.data_source.models.serializable.LinkedCategory;
import fm.doe.national.data.data_source.models.serializable.LinkedSchoolAccreditation;

public class OrmLiteSchoolAccreditation implements LinkedSchoolAccreditation {

    private OrmLiteSurvey survey;

    private CategoryProgress progress;

    private List<LinkedCategory> categories;

    public OrmLiteSchoolAccreditation(OrmLiteSurvey survey, CategoryProgress progress, List<LinkedCategory> categories) {
        this(survey, progress);
        this.categories = categories;
    }

    public OrmLiteSchoolAccreditation(OrmLiteSurvey survey, CategoryProgress progress) {
        this.survey = survey;
        this.progress = progress;
    }

    @Override
    public CategoryProgress getCategoryProgress() {
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

    @Override
    public List<? extends LinkedCategory> getCategories() {
        return categories;
    }
}
