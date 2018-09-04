package fm.doe.national.data.data_source.models.db.wrappers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;

public class OrmLiteSubCriteria implements SubCriteria {

    private OrmLiteSurveyItem surveyItem;

    private Answer answer;

    @Nullable
    private String interviewQuestions = null;

    @Nullable
    private String hint = null;

    public OrmLiteSubCriteria(OrmLiteSurveyItem surveyItem, Answer answer) {
        this.surveyItem = surveyItem;
        this.answer = answer;
    }

    public OrmLiteSubCriteria(OrmLiteSurveyItem surveyItem, Answer answer,
                              @Nullable String interviewQuestions, @Nullable String hint) {
        this.surveyItem = surveyItem;
        this.answer = answer;
        this.interviewQuestions = interviewQuestions;
        this.hint = hint;
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

    @Nullable
    @Override
    public String getInterviewQuestions() {
        return interviewQuestions;
    }

    @Nullable
    @Override
    public String getHint() {
        return hint;
    }
}