package com.micronesia.data_source.db.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.micronesia.models.survey.GroupStandard;
import com.micronesia.models.survey.Standard;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable
public class OrmLiteGroupStandard implements GroupStandard {

    public interface Column {
        String ID = "id";
        String STANDARDS = "standards";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    protected long id;
    @Nullable
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
        return (standards == null) ? new ArrayList<>() : new ArrayList<>(standards);
    }

}
