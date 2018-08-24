package fm.doe.national.data.cloud;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CloudDirectory implements Serializable {

    @NonNull
    private String data;

    @NonNull
    private String name;

    @NonNull
    private List<CloudDirectory> subDirectories = new ArrayList<>();

    @Nullable
    private CloudDirectory parent;

    public CloudDirectory(@NonNull String name, String data) {
        this.data = data;
        this.name = name;
    }

    @NonNull
    public String getData() {
        return data;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public List<CloudDirectory> getSubDirectories() {
        return subDirectories;
    }

    public void addSubDirectory(@NonNull CloudDirectory directory) {
        directory.setParent(this);
        subDirectories.add(directory);
    }

    @Nullable
    public CloudDirectory getParent() {
        return parent;
    }

    public void setParent(@NonNull CloudDirectory parent) {
        this.parent = parent;
    }
}
