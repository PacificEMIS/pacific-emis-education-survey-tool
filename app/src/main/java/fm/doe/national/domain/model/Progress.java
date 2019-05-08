package fm.doe.national.domain.model;

public class Progress {
    public int total;
    public int completed;

    public Progress(int total, int completed) {
        this.total = total;
        this.completed = completed;
    }

    public void add(Progress other) {
        this.total += other.total;
        this.completed += other.completed;
    }
}
