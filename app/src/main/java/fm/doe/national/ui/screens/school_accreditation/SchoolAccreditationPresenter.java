package fm.doe.national.ui.screens.school_accreditation;

import com.arellomobile.mvp.InjectViewState;
import com.omega_r.libs.omegatypes.Text;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SchoolAccreditationPresenter extends BaseDrawerPresenter<SchoolAccreditationView> {

    @Inject
    DataSource dataSource;

    private List<SchoolAccreditationPassing> passings = new ArrayList<>();

    public SchoolAccreditationPresenter() {
        MicronesiaApplication.getAppComponent().inject(this);
        loadRecentSurveys();
    }

    private void loadRecentSurveys() {
        dataSource.requestSchoolAccreditationPassings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    getViewState().showWaiting();
                    add(disposable);
                })
                .doOnSuccess(passings -> {
                    this.passings = passings;
                    getViewState().setAccreditations(passings);
                })
                .doOnError(throwable -> getViewState().showWarning(
                        Text.from(R.string.title_warning),
                        Text.from(R.string.warn_unable_to_load_recent_surveys)))
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe();
    }

    public void onAccreditationClicked(SchoolAccreditationPassing schoolAccreditationPassing) {
        getViewState().navigateToCategoryChooser(-1);
    }

    public void onSearchQueryChanged(String query) {
        List<SchoolAccreditationPassing> queriedPassings = new ArrayList<>();
        for (SchoolAccreditationPassing passing : passings) {
            if (passing.getSchool().getName().contains(query)) {
                queriedPassings.add(passing);
            }
        }
        getViewState().setAccreditations(queriedPassings);
    }

}
