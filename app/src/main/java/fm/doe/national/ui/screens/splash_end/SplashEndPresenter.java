package fm.doe.national.ui.screens.splash_end;

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
import fm.doe.national.ui.screens.menu.base.MenuPresenter;
import fm.doe.national.utils.Constants;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SplashEndPresenter extends MenuPresenter<SplashEndView> {

}
