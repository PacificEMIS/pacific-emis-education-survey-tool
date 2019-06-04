package fm.doe.national.survey_core.navigation.survey_navigator;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.data.model.IdentifiedObject;
import fm.doe.national.survey_core.navigation.BuildableNavigationItem;
import fm.doe.national.survey_core.navigation.NavigationItem;
import fm.doe.national.survey_core.navigation.ProgressablePrefixedBuildableNavigationItem;
import fm.doe.national.survey_core.ui.survey.SurveyView;


public class SurveyNavigatiorImpl implements SurveyNavigator {

    private WeakReference<SurveyView> viewWeakReference;
    private BuildableNavigationItem currentItem;

    private List<BuildableNavigationItem> items;

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

    @Override
    public void select(IdentifiedObject identifiedObject) {
        if (items == null) {
            return;
        }

        items.parallelStream()
                .filter(item -> item.getId() == identifiedObject.getId())
                .findFirst()
                .ifPresent(this::select);
    }

    @Override
    public void setNavigation(List<NavigationItem> items) {
        this.items = items.stream()
                .filter(item -> item instanceof BuildableNavigationItem)
                .map(item -> (BuildableNavigationItem) item)
                .collect(Collectors.toList());
    }
}
