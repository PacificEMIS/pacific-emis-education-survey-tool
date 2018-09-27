package fm.doe.national.ui.screens.splash;

import android.content.res.AssetManager;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;
import java.util.concurrent.TimeUnit;

import fm.doe.national.BuildConfig;
import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.serializable.LinkedSchoolAccreditation;
import fm.doe.national.data.parsers.Parser;
import fm.doe.national.ui.screens.base.BasePresenter;
import fm.doe.national.utils.Constants;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SplashStartPresenter extends BasePresenter<SplashStartView> {

    private final Parser<LinkedSchoolAccreditation> schoolAccreditationParser =
            MicronesiaApplication.getAppComponent().getSchoolAccreditationParser();
    private final Parser<List<School>> schoolsParser = MicronesiaApplication.getAppComponent().getSchoolsParser();
    private final DataSource dataSource = MicronesiaApplication.getAppComponent().getDataSource();
    
    private boolean isFinishedLoading;

    public SplashStartPresenter(AssetManager assetManager) {
        loadAssets(assetManager);
        scheduleShowingProgress();
    }

    private void loadAssets(AssetManager assetManager) {
        addDisposable(Single.fromCallable(() -> schoolAccreditationParser.parse(assetManager.open(BuildConfig.SURVEYS_FILE_NAME)))
                .flatMapCompletable(schoolAccreditation -> Single.fromCallable(() ->
                    schoolsParser.parse(assetManager.open(BuildConfig.SCHOOLS_FILE_NAME)))
                        .flatMapCompletable(schools -> dataSource.requestSchools()
                                    .flatMapCompletable(resultList -> {
                                        if (resultList.isEmpty()) {
                                            return dataSource
                                                    .addSchools(schools)
                                                    .andThen(dataSource.createSchoolAccreditation(schoolAccreditation));
                                        }
                                        return Completable.complete(); // already exists
                                    })))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    isFinishedLoading = true;
                    getViewState().navigateToSplashEnd();
                } , this::handleError));
    }

    private void scheduleShowingProgress() {
        addDisposable(Completable.complete()
                .delay(Constants.SECONDS_TO_LONG_LOADING_INDICATION, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    if (!isFinishedLoading) getViewState().showLongLoadingProgressBar();
                }));
    }
}