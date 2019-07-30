package fm.doe.national.ui.screens.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.offline_sync.ui.base.BaseBluetoothActivity;
import fm.doe.national.ui.dialogs.merge_progress.MergeProgressDialogFragment;
import fm.doe.national.ui.screens.license.LicenseActivity;
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

    @BindView(R.id.textview_hint_login)
    View loginHintView;

    @BindView(R.id.textview_auth_name)
    TextView accountTextView;

    @BindView(R.id.textview_auth)
    TextView authTextView;

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

    @OnClick(R.id.textview_license)
    void onCreditsPressed() {
        presenter.onLicensePressed();
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

    @OnClick(R.id.textview_auth)
    void onAuthButtonPressed() {
        presenter.onAuthButtonPressed();
    }

    @Override
    public void setIcon(Image image) {
        image.applyImage(iconImageView, 0);
    }

    @Override
    public void setTitle(Text title) {
        title.applyTo(titleTextView);
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
    public void navigateToLicense() {
        startActivity(LicenseActivity.createIntent(this));
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

    @Override
    public void setAccountName(@Nullable String accountName) {
        if (accountName == null) {
            accountTextView.setText(R.string.label_account);
            authTextView.setText(R.string.label_sign_in);
            loginHintView.setVisibility(View.VISIBLE);
            accreditationButton.setEnabled(false);
            washButton.setEnabled(false);
            return;
        }

        accountTextView.setText(accountName);
        authTextView.setText(R.string.label_sign_out);
        loginHintView.setVisibility(View.INVISIBLE);
        accreditationButton.setEnabled(true);
        washButton.setEnabled(true);
    }
}
