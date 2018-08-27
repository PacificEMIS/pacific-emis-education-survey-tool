package fm.doe.national.ui.screens.splash;

import android.content.res.AssetManager;
import android.support.transition.ChangeBounds;
import android.support.transition.Transition;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;

import com.arellomobile.mvp.InjectViewState;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.parsers.Parser;
import fm.doe.national.ui.screens.menu.base.MenuPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SplashPresenter extends MenuPresenter<SplashView> {

    @Inject
    Parser<SchoolAccreditation> accreditationParser;

    @Inject
    Parser<List<School>> schoolsParser;

    @Inject
    DataSource dataSource;

    private static final long DURATION_ANIMATION = 1000; // 1 sec

    public SplashPresenter(AssetManager manager) {
        MicronesiaApplication.getAppComponent().inject(this);
        try {
            add(
                    dataSource.createSchoolAccreditation(accreditationParser.parse(manager.open("surveys.xml")))
                            .andThen(dataSource.addSchools(schoolsParser.parse(manager.open("schools.csv"))))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    () -> {
                                        Log.d("ASDASD", "ASD");
                                        getViewState().startAnimate(createTransition());
                                    },
                                    throwable -> Log.e("ASD", "setAssets: "))
            );

        } catch (IOException | NullPointerException ex) {
            Log.e("ASD", "setAssets: ");
        }
    }

    private Transition createTransition() {
        return new ChangeBounds()
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(DURATION_ANIMATION);
    }

}
