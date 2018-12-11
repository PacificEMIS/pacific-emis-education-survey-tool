package fm.doe.national.ui.screens.criterias;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.utils.Constants;
import fm.doe.national.utils.ViewUtils;

public class FullscreenImageActivity extends BaseActivity {
    private static final String EXTRA_PATH = "EXTRA_PATH";
    private static final String EXTRA_TRANSITION_NAME = "EXTRA_TRANSITION_NAME";

    @BindView(R.id.imageview_content)
    ImageView contentImageView;

    public static Intent createIntent(Context context, String imagePath, String transitionName) {
        return new Intent(context, FullscreenImageActivity.class)
                .putExtra(EXTRA_PATH, imagePath)
                .putExtra(EXTRA_TRANSITION_NAME, transitionName);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_fullscreen_image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String imagePath = intent.getStringExtra(EXTRA_PATH);
        String transitionName = intent.getStringExtra(EXTRA_TRANSITION_NAME);
        if (imagePath == null || transitionName == null) throw new RuntimeException(Constants.Errors.WRONG_INTENT);

        ViewCompat.setTransitionName(contentImageView, transitionName);

        ViewUtils.setImageTo(contentImageView, imagePath);
    }

    @OnClick(R.id.imageview_content)
    public void onImageClick(View v) {
        supportFinishAfterTransition();
    }

}
