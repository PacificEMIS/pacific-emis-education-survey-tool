package fm.doe.national.data.data_source.models.db.wrappers;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;

public class OrmLiteCriteria implements Criteria{

    private OrmLiteSurveyItem surveyItem;

    private OrmLiteStandard standard;

    private List<OrmLiteSubCriteria> subCriterias;

    public OrmLiteCriteria(OrmLiteSurveyItem surveyItem, OrmLiteStandard standard) {
        this.surveyItem = surveyItem;
        this.standard = standard;
    }

    @Override
    public Long getId() {
        return surveyItem.getId();
    }

    @Override
    public Standard getStandard() {
        return standard;
    }

    @NonNull
    @Override
    public String getName() {
        return surveyItem.getName();
    }

    @Override
    public List<? extends SubCriteria> getSubCriterias() {
        if (subCriterias == null) {
            subCriterias = new ArrayList<>();
            for (OrmLiteSurveyItem surveyItem : surveyItem.getChildrenItems()) {
                subCriterias.add(new OrmLiteSubCriteria(surveyItem, this));
            }
        }

        return subCriterias;
    }

}
