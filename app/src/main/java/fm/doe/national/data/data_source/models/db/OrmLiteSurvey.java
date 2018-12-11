package fm.doe.national.data.data_source.models.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;
import java.util.List;


@DatabaseTable
public class OrmLiteSurvey {

    public interface Column {
        String ID = "id";
        String VERSION = "version";
        String SURVEY_ITEMS = "surveyItems";
        String TYPE = "type";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    protected long id;

    @DatabaseField(columnName = Column.VERSION)
    protected int version;

    @DatabaseField(columnName = Column.TYPE)
    protected String type;

    @ForeignCollectionField(eager = true, columnName = Column.SURVEY_ITEMS)
    protected Collection<OrmLiteSurveyItem> surveyItems;

    public OrmLiteSurvey() {
    }

    public OrmLiteSurvey(int version, String type) {
        this.version = version;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public String getType() {
        return type;
    }

    public Collection<OrmLiteSurveyItem> getSurveyItems() {
        return surveyItems;
    }

    public void setSurveyItems(List<OrmLiteSurveyItem> surveyItems) {
        this.surveyItems = surveyItems;
    }
}
