package fm.doe.national.ui.screens.main.slide_menu;

import com.arellomobile.mvp.InjectViewState;

import fm.doe.national.ui.screens.base.BasePresenter;

/**
 * Created by Alexander Chibirev on 8/9/2018.
 */

@InjectViewState
public class SideMenuPresenter extends BasePresenter<SideMenuView> {

    public void onSchoolDataVerificationClicked() {
        getViewState().showSchoolDataVerificationScreen();
    }

    public void onSchoolAccreditationClicked() {
        getViewState().showSchoolAccreditationScreen();
    }

    public void onMonitoringAndEvaluationClicked() {
        getViewState().shoMonitoringAndEvaluationScreen();
    }

    public void onEducationSurveyToolClicked() {
        getViewState().showEducationSurveyToolScreen();
    }

}
