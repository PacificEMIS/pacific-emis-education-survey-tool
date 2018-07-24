package fm.doe.national.data.data_source.db.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

import fm.doe.national.models.survey.Standard;

@DatabaseTable
public class OrmLiteStandard implements Standard {

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
    protected OrmLiteGroupStandard groupStandard;

    @Nullable
    @SerializedName("criteria")
    @ForeignCollectionField(eager = true, columnName = Column.CRITERIAS)
    protected ForeignCollection<OrmLiteCriteria> criterias;

    public OrmLiteStandard() {
    }

    public OrmLiteStandard(String name, OrmLiteGroupStandard groupStandard) {
        this.name = name;
        this.groupStandard = groupStandard;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public long getGroupStandardId() {
        return groupStandard.getId();
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    public Collection<OrmLiteCriteria> getCriterias() {
        return criterias;
    }

}

