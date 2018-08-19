package fm.doe.national.ui.screens.splash;

import android.os.Handler;
import android.os.Looper;
import android.support.transition.ChangeBounds;
import android.support.transition.Transition;
import android.view.animation.AccelerateInterpolator;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.data_source.models.School;
import fm.doe.national.ui.screens.base.BasePresenter;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */

@InjectViewState
public class SplashPresenter extends BasePresenter<SplashView> {

    private static final long DURATION_ANIMATION = 1000; // 1 sec
    private static final long POST_DELAYED = 1000; //ms

    public SplashPresenter() {
        //TODO for test
        List<School> schools = new ArrayList<>();
        schools.add(new School() {
            @Override
            public String getId() {
                return "test";
            }

            @Override
            public String getName() {
                return "School accreditation";
            }
        });
        schools.add(new School() {
            @Override
            public String getId() {
                return "test1";
            }

            @Override
            public String getName() {
                return "School data verification";
            }
        });
        schools.add(new School() {
            @Override
            public String getId() {
                return "test1";
            }

            @Override
            public String getName() {
                return "Monitoring and evalution";
            }
        });
        getViewState().setSchools(schools);
        startPostDelayedAnimate();
    }

    private void startPostDelayedAnimate() {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> getViewState().startAnimate(createTransition()), POST_DELAYED);
    }

    private Transition createTransition() {
        return new ChangeBounds()
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(DURATION_ANIMATION);
    }

    public void onTypeTestClicked() {
        getViewState().showSchoolAccreditationScreen();
    }

}
