package fm.doe.national.ui.screens.menu.drawer;

import fm.doe.national.ui.screens.menu.base.MenuPresenter;

public abstract class BaseDrawerPresenter<V extends BaseDrawerView> extends MenuPresenter<V> {

    public void onEducationSurveyToolClicked() {
        getViewState().showEducationSurveyToolScreen();
    }

    public void onSettingClicked() {
        getViewState().showEducationSurveyToolScreen();
    }

}
