package org.pacific_emis.surveys.rmi_report.ui;

import com.omegar.mvp.InjectViewState;

import org.pacific_emis.surveys.report_core.ui.base.BaseReportPresenter;
import org.pacific_emis.surveys.rmi_report.di.RmiReportComponent;
import org.pacific_emis.surveys.rmi_report.domain.RmiReportInteractor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class LevelsPresenter extends BaseReportPresenter<LevelsView> {

    private final RmiReportInteractor interactor;

    LevelsPresenter(RmiReportComponent component) {
        super(component.getRmiReportInteractor());
        this.interactor = component.getRmiReportInteractor();
        loadSchoolAccreditationLevel();
    }

    private void loadSchoolAccreditationLevel() {
        addDisposable(interactor.getLevelObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    if (!data.isEmpty()) {
                        getViewState().setData(data);
                    }
                }, this::handleError));
    }
}
