package fm.doe.national.data.data_source.db.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fm.doe.national.models.survey.GroupStandard;
import fm.doe.national.models.survey.Standard;

@DatabaseTable
public class OrmLiteGroupStandard implements GroupStandard {

    public interface Column {
        String ID = "id";
        String STANDARDS = "standards";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    protected long id;

    @Nullable
    @SerializedName("standard")
    @ForeignCollectionField(eager = true, columnName = Column.STANDARDS)
    protected ForeignCollection<OrmLiteStandard> standards;

    @Override
    public long getId() {
        return id;
    }

    @NonNull
    @Override
    public String getName() {
        if (standards == null || standards.isEmpty()) {
            return  "";
        }
        OrmLiteStandard[] standards = (OrmLiteStandard[]) this.standards.toArray();
        return standards[0].getName();
    }

    @Override
    public List<Standard> getStandards() {
        return (standards == null) ? Collections.EMPTY_LIST : new ArrayList<>(standards);
    }

}
