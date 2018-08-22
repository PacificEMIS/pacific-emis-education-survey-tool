package fm.doe.national.data.cloud.drive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityOptions;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;

public class DriveActivity extends AppCompatActivity {

    public static final int ACTION_AUTH = 0;
    public static final int ACTION_OPEN_FILE = 1;
    public static final int ACTION_UPLOAD_FILE = 2;
    private static final int ACTION_DEFAULT = -1;

    private static final int REQUEST_CODE_AUTH = ACTION_AUTH;
    private static final int REQUEST_CODE_OPEN_FILE = ACTION_OPEN_FILE;
    private static final int REQUEST_UPLOAD_OPEN_FILE = ACTION_UPLOAD_FILE;

    private static final String ACTION_EXTRA = "ACTION_EXTRA";

    private final DriveCloudAccessor driveCloudAccessor =
            MicronesiaApplication.getAppComponent().getCloudAccessor();

    @NonNull
    public static Intent createIntent(@NonNull Context context, int action) {
        return new Intent(context, DriveActivity.class)
                .putExtra(ACTION_EXTRA, action);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);
        parseIntent(getIntent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_AUTH:
                driveCloudAccessor.onAuth();
                break;
            case REQUEST_CODE_OPEN_FILE:
                if (resultCode == RESULT_OK) {
                    DriveId id = data.getParcelableExtra(OpenFileActivityOptions.EXTRA_RESPONSE_DRIVE_ID);
                    driveCloudAccessor.onFileContentObtained(id);
                } else {
                    driveCloudAccessor.onFileContentObtained(null);
                }
                break;
        }
        finish();
    }

    private void parseIntent(@NonNull Intent intent) {
        int action = intent.getIntExtra(ACTION_EXTRA, ACTION_DEFAULT);
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
                throw new RuntimeException("DriveActivity started with wrong action");
        }
    }

    private void signIn() {
        GoogleSignInClient GoogleSignInClient = buildGoogleSignInClient();
        startActivityForResult(GoogleSignInClient.getSignInIntent(), REQUEST_CODE_AUTH);
    }

    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(Drive.SCOPE_FILE)
                        .build();
        return GoogleSignIn.getClient(this, signInOptions);
    }

    private void importFileContent() {
        OpenFileActivityOptions options = new OpenFileActivityOptions.Builder()
                .setActivityTitle(getString(R.string.title_drive_open_file))
                .build();

        DriveClient client = driveCloudAccessor.getDriveClient();
        if (client == null) {
            driveCloudAccessor.onFileContentObtained(null);
        } else {
            client.newOpenFileActivityIntentSender(options)
                    .continueWith(task -> {
                        startIntentSenderForResult(task.getResult(), REQUEST_CODE_OPEN_FILE,
                            null, 0, 0, 0);
                        return null;
                    });
        }
    }
}
