package fm.doe.national.data.models.survey;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

@DatabaseTable
public class Standard {

    public interface Column {
        String ID = "id";
        String NAME = "name";
        String GROUP_STANDARDS = "groupStandard";
        String CRITERIAS = "criterias";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    protected long id;

    @DatabaseField(columnName = Column.NAME)
    protected String name;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.GROUP_STANDARDS)
    protected GroupStandard groupStandard;

    @SerializedName("criteria")
    @ForeignCollectionField(eager = true, columnName = Column.CRITERIAS)
    protected Collection<Criteria> criterias;

    public Standard() {
    }

    public Standard(String name, GroupStandard groupStandard) {
        this.name = name;
        this.groupStandard = groupStandard;
    }

    public long getId() {
        return id;
    }

    public long getGroupStandardId() {
        return groupStandard.getId();
    }

    @NonNull
    public String getName() {
        return name;
    }

    public Collection<Criteria> getCriterias() {
        return criterias;
    }

    public void setGroupStandard(GroupStandard groupStandard) {
        this.groupStandard = groupStandard;
    }
}

