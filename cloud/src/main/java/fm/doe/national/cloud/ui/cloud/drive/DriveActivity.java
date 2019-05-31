package fm.doe.national.cloud.ui.cloud.drive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.SearchableField;

import fm.doe.national.cloud.R;
import fm.doe.national.cloud.di.ComponentInjector;
import fm.doe.national.cloud.model.drive.DriveCloudAccessor;
import fm.doe.national.core.data.exceptions.FileImportException;
import fm.doe.national.core.data.exceptions.PickException;
import fm.doe.national.core.data.exceptions.PickerDeclinedException;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.core.utils.Constants;

public class DriveActivity extends BaseActivity {

    public static final int ACTION_AUTH = 0;
    public static final int ACTION_OPEN_FILE = 1;
    public static final int ACTION_PICK_FOLDER = 3;
    private static final int ACTION_DEFAULT = -1;

    private static final int REQUEST_CODE_AUTH = ACTION_AUTH;
    private static final int REQUEST_CODE_OPEN_FILE = ACTION_OPEN_FILE;
    private static final int REQUEST_CODE_PICK_FOLDER = ACTION_PICK_FOLDER;

    private static final String EXTRA_ACTION = "EXTRA_ACTION";

    private DriveCloudAccessor driveCloudAccessor;

    @NonNull
    public static Intent createIntent(@NonNull Context context, int action) {
        return new Intent(context, DriveActivity.class).putExtra(EXTRA_ACTION, action);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_transparent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driveCloudAccessor = ComponentInjector.getComponent(getApplication()).getDriveCloudAccessor();
        parseIntent(getIntent());
    }

    private void parseIntent(@NonNull Intent intent) {
        int action = intent.getIntExtra(EXTRA_ACTION, ACTION_DEFAULT);
        switch (action) {
            case ACTION_AUTH:
                signIn();
                break;
            case ACTION_OPEN_FILE:
                importFileContent();
                break;
            case ACTION_PICK_FOLDER:
                pickFolder();
                break;
            default:
                throw new RuntimeException(Constants.Errors.WRONG_INTENT);
        }
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
                    driveCloudAccessor.onActionFailure(new PickerDeclinedException());
                }
                break;
            case REQUEST_CODE_PICK_FOLDER:
                if (resultCode == RESULT_OK) {
                    DriveId id = data.getParcelableExtra(OpenFileActivityOptions.EXTRA_RESPONSE_DRIVE_ID);
                    driveCloudAccessor.onFolderPicked(id);
                } else {
                    driveCloudAccessor.onActionFailure(new PickerDeclinedException());
                }
        }
        finish();
    }

    private void signIn() {
        GoogleSignInClient GoogleSignInClient = buildGoogleSignInClient();
        startActivityForResult(GoogleSignInClient.getSignInIntent(), REQUEST_CODE_AUTH);
    }

    @NonNull
    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(Drive.SCOPE_FILE)
                        .build();
        return GoogleSignIn.getClient(this, signInOptions);
    }

    private void importFileContent() {
        DriveClient client = driveCloudAccessor.getDriveClient();
        if (client == null) {
            failure(new FileImportException(Constants.Errors.DRIVE_CLIENT_IS_NULL));
            return;
        }

        OpenFileActivityOptions options = new OpenFileActivityOptions.Builder()
                .setActivityTitle(getString(R.string.title_drive_open_file))
                .build();

        client.newOpenFileActivityIntentSender(options)
                .continueWith(task -> {
                    startIntentSenderForResult(task.getResult(), REQUEST_CODE_OPEN_FILE,
                            null, 0, 0, 0);
                    return null;
                });
    }

    private void pickFolder() {
        DriveClient client = driveCloudAccessor.getDriveClient();
        if (client == null) {
            failure(new PickException("DriveClient is null"));
            return;
        }
        OpenFileActivityOptions openOptions = new OpenFileActivityOptions.Builder()
                .setSelectionFilter(
                        Filters.eq(SearchableField.MIME_TYPE, DriveFolder.MIME_TYPE))
                .setActivityTitle(getString(R.string.title_select_folder))
                .build();
        client.newOpenFileActivityIntentSender(openOptions)
                .continueWith(task -> {
                    startIntentSenderForResult(
                            task.getResult(), REQUEST_CODE_PICK_FOLDER, null, 0, 0, 0);
                    return null;
                });
    }

    private void failure(Throwable throwable) {
        driveCloudAccessor.onActionFailure(throwable);
        finish();
    }
}
