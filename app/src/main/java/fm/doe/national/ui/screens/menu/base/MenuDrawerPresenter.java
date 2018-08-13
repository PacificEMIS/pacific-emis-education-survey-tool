package fm.doe.national.ui.screens.menu.base;

import fm.doe.national.ui.screens.menu.drawer.BaseDrawerPresenter;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */

abstract public class MenuDrawerPresenter<V extends MenuDrawerView> extends BaseDrawerPresenter<V> {

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

    public void onMenuItemClicked() {
        //nothing
    }

}
