package fm.doe.national.data.data_source.models.db;

import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

@DatabaseTable
public class OrmLiteSurveyItem {

    public interface Column {
        String ID = "id";
        String NAME = "name";
        String TYPE = "type";
        String PARENT = "parent";
        String SURVEY = "survey";
        String CHILDREN = "children";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    protected long id;

    @DatabaseField(columnName = Column.NAME)
    protected String name;

    @DatabaseField(columnName = Column.TYPE)
    protected String type;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.PARENT)
    protected OrmLiteSurveyItem parentItem;

    @ForeignCollectionField(eager = true, columnName = Column.CHILDREN)
    protected Collection<OrmLiteSurveyItem> childrenItems;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.SURVEY)
    protected OrmLiteBaseSurvey survey;

    public OrmLiteSurveyItem() {
    }

    public OrmLiteSurveyItem(String name, String type, OrmLiteBaseSurvey survey, OrmLiteSurveyItem parentItem) {
        this.name = name;
        this.type = type;
        this.survey = survey;
        this.parentItem = parentItem;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public OrmLiteSurveyItem getParentItem() {
        return parentItem;
    }

    public void setParentItem(OrmLiteSurveyItem parentItem) {
        this.parentItem = parentItem;
    }

    public Collection<OrmLiteSurveyItem> getChildrenItems() {
        return childrenItems;
    }

    public void addChild(OrmLiteSurveyItem surveyItem) {
        surveyItem.setParentItem(this);
        childrenItems.add(surveyItem);
    }

    public OrmLiteBaseSurvey getSurvey() {
        return survey == null ? parentItem.getSurvey() : survey;
    }
}
