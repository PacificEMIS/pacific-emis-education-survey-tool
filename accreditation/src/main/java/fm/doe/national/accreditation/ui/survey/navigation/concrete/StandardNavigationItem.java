package fm.doe.national.accreditation.ui.survey.navigation.concrete;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.accreditation.ui.survey.navigation.ProgressablePrefixedBuildableNavigationItem;
import fm.doe.national.core.R;
import fm.doe.national.core.data.exceptions.NotImplementedException;
import fm.doe.national.core.data.model.Category;
import fm.doe.national.core.data.model.Progress;
import fm.doe.national.core.data.model.Standard;
import fm.doe.national.core.ui.screens.base.BaseFragment;

public class StandardNavigationItem extends ProgressablePrefixedBuildableNavigationItem {

    private final Category category;
    private final Standard standard;

    public StandardNavigationItem(Category criteria, Standard standard) {
        super(Text.from(standard.getTitle()), Text.from(R.string.format_standard, standard.getSuffix()));
        this.category = criteria;
        this.standard = standard;
    }

    @NonNull
    @Override
    public Progress getProgress() {
        return standard.getProgress();
    }

    @NonNull
    @Override
    public BaseFragment buildFragment() {
        throw new NotImplementedException();
    }

}
