package fm.doe.national.ui.screens.templates.accreditation;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;

import fm.doe.national.R;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.ui.screens.templates.SurveyTemplateFragment;

public class AccreditationSurveyTemplateFragment extends SurveyTemplateFragment {

    @InjectPresenter
    AccreditationSurveyTemplatePresenter presenter;

    public static AccreditationSurveyTemplateFragment create() {
        return new AccreditationSurveyTemplateFragment();
    }

    @Override
    public Text getPageTitle() {
        return Text.from(R.string.title_school_accreditation);
    }

    @Override
    public Text getLoadText() {
        return Text.from(R.string.label_load_sa_template);
    }

    @Nullable
    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }
}
