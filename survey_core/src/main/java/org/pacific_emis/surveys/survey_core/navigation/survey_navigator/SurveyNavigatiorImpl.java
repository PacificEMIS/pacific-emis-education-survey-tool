package org.pacific_emis.surveys.survey_core.navigation.survey_navigator;

import com.omega_r.libs.omegatypes.Text;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.stream.Collectors;

import org.pacific_emis.surveys.core.data.model.IdentifiedObject;
import org.pacific_emis.surveys.core.data.model.Progress;
import org.pacific_emis.surveys.survey_core.navigation.BuildableNavigationItem;
import org.pacific_emis.surveys.survey_core.navigation.NavigationItem;
import org.pacific_emis.surveys.survey_core.navigation.PrefixedBuildableNavigationItem;
import org.pacific_emis.surveys.survey_core.navigation.ProgressablePrefixedBuildableNavigationItem;
import org.pacific_emis.surveys.survey_core.ui.survey.SurveyView;


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

        Text prefixText = null;
        Progress progress = null;

        if (item instanceof PrefixedBuildableNavigationItem) {
            prefixText = ((PrefixedBuildableNavigationItem) item).getTitlePrefix();
            if (item instanceof ProgressablePrefixedBuildableNavigationItem) {
                progress = ((ProgressablePrefixedBuildableNavigationItem) item).getProgress();
            }
        }

        surveyView.setNavigationTitle(
                prefixText,
                item.getTitle(),
                progress
        );

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

    @Override
    public void close() {
        SurveyView view = viewWeakReference.get();
        if (view != null) {
            view.close();
        }
    }
}
