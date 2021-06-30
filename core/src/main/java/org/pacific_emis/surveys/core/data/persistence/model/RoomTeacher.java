package org.pacific_emis.surveys.core.data.persistence.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.pacific_emis.surveys.core.data.model.Teacher;
import org.pacific_emis.surveys.core.data.model.abstract_implementations.TeacherImpl;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;

@Entity(indices = {@Index("id")})
public class RoomTeacher extends TeacherImpl {

    @PrimaryKey
    @NonNull
    public Integer id;

    public String name;

    public AppRegion appRegion;

    public RoomTeacher(String name, int id, AppRegion appRegion) {
        this.name = name;
        this.id = id;
        this.appRegion = appRegion;
    }

    public RoomTeacher(@NonNull Teacher other) {
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
    public Integer getId() {
        return id;
    }

    @NonNull
    @Override
    public AppRegion getAppRegion() {
        return appRegion;
    }
}