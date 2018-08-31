package fm.doe.national.data.data_source.models.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.CategoryProgress;

@DatabaseTable
public class OrmLiteCategoryProgress implements CategoryProgress {

    public interface Column {
        String ID = "id";
        String ANSWERED_QUESTIONS = "answeredQuestionsCount";
        String TOTAL_QUESTIONS_COUNT = "totalQuestionsCount";
        String SURVEY_ITEM = "surveyItem";
        String SURVEY_PASSING = "surveyPassing";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    protected long id;

    @DatabaseField(columnName = Column.ANSWERED_QUESTIONS)
    protected int answeredQuestionsCount;

    @DatabaseField(columnName = Column.TOTAL_QUESTIONS_COUNT)
    protected int totalQuestionsCount;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.SURVEY_ITEM)
    protected OrmLiteSurveyItem surveyItem;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.SURVEY_PASSING)
    protected OrmLiteSurveyPassing surveyPassing;

    public OrmLiteCategoryProgress() {
    }

    public OrmLiteCategoryProgress(OrmLiteSurveyItem surveyItem, OrmLiteSurveyPassing surveyPassing) {
        this.surveyItem = surveyItem;
        this.surveyPassing = surveyPassing;
        this.totalQuestionsCount = OrmLiteSurveyItem.getTotalChildrenCount(surveyItem);
    }

    public long getId() {
        return id;
    }

    @Override
    public int getTotalQuestionsCount() {
        return totalQuestionsCount;
    }

    public void setTotalQuestionsCount(int totalQuestionsCount) {
        this.totalQuestionsCount = totalQuestionsCount;
    }

    @Override
    public int getAnsweredQuestionsCount() {
        return answeredQuestionsCount;
    }

    public void setAnsweredQuestionsCount(int answeredQuestionsCount) {
        this.answeredQuestionsCount = answeredQuestionsCount;
    }

    @Override
    public void recalculate(Answer.State previousState, Answer.State state) {
        if (previousState == state) {
            return;
        }
        if (previousState != Answer.State.NOT_ANSWERED && state != Answer.State.NOT_ANSWERED) {
            return;
        }

        if (state == Answer.State.NOT_ANSWERED) {
            answeredQuestionsCount--;
        } else {
            answeredQuestionsCount++;
        }
    }

    public OrmLiteSurveyItem getSurveyItem() {
        return surveyItem;
    }

    public OrmLiteSurveyPassing getSurveyPassing() {
        return surveyPassing;
    }

}
