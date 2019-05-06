package fm.doe.national.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.model.Criteria;
import fm.doe.national.data.model.Standard;

public class MutableStandard extends BaseMutableEntity implements Standard {

    private String title;
    private String suffix;
    private List<MutableCriteria> criterias;

    public MutableStandard() {
    }

    public MutableStandard(@NonNull Standard other) {
        this.id = other.getId();
        this.title = other.getTitle();
        this.suffix = other.getSuffix();

        if (other.getCriterias() == null) {
            return;
        }

        ArrayList<MutableCriteria> criterias = new ArrayList<>();
        for (Criteria criteria : other.getCriterias()) {
            criterias.add(new MutableCriteria(criteria));
        }
        this.criterias = criterias;
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @NonNull
    @Override
    public String getSuffix() {
        return suffix;
    }

    @Nullable
    @Override
    public List<MutableCriteria> getCriterias() {
        return criterias;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setCriterias(List<MutableCriteria> criterias) {
        this.criterias = criterias;
    }
}
