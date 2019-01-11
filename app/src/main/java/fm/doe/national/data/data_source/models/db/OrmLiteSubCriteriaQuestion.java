package fm.doe.national.data.data_source.models.db;


import com.j256.ormlite.field.DatabaseField;

import androidx.annotation.Nullable;
import fm.doe.national.data.data_source.models.SubCriteriaQuestion;

public class OrmLiteSubCriteriaQuestion implements SubCriteriaQuestion {

    public interface Column {
        String ID = "id";
        String INTERVIEW_QUESTION = "interviewQuestion";
        String HINT = "hint";
        String SURVEY_ITEM = "surveyItem";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    protected long id;

    @Nullable
    @DatabaseField(columnName = Column.INTERVIEW_QUESTION, canBeNull = true)
    protected String interviewQuestion;

    @Nullable
    @DatabaseField(columnName = Column.HINT, canBeNull = true)
    protected String hint;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true,
            columnName = Column.SURVEY_ITEM)
    protected OrmLiteSurveyItem surveyItem;


    public OrmLiteSubCriteriaQuestion() {
    }

    public OrmLiteSubCriteriaQuestion(@Nullable String interviewQuestion, @Nullable String hint, OrmLiteSurveyItem surveyItem) {
        this.interviewQuestion = interviewQuestion;
        this.hint = hint;
        this.surveyItem = surveyItem;
    }

    public long getId() {
        return id;
    }

    @Nullable
    @Override
    public String getInterviewQuestion() {
        return interviewQuestion;
    }

    @Nullable
    @Override
    public String getHint() {
        return hint;
    }
}
