package fm.doe.national.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.model.Category;
import fm.doe.national.data.model.Standard;

public class MutableCategory extends BaseMutableEntity implements Category {

    private String title;
    private List<MutableStandard> standards;

    public MutableCategory(@NonNull Category other) {
        this.id = other.getId();
        this.title = other.getTitle();

        if (other.getStandards() == null) {
            return;
        }

        ArrayList<MutableStandard> standards = new ArrayList<>();
        for (Standard standard : other.getStandards()) {
            standards.add(new MutableStandard(standard));
        }
        this.standards = standards;
    }

    public MutableCategory() {
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public List<MutableStandard> getStandards() {
        return standards;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStandards(List<MutableStandard> standards) {
        this.standards = standards;
    }
}
