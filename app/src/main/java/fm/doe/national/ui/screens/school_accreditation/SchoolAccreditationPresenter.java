package fm.doe.national.ui.screens.school_accreditation;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.model.Survey;
import fm.doe.national.data.model.mutable.MutableSurvey;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerPresenter;
import fm.doe.national.utils.CollectionUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SchoolAccreditationPresenter extends BaseDrawerPresenter<SchoolAccreditationView> {

    private final DataSource dataSource = MicronesiaApplication.getAppComponent().getDataSource();

    private List<MutableSurvey> surveys = new ArrayList<>();

    private Survey passingToDelete;

    @Override
    public void attachView(SchoolAccreditationView view) {
        super.attachView(view);
        loadRecentPassings();
    }

    private void loadRecentPassings() {

        addDisposable(dataSource.loadAllSurveys()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(surveys -> {
                    this.surveys = surveys;
                    getViewState().setAccreditations(CollectionUtils.map(this.surveys, item -> item));
                }, this::handleError));
    }

    public void onAccreditationClicked(Survey schoolAccreditationPassing) {
        getViewState().navigateToCategoryChooser(schoolAccreditationPassing.getId());
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
        addDisposable(dataSource.deleteSurvey(passingToDelete)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> getViewState().removeSurveyPassing(passingToDelete), this::handleError));
    }
}
