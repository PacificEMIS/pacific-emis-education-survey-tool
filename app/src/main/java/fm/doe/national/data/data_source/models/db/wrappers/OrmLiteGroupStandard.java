package fm.doe.national.data.data_source.models.db.wrappers;

import android.support.annotation.NonNull;

import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;

public class OrmLiteGroupStandard implements GroupStandard {

    private OrmLiteSurveyItem surveyItem;

    private CategoryProgress progress;

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

}
