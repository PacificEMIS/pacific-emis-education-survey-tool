package fm.doe.national.ui.screens.cloud;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.CreateFileActivityOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.SearchableField;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.cloud.drive.DriveCloudAccessor;
import fm.doe.national.data.cloud.exceptions.FileExportException;
import fm.doe.national.data.cloud.exceptions.FileImportException;
import fm.doe.national.data.cloud.exceptions.PickException;
import fm.doe.national.ui.screens.base.BaseActivity;

public class DriveActivity extends BaseActivity {

    public static final int ACTION_AUTH = 0;
    public static final int ACTION_OPEN_FILE = 1;
    public static final int ACTION_UPLOAD_FILE = 2;
    public static final int ACTION_PICK_FOLDER = 3;
    private static final int ACTION_DEFAULT = -1;

    private static final int REQUEST_CODE_AUTH = ACTION_AUTH;
    private static final int REQUEST_CODE_OPEN_FILE = ACTION_OPEN_FILE;
    private static final int REQUEST_CODE_UPLOAD_FILE = ACTION_UPLOAD_FILE;
    private static final int REQUEST_CODE_PICK_FOLDER = ACTION_PICK_FOLDER;

    private static final String EXTRA_ACTION = "EXTRA_ACTION";
    private static final String EXTRA_ACTION_UPLOAD_FILE = "EXTRA_ACTION_UPLOAD_FILE";

    private final DriveCloudAccessor driveCloudAccessor = MicronesiaApplication.getAppComponent().getDriveCloudAccessor();

    @NonNull
    public static Intent createIntent(@NonNull Context context, int action) {
        return new Intent(context, DriveActivity.class)
                .putExtra(EXTRA_ACTION, action);
    }

    @NonNull
    public static Intent createUploadIntent(@NonNull Context context, @NonNull String content) {
        return new Intent(context, DriveActivity.class)
                .putExtra(EXTRA_ACTION, ACTION_UPLOAD_FILE)
                .putExtra(EXTRA_ACTION_UPLOAD_FILE, content);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_transparent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    driveCloudAccessor.onActionFailure(new FileImportException("Failed to open"));
                }
                break;
            case REQUEST_CODE_PICK_FOLDER:
                if (resultCode == RESULT_OK) {
                    DriveId id = data.getParcelableExtra(OpenFileActivityOptions.EXTRA_RESPONSE_DRIVE_ID);
                    driveCloudAccessor.onFolderPicked(id);
                } else {
                    driveCloudAccessor.onActionFailure(new PickException("Picker declined"));
                }
            case REQUEST_CODE_UPLOAD_FILE:
                driveCloudAccessor.onExport();
                break;
        }
        finish();
    }

    private void parseIntent(@NonNull Intent intent) {
        RuntimeException exception = new RuntimeException("DriveActivity started with wrong action");
        int action = intent.getIntExtra(EXTRA_ACTION, ACTION_DEFAULT);
        switch (action) {
            case ACTION_AUTH:
                signIn();
                break;
            case ACTION_OPEN_FILE:
                importFileContent();
                break;
            case ACTION_UPLOAD_FILE:
                String content = intent.getStringExtra(EXTRA_ACTION_UPLOAD_FILE);
                if (content == null) {
                    throw exception;
                }
                exportContentToNewFile(content);
                break;
            case ACTION_PICK_FOLDER:
                pickFolder();
                break;
            default:
                throw exception;
        }
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
            failure(new FileImportException("driveCloudAccessor not set up"));
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

    private void exportContentToNewFile(String content) {
        DriveResourceClient resourceClient = driveCloudAccessor.getDriveResourceClient();
        DriveClient client = driveCloudAccessor.getDriveClient();
        if (resourceClient == null || client == null) {
            failure(new FileExportException("driveCloudAccessor not set up"));
            return;
        }

        resourceClient.createContents()
                .continueWithTask(task -> {
                    DriveContents contents = task.getResult();
                    OutputStream outputStream = contents.getOutputStream();
                    try (Writer writer = new OutputStreamWriter(outputStream)) {
                        writer.write(content);
                    }

                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle(getString(R.string.survey_report, new Date().toString())) // TODO: change to proper file name
                            .setMimeType("text/plain")
                            .build();

                    CreateFileActivityOptions createOptions = new CreateFileActivityOptions.Builder()
                            .setInitialDriveContents(contents)
                            .setInitialMetadata(changeSet)
                            .build();
                    return client.newCreateFileActivityIntentSender(createOptions);
                })
                .addOnSuccessListener(this,
                        intentSender -> {
                            try {
                                startIntentSenderForResult(
                                        intentSender, REQUEST_CODE_UPLOAD_FILE,
                                        null, 0, 0, 0);
                            } catch (IntentSender.SendIntentException e) {
                                failure(e);
                            }
                        })
                .addOnFailureListener(this, this::failure);
    }

    private void pickFolder() {
        DriveClient client = driveCloudAccessor.getDriveClient();
        if (client == null)  {
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
