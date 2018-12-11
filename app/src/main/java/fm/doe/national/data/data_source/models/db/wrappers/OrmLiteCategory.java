package fm.doe.national.data.data_source.models.db.wrappers;

import android.support.annotation.NonNull;

import java.util.List;

import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;
import fm.doe.national.data.data_source.models.serializable.LinkedCategory;
import fm.doe.national.data.data_source.models.serializable.LinkedStandard;

public class OrmLiteCategory implements LinkedCategory {

    private OrmLiteSurveyItem surveyItem;

    private CategoryProgress progress;

    private List<LinkedStandard> standards;

    public OrmLiteCategory(OrmLiteSurveyItem surveyItem, CategoryProgress progress, List<LinkedStandard> standards) {
        this(surveyItem, progress);
        this.standards = standards;
    }

    public OrmLiteCategory(OrmLiteSurveyItem surveyItem, CategoryProgress progress) {
        this.surveyItem = surveyItem;
        this.progress = progress;
    }

    @Override
    public long getId() {
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
