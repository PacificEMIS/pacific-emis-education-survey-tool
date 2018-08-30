package fm.doe.national.data.data_source.models.db.wrappers;

import android.support.annotation.NonNull;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;

public class OrmLiteSubCriteria implements SubCriteria {

    private OrmLiteSurveyItem surveyItem;

    private Answer answer;

    public OrmLiteSubCriteria(OrmLiteSurveyItem surveyItem, Answer answer) {
        this.surveyItem = surveyItem;
        this.answer = answer;
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
    public Answer getAnswer() {
        return answer;
    }
    

}