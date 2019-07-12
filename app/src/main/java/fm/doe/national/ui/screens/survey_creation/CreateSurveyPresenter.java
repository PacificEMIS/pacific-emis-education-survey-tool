package fm.doe.national.ui.screens.survey_creation;

import androidx.annotation.Nullable;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.domain.SurveyInteractor;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class CreateSurveyPresenter extends BasePresenter<CreateSurveyView> {

    private final DataSource dataSource = MicronesiaApplication.getInjection().getDataSourceComponent().getDataSource();
    private final SurveyInteractor accreditationSurveyInteractor = MicronesiaApplication.getInjection().getSurveyComponent().getSurveyInteractor();

    private Date surveyDate = new Date();
    private List<? extends School> schools;

    @Nullable
    private School selectedSchool;

    CreateSurveyPresenter() {
        loadDate();
        loadSchools();
        updateContinueAvailability();
    }

    private void loadDate() {
        getViewState().setStartDate(surveyDate);
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

    void onSchoolPicked(School school) {
        selectedSchool = school;
        updateContinueAvailability();
    }

    private void updateContinueAvailability() {
        getViewState().setContinueEnabled(selectedSchool != null);
    }

    void onContinuePressed() {
        addDisposable(dataSource.createSurvey(selectedSchool.getId(), selectedSchool.getName(), new Date(), surveyDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(survey -> {
                    accreditationSurveyInteractor.setCurrentSurvey(survey, true);
                    getViewState().navigateToSurvey();
                }, this::handleError));
    }

    void onSearchQueryChanged(String query) {
        List<School> queriedSchools = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for (School school : schools) {
            if (school.getName().toLowerCase().contains(lowerQuery) || school.getId().toLowerCase().contains(lowerQuery)) {
                queriedSchools.add(school);
            }
        }
        getViewState().setSchools(queriedSchools);
    }

    void onEditButtonClick() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        getViewState().showDatePicker(year, month, day);
    }

    void onDatePicked(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        Date surveyStartDate = calendar.getTime();

        getViewState().setStartDate(surveyStartDate);

        this.surveyDate = surveyStartDate;
    }
}
