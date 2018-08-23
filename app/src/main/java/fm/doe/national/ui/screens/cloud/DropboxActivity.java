package fm.doe.national.ui.screens.cloud;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.dropbox.chooser.android.DbxChooser;
import com.dropbox.core.android.Auth;

import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseActivity;

public class DropboxActivity extends BaseActivity implements DropboxView {

    private static final int ACTION_DEFAULT = -1;
    private static final int ACTION_AUTH = 0;
    private static final int ACTION_OPEN_FILE = 1;
    private static final int ACTION_UPLOAD_FILE = 2;

    private static final String EXTRA_ACTION = "EXTRA_ACTION";
    private static final int REQUEST_CODE_OPEN_FILE = 99;

    @InjectPresenter
    DropboxPresenter presenter;

    @ProvidePresenter
    public DropboxPresenter providePresenter() {
        return new DropboxPresenter(getActionFromIntent(getIntent()));
    }

    private boolean haveBeenPaused = false;

    @NonNull
    public static Intent createIntent(@NonNull Context context, DropboxView.Action action) {
        int extraAction = ACTION_DEFAULT;
        switch (action) {
            case AUTH:
                extraAction = ACTION_AUTH;
                break;
            case PICK_FILE:
                extraAction = ACTION_OPEN_FILE;
                break;
            case UPLOAD_FILE:
                extraAction = ACTION_UPLOAD_FILE;
                break;
        }
        return new Intent(context, DropboxActivity.class)
                .putExtra(EXTRA_ACTION, extraAction);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_transparent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (haveBeenPaused && !isFinishing()) {
            presenter.onViewResumedFromPause();
        }
        haveBeenPaused = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        haveBeenPaused = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_OPEN_FILE:
                presenter.onCloudFilePathObtained(resultCode == RESULT_OK ? new DbxChooser.Result(data) : null);
                break;
        }
    }

    private DropboxView.Action getActionFromIntent(@NonNull Intent intent) {
        int action = intent.getIntExtra(EXTRA_ACTION, ACTION_DEFAULT);
        switch (action) {
            case ACTION_AUTH:
                return DropboxView.Action.AUTH;
            case ACTION_OPEN_FILE:
                return DropboxView.Action.PICK_FILE;
            case ACTION_UPLOAD_FILE:
                return Action.UPLOAD_FILE;
            default:
                throw new RuntimeException("DropboxActivity started with wrong action");
        }
    }

    @Override
    public void startAuthentication() {
        Auth.startOAuth2Authentication(this, getString(R.string.dropbox_api_app_key));
    }

    @Override
    public void showSdkChooser() {
        new DbxChooser(getString(R.string.dropbox_api_app_key))
                .forResultType(DbxChooser.ResultType.DIRECT_LINK)
                .launch(this, REQUEST_CODE_OPEN_FILE);
    }

    @Override
    public void die() {
        finish();
    }
}
