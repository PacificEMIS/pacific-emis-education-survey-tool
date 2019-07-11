package fm.doe.national.survey_core.ui.photos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;

import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.core.utils.Constants;
import fm.doe.national.survey_core.R;

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

        Glide.with(this)
                .load(imagePath)
                .into(contentImageView);
    }

    public void onImageClick(View v) {
        supportFinishAfterTransition();
    }

}
