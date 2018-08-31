package fm.doe.national.data.data_source.models.db.wrappers;

import android.support.annotation.NonNull;

import java.util.List;

import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;

public class OrmLiteCriteria implements Criteria{

    private OrmLiteSurveyItem surveyItem;

    private List<OrmLiteSubCriteria> subCriterias;

    private CategoryProgress progress;

    public OrmLiteCriteria(OrmLiteSurveyItem surveyItem, List<OrmLiteSubCriteria> subCriterias, CategoryProgress progress) {
        this.surveyItem = surveyItem;
        this.progress = progress;
        this.subCriterias = subCriterias;
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
    public List<? extends SubCriteria> getSubCriterias() {
        return subCriterias;
    }

}
