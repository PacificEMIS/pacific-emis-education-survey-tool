package fm.doe.national.ui.screens.all_surveys;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.model.Survey;
import fm.doe.national.data.model.mutable.MutableSurvey;
import fm.doe.national.domain.SurveyInteractor;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class AllSurveysPresenter extends BaseDrawerPresenter<AllSurveysView> {

    private final SurveyInteractor interactor = MicronesiaApplication.getAppComponent().getSurveyInteractor();
    private final DataSource dataSource = MicronesiaApplication.getAppComponent().getDataSource();

    private List<MutableSurvey> surveys = new ArrayList<>();

    private Survey passingToDelete;

    @Override
    public void attachView(AllSurveysView view) {
        super.attachView(view);
        loadRecentPassings();
    }

    private void loadRecentPassings() {

        addDisposable(interactor.getAllSurveys()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(surveys -> {
                    this.surveys = surveys;
                    getViewState().setAccreditations(new ArrayList<>(this.surveys));
                }, this::handleError));
    }

    public void onAccreditationClicked(Survey survey) {
        interactor.setCurrentSurvey(MutableSurvey.toMutable(survey));
        getViewState().navigateToCategoryChooser();
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
        getViewState().setAccreditations(queriedPassings);
    }

    public void onAccreditationLongClicked(Survey item) {
        passingToDelete = item;
        getViewState().showSurveyDeleteConfirmation();
    }

    public void onSurveyDeletionConfirmed() {
        addDisposable(dataSource.deleteSurvey(passingToDelete.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> getViewState().removeSurveyPassing(passingToDelete), this::handleError));
    }
}
