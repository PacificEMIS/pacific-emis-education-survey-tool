package fm.doe.national.data.data_source.models.db;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.data_source.models.SynchronizePlatform;
import fm.doe.national.data.data_source.models.Answer;

@DatabaseTable
public class OrmLiteAnswer implements Answer {

    public interface Column {
        String ID = "id";
        String ANSWER = "answer";
        String SYNCHRONIZED_PLATFORMS = "synchronizePlatforms";
        String PARENT_ITEM = "parentItem";
        String SURVEY_RESULT = "surveyResult";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    protected long id;

    @DatabaseField(columnName = Column.ANSWER)
    protected boolean answer;

    @DatabaseField(dataType = DataType.SERIALIZABLE, columnName = Column.SYNCHRONIZED_PLATFORMS)
    protected ArrayList<SynchronizePlatform> synchronizePlatforms;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.PARENT_ITEM)
    protected OrmLiteSurveyItem parentSurveyItem;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.SURVEY_RESULT)
    protected OrmLiteBaseSurveyResult surveyResult;

    public OrmLiteAnswer() {
    }

    public OrmLiteAnswer(boolean answer, OrmLiteSurveyItem parentSurveyItem, OrmLiteBaseSurveyResult surveyResult) {
        this.answer = answer;
        this.parentSurveyItem = parentSurveyItem;
        this.surveyResult = surveyResult;
        this.synchronizePlatforms = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean getAnswer() {
        return answer;
    }

    @Override
    public void setAnswer(boolean answer) {
        if (this.answer != answer) {
            this.answer = answer;
            synchronizePlatforms.clear();
        }
    }

    @Override
    public List<SynchronizePlatform> getSynchronizedPlatforms() {
        return synchronizePlatforms;
    }

    @Override
    public void addSynchronizedPlatform(SynchronizePlatform platform) {
        if (!synchronizePlatforms.contains(platform)) {
            synchronizePlatforms.add(platform);
        }
    }

    public OrmLiteSurveyItem getParentSurveyItem() {
        return parentSurveyItem;
    }

    public OrmLiteBaseSurveyResult getSurveyResult() {
        return surveyResult;
    }
}
