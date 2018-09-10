package fm.doe.national.ui.screens.standard;

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
    public static final String TRANSITION_IMAGE = "TRANSITION_IMAGE";
    private static final String EXTRA_PATH = "EXTRA_PATH";

    @BindView(R.id.imageview_content)
    ImageView contentImageView;

    public static Intent createIntent(Context context, String imagePath) {
        return new Intent(context, FullscreenImageActivity.class)
                .putExtra(EXTRA_PATH, imagePath);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_fullscreen_image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String imagePath = getIntent().getStringExtra(EXTRA_PATH);
        if (imagePath == null) throw new RuntimeException(Constants.Errors.WRONG_INTENT);

        ViewCompat.setTransitionName(contentImageView, TRANSITION_IMAGE);

        ViewUtils.setImageTo(contentImageView, imagePath);
    }

    @OnClick(R.id.imageview_content)
    public void onImageClick(View v) {
        supportFinishAfterTransition();
    }

}
