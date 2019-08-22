package fm.doe.national.core.data.persistence.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import fm.doe.national.core.data.model.School;
import fm.doe.national.core.preferences.entities.AppRegion;

@Entity(indices = {@Index("id")})
public class RoomSchool implements School {

    @PrimaryKey
    @NonNull
    public String id;

    public String name;

    public AppRegion appRegion;

    public RoomSchool(String name, String id, AppRegion appRegion) {
        this.name = name;
        this.id = id;
        this.appRegion = appRegion;
    }

    public RoomSchool(@NonNull School other) {
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
