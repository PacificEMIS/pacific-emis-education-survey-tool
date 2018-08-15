package fm.doe.national.data.data_source.models.survey.db;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

import fm.doe.national.data.data_source.models.survey.Criteria;
import fm.doe.national.data.data_source.models.survey.Standard;
import fm.doe.national.data.data_source.models.survey.SubCriteria;

@DatabaseTable
public class OrmLiteCriteria implements Criteria{

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

    @Override
    public Standard getStandard() {
        return standard;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<? extends SubCriteria> getSubCriterias() {
        return subCriterias;
    }

    public void setSubCriterias(Collection<OrmLiteSubCriteria> subCriterias) {
        this.subCriterias = subCriterias;
    }
}
