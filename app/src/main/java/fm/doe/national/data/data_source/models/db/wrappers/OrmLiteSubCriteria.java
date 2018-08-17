package fm.doe.national.data.data_source.models.db.wrappers;

import android.support.annotation.NonNull;

import com.j256.ormlite.table.DatabaseTable;

import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;

@DatabaseTable
public class OrmLiteSubCriteria implements SubCriteria {

    private OrmLiteSurveyItem surveyItem;

    public OrmLiteSubCriteria(OrmLiteSurveyItem surveyItem) {
        this.surveyItem = surveyItem;
    }

    @Override
    public Criteria getCriteria() {
        return new OrmLiteCriteria(surveyItem.getParentItem());
    }

    @NonNull
    @Override
    public String getName() {
        return surveyItem.getName();
    }

}
