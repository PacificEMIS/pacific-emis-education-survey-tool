package fm.doe.national.ui.screens.templates.wash;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;

import fm.doe.national.R;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.ui.screens.templates.SurveyTemplateFragment;

public class WashSurveyTemplateFragment extends SurveyTemplateFragment {

    @InjectPresenter
    WashSurveyTemplatePresenter presenter;

    public static WashSurveyTemplateFragment create() {
        return new WashSurveyTemplateFragment();
    }

    @Override
    public Text getPageTitle() {
        return Text.from(R.string.title_water_sanitation_and_hygiene);
    }

    @Override
    public Text getLoadText() {
        return Text.from(R.string.label_load_wash_template);
    }

    @Nullable
    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }
}
