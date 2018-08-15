package fm.doe.national.data.data_source.models.survey.db;

import com.j256.ormlite.dao.ForeignCollection;
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
    protected ForeignCollection<OrmLiteSurveyItem> childrenItems;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public OrmLiteSurveyItem getParentItem() {
        return parentItem;
    }

    public Collection<OrmLiteSurveyItem> getChildrenItems() {
        return childrenItems;
    }
}
