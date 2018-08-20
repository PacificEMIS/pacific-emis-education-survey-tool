package fm.doe.national.ui.screens.main;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.OpenFileActivityOptions;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseActivity;

public class MainActivity extends BaseActivity {
    private static final int REQUEST_CODE_SIGN_IN = 0;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 1;
    private static final int REQUEST_CODE_CREATOR = 2;

    private DriveClient mDriveClient;
    private DriveResourceClient mDriveResourceClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MicronesiaApplication.getAppComponent().inject(this);
        signIn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    // Use the last signed in account here since it already have a Drive scope.
                    mDriveClient = Drive.getDriveClient(this, GoogleSignIn.getLastSignedInAccount(this));
                    // Build a drive resource client.
                    mDriveResourceClient =
                            Drive.getDriveResourceClient(this, GoogleSignIn.getLastSignedInAccount(this));

                    OpenFileActivityOptions options = new OpenFileActivityOptions.Builder().setActivityTitle("WOOW TITLE")
                            .build();
                    mDriveClient.newOpenFileActivityIntentSender(options)
                            .continueWith(intentSenderTask -> {
                                startIntentSenderForResult(intentSenderTask.getResult(), REQUEST_CODE_CREATOR,
                                        null, 0, 0, 0);
                                return null;
                            });
                }
                break;
            case REQUEST_CODE_CREATOR:
                if (resultCode == RESULT_OK) {
                    DriveId id = data.getParcelableExtra(OpenFileActivityOptions.EXTRA_RESPONSE_DRIVE_ID);
                    DriveFile file = id.asDriveFile();

                    mDriveResourceClient.openFile(file, DriveFile.MODE_READ_ONLY)
                            .continueWith(task -> {
                                DriveContents contents = task.getResult();
                                try (BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(contents.getInputStream()))) {
                                    StringBuilder builder = new StringBuilder();
                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        builder.append(line).append("\n");
                                    }
                                    String s = builder.toString();
                                }
                                return mDriveResourceClient.discardContents(contents);
                            });
                }
                break;

        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    private void signIn() {
        GoogleSignInClient GoogleSignInClient = buildGoogleSignInClient();
        startActivityForResult(GoogleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(Drive.SCOPE_FILE)
                        .build();
        return GoogleSignIn.getClient(this, signInOptions);
    }

}
