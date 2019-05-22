package fm.doe.national.ui.screens.settings;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.omega_r.libs.omegaintentbuilder.OmegaIntentBuilder;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;
import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import fm.doe.national.R;
import fm.doe.national.app_support.utils.PackageUtils;
import fm.doe.national.app_support.utils.ViewUtils;
import fm.doe.national.data.cloud.CloudAccountData;
import fm.doe.national.data.cloud.CloudType;
import fm.doe.national.data.preferences.entities.AppContext;
import fm.doe.national.ui.custom_views.CloudConnectionCardView;
import fm.doe.national.ui.screens.base.BaseActivity;

public class SettingsActivity extends BaseActivity implements
        SettingsView,
        CloudConnectionCardView.OnConnectClickListener,
        SettingsAdapter.Callback {

    private static final int REQUEST_CODE_GALLERY = 201;

    private final SettingsAdapter adapter = new SettingsAdapter();

    @InjectPresenter
    SettingsPresenter presenter;

    @BindView(R.id.imageview_logo)
    ImageView logoImageView;

    @BindView(R.id.textview_logo)
    TextView logoNameTextView;

    @BindView(R.id.textview_change_logo)
    TextView changeLogoTextView;

    @BindView(R.id.recyclerview_accounts)
    OmegaRecyclerView accountsRecycler;

    @BindView(R.id.cardview_dropbox)
    CloudConnectionCardView dropboxView;

    @BindView(R.id.cardview_drive)
    CloudConnectionCardView driveView;

    @BindView(R.id.layout_possible_connections)
    View possibleConnectionsView;

    @BindView(R.id.layout_connections)
    ConstraintLayout layoutConnections;

    @BindView(R.id.edittext_app_context)
    EditText appContextEditText;

    public static Intent createIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        String versionName = PackageUtils.getVersionName(this);
        int versionCode = PackageUtils.getVersionNumber(this);
        setTitle(getString(R.string.label_settings, versionName, versionCode));

        adapter.setCallback(this);
        accountsRecycler.setAdapter(adapter);

        dropboxView.setCloudName(Text.from(R.string.integration_dropbox));
        dropboxView.setIconDrawableId(R.drawable.ic_dropbox);
        dropboxView.setListener(this);

        driveView.setCloudName(Text.from(R.string.integration_drive));
        driveView.setIconDrawableId(R.drawable.ic_google_drive);
        driveView.setListener(this);

        // TODO: this is temporary, will be implemented in total redesign feature
        appContextEditText.setOnEditorActionListener((tv, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                presenter.onAppContextEntered(appContextEditText.getText().toString());
                return true;
            }
            return false;
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_settings;
    }

    @OnClick(R.id.textview_change_logo)
    public void onChangeLogoClick(View v) {
        presenter.onChangeLogoClick();
    }

    @Override
    public void onConnectClick(View v) {
        switch (v.getId()) {
            case R.id.cardview_dropbox:
                presenter.onConnectToDropboxClick();
                break;
            case R.id.cardview_drive:
                presenter.onConnectToDriveClick();
                break;
        }
    }

    @Override
    public void onImportSchoolsClick(CloudAccountData viewData) {
        presenter.onImportSchoolsClick(viewData);
    }

    @Override
    public void onImportSurveyClick(CloudAccountData viewData) {
        presenter.onImportSurveyClick(viewData);
    }

    @Override
    public void onChooseFolderClick(CloudAccountData viewData) {
        presenter.onChooseFolderClick(viewData);
    }

    @Override
    public void onChooseDefaultClick(CloudAccountData viewData) {
        presenter.onSetDefaultClick(viewData);
    }

    @Override
    public void showAccountConnections(List<CloudAccountData> viewDataList) {
        if (viewDataList.size() > 0) {
            adapter.setItems(viewDataList);
            accountsRecycler.setHeadersVisibility(true);
        } else {
            accountsRecycler.setHeadersVisibility(false);
        }
    }

    @Override
    public void hideConnectViews(List<CloudType> types) {
        // hide possible connection view if all clouds already connected
        if (CloudType.values().length - 1 == types.size()) {
            possibleConnectionsView.setVisibility(View.GONE);
        } else {
            for (CloudType type : types) {
                switch (type) {
                    case DRIVE:
                        hideDrive();
                        break;
                    case DROPBOX:
                        hideDropbox();
                        break;
                }
            }
        }
    }

    private void hideDrive() {
        driveView.setVisibility(View.GONE);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layoutConnections);
        constraintSet.connect(dropboxView.getId(), ConstraintSet.START, R.id.view_left_panel, ConstraintSet.START);
        constraintSet.connect(dropboxView.getId(), ConstraintSet.END, R.id.view_left_panel, ConstraintSet.END);
        constraintSet.applyTo(layoutConnections);
    }

    private void hideDropbox() {
        dropboxView.setVisibility(View.GONE);
    }

    @Override
    public void setLogo(String path) {
        ViewUtils.setScaledDownImageTo(logoImageView, path);
    }

    @Override
    public void setLogoName(String logoName) {
        logoNameTextView.setText(logoName);
    }

    @Override
    public void pickPhotoFromGallery() {
        OmegaIntentBuilder.from(this)
                .pick()
                .image()
                .multiply(false)
                .createIntentHandler(this)
                .startActivityForResult(REQUEST_CODE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                presenter.onImagePicked(bitmap);
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setAppContext(AppContext appContext) {
        appContextEditText.setText(String.valueOf(appContext.getValue()));
    }
}
