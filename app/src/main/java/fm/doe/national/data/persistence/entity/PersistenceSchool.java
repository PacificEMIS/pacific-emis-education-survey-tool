package fm.doe.national.data.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import fm.doe.national.data.persistence.new_model.School;

@Entity(indices = {@Index("uid")})
public class PersistenceSchool implements School {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String name;

    public String suffix;

    public PersistenceSchool(String name, String suffix) {
        this.name = name;
        this.suffix = suffix;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String getSuffix() {
        return suffix;
    }

    @Override
    public long getId() {
        return uid;
    }

}
