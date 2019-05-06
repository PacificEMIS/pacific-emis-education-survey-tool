package fm.doe.national.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.model.Criteria;
import fm.doe.national.data.model.SubCriteria;

public class MutableCriteria extends BaseMutableEntity implements Criteria {

    private String title;
    private String suffix;
    private List<MutableSubCriteria> subCriteriaList;

    public MutableCriteria() {
    }

    public MutableCriteria(@NonNull Criteria other) {
        this.id = other.getId();
        this.title = other.getTitle();
        this.suffix = other.getSuffix();

        if (other.getSubCriterias() == null) {
            return;
        }

        ArrayList<MutableSubCriteria> subCriterias = new ArrayList<>();
        for (SubCriteria subCriteria : other.getSubCriterias()) {
            subCriterias.add(new MutableSubCriteria(subCriteria));
        }
        this.subCriteriaList = subCriterias;
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
    public List<MutableSubCriteria> getSubCriterias() {
        return subCriteriaList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setSubCriterias(@Nullable List<MutableSubCriteria> subCriterias) {
        this.subCriteriaList = subCriterias;
    }
}
