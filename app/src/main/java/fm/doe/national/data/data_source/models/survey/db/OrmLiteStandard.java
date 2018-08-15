package fm.doe.national.data.data_source.models.survey.db;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

import fm.doe.national.data.data_source.models.survey.Criteria;
import fm.doe.national.data.data_source.models.survey.GroupStandard;
import fm.doe.national.data.data_source.models.survey.Standard;

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

    @SerializedName("criteria")
    @ForeignCollectionField(eager = true, columnName = Column.CRITERIAS)
    protected Collection<OrmLiteCriteria> criterias;

    public OrmLiteStandard() {
    }

    public OrmLiteStandard(String name, OrmLiteGroupStandard groupStandard) {
        this.name = name;
        this.groupStandard = groupStandard;
    }

    public long getId() {
        return id;
    }

    public long getGroupStandardId() {
        return groupStandard.getId();
    }

    @Override
    public GroupStandard getGroupStandard() {
        return groupStandard;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<? extends Criteria> getCriterias() {
        return criterias;
    }

    public void setGroupStandard(OrmLiteGroupStandard groupStandard) {
        this.groupStandard = groupStandard;
    }

    public void addCriterias(Collection<OrmLiteCriteria> criterias) {
        this.criterias.addAll(criterias);
    }
}

