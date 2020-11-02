package org.pacific_emis.surveys.ui.screens.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omega_r.libs.omegatypes.image.Image;
import com.omegar.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import org.pacific_emis.surveys.R;
import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;
import org.pacific_emis.surveys.core.utils.ViewUtils;
import org.pacific_emis.surveys.offline_sync.ui.base.BaseBluetoothActivity;
import org.pacific_emis.surveys.ui.dialogs.merge_progress.MergeProgressDialogFragment;
import org.pacific_emis.surveys.ui.screens.license.LicenseActivity;
import org.pacific_emis.surveys.ui.screens.settings.SettingsActivity;
import org.pacific_emis.surveys.ui.screens.surveys.SurveysActivity;

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

    @BindView(R.id.imagebutton_merge_hint)
    View mergeHintView;

    @BindView(R.id.view_poput_constraint)
    View popupConstraintView;

    private View popupView;

    public static Intent createIntent(Context parentContext) {
        return new Intent(parentContext, MainMenuActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupView = getLayoutInflater().inflate (R.layout.popup_hint, null, false);
        ((TextView)popupView.findViewById(R.id.textview_hint)).setText(R.string.label_hint_merge_mode);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main_menu;
    }

    @OnClick(R.id.textview_license)
    void onLicensePressed() {
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

    @OnClick(R.id.imagebutton_merge_hint)
    void onMergeHintPressed() {
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setClippingEnabled(false);

        // need to measure view before it rendered
        // showAsDropDown cannot draw popup above anchor in ViewHolder
        // so just offset it manually
        popupView.measure(View.MeasureSpec.makeMeasureSpec(popupConstraintView.getMeasuredWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        popupWindow.showAsDropDown(mergeHintView,
                0,
                -mergeHintView.getMeasuredHeight() - popupView.getMeasuredHeight(),
                Gravity.TOP);
    }

    @Override
    public void setIcon(Image image) {
        ViewUtils.setImageTo(iconImageView, image);
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
