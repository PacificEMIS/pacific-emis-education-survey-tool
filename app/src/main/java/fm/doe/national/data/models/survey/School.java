package fm.doe.national.data.models.survey;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;

@DatabaseTable
public class School {

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
    protected ArrayList<Survey> surveys;

    public School() {
    }

    public School(String name) {
        // TODO delete it when backend will be ready
        this(name, name);
    }

    public School(String id, String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public Collection<Survey> getSurveys() {
        return surveys;
    }
}
