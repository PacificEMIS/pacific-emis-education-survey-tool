package org.pacific_emis.surveys.ui.screens.templates.wash;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;

import org.pacific_emis.surveys.R;
import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;
import org.pacific_emis.surveys.ui.screens.templates.SurveyTemplateFragment;

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
