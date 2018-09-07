package fm.doe.national.ui.screens.standard;

public class PhotoViewData {
    private String path;
    private Type type;

    public PhotoViewData(String path, Type type) {
        this.path = path;
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public Type getType() {
        return type;
    }

    enum Type {
        PHOTO, ADDER
    }
}
