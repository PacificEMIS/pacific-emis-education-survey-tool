package fm.doe.national.data.cloud.dropbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

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

    private int currentAction = ACTION_DEFAULT;

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
        if (currentAction == ACTION_AUTH) {
            cloudAccessor.onAuthActionComplete(cloudAccessor.isSuccessfullAuth());
            finish();
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
    }
}
