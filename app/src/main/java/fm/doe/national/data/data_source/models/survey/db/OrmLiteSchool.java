package fm.doe.national.data.data_source.models.survey.db;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

import fm.doe.national.data.data_source.models.survey.School;
import fm.doe.national.data.data_source.models.survey.Survey;

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
    protected Collection<OrmLiteSurvey> surveys;

    public OrmLiteSchool() {
    }

    public OrmLiteSchool(String name) {
        // TODO delete it when backend will be ready
        this(name, name);
    }

    public OrmLiteSchool(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public Collection<? extends Survey> getSurveys() {
        return surveys;
    }
}
