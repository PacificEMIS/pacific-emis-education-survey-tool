package fm.doe.national.core.data.model.mutable;

import androidx.annotation.NonNull;

import fm.doe.national.core.data.model.School;
import fm.doe.national.core.preferences.entities.AppRegion;

public final class MutableSchool implements School {

    private String id;
    private String name;
    private AppRegion appRegion;

    public MutableSchool(String id, String name, AppRegion appRegion) {
        this.id = id;
        this.name = name;
        this.appRegion = appRegion;
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

    @NonNull
    @Override
    public AppRegion getAppRegion() {
        return appRegion;
    }

    public void setAppRegion(AppRegion appRegion) {
        this.appRegion = appRegion;
    }
}
