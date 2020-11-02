package org.pacific_emis.surveys.survey_core.ui.photos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.core.view.ViewCompat;

import com.omega_r.libs.omegatypes.image.UrlImage;

import org.pacific_emis.surveys.core.ui.screens.base.BaseActivity;
import org.pacific_emis.surveys.core.utils.Constants;
import org.pacific_emis.surveys.core.utils.ViewUtils;
import org.pacific_emis.surveys.survey_core.R;

public class FullscreenImageActivity extends BaseActivity {
    private static final String EXTRA_PATH = "EXTRA_PATH";
    private static final String EXTRA_TRANSITION_NAME = "EXTRA_TRANSITION_NAME";

    private ImageView contentImageView;

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

        contentImageView = findViewById(R.id.imageview_content);
        contentImageView.setOnClickListener(this::onImageClick);

        ViewCompat.setTransitionName(contentImageView, transitionName);

        ViewUtils.setImageTo(contentImageView, new UrlImage(imagePath));
    }

    public void onImageClick(View v) {
        supportFinishAfterTransition();
    }

}
