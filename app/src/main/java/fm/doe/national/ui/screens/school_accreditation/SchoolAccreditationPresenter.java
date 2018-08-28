package fm.doe.national.ui.screens.school_accreditation;

import com.arellomobile.mvp.InjectViewState;
import com.omega_r.libs.omegatypes.Text;

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

    public SchoolAccreditationPresenter() {
        MicronesiaApplication.getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadRecentSurveys();
    }

    public void onAccreditationClicked(SchoolAccreditationPassing schoolAccreditationPassing) {
        getViewState().navigateToCategoryChooser(-1);
    }

    private void loadRecentSurveys() {
        getViewState().showProgressDialog(Text.empty());
        add(
                dataSource.requestSchoolAccreditationPassings()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> getViewState().hideProgressDialog())
                        .subscribe(
                                passings -> getViewState().setAccreditations(passings),
                                throwable -> getViewState().showWarning(
                                        Text.from(R.string.title_warning),
                                        Text.from(R.string.warn_unable_to_load_recent_surveys)))
        );
    }

}
