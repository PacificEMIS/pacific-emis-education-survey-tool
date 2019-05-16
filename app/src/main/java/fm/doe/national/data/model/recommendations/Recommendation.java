package fm.doe.national.data.model.recommendations;

public abstract class Recommendation<T> {
    private String content;
    private T object;
    private int level;

    public Recommendation(T object, String content, int level) {
        this.object = object;
        this.content = content;
        this.level = level;
    }

    public String getContent() {
        return content;
    }

    public int getLevel() {
        return level;
    }

    public T getObject() {
        return object;
    }
}
