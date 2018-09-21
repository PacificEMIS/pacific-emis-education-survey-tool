package fm.doe.national.ui.screens.survey_creation;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.ui.screens.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class CreateSurveyPresenter extends BasePresenter<CreateSurveyView> {

    private final DataSource dataSource = MicronesiaApplication.getAppComponent().getDataSource();

    private int year;
    private List<School> schools;

    public CreateSurveyPresenter() {
        loadYear();
        loadSchools();
    }

    private void loadYear() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        getViewState().setYear(year);
    }

    private void loadSchools() {
        addDisposable(dataSource.requestSchools()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(schools -> {
                    this.schools = schools;
                    getViewState().setSchools(schools);
                }, this::handleError));
    }

    public void onSchoolPicked(School school) {
        addDisposable(dataSource.createNewSchoolAccreditationPassing(year, school)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(passing -> getViewState().navigateToCategoryChooser(passing.getId()), this::handleError));
    }


    public void onSearchQueryChanged(String query) {
        List<School> queriedSchools = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for (School school : schools) {
            if (school.getName().toLowerCase().contains(lowerQuery) || school.getId().toLowerCase().contains(lowerQuery)) {
                queriedSchools.add(school);
            }
        }
        getViewState().setSchools(queriedSchools);
    }
}
