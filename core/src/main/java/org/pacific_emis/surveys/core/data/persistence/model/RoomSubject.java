package org.pacific_emis.surveys.core.data.persistence.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.pacific_emis.surveys.core.data.model.Subject;
import org.pacific_emis.surveys.core.data.model.abstract_implementations.SubjectImpl;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;

@Entity(indices = {@Index("id")})
public class RoomSubject extends SubjectImpl {

    @PrimaryKey
    @NonNull
    public String id;

    public String name;

    public AppRegion appRegion;

    public RoomSubject(String name, String id, AppRegion appRegion) {
        this.name = name;
        this.id = id;
        this.appRegion = appRegion;
    }

    public RoomSubject(@NonNull Subject other) {
        this.name = other.getName();
        this.id = other.getId();
        this.appRegion = other.getAppRegion();
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String getId() {
        return id;
    }

    @NonNull
    @Override
    public AppRegion getAppRegion() {
        return appRegion;
    }
}

