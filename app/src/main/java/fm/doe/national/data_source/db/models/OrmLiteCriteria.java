package fm.doe.national.data_source.db.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.Collection;

import fm.doe.national.models.survey.Criteria;

public class OrmLiteCriteria implements Criteria {

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
    @Nullable
    @ForeignCollectionField(eager = true, columnName = Column.CRITERIAS)
    protected ForeignCollection<OrmLiteSubCriteria> criterias;

    public OrmLiteCriteria() {
    }

    public OrmLiteCriteria(String name, OrmLiteStandard standard) {
        this.name = name;
        this.standard = standard;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public long getStandardId() {
        return standard.getId();
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    public Collection<OrmLiteSubCriteria> getCriterias() {
        return criterias;
    }

}
