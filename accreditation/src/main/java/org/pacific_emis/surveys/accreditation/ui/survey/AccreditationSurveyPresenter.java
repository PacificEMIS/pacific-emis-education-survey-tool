package org.pacific_emis.surveys.accreditation.ui.survey;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import org.pacific_emis.surveys.accreditation.ui.navigation.concrete.CategoryNavigationItem;
import org.pacific_emis.surveys.accreditation.ui.navigation.concrete.ClassroomObservationInfoNavigationItem;
import org.pacific_emis.surveys.accreditation.ui.navigation.concrete.ClassroomObservationLogNavigationItem;
import org.pacific_emis.surveys.accreditation.ui.navigation.concrete.ReportNavigationItem;
import org.pacific_emis.surveys.accreditation.ui.navigation.concrete.ReportTitleNavigationItem;
import org.pacific_emis.surveys.accreditation.ui.navigation.concrete.StandardNavigationItem;
import org.pacific_emis.surveys.accreditation_core.data.model.EvaluationForm;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableCategory;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableStandard;
import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreComponent;
import org.pacific_emis.surveys.accreditation_core.interactors.AccreditationSurveyInteractor;
import org.pacific_emis.surveys.core.domain.SurveyInteractor;
import org.pacific_emis.surveys.survey_core.di.SurveyCoreComponent;
import org.pacific_emis.surveys.survey_core.navigation.BuildableNavigationItem;
import org.pacific_emis.surveys.survey_core.navigation.NavigationItem;
import org.pacific_emis.surveys.survey_core.ui.survey.SurveyPresenter;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class AccreditationSurveyPresenter extends SurveyPresenter<AccreditationSurveyView> {

    private final static long FIRST_CLASSROOM_OBSERVATION_INFO_ID_FOR_GENERATION = -100;
    private final static long FIRST_CLASSROOM_OBSERVATION_LOG_ID_FOR_GENERATION = -1000;

    private final AccreditationSurveyInteractor accreditationSurveyInteractor;

    public AccreditationSurveyPresenter(AccreditationCoreComponent accreditationCoreComponent, SurveyCoreComponent surveyCoreComponent) {
        super(surveyCoreComponent);
        accreditationSurveyInteractor = accreditationCoreComponent.getAccreditationSurveyInteractor();
        onInit();
    }

    @Override
    protected SurveyInteractor getSurveyInteractor() {
        return accreditationSurveyInteractor;
    }

    @Override
    protected void subscribeOnEditingEvents() {
        addDisposable(
                accreditationSurveyInteractor.getStandardProgressObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(updatedStandard ->
                                        getViewState().updateQuestionsGroupProgress(
                                                updatedStandard.getId(),
                                                updatedStandard.getProgress()
                                        ),
                                this::handleError)
        );
    }

    @Override
    protected Single<List<NavigationItem>> requestNavigationItems() {
        return accreditationSurveyInteractor.requestCategories()
                .flatMap(categories -> Single.fromCallable(() -> {
                    List<NavigationItem> navigationItems = new ArrayList<>();
                    BuildableNavigationItem prevBuildableNavigationItem = null;
                    long generatedClassroomObservationInfoId = FIRST_CLASSROOM_OBSERVATION_INFO_ID_FOR_GENERATION;
                    long generatedClassroomObservationLogId = FIRST_CLASSROOM_OBSERVATION_LOG_ID_FOR_GENERATION;

                    for (MutableCategory category : categories) {
                        navigationItems.add(new CategoryNavigationItem(category));

                        if (category.getEvaluationForm() == EvaluationForm.CLASSROOM_OBSERVATION) {
                            ClassroomObservationInfoNavigationItem infoItem = new ClassroomObservationInfoNavigationItem(
                                    generatedClassroomObservationInfoId,
                                    category.getId()
                            );
                            addNavigationItem(navigationItems, infoItem, prevBuildableNavigationItem);
                            prevBuildableNavigationItem = infoItem;

                            ClassroomObservationLogNavigationItem logItem = new ClassroomObservationLogNavigationItem(
                                    generatedClassroomObservationLogId,
                                    category.getId()
                            );
                            addNavigationItem(navigationItems, logItem, prevBuildableNavigationItem);
                            prevBuildableNavigationItem = logItem;

                            generatedClassroomObservationInfoId--;
                            generatedClassroomObservationLogId--;
                        }

                        for (MutableStandard standard : category.getStandards()) {
                            StandardNavigationItem standardItem = new StandardNavigationItem(category, standard);
                            addNavigationItem(navigationItems, standardItem, prevBuildableNavigationItem);
                            prevBuildableNavigationItem = standardItem;
                        }
                    }
                    navigationItems.add(new ReportTitleNavigationItem());
                    ReportNavigationItem reportNavigationItem = new ReportNavigationItem();

                    if (prevBuildableNavigationItem != null) {
                        prevBuildableNavigationItem.setNextItem(reportNavigationItem);
                    }

                    navigationItems.add(reportNavigationItem);
                    return navigationItems;
                }));
    }

    private void addNavigationItem(@NonNull List<NavigationItem> navigationItems,
                                   @NonNull BuildableNavigationItem item,
                                   @Nullable BuildableNavigationItem previousItem) {
        item.setPreviousItem(previousItem);
        if (previousItem != null) {
            previousItem.setNextItem(item);
        }
        navigationItems.add(item);
    }

    @Override
    protected String getSchoolName() {
        return accreditationSurveyInteractor.getCurrentSurvey().getSchoolName();
    }
}
