package fm.doe.national.core.ui.screens.survey.navigation;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.core.R;
import fm.doe.national.core.data.model.Category;
import fm.doe.national.core.data.model.Progress;
import fm.doe.national.core.data.model.Standard;
import fm.doe.national.core.ui.screens.base.BaseFragment;

public class SchoolAccreditationNavigationItem implements ProgressableNavigationItem {

    private final Category category;
    private final Standard standard;
    private final Text namePrefix;
    private final Text name;
    private final Text criteriaName;

    public SchoolAccreditationNavigationItem(Category criteria, Standard standard) {
        this.category = criteria;
        this.standard = standard;
        this.namePrefix =  Text.from(R.string.format_standard, standard.getSuffix());
        this.name =  Text.from(standard.getTitle());
        this.criteriaName = Text.from(criteria.getTitle());
    }

    @Override
    public Text getNamePrefix() {
        return namePrefix;
    }

    @NonNull
    @Override
    public Progress getProgress() {
        return standard.getProgress();
    }

    @NonNull
    @Override
    public BaseFragment buildFragment() {
        return null;
    }

    @Override
    public Text getName() {
        return name;
    }

    @Override
    public long getHeaderId() {
        return category.getId();
    }

    @Override
    public Text getHeader() {
        return criteriaName;
    }
}
