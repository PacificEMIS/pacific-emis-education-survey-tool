package fm.doe.national.ui.screens.splash;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.ui.screens.menu.base.MenuActivity;
import fm.doe.national.ui.screens.menu.base.MenuPresenter;
import fm.doe.national.ui.screens.shool_accreditation.SchoolAccreditationActivity;

/**
 * Created by Alexander Chibirev on 8/9/2018.
 */

public class SplashActivity extends MenuActivity implements SplashView, TypeTestAdapter.Callback {

    @InjectPresenter
    SplashPresenter splashPresenter;

    @BindView(R.id.layout_splash_start)
    ConstraintLayout constraintLayout;

    @Override
    protected int getContentView() {
        return R.layout.activity_splash_start;
    }

    @Override
    public void startAnimate(@NonNull Transition transition) {
        ConstraintSet newConstraintSet = new ConstraintSet();
        newConstraintSet.clone(getApplicationContext(), R.layout.activity_splash_end);
        newConstraintSet.applyTo(constraintLayout);
        TransitionManager.beginDelayedTransition(constraintLayout, transition);
    }

    @Override
    protected MenuPresenter getPresenter() {
        return splashPresenter;
    }

}
