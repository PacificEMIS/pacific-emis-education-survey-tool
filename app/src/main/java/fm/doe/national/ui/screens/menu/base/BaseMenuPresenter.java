package fm.doe.national.ui.screens.menu.base;

import fm.doe.national.ui.screens.base.BasePresenter;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */

abstract public class BaseMenuPresenter<V extends BaseMenuView> extends BasePresenter<V> {

    protected void onSchoolDataVerificationClicked() {
        getViewState().showSchoolDataVerificationScreen();
    }

    protected void onSchoolAccreditationClicked() {
        getViewState().showSchoolAccreditationScreen();
    }

    protected void onMonitoringAndEvaluationClicked() {
        getViewState().shoMonitoringAndEvaluationScreen();
    }

    protected void onEducationSurveyToolClicked() {
        getViewState().showEducationSurveyToolScreen();
    }

    protected void onMenuItemClicked() {

    }

}
