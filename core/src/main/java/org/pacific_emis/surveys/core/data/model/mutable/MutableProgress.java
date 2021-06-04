package org.pacific_emis.surveys.core.data.model.mutable;

import androidx.annotation.NonNull;

import org.pacific_emis.surveys.core.data.model.Progress;

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

    public static MutableProgress plus(Progress lv, Progress rv) {
        MutableProgress newProgress = createEmptyProgress();
        newProgress.add(lv);
        newProgress.add(rv);
        return newProgress;
    }

    @NonNull
    @Override
    public String toString() {
        return "MutableProgress(" + completed + "/" + total + ")";
    }
}
