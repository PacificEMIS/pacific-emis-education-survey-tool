package fm.doe.national.data.cloud.dropbox;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.android.Auth;
import com.dropbox.core.http.OkHttp3Requestor;
import com.dropbox.core.v2.DbxClientV2;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.cloud.CloudAccessor;
import fm.doe.national.data.cloud.drive.DriveActivity;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.SingleSubject;

public class DropboxCloudAccessor implements CloudAccessor {

    private final static String PREF_TOKEN = "dropbox-access-token";
    private final static String PREF_USER_ID = "dropbox-user-id";


    private Context context;
    private SingleSubject<Object> authSingle;
    private DbxClientV2 dropboxClient;

    private final SharedPreferences sharedPreferences = MicronesiaApplication.getAppComponent().getSharedPreferences();

    public DropboxCloudAccessor(Context appContext) {
        context = appContext;
    }

    @Override
    public Single<String> importContentFromCloud() {
        Single<String> single = Single.fromCallable(() ->  {
            //DropboxClientFactory.getClient().files().listFolder("");
            return "";
        });
        if (hasAuthToken()) {
            return single;
        } else {
            return auth().andThen(single);
        }
    }

    @Override
    public Completable exportContentToCloud(@NonNull String content) {
        return null;
    }

    @Override
    public Completable auth() {
        authSingle = SingleSubject.create();
        startActivityAction(DriveActivity.ACTION_AUTH);
        return Completable.fromSingle(authSingle).subscribeOn(Schedulers.io());
    }

    protected void onAuthActionComplete(boolean success) {
        if (success) {
            initDropbox();
            authSingle.onSuccess(new Object());
        } else {
            authSingle.onError(new Exception("Failed to authenticate"));
        }
    }

    protected boolean isSuccessfullAuth() {
        return Auth.getOAuth2Token() != null;
    }

    private void startActivityAction(int action) {
        Activity activity = ((MicronesiaApplication)context).getCurrentActivity();
        if (activity != null) {
            activity.startActivity(DropboxActivity.createIntent(activity, action));
        } else {
            //onActionFailure(new Exception("No activities running"));
        }
    }

    private boolean hasAuthToken() {
        String accessToken = sharedPreferences.getString(PREF_TOKEN, null);
        return accessToken != null;
    }

    private void initDropbox() {
        String accessToken = sharedPreferences.getString(PREF_TOKEN, null);
        if (accessToken == null) {
            accessToken = Auth.getOAuth2Token();
            if (accessToken != null) {
                sharedPreferences.edit().putString(PREF_TOKEN, accessToken).apply();
                initClient(accessToken);
            }
        } else {
            initClient(accessToken);
        }

        String uid = Auth.getUid();
        String storedUid = sharedPreferences.getString(PREF_USER_ID, null);
        if (uid != null && !uid.equals(storedUid)) {
            sharedPreferences.edit().putString(PREF_USER_ID, uid).apply();
        }
    }

    private void initClient(@NonNull String accessToken) {
        DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder(context.getApplicationInfo().name)
                .withHttpRequestor(new OkHttp3Requestor(OkHttp3Requestor.defaultOkHttpClient()))
                .build();
        dropboxClient = new DbxClientV2(requestConfig, accessToken);
    }
}
