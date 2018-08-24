package fm.doe.national.data.data_source.models.db.wrappers;

import android.support.annotation.NonNull;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;

public class OrmLiteSubCriteria implements SubCriteria {

    private OrmLiteSurveyItem surveyItem;

    private OrmLiteCriteria criteria;

    private Answer answer;

    public OrmLiteSubCriteria(OrmLiteSurveyItem surveyItem, OrmLiteCriteria criteria) {
        this.surveyItem = surveyItem;
        this.criteria = criteria;
    }

    @Override
    public Long getId() {
        return surveyItem.getId();
    }

    @Override
    public Criteria getCriteria() {
        return criteria;
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
    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
