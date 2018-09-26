package fm.doe.national.ui.screens.splash_end;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.ui.screens.menu.base.MenuActivity;
import fm.doe.national.ui.screens.menu.base.MenuPresenter;

public class SplashEndActivity extends MenuActivity implements SplashEndView {

    private static final int ANIM_OFFSET_Y = 200;
    private static final int ANIM_DELAY = 100;

    @BindView(R.id.textview_title)
    TextView titleTextView;

    @BindView(R.id.recyclerview_drawer)
    View recyclerView;

    @InjectPresenter
    SplashEndPresenter splashEndPresenter;

    @Override
    protected int getContentView() {
        return R.layout.activity_splash_end;
    }

    @Override
    protected MenuPresenter getPresenter() {
        return splashEndPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(null);
        }

        float y = recyclerView.getY();
        recyclerView.setAlpha(0.0f);
        recyclerView.setY(y + ANIM_OFFSET_Y);
        recyclerView.animate()
                .translationY(y)
                .alpha(1.0f)
                .setStartDelay(ANIM_DELAY);
    }
}
