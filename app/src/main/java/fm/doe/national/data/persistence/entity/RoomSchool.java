package fm.doe.national.data.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import fm.doe.national.data.model.School;

@Entity(indices = {@Index("uid")})
public class RoomSchool implements School {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String name;

    public String suffix;

    public RoomSchool(String name, String suffix) {
        this.name = name;
        this.suffix = suffix;
    }

    public RoomSchool(School other) {
        this.name = other.getName();
        this.suffix = other.getIdentifier();
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String getIdentifier() {
        return suffix;
    }

    @Override
    public long getId() {
        return uid;
    }

}
