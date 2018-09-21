package fm.doe.national.data.data_source.models.db.wrappers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;
import fm.doe.national.data.data_source.models.serializable.LinkedStandard;

public class OrmLiteStandard implements LinkedStandard {

    private OrmLiteSurveyItem surveyItem;

    private CategoryProgress progress;

    private List<Criteria> criterias;

    public OrmLiteStandard(OrmLiteSurveyItem surveyItem, CategoryProgress progress, List<Criteria> criterias) {
        this(surveyItem, progress);
        this.criterias = criterias;
    }

    public OrmLiteStandard(OrmLiteSurveyItem surveyItem, CategoryProgress progress) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrmLiteStandard)) return false;
        OrmLiteStandard that = (OrmLiteStandard) o;
        return this.getId() == that.getId();
    }

    @Override
    public List<? extends Criteria> getCriterias() {
        return criterias;
    }

    @Nullable
    @Override
    public Integer getIcon() {
        return surveyItem.getIcon();
    }
}

