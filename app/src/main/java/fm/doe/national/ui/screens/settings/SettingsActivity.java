package fm.doe.national.ui.screens.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;
import com.omega_r.libs.omegatypes.Text;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.cloud.CloudAccountData;
import fm.doe.national.ui.custom_views.CloudConnectionView;
import fm.doe.national.ui.screens.base.BaseActivity;

public class SettingsActivity extends BaseActivity implements
        SettingsView,
        View.OnClickListener,
        CloudConnectionView.OnConnectClickListener,
        SettingsAdapter.Callback {

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

    @BindView(R.id.view_dropbox)
    CloudConnectionView dropboxView;

    @BindView(R.id.view_drive)
    CloudConnectionView driveView;

    public static Intent createIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        logoNameTextView.setOnClickListener(this);
        adapter.setCallback(this);
        accountsRecycler.setAdapter(adapter);

        dropboxView.setCloudName(Text.from(R.string.integration_dropbox));
        dropboxView.setIconDrawableId(R.drawable.ic_dropbox);
        dropboxView.setListener(this);

        driveView.setCloudName(Text.from(R.string.integration_drive));
        driveView.setIconDrawableId(R.drawable.ic_google_drive);
        driveView.setListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_settings;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textview_change_logo:
                presenter.onChangeLogoClick();
                break;
        }
    }

    @Override
    public void onConnectClick(View v) {
        switch (v.getId()) {
            case R.id.view_dropbox:
                presenter.onConnectToDropboxClick();
                break;
            case R.id.view_drive:
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
    public void hideDropboxConnect() {
        dropboxView.setVisibility(View.GONE);
    }

    @Override
    public void hideDriveConnect() {
        driveView.setVisibility(View.GONE);
    }
}
