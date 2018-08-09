package fm.doe.national.data.models.survey;

import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class SubCriteria {

    public interface Column {
        String ID = "id";
        String NAME = "name";
        String CRITERIA = "criteria";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    protected long id;

    @DatabaseField(columnName = Column.NAME)
    protected String name;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.CRITERIA)
    protected Criteria criteria;

    public SubCriteria() {
    }

    public SubCriteria(String name, Criteria criteria) {
        this.name = name;
        this.criteria = criteria;
    }

    public long getId() {
        return id;
    }

    public long getCriteriaId() {
        return criteria.getId();
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    @NonNull
    public String getName() {
        return name;
    }

}
