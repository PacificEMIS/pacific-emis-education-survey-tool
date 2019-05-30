package fm.doe.national.accreditation.ui.questions;

import androidx.annotation.Nullable;

import java.util.Objects;

import fm.doe.national.core.data.model.Criteria;
import fm.doe.national.core.data.model.SubCriteria;

public class Question {

    @Nullable
    private SubCriteria subCriteria;

    @Nullable
    private Criteria criteria;

    public Question(@Nullable SubCriteria subCriteria) {
        this.subCriteria = subCriteria;
    }

    public Question(@Nullable Criteria criteria) {
        this.criteria = criteria;
    }

    @Nullable
    public SubCriteria getSubCriteria() {
        return subCriteria;
    }

    @Nullable
    public Criteria getCriteria() {
        return criteria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return subCriteria.equals(question.subCriteria) &&
                criteria.equals(question.criteria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subCriteria, criteria);
    }
}
