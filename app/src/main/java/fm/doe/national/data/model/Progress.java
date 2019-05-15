package fm.doe.national.data.model;

public class Progress {
    public int total;
    public int completed;

    public static Progress createEmptyProgress() {
        return new Progress(0, 0);
    }

    public Progress(int total, int completed) {
        this.total = total;
        this.completed = completed;
    }

    public void add(Progress other) {
        this.total += other.total;
        this.completed += other.completed;
    }
}
