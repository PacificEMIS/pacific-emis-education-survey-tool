package fm.doe.national.ui.screens.menu.drawer;

import com.arellomobile.mvp.InjectViewState;

import fm.doe.national.ui.screens.menu.base.BaseMenuPresenter;

/**
 * Created by Alexander Chibirev on 8/9/2018.
 */

@InjectViewState
public class BaseDrawerPresenter extends BaseMenuPresenter<BaseDrawerView> {

    @Override
    public void onSchoolDataVerificationClicked() {
        super.onSchoolDataVerificationClicked();
    }

    @Override
    public void onSchoolAccreditationClicked() {
        super.onSchoolAccreditationClicked();
    }

    @Override
    public void onMonitoringAndEvaluationClicked() {
        super.onMonitoringAndEvaluationClicked();
    }

    @Override
    public void onEducationSurveyToolClicked() {
        super.onEducationSurveyToolClicked();
    }

    @Override
    public void onMenuItemClicked() {
        getViewState().hideMenu();
    }

}
