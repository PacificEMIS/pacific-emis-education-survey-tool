package fm.doe.national.ui.screens.survey_creation;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.model.School;
import fm.doe.national.data.model.mutable.MutableSurvey;
import fm.doe.national.domain.SurveyInteractor;
import fm.doe.national.ui.screens.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class CreateSurveyPresenter extends BasePresenter<CreateSurveyView> {

    private final DataSource dataSource = MicronesiaApplication.getAppComponent().getDataSource();
    private final SurveyInteractor surveyInteractor = MicronesiaApplication.getAppComponent().getSurveyInteractor();

    private Date surveyStartDate = new Date();
    private List<? extends School> schools;

    public CreateSurveyPresenter() {
        loadDate();
        loadSchools();
    }

    private void loadDate() {
        getViewState().setStartDate(surveyStartDate);
    }

    private void loadSchools() {
        addDisposable(dataSource.loadSchools()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(schools -> {
                    this.schools = schools;
                    getViewState().setSchools(new ArrayList<>(this.schools));
                }, this::handleError));
    }

    public void onSchoolPicked(School school) {
        addDisposable(dataSource.createSurvey(school.getId(), school.getName(), surveyStartDate)
                .map(MutableSurvey::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(survey -> {
                    surveyInteractor.setCurrentSurvey(survey, true);
                    getViewState().navigateToCategoryChooser();
                }, this::handleError));
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

    public void onEditButtonClick() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        getViewState().showDatePicker(year, month, day);
    }

    public void onDatePicked(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        Date surveyStartDate = calendar.getTime();

        getViewState().setStartDate(surveyStartDate);

        this.surveyStartDate = surveyStartDate;
    }
}
