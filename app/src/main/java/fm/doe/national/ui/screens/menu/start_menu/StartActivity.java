package fm.doe.national.ui.screens.menu.start_menu;

import android.os.Bundle;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerView;
import fm.doe.national.ui.screens.shool_accreditation.SchoolAccreditationActivity;

/**
 * Created by Alexander Chibirev on 8/9/2018.
 */

public class StartActivity extends BaseActivity implements StartView {

    @InjectPresenter
    StartPresenter startPresenter;

    @Override
    protected int getContentView() {
        return R.layout.activity_start_screen;
    }

    @Override
    public void showSchoolAccreditationScreen() {
        startActivity(SchoolAccreditationActivity.createIntent(this));
    }

    @Override
    public void showSchoolDataVerificationScreen() {

    }

    @Override
    public void shoMonitoringAndEvaluationScreen() {

    }

    @Override
    public void showEducationSurveyToolScreen() {

    }

    @Override
    public void hideMenu() {

    }

    @Override
    public void selectMenuOption(MenuItems menuOption) {

    }

    @OnClick({R.id.textview_school_accreditation, R.id.textview_school_data_verification,
            R.id.textview_monitoring_and_evaluation, R.id.textview_education_survey_tool})
    public void onMenuItemClick(View view) {
        startPresenter.onMenuItemClicked();
        switch (view.getId()) {
            case R.id.textview_school_accreditation:
                startPresenter.onSchoolAccreditationClicked();
                break;
            case R.id.textview_school_data_verification:
                startPresenter.onSchoolDataVerificationClicked();
                break;
            case R.id.textview_monitoring_and_evaluation:
                startPresenter.onMonitoringAndEvaluationClicked();
                break;
            case R.id.textview_education_survey_tool:
                startPresenter.onEducationSurveyToolClicked();
                break;
        }
    }

}
