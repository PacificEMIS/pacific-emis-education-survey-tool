package fm.doe.national.fcm_report.ui.levels;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.fcm_report.di.FcmReportComponent;
import fm.doe.national.fcm_report.domain.FcmReportInteractor;
import fm.doe.national.report_core.ui.base.BaseReportPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class LevelsPresenter extends BaseReportPresenter<LevelsView> {

    private final FcmReportInteractor interactor;

    LevelsPresenter(FcmReportComponent component) {
        super(component.getFcmReportInteractor());
        this.interactor = component.getFcmReportInteractor();
        loadSchoolAccreditationLevel();
    }

    private void loadSchoolAccreditationLevel() {
        addDisposable(interactor.getLevelSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(data -> {
                    if (!data.isEmpty()) {
                        getViewState().setData(data);
                    }
                })
                .doOnError(this::handleError)
                .subscribe());
    }
}
