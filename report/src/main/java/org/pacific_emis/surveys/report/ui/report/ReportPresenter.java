package org.pacific_emis.surveys.report.ui.report;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omegar.mvp.InjectViewState;

import org.pacific_emis.surveys.accreditation_core.data.model.AccreditationSurvey;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableAccreditationSurvey;
import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreComponent;
import org.pacific_emis.surveys.accreditation_core.interactors.AccreditationSurveyInteractor;
import org.pacific_emis.surveys.core.data.model.Teacher;
import org.pacific_emis.surveys.core.di.CoreComponent;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;
import org.pacific_emis.surveys.report.di.ReportComponent;
import org.pacific_emis.surveys.report_core.domain.ReportInteractor;
import org.pacific_emis.surveys.report_core.domain.ReportsProvider;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class ReportPresenter extends BasePresenter<ReportView> {

    private final ReportsProvider reportsProvider;
    private final ReportInteractor reportInteractor;

    private final AccreditationSurveyInteractor accreditationSurveyInteractor;
    private final LocalSettings localSettings;

    private MutableAccreditationSurvey accreditationSurvey;

    public ReportPresenter(ReportComponent component,
                           CoreComponent coreComponent,
                           AccreditationCoreComponent accreditationCoreComponent) {
        reportsProvider = component.getReportsProvider();
        reportInteractor = component.getReportInteractor();
        accreditationSurveyInteractor = accreditationCoreComponent.getAccreditationSurveyInteractor();
        localSettings = coreComponent.getLocalSettings();

        getViewState().setReportPages(reportsProvider.getPages());
        reportsProvider.requestReports();
        accreditationSurvey = (MutableAccreditationSurvey) reportsProvider.getCurrentSurvey();

        loadHeaderItemObservable();
        loadTeachers();
    }

    private void loadHeaderItemObservable() {
        addDisposable(reportInteractor.getHeaderItemObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    if (!item.isEmpty()) {
                        getViewState().setHeaderItem(item);
                    }
                }, this::handleError)
        );
    }

    private void loadTeachers() {
        addDisposable(
                accreditationSurveyInteractor.loadTeachers(localSettings.getCurrentAppRegion())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> getViewState().showWaiting())
                        .doFinally(() -> getViewState().hideWaiting())
                        .subscribe(teachers -> {
                            getViewState().setPrincipalToAutocompleteField(teachers);
                        }, this::handleError)
        );
    }

    public void onPrincipalNameChanges(Teacher teacher) {
        accreditationSurvey.setPrincipalName(teacher.getName());
        save(accreditationSurvey);
    }

    public void onPrincipalNameChanges(@Nullable String teacher) {
        accreditationSurvey.setPrincipalName(teacher);
        save(accreditationSurvey);
    }

    private void save(@NonNull AccreditationSurvey accreditationSurvey) {
        accreditationSurveyInteractor.setCurrentSurvey(accreditationSurvey);
        addDisposable(accreditationSurveyInteractor.updateSurvey()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, this::handleError)
        );
    }
}
