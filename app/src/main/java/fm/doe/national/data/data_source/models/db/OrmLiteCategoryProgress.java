package fm.doe.national.data.data_source.models.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fm.doe.national.data.data_source.models.CategoryProgress;

@DatabaseTable
public class OrmLiteCategoryProgress implements CategoryProgress {

    public interface Column {
        String ID = "id";
        String COMPLETED_ITEMS_COUNT = "completedItemsCount";
        String TOTAL_ITEMS_COUNT = "totalItemsCount";
        String SURVEY_ITEM = "surveyItem";
        String SURVEY_PASSING = "surveyPassing";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    protected long id;

    @DatabaseField(columnName = Column.COMPLETED_ITEMS_COUNT)
    protected int completedItemsCount;

    @DatabaseField(columnName = Column.TOTAL_ITEMS_COUNT)
    protected int totalItemsCount;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.SURVEY_ITEM)
    protected OrmLiteSurveyItem surveyItem;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.SURVEY_PASSING)
    protected OrmLiteSurveyPassing surveyPassing;

    public OrmLiteCategoryProgress() {
    }

    public OrmLiteCategoryProgress(OrmLiteSurveyItem surveyItem, OrmLiteSurveyPassing surveyPassing) {
        this.surveyItem = surveyItem;
        this.surveyPassing = surveyPassing;
        this.totalItemsCount = OrmLiteSurveyItem.getTotalChildrenSize(surveyItem);
    }

    public long getId() {
        return id;
    }

    @Override
    public int getTotalItemsCount() {
        return totalItemsCount;
    }

    public void setTotalItemsCount(int totalItemsCount) {
        this.totalItemsCount = totalItemsCount;
    }

    @Override
    public int getCompletedItemsCount() {
        return completedItemsCount;
    }

    public void setCompletedItemsCount(int completedItemsCount) {
        this.completedItemsCount = completedItemsCount;
    }

    public void incrementCompletedItems() {
        this.completedItemsCount++;
    }

    public void decrementCompletedItems() {
        this.completedItemsCount--;
    }

    public OrmLiteSurveyItem getSurveyItem() {
        return surveyItem;
    }

    public OrmLiteSurveyPassing getSurveyPassing() {
        return surveyPassing;
    }

}
