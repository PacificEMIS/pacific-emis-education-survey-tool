package fm.doe.national.data.data_source.db.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.Collection;

import fm.doe.national.models.survey.Criteria;

public class OrmLiteCriteria {

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
    protected OrmLiteStandard standard;

    @SerializedName("subcriteria")
    @ForeignCollectionField(eager = true, columnName = Column.CRITERIAS)
    protected Collection<OrmLiteSubCriteria> subCriterias;

    public OrmLiteCriteria() {
    }

    public OrmLiteCriteria(String name, OrmLiteStandard standard) {
        this.name = name;
        this.standard = standard;
    }

    public long getId() {
        return id;
    }

    public long getStandardId() {
        return standard.getId();
    }

    public void setStandard(OrmLiteStandard standard) {
        this.standard = standard;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public Collection<OrmLiteSubCriteria> getSubCriterias() {
        return subCriterias;
    }

}
