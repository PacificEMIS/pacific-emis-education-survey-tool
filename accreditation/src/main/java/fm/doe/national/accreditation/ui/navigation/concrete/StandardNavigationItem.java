package fm.doe.national.accreditation.ui.navigation.concrete;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.accreditation.ui.questions.QuestionsFragment;
import fm.doe.national.accreditation_core.data.model.Category;
import fm.doe.national.accreditation_core.data.model.Standard;
import fm.doe.national.accreditation_core.data.model.mutable.MutableStandard;
import fm.doe.national.core.R;
import fm.doe.national.core.data.model.Progress;
import fm.doe.national.core.data.model.mutable.MutableProgress;
import fm.doe.national.core.ui.screens.base.BaseFragment;
import fm.doe.national.survey_core.navigation.ProgressablePrefixedBuildableNavigationItem;

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
