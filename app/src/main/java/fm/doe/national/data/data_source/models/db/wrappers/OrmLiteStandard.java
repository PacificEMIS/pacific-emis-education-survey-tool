package fm.doe.national.data.data_source.models.db.wrappers;

import android.support.annotation.NonNull;

import java.util.List;

import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;

public class OrmLiteStandard implements Standard {

    private OrmLiteSurveyItem surveyItem;

    private CategoryProgress progress;


    public OrmLiteStandard(OrmLiteSurveyItem surveyItem, CategoryProgress progress) {
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

