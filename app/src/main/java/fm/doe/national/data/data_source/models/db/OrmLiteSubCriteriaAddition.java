package fm.doe.national.data.data_source.models.db;

import android.support.annotation.Nullable;

import com.j256.ormlite.field.DatabaseField;

import fm.doe.national.data.data_source.models.SubCriteriaAddition;

public class OrmLiteSubCriteriaAddition implements SubCriteriaAddition {

    public interface Column {
        String ID = "id";
        String INTERVIEW_QUESTIONS = "answeredQuestionsCount";
        String HINT = "totalQuestionsCount";
        String SURVEY_ITEM = "surveyItem";
    }

    @DatabaseField(generatedId = true, columnName = OrmLiteSubCriteriaAddition.Column.ID)
    protected long id;

    @Nullable
    @DatabaseField(columnName = OrmLiteSubCriteriaAddition.Column.INTERVIEW_QUESTIONS, canBeNull = true)
    protected String interviewQuestions;

    @Nullable
    @DatabaseField(columnName = OrmLiteSubCriteriaAddition.Column.HINT, canBeNull = true)
    protected String hint;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true,
            columnName = OrmLiteSubCriteriaAddition.Column.SURVEY_ITEM)
    protected OrmLiteSurveyItem surveyItem;


    public OrmLiteSubCriteriaAddition() {
    }

    public OrmLiteSubCriteriaAddition(@Nullable String interviewQuestions, @Nullable String hint, OrmLiteSurveyItem surveyItem) {
        this.interviewQuestions = interviewQuestions;
        this.hint = hint;
        this.surveyItem = surveyItem;
    }

    public long getId() {
        return id;
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
