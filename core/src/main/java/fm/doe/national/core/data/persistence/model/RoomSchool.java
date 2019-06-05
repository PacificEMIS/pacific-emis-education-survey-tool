package fm.doe.national.core.data.persistence.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import fm.doe.national.core.data.model.School;

@Entity(indices = {@Index("id")})
public class RoomSchool implements School {

    @PrimaryKey
    @NonNull
    public String id;

    public String name;

    public RoomSchool(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public RoomSchool(@NonNull School other) {
        this.name = other.getName();
        this.id = other.getId();
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

}
