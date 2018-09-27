package fm.doe.national.ui.screens.school_accreditation;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerPresenter;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.CompletableSubject;

@InjectViewState
public class SchoolAccreditationPresenter extends BaseDrawerPresenter<SchoolAccreditationView> {

    private final DataSource dataSource = MicronesiaApplication.getAppComponent().getDataSource();

    private List<SchoolAccreditationPassing> passings = new ArrayList<>();

    private SchoolAccreditationPassing pendingToDeletePassing;

    private CompletableSubject completableSubject;

    @Override
    public void attachView(SchoolAccreditationView view) {
        super.attachView(view);
        loadRecentPassings();
    }

    private void loadRecentPassings() {
        addDisposable(dataSource.requestSchoolAccreditationPassings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(passings -> {
                    this.passings = passings;
                    getViewState().setAccreditations(passings);
                }, this::handleError));
    }

    public void onAccreditationClicked(SchoolAccreditationPassing schoolAccreditationPassing) {
        getViewState().navigateToCategoryChooser(schoolAccreditationPassing.getId());
    }

    public void onSearchQueryChanged(String query) {
        List<SchoolAccreditationPassing> queriedPassings = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for (SchoolAccreditationPassing passing : passings) {
            if (passing.getSchool().getName().toLowerCase().contains(lowerQuery) ||
                    passing.getSchool().getId().toLowerCase().contains(lowerQuery)) {
                queriedPassings.add(passing);
            }
        }
        getViewState().setAccreditations(queriedPassings);
    }

    public void onAccreditationLongClicked(SchoolAccreditationPassing item) {
        pendingToDeletePassing = item;
        getViewState().showSurveyDeleteConfirmation();
    }

    public void onSurveyDeletionConfirmed() {
        addDisposable(dataSource.removeSchoolAccreditationPassing(pendingToDeletePassing.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    getViewState().removeSurveyPassing(pendingToDeletePassing);
                }, this::handleError));
    }
}
