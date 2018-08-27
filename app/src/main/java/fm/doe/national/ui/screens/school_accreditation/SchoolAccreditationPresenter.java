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
        getViewState().showProgressDialog(Text.empty());
        loadRecentSurveys();
    }

    public void onSchoolClicked(SchoolAccreditationPassing schoolAccreditationPassing) {
        getViewState().navigateToCategoryChooser(schoolAccreditationPassing);
    }

    private void loadRecentSurveys() {
        add(
                dataSource.requestSchoolAccreditationPassings()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(passings -> {
                            getViewState().hideProgressDialog();
                            getViewState().setAccreditations(passings);
                        }, throwable -> {
                            getViewState().hideProgressDialog();
                            getViewState().showWarning(
                                    Text.from(R.string.title_warning),
                                    Text.from(R.string.warn_unable_to_load_recent_surveys));
                        })
        );
    }

}
