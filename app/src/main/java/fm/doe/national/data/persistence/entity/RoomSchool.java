package fm.doe.national.data.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import fm.doe.national.data.model.School;

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

    public RoomSchool(@NotNull School other) {
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
