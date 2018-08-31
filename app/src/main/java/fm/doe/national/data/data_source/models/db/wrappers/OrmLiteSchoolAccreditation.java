package fm.doe.national.data.data_source.models.db.wrappers;

import java.util.List;

import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.data_source.models.db.OrmLiteSurvey;
import fm.doe.national.data.data_source.models.serializable.LinkedGroupStandard;
import fm.doe.national.data.data_source.models.serializable.LinkedSchoolAccreditation;

public class OrmLiteSchoolAccreditation implements LinkedSchoolAccreditation {

    private OrmLiteSurvey survey;

    private CategoryProgress progress;

    private List<LinkedGroupStandard> groupStandards;

    public OrmLiteSchoolAccreditation(OrmLiteSurvey survey, CategoryProgress progress, List<LinkedGroupStandard> groupStandards) {
        this(survey, progress);
        this.groupStandards = groupStandards;
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
    public List<? extends LinkedGroupStandard> getGroupStandards() {
        return groupStandards;
    }
}
