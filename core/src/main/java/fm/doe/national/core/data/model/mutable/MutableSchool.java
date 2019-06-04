package fm.doe.national.core.data.model.mutable;

import androidx.annotation.NonNull;

import fm.doe.national.core.data.model.School;

public final class MutableSchool implements School {

    private String id;
    private String name;

    public MutableSchool(String id, String name) {
        this.id = id;
        this.name = name;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
