package fm.doe.national.ui.screens.surveys;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.R;
import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.domain.SurveyInteractor;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.domain.SettingsInteractor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SurveysPresenter extends BasePresenter<SurveysView> {

    private final SurveyInteractor interactor = MicronesiaApplication.getInjection().getSurveyComponent().getSurveyInteractor();
    private final DataSource dataSource = MicronesiaApplication.getInjection().getDataSourceComponent().getDataSource();
    private final GlobalPreferences globalPreferences = MicronesiaApplication.getInjection().getCoreComponent().getGlobalPreferences();
    private final SettingsInteractor settingsInteractor = MicronesiaApplication.getInjection().getAppComponent().getSettingsInteractor();

    private List<Survey> surveys = new ArrayList<>();

    private Survey surveyToDelete;

    @Override
    public void attachView(SurveysView view) {
        super.attachView(view);
        loadRecentSurveys();
    }

    private void loadRecentSurveys() {
        addDisposable(interactor.getAllSurveys()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(surveys -> {
                    this.surveys = surveys;
                    getViewState().setSurveys(new ArrayList<>(this.surveys));
                }, this::handleError));
    }

    public void onSurveyPressed(Survey survey) {
        interactor.setCurrentSurvey(survey);
        getViewState().navigateToSurvey();
    }

    public void onSearchQueryChanged(String query) {
        List<Survey> queriedPassings = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for (Survey passing : surveys) {
            if (passing.getSchoolName().toLowerCase().contains(lowerQuery) ||
                    passing.getSchoolId().toLowerCase().contains(lowerQuery)) {
                queriedPassings.add(passing);
            }
        }
        getViewState().setSurveys(queriedPassings);
    }

    public void onSurveyMergePressed(Survey survey) {
        // TODO: not implemented
        getViewState().showToast(Text.from(R.string.coming_soon));
    }

    public void onSurveyExportToExcelPressed(Survey survey) {
        // TODO: not implemented
        getViewState().showToast(Text.from(R.string.coming_soon));
    }

    public void onSurveyRemovePressed(Survey survey) {
        surveyToDelete = survey;
        getViewState().promptMasterPassword(Text.from(R.string.message_delete_password_prompt));
    }

    public void onLoadPartiallySavedSurveyPressed() {
        addDisposable(settingsInteractor.createFilledSurveyFromCloud()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(this::loadRecentSurveys, this::handleError));
    }

    @NonNull
    @Override
    protected String provideMasterPassword() {
        return globalPreferences.getMasterPassword();
    }

    @NonNull
    @Override
    protected String provideFactoryPassword() {
        return globalPreferences.getFactoryPassword();
    }

    @Override
    protected void onMasterPasswordValidated() {
        if (surveyToDelete != null) {
            deleteSurvey();
        }
    }

    private void deleteSurvey() {
        addDisposable(dataSource.deleteSurvey(surveyToDelete.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(() -> {
                    getViewState().removeSurvey(surveyToDelete);
                    surveyToDelete = null;
                }, this::handleError));
    }
}
