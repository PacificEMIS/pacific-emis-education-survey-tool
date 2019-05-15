package fm.doe.national.data.cloud.dropbox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BrowsingTreeObject implements Serializable {

    private static final String ROOT_PATH = "";
    private static final String ROOT_NAME = "~";

    @NonNull
    private String name = ROOT_NAME;

    @NonNull
    private String path = ROOT_PATH;

    private boolean isDirectory;

    @Nullable
    private BrowsingTreeObject parent = null;

    @NonNull
    private List<BrowsingTreeObject> childs = new ArrayList<>();

    @NonNull
    public String getPath() {
        return path;
    }

    public void setPath(@NonNull String path) {
        this.path = path;
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
