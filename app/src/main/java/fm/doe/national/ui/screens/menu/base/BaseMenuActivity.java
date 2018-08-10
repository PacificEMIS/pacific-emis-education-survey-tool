package fm.doe.national.ui.screens.menu.base;

import android.view.View;

import butterknife.OnClick;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseActivity;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */
public abstract class BaseMenuActivity extends BaseActivity implements BaseMenuView {

    protected abstract BaseMenuPresenter getPresenter();

    @OnClick({R.id.textview_school_accreditation, R.id.textview_school_data_verification,
            R.id.textview_monitoring_and_evaluation, R.id.textview_education_survey_tool})
    public void onMenuItemClick(View view) {
        getPresenter().onMenuItemClicked();
        switch (view.getId()) {
            case R.id.textview_school_accreditation:
                getPresenter().onSchoolAccreditationClicked();
                break;
            case R.id.textview_school_data_verification:
                getPresenter().onSchoolDataVerificationClicked();
                break;
            case R.id.textview_monitoring_and_evaluation:
                getPresenter().onMonitoringAndEvaluationClicked();
                break;
            case R.id.textview_education_survey_tool:
                getPresenter().onEducationSurveyToolClicked();
                break;
        }
    }

    @Override
    public void showSchoolDataVerificationScreen() {
        //TODO added logic
    }

    @Override
    public void shoMonitoringAndEvaluationScreen() {
        //TODO added logic
    }

    @Override
    public void showEducationSurveyToolScreen() {
        //TODO added logic
    }

}
