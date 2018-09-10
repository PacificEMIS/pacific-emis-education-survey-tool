package fm.doe.national.data.data_source.models.db.wrappers;

import android.support.annotation.NonNull;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.SubCriteriaQuestion;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;

public class OrmLiteSubCriteria implements SubCriteria {

    private OrmLiteSurveyItem surveyItem;

    private Answer answer;

    private SubCriteriaQuestion question;

    public OrmLiteSubCriteria(OrmLiteSurveyItem surveyItem, Answer answer, SubCriteriaQuestion question) {
        this.surveyItem = surveyItem;
        this.answer = answer;
        this.question = question;
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
    public SubCriteriaQuestion getSubCriteriaQuestion() {
        return question;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SubCriteria) {
            return ((SubCriteria) obj).getId().equals(getId());
        } else {
            return super.equals(obj);
        }
    }
}