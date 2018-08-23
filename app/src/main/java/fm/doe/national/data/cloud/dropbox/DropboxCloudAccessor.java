package fm.doe.national.data.cloud.dropbox;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.dropbox.chooser.android.DbxChooser;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.android.Auth;
import com.dropbox.core.http.OkHttp3Requestor;
import com.dropbox.core.v2.DbxClientV2;

import java.io.ByteArrayOutputStream;
import java.util.List;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.cloud.CloudAccessor;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.SingleSubject;

public class DropboxCloudAccessor implements CloudAccessor {

    private final static String PREF_TOKEN = "dropbox-access-token";


    private Context context;
    private SingleSubject<Object> authSingle;
    private SingleSubject<String> importSingle;
    private DbxClientV2 dropboxClient;

    private final SharedPreferences sharedPreferences = MicronesiaApplication.getAppComponent().getSharedPreferences();

    public DropboxCloudAccessor(Context appContext) {
        context = appContext;
        if (hasAuthToken()) initDropbox();
    }

    @Override
    public Single<String> importContentFromCloud() {
        importSingle = SingleSubject.create();
        Single<String> launchActivityForContent =
                Completable.fromRunnable(() -> startActivityAction(DropboxActivity.ACTION_OPEN_FILE))
                        .andThen(importSingle);
        if (hasAuthToken()) {
            return launchActivityForContent;
        } else {
            return auth().andThen(launchActivityForContent);
        }
    }

    @Override
    public Completable exportContentToCloud(@NonNull String content) {
        return null;
    }

    @Override
    public Completable auth() {
        authSingle = SingleSubject.create();
        return Completable
                .fromRunnable(() -> startActivityAction(DropboxActivity.ACTION_AUTH))
                .andThen(Completable.fromSingle(authSingle));
    }

    protected void onAuthActionComplete() {
        initDropbox();
        authSingle.onSuccess(new Object());
    }

    protected boolean isSuccessfulAuth() {
        return Auth.getOAuth2Token() != null;
    }

    protected void onCloudFilePathObtained(@NonNull DbxChooser.Result result) {
        Single.fromCallable(() -> {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            dropboxClient.files()
                    .download(dropboxPath(result.getLink()))
                    .download(outputStream);
            return new String(outputStream.toByteArray());
        })
                .subscribeOn(Schedulers.io())
                .doOnSuccess(importSingle::onSuccess)
                .doOnError(importSingle::onError)
                .subscribe();
    }

    protected void onActionFailure(Throwable throwable) {
        if (authSingle != null) authSingle.onError(throwable);
        if (importSingle != null) importSingle.onError(throwable);
//        if (exportSingle != null) exportSingle.onError(throwable);
    }

    private void startActivityAction(int action) {
        Activity activity = ((MicronesiaApplication) context).getCurrentActivity();
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
    }

    private void initClient(@NonNull String accessToken) {
        DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder(context.getApplicationInfo().name)
                .withHttpRequestor(new OkHttp3Requestor(OkHttp3Requestor.defaultOkHttpClient()))
                .build();
        dropboxClient = new DbxClientV2(requestConfig, accessToken);
    }

    private String dropboxPath(Uri fromUri) {
        int segmentsToSkip = 3;
        StringBuilder builder = new StringBuilder();
        builder.append('/');
        List<String> pathSegments = fromUri.getPathSegments();
        for (int i = segmentsToSkip; i < pathSegments.size(); i++) {
            builder.append(pathSegments.get(i));
            if (i < pathSegments.size() - 1) builder.append('/');
        }
        return builder.toString();
    }
}
