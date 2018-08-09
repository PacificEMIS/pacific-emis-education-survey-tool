package fm.doe.national.data.models.survey;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.Collection;

public class Criteria {

    public interface Column {
        String ID = "id";
        String NAME = "name";
        String STANDARD = "standard";
        String CRITERIAS = "criterias";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    protected long id;

    @DatabaseField(columnName = Column.NAME)
    protected String name;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.STANDARD)
    protected Standard standard;

    @SerializedName("subcriteria")
    @ForeignCollectionField(eager = true, columnName = Column.CRITERIAS)
    protected Collection<SubCriteria> subCriterias;

    public Criteria() {
    }

    public Criteria(String name, Standard standard) {
        this.name = name;
        this.standard = standard;
    }

    public long getId() {
        return id;
    }

    public long getStandardId() {
        return standard.getId();
    }

    public void setStandard(Standard standard) {
        this.standard = standard;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public Collection<SubCriteria> getSubCriterias() {
        return subCriterias;
    }

}
