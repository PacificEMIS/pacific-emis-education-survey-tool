package fm.doe.national.ui.screens.survey_creation;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import fm.doe.national.MicronesiaApplication;
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
        loadYear();
        loadSchools();
    }

    private void loadYear() {
        Calendar calendar = Calendar.getInstance();
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
                .doOnError(this::handleError)
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
                .doOnSuccess(passing -> getViewState().navigateToCategoryChooser(passing.getId()))
                .doOnError(this::handleError)
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
