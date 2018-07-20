package fm.doe.national.data_source.db.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

import fm.doe.national.models.survey.School;

@DatabaseTable
public class OrmLiteSchool implements School {

    public interface Column {
        String ID = "id";
        String NAME = "name";
        String SURVEYS = "surveys";
    }

    @DatabaseField(id = true, columnName = Column.ID)
    protected String id;
    @DatabaseField(columnName = Column.NAME)
    protected String name;
    @Nullable
    @ForeignCollectionField(eager = true, columnName = Column.SURVEYS)
    protected ForeignCollection<OrmLiteSurvey> surveys;

    public OrmLiteSchool() {
    }

    public OrmLiteSchool(String name) {
        // TODO delete it when backend will be ready
        this(name, name);
    }

    public OrmLiteSchool(String id, String name) {
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

    @Nullable
    public Collection<OrmLiteSurvey> getSurveys() {
        return surveys;
    }
}
