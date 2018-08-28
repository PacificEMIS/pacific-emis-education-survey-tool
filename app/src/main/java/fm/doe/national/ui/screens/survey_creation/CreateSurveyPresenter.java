package fm.doe.national.ui.screens.survey_creation;

import com.arellomobile.mvp.InjectViewState;
import com.omega_r.libs.omegatypes.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.inject.Inject;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.ui.screens.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class CreateSurveyPresenter extends BasePresenter<CreateSurveyView> {
    @Inject
    DataSource dataSource;

    private int year;

    public CreateSurveyPresenter() {
        MicronesiaApplication.getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadYear();
        loadSchools();
    }

    private void loadYear() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        year = calendar.get(Calendar.YEAR);
        getViewState().setYear(year);
    }

    private void loadSchools() {
        getViewState().showProgressDialog(Text.from("loading"));
        add(
                dataSource.requestSchools()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> getViewState().hideProgressDialog())
                        .subscribe(
                                getViewState()::setSchools,
                                throwable -> getViewState().showWarning(
                                        Text.from(R.string.title_warning),
                                        Text.from(R.string.warn_unable_to_get_schools)))
        );
    }

    public void onSchoolPicked(School school) {
        getViewState().showProgressDialog(Text.from("loading"));
        add(
                dataSource.createNewSchoolAccreditationPassing(year, school)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> getViewState().hideProgressDialog())
                        .subscribe(
                                accreditation -> getViewState().navigateToCategoryChooser(accreditation),
                                throwable -> getViewState().showWarning(
                                        Text.from(R.string.title_warning),
                                        Text.from(R.string.warn_unable_to_get_schools)))
        );
    }
}
