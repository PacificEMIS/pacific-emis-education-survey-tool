package org.pacific_emis.surveys.accreditation.ui.navigation.concrete;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import org.pacific_emis.surveys.accreditation.ui.questions.QuestionsFragment;
import org.pacific_emis.surveys.accreditation_core.data.model.Category;
import org.pacific_emis.surveys.accreditation_core.data.model.Standard;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableStandard;
import org.pacific_emis.surveys.core.R;
import org.pacific_emis.surveys.core.data.model.Progress;
import org.pacific_emis.surveys.core.data.model.mutable.MutableProgress;
import org.pacific_emis.surveys.core.ui.screens.base.BaseFragment;
import org.pacific_emis.surveys.survey_core.navigation.ProgressablePrefixedBuildableNavigationItem;

public class StandardNavigationItem extends ProgressablePrefixedBuildableNavigationItem {

    private final Category category;
    private final Standard standard;

    public StandardNavigationItem(Category criteria, Standard standard) {
        super(standard.getId(), Text.from(standard.getTitle()), Text.from(R.string.format_standard, standard.getSuffix()));
        this.category = criteria;
        this.standard = standard;
    }

    @NonNull
    @Override
    public Progress getProgress() {
        return standard.getProgress();
    }

    @Override
    public void setProgress(Progress progress) {
        if (standard instanceof MutableStandard) {
            ((MutableStandard) standard).setProgress(new MutableProgress(progress.getTotal(), progress.getCompleted()));
        }
    }

    @NonNull
    @Override
    public BaseFragment buildFragment() {
        return QuestionsFragment.create(category.getId(), standard.getId());
    }

}
