package fm.doe.national.ui.screens.templates;

import fm.doe.national.core.ui.screens.base.BasePresenter;

public abstract class SurveyTemplatePresenter extends BasePresenter<SurveyTemplateView> {

    protected abstract void loadItems();

    protected abstract void onLoadPressed();

}
