package fm.doe.national.data.cloud.dropbox;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BrowsingTreeObject implements Serializable {

    @NonNull
    private String name = ""; // root folder name should be "" (Empty String)

    private boolean isDirectory;

    @Nullable
    private BrowsingTreeObject parent;

    @NonNull
    private List<BrowsingTreeObject> childs = new ArrayList<>();

    @NonNull
    public String getFullPath() {
        StringBuilder builder = new StringBuilder();
        if (parent != null) builder.append(parent.getFullPath()).append('/');
        builder.append(name);
        return builder.toString();
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    @Nullable
    public BrowsingTreeObject getParent() {
        return parent;
    }

    public void setParent(@Nullable BrowsingTreeObject parent) {
        this.parent = parent;
    }

    @NonNull
    public List<BrowsingTreeObject> getChilds() {
        return childs;
    }

    public void setChilds(@NonNull List<BrowsingTreeObject> childs) {
        this.childs = childs;
    }

    public void addChild(@NonNull BrowsingTreeObject child) {
        childs.add(child);
    }
}
