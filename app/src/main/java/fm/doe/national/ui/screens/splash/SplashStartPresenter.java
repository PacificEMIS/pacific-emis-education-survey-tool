package fm.doe.national.ui.screens.splash;

import android.content.res.AssetManager;

import com.omegar.mvp.InjectViewState;

import java.util.List;
import java.util.concurrent.TimeUnit;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.serialization.parsers.Parser;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.core.utils.Constants;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SplashStartPresenter extends BasePresenter<SplashStartView> {

    private final Parser<Survey> schoolAccreditationParser = MicronesiaApplication.getInjection().getCoreComponent().getSurveyParser();
    private final Parser<List<School>> schoolsParser = MicronesiaApplication.getInjection().getCoreComponent().getSchoolsParser();
    private final DataSource dataSource = MicronesiaApplication.getInjection().getCoreComponent().getDataSource();
    
    private boolean isFinishedLoading;

    public SplashStartPresenter(AssetManager assetManager) {
        loadAssets(assetManager);
        scheduleShowingProgress();
    }

    private void loadAssets(AssetManager assetManager) {
        addDisposable(Single.fromCallable(() -> schoolAccreditationParser.parse(assetManager.open(dataSource.getSurveyTemplateFileName())))
                .flatMapCompletable(schoolAccreditation -> Single.fromCallable(() ->
                    schoolsParser.parse(assetManager.open(dataSource.getSchoolsFileName())))
                        .flatMapCompletable(schools -> dataSource.loadSchools()
                                    .flatMapCompletable(resultList -> {
                                        if (resultList.isEmpty()) {
                                            return dataSource
                                                    .rewriteAllSchools(schools)
                                                    .andThen(dataSource.rewriteTemplateSurvey(schoolAccreditation));
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
