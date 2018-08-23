package fm.doe.national.data.cloud.dropbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.dropbox.chooser.android.DbxChooser;
import com.dropbox.core.android.Auth;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseActivity;

public class DropboxActivity extends BaseActivity {

    public static final int ACTION_AUTH = 0;
    public static final int ACTION_OPEN_FILE = 1;
    public static final int ACTION_UPLOAD_FILE = 2;
    private static final int ACTION_DEFAULT = -1;

    private static final String EXTRA_ACTION = "EXTRA_ACTION";
    private static final int REQUEST_CODE_OPEN_FILE = ACTION_OPEN_FILE;

    private int currentAction = ACTION_DEFAULT;
    private boolean haveBeenPaused = false;

    @NonNull
    public static Intent createIntent(@NonNull Context context, int action) {
        return new Intent(context, DropboxActivity.class)
                .putExtra(EXTRA_ACTION, action);
    }

    private final DropboxCloudAccessor cloudAccessor = MicronesiaApplication.getAppComponent().getDropboxCloudAccessor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseIntent(getIntent());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_transparent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (haveBeenPaused) {
            switch (currentAction) {
                case ACTION_AUTH:
                    if (cloudAccessor.isSuccessfulAuth()) {
                        cloudAccessor.onAuthActionComplete();
                        finish();
                    } else {
                        failure(new Exception("failed to auth")); // TODO: all exceptions to new EX class
                    }
                    break;
                case ACTION_OPEN_FILE:
                    importFileContent(); // just rerun
                    break;
            }
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
                if (resultCode == RESULT_OK) {
                    cloudAccessor.onCloudFilePathObtained(new DbxChooser.Result(data));
                    finish();
                } else {
                    failure(new Exception("failed to get file"));
                }
                break;
        }
    }

    private void parseIntent(@NonNull Intent intent) {
        RuntimeException exception = new RuntimeException("DropboxActivity started with wrong action");
        int action = intent.getIntExtra(EXTRA_ACTION, ACTION_DEFAULT);
        switch (action) {
            case ACTION_AUTH:
                signIn();
                break;
            case ACTION_OPEN_FILE:
                importFileContent();
                break;
            case ACTION_UPLOAD_FILE:
                break;
            default:
                throw exception;
        }
    }

    private void signIn() {
        currentAction = ACTION_AUTH;
        Auth.startOAuth2Authentication(this, getString(R.string.dropbox_api_app_key));
    }

    private void importFileContent() {
        currentAction = ACTION_OPEN_FILE;
        new DbxChooser(getString(R.string.dropbox_api_app_key))
                .forResultType(DbxChooser.ResultType.DIRECT_LINK)
                .launch(this, REQUEST_CODE_OPEN_FILE);
    }

    private void failure(Throwable throwable) {
        cloudAccessor.onActionFailure(throwable);
        finish();
    }
}
