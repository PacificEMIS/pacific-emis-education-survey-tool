package fm.doe.national.data.data_source.models.db;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SynchronizePlatform;

@DatabaseTable
public class OrmLiteAnswersCount {

    public interface Column {
        String ID = "id";
        String STATE = "state";
        String SYNCHRONIZED_PLATFORMS = "synchronizePlatforms";
        String PARENT_ITEM = "parentItem";
        String SURVEY_PASSING = "surveyPassing";
    }

    @DatabaseField(generatedId = true, columnName = OrmLiteAnswer.Column.ID)
    protected long id;

    @DatabaseField(columnName = OrmLiteAnswer.Column.STATE, dataType = DataType.ENUM_STRING)
    protected Answer.State state;

    @DatabaseField(dataType = DataType.SERIALIZABLE, columnName = OrmLiteAnswer.Column.SYNCHRONIZED_PLATFORMS)
    protected ArrayList<SynchronizePlatform> synchronizePlatforms;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = OrmLiteAnswer.Column.PARENT_ITEM)
    protected OrmLiteSurveyItem parentSurveyItem;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = OrmLiteAnswer.Column.SURVEY_PASSING)
    protected OrmLiteSurveyPassing surveyPassing;

    public OrmLiteAnswersCount() {
    }

    public OrmLiteAnswersCount(Answer.State state, OrmLiteSurveyItem parentSurveyItem, OrmLiteSurveyPassing surveyPassing) {
        this.state = state;
        this.parentSurveyItem = parentSurveyItem;
        this.surveyPassing = surveyPassing;
        this.synchronizePlatforms = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public Answer.State getState() {
        return state;
    }

    public void setState(Answer.State state) {
        if (this.state != state) {
            this.state = state;
            synchronizePlatforms.clear();
        }
    }

    public List<SynchronizePlatform> getSynchronizedPlatforms() {
        return synchronizePlatforms;
    }

    public void addSynchronizedPlatform(SynchronizePlatform platform) {
        if (!synchronizePlatforms.contains(platform)) {
            synchronizePlatforms.add(platform);
        }
    }

    public OrmLiteSurveyItem getParentSurveyItem() {
        return parentSurveyItem;
    }

    public OrmLiteSurveyPassing getSurveyPassing() {
        return surveyPassing;
    }

}
