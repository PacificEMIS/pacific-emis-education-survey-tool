package fm.doe.national.ui.screens.menu.base;

import fm.doe.national.ui.screens.base.BasePresenter;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */

abstract public class BaseMenuPresenter<V extends BaseMenuView>  extends BasePresenter<V> {

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
