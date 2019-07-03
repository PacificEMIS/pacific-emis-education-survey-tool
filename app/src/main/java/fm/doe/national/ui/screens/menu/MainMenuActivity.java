package fm.doe.national.ui.screens.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Image;
import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import fm.doe.national.R;
import fm.doe.national.core.preferences.entities.SurveyType;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.offline_sync.ui.base.BaseBluetoothActivity;
import fm.doe.national.ui.dialogs.merge_progress.MergeProgressDialogFragment;
import fm.doe.national.ui.screens.settings.SettingsActivity;
import fm.doe.national.ui.screens.surveys.SurveysActivity;

public class MainMenuActivity extends BaseBluetoothActivity implements MainMenuView {

    private static final String TAG_PROGRESS_FRAGMENT = "TAG_PROGRESS_FRAGMENT";

    @InjectPresenter
    MainMenuPresenter presenter;

    @BindView(R.id.textview_title)
    TextView titleTextView;

    @BindView(R.id.imageview_icon)
    ImageView iconImageView;

    @BindView(R.id.button_accreditation)
    Button accreditationButton;

    @BindView(R.id.button_wash)
    Button washButton;

    public static Intent createIntent(Context parentContext) {
        return new Intent(parentContext, MainMenuActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main_menu;
    }

    @OnClick(R.id.textview_credits)
    void onCreditsPressed() {
        presenter.onCreditsPressed();
    }

    @OnClick(R.id.textview_settings)
    void onSettingsPressed() {
        presenter.onSettingsPressed();
    }

    @OnClick(R.id.button_accreditation)
    void onAccreditationPressed() {
        presenter.onAccreditationPressed();
    }

    @OnClick(R.id.button_wash)
    void onWashPressed() {
        presenter.onWashPressed();
    }

    @OnClick(R.id.textview_merge)
    void onMergePressed() {
        presenter.onMergePressed();
    }

    @Override
    public void setIcon(Image image) {
        image.applyImage(iconImageView, R.drawable.ic_default_logo);
    }

    @Override
    public void setTitle(Text title) {
        title.applyTo(titleTextView);
    }

    @Override
    public void setCurrentSurveyType(@Nullable SurveyType surveyType) {
        accreditationButton.setActivated(false);
        washButton.setActivated(false);

        if (surveyType != null) {
            switch (surveyType) {
                case SCHOOL_ACCREDITATION:
                    accreditationButton.setActivated(true);
                    washButton.setActivated(false);
                    break;
                case WASH:
                    accreditationButton.setActivated(false);
                    washButton.setActivated(true);
                    break;
            }
        }
    }

    @Override
    public void navigateToSurveys() {
        startActivity(SurveysActivity.createIntent(this));
    }

    @Override
    public void navigateToSettings() {
        startActivity(SettingsActivity.createIntent(this));
    }

    @Override
    public void navigateToCredits() {
        showToast(Text.from(R.string.coming_soon));
    }

    @Nullable
    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showMergeProgress() {
        MergeProgressDialogFragment.create().show(getSupportFragmentManager(), TAG_PROGRESS_FRAGMENT);
    }
}
