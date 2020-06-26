package org.pacific_emis.surveys.fsm_report.levels;

import com.omegar.mvp.InjectViewState;

import org.pacific_emis.surveys.fsm_report.di.FsmReportComponent;
import org.pacific_emis.surveys.fsm_report.domain.FsmReportInteractor;
import org.pacific_emis.surveys.report_core.ui.base.BaseReportPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class LevelsPresenter extends BaseReportPresenter<LevelsView> {

    private final FsmReportInteractor interactor;

    LevelsPresenter(FsmReportComponent component) {
        super(component.getFsmReportInteractor());
        this.interactor = component.getFsmReportInteractor();
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
