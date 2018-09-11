package fm.doe.national.ui.screens.splash;

import android.content.res.AssetManager;

import com.arellomobile.mvp.InjectViewState;
import com.omega_r.libs.omegatypes.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import fm.doe.national.BuildConfig;
import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.serializable.LinkedSchoolAccreditation;
import fm.doe.national.data.parsers.Parser;
import fm.doe.national.ui.screens.menu.base.MenuPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SplashPresenter extends MenuPresenter<SplashView> {

    private final Parser<LinkedSchoolAccreditation> schoolAccreditationParser =
            MicronesiaApplication.getAppComponent().getSchoolAccreditationParser();
    private final Parser<List<School>> schoolsParser = MicronesiaApplication.getAppComponent().getSchoolsParser();
    private final DataSource dataSource = MicronesiaApplication.getAppComponent().getDataSource();

    public SplashPresenter(AssetManager assetManager) {
        loadAssets(assetManager);
    }

    private void loadAssets(AssetManager assetManager) {
        try {
            InputStream inputStream = assetManager.open(BuildConfig.SURVEYS_FILE_NAME);
            LinkedSchoolAccreditation schoolAccreditation = schoolAccreditationParser.parse(inputStream);

            inputStream = assetManager.open(BuildConfig.SCHOOLS_FILE_NAME);
            List<School> schools = schoolsParser.parse(inputStream);

            addDisposable(dataSource.addSchools(schools)
                    .andThen(dataSource.createSchoolAccreditation(schoolAccreditation))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::showSelector, this::handleError));

        } catch (IOException | Parser.ParseException ex) {
            getViewState().showWarning(Text.from(R.string.title_warning), Text.from(R.string.warn_failed_to_load_assets));
        }
    }
}
