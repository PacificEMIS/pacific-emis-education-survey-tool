package fm.doe.national.ui.screens.survey_creation;

import com.arellomobile.mvp.InjectViewState;
import com.omega_r.libs.omegatypes.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
    private List<School> schools;

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
        dataSource.requestSchools()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    getViewState().showWaiting();
                    add(disposable);
                })
                .doOnSuccess(schools -> {
                    this.schools = schools;
                    getViewState().setSchools(schools);
                })
                .doOnError(t -> getViewState().showWarning(
                        Text.from(R.string.title_warning),
                        Text.from(R.string.warn_unable_to_get_schools)))
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe();
    }

    public void onSchoolPicked(School school) {
        dataSource.createNewSchoolAccreditationPassing(year, school)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    getViewState().showWaiting();
                    add(disposable);
                })
                .doOnSuccess(schools -> getViewState().navigateToCategoryChooser(-1))
                .doOnError(t -> getViewState().showWarning(
                        Text.from(R.string.title_warning),
                        Text.from(R.string.warn_unable_to_get_schools)))
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe();
    }


    public void onSearchQueryChanged(String query) {
        List<School> queriedSchools = new ArrayList<>();
        for (School school : schools) {
            if (school.getName().contains(query)) {
                queriedSchools.add(school);
            }
        }
        getViewState().setSchools(queriedSchools);
    }
}
