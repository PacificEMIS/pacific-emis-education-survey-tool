package org.pacific_emis.surveys.offline_sync.ui.surveys;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import java.util.List;

import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;
import org.pacific_emis.surveys.offline_sync.R;
import org.pacific_emis.surveys.offline_sync.data.accessor.OfflineAccessor;
import org.pacific_emis.surveys.offline_sync.di.OfflineSyncComponent;
import org.pacific_emis.surveys.offline_sync.domain.OfflineSyncUseCase;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SyncSurveysPresenter extends BasePresenter<SyncSurveysView> {

    private final OfflineAccessor offlineAccessor;
    private final OfflineSyncUseCase useCase;
    private Survey targetSurvey;
    private Survey selectedSurvey;

    public SyncSurveysPresenter(OfflineSyncComponent component) {
        getViewState().setNextEnabled(false);
        offlineAccessor = component.getAccessor();
        useCase = component.getUseCase();
        targetSurvey = useCase.getTargetSurvey();
        onRefresh();
    }

    public void onSurveyPressed(Survey survey) {
        selectedSurvey = survey;
        getViewState().setNextEnabled(true);
        getViewState().setEmptyStateEnabled(false);
    }

    public void onRefresh() {
        addDisposable(
                offlineAccessor.requestSurveys(targetSurvey.getSchoolId(), targetSurvey.getSurveyTag())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(d -> getViewState().setListLoadingVisible(true))
                        .doFinally(() -> getViewState().setListLoadingVisible(false))
                        .subscribe(this::setSurveys, this::handleError)
        );
    }

    private void setSurveys(List<Survey> surveys) {
        getViewState().setEmptyStateEnabled(surveys == null || surveys.isEmpty());
        getViewState().setSurveys(surveys);
    }

    public void onNextPressed() {
        if (selectedSurvey == null) {
            return;
        }

        if (selectedSurvey.getState() != null) {
            switch (selectedSurvey.getState()) {
                case COMPLETED:
                    getViewState().showMessage(Text.from(R.string.title_merge_survey), Text.from(R.string.message_merge_survey_completed));
                    break;
                case MERGED:
                    getViewState().showPrompt(Text.from(R.string.title_merge_survey), Text.from(R.string.message_merge_survey_merged),
                            this::setExternalSurvey);
                    break;
                case NOT_COMPLETED:
                    setExternalSurvey();
                    break;
            }
        }
    }

    private void setExternalSurvey() {
        useCase.setExternalSurvey(selectedSurvey);
        getViewState().navigateToProgress();
        getViewState().close();
    }
}
