package fm.doe.national.data.data_source.models.db.wrappers;

import android.support.annotation.NonNull;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.SubCriteriaAddition;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;

public class OrmLiteSubCriteria implements SubCriteria {

    private OrmLiteSurveyItem surveyItem;

    private Answer answer;

    private SubCriteriaAddition addition;

    public OrmLiteSubCriteria(OrmLiteSurveyItem surveyItem, Answer answer, SubCriteriaAddition addition) {
        this.surveyItem = surveyItem;
        this.answer = answer;
        this.addition = addition;
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

    @Override
    public SubCriteriaAddition getSubCriteriaAddition() {
        return null;
    }

}