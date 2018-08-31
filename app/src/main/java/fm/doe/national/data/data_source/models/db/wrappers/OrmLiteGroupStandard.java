package fm.doe.national.data.data_source.models.db.wrappers;

import android.support.annotation.NonNull;

import java.util.List;

import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;
import fm.doe.national.data.data_source.models.serializable.LinkedGroupStandard;
import fm.doe.national.data.data_source.models.serializable.LinkedStandard;

public class OrmLiteGroupStandard implements LinkedGroupStandard {

    private OrmLiteSurveyItem surveyItem;

    private CategoryProgress progress;

    private List<LinkedStandard> standards;

    public OrmLiteGroupStandard(OrmLiteSurveyItem surveyItem, CategoryProgress progress, List<LinkedStandard> standards) {
        this(surveyItem, progress);
        this.standards = standards;
    }

    public OrmLiteGroupStandard(OrmLiteSurveyItem surveyItem, CategoryProgress progress) {
        this.surveyItem = surveyItem;
        this.progress = progress;
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
    public CategoryProgress getCategoryProgress() {
        return progress;
    }

    @Override
    public List<? extends LinkedStandard> getStandards() {
        return standards;
    }
}
