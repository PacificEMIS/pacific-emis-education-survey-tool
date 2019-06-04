package fm.doe.national.accreditation.ui.questions;

import androidx.annotation.Nullable;

import fm.doe.national.accreditation_core.data.model.Criteria;
import fm.doe.national.accreditation_core.data.model.SubCriteria;


public class Question {

    @Nullable
    private SubCriteria subCriteria;
    private Criteria criteria;

    public Question(Criteria criteria, @Nullable SubCriteria subCriteria) {
        this.criteria = criteria;
        this.subCriteria = subCriteria;
    }

    public Question(@Nullable Criteria criteria) {
        this.criteria = criteria;
    }

    public boolean isCriteriaOnly() {
        return subCriteria == null;
    }

    @Nullable
    public SubCriteria getSubCriteria() {
        return subCriteria;
    }

    public Criteria getCriteria() {
        return criteria;
    }

}
