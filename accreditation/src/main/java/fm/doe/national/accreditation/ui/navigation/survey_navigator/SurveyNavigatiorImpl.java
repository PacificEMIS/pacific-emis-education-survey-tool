package fm.doe.national.accreditation.ui.navigation.survey_navigator;

import java.lang.ref.WeakReference;

import fm.doe.national.accreditation.ui.navigation.BuildableNavigationItem;
import fm.doe.national.accreditation.ui.navigation.ProgressablePrefixedBuildableNavigationItem;
import fm.doe.national.accreditation.ui.survey.SurveyView;

public class SurveyNavigatiorImpl implements SurveyNavigator {

    private WeakReference<SurveyView> viewWeakReference;
    private BuildableNavigationItem currentItem;

    @Override
    public void select(BuildableNavigationItem item) {
        if (viewWeakReference == null) {
            return;
        }

        SurveyView surveyView = viewWeakReference.get();
        if (surveyView == null) {
            return;
        }

        if (item instanceof ProgressablePrefixedBuildableNavigationItem) {
            ProgressablePrefixedBuildableNavigationItem navigationItem = (ProgressablePrefixedBuildableNavigationItem) item;
            surveyView.setNavigationTitle(
                    navigationItem.getTitlePrefix(),
                    navigationItem.getTitle(),
                    navigationItem.getProgress()
            );
        } else {
            surveyView.setNavigationTitle(null, item.getTitle(), null);
        }

        surveyView.showNavigationItem(item);
        currentItem = item;
    }

    @Override
    public void selectPrevious() {
        if (currentItem == null) {
            return;
        }

        BuildableNavigationItem prevItem = currentItem.getPreviousItem();

        if (prevItem != null) {
            select(prevItem);
        }
    }

    @Override
    public void selectNext() {
        if (currentItem == null) {
            return;
        }

        BuildableNavigationItem nextItem = currentItem.getNextItem();

        if (nextItem != null) {
            select(nextItem);
        }
    }

    @Override
    public void setViewState(SurveyView surveyView) {
        viewWeakReference = new WeakReference<>(surveyView);
    }

    @Override
    public BuildableNavigationItem getCurrentItem() {
        return currentItem;
    }
}
