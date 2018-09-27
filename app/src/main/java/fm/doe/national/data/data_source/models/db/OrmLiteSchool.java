package fm.doe.national.data.data_source.models.db;

import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

import fm.doe.national.data.data_source.models.School;

@DatabaseTable
public class OrmLiteSchool implements School {

    public interface Column {
        String ID = "id";
        String NAME = "name";
    }

    @DatabaseField(id = true, columnName = Column.ID)
    protected String id;

    @DatabaseField(columnName = Column.NAME)
    protected String name;

    public OrmLiteSchool() {
    }

    public OrmLiteSchool(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrmLiteSchool)) return false;
        OrmLiteSchool school = (OrmLiteSchool) o;
        return Objects.equals(getId(), school.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
