package fm.doe.national.core.data.model.mutable;

import fm.doe.national.core.data.model.Progress;

public class MutableProgress implements Progress {
    public int total;
    public int completed;

    public static MutableProgress createEmptyProgress() {
        return new MutableProgress(0, 0);
    }

    public MutableProgress(int total, int completed) {
        this.total = total;
        this.completed = completed;
    }

    public void add(Progress other) {
        this.total += other.getTotal();
        this.completed += other.getCompleted();
    }

    @Override
    public int getTotal() {
        return total;
    }

    @Override
    public int getCompleted() {
        return completed;
    }

    @Override
    public boolean isFinished() {
        return total == completed;
    }
}
