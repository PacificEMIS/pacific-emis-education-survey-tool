package org.pacific_emis.surveys.remote_storage.data.accessor;

import android.app.Activity;
import android.util.Pair;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import org.pacific_emis.surveys.accreditation_core.data.model.AccreditationSurvey;
import org.pacific_emis.surveys.core.data.exceptions.AuthenticationException;
import org.pacific_emis.surveys.core.data.exceptions.NotImplementedException;
import org.pacific_emis.surveys.core.data.exceptions.PickerDeclinedException;
import org.pacific_emis.surveys.core.utils.LifecycleListener;
import org.pacific_emis.surveys.fsm_report.domain.FsmReportInteractor;
import org.pacific_emis.surveys.remote_storage.data.model.ExportType;
import org.pacific_emis.surveys.remote_storage.data.model.ReportBundle;
import org.pacific_emis.surveys.remote_storage.data.storage.RemoteStorage;
import org.pacific_emis.surveys.remote_storage.data.uploader.RemoteUploader;
import org.pacific_emis.surveys.remote_storage.ui.auth.GoogleAuthActivity;
import org.pacific_emis.surveys.remote_storage.ui.remote_storage.DriveStorageActivity;
import org.pacific_emis.surveys.report.di.ReportComponentInjector;
import org.pacific_emis.surveys.report_core.domain.ReportInteractor;
import org.pacific_emis.surveys.rmi_report.domain.RmiReportInteractor;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.SingleSubject;

public final class RemoteStorageAccessorImpl implements RemoteStorageAccessor {

    private static final long EMPTY_EMIT_DELAY_MS = 500;

    private final LifecycleListener lifecycleListener;
    private final RemoteUploader uploader;
    private final RemoteStorage storage;

    private SingleSubject<String> contentSubject;
    private CompletableSubject authSubject;

    public RemoteStorageAccessorImpl(LifecycleListener lifecycleListener,
                                     RemoteUploader uploader,
                                     RemoteStorage storage) {
        this.lifecycleListener = lifecycleListener;
        this.uploader = uploader;
        this.storage = storage;
    }

    @Override
    public Completable signInAsUser() {
        if (storage.getUserAccount() != null) {
            return Completable.complete();
        }

        Activity currentActivity = lifecycleListener.getCurrentActivity();

        if (currentActivity == null) {
            return Completable.error(new AuthenticationException("No activity"));
        }

        authSubject = CompletableSubject.create();
        return Completable.fromAction(() -> currentActivity.startActivity(GoogleAuthActivity.createIntent(currentActivity)))
                .andThen(authSubject);
    }

    @Override
    public void signOutAsUser() {
        storage.setUserAccount(null);

        Activity currentActivity = lifecycleListener.getCurrentActivity();
        if (currentActivity != null) {
            GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            GoogleSignInClient client = GoogleSignIn.getClient(currentActivity, options);
            client.signOut();
        }
    }

    @Override
    public void scheduleUploading(long surveyId) {
        uploader.scheduleUploading(surveyId);
    }

    private void scheduleEmptyEmit() {
        Schedulers.io().scheduleDirect(() -> onContentReceived(EMPTY_CONTENT), EMPTY_EMIT_DELAY_MS, TimeUnit.MILLISECONDS);
    }

    @Override
    public Single<String> requestContentFromStorage() {
        contentSubject = SingleSubject.create();

        return Completable.fromAction(() -> {
            Activity currentActivity = lifecycleListener.getCurrentActivity();

            if (currentActivity == null) {
                scheduleEmptyEmit();
                return;
            }

            currentActivity.startActivity(DriveStorageActivity.createIntent(currentActivity, false));
        })
                .andThen(contentSubject);
    }

    @Override
    public void onContentReceived(String content) {
        if (contentSubject != null) {
            contentSubject.onSuccess(content);
            contentSubject = null;
        }
    }

    @Override
    public void onContentNotReceived() {
        if (contentSubject != null) {
            contentSubject.onError(new PickerDeclinedException());
            contentSubject = null;
        }
    }

    @Override
    public void showDebugStorage() {
        Activity currentActivity = lifecycleListener.getCurrentActivity();

        if (currentActivity == null) {
            scheduleEmptyEmit();
            return;
        }

        currentActivity.startActivity(DriveStorageActivity.createIntent(currentActivity, true));
    }

    @Override
    public void onGoogleSignInAccountReceived(GoogleSignInAccount account) {
        storage.setUserAccount(account);

        if (authSubject != null) {
            if (account == null) {
                authSubject.onError(new AuthenticationException("Auth failed"));
            } else {
                authSubject.onComplete();
            }
            authSubject = null;
        }
    }

    @Nullable
    @Override
    public String getUserEmail() {
        GoogleSignInAccount account = storage.getUserAccount();
        return account == null ? null : account.getEmail();
    }

    @Override
    public Single<String> exportToExcel(AccreditationSurvey survey, ExportType exportType) {
        return Single.fromCallable(() -> {
            ReportInteractor reportInteractor = ReportComponentInjector
                    .getComponent(lifecycleListener.getCurrentActivity().getApplication())
                    .getReportInteractor();
            reportInteractor.requestReports(survey);
            return reportInteractor;
        })
                .flatMap(reportInteractor -> {
                            Single<ReportBundle> single = reportInteractor.getHeaderItemObservable().firstOrError()
                                    .zipWith(
                                            reportInteractor.requestFlattenSummary(survey),
                                            Pair::create
                                    )
                                    .zipWith(
                                            reportInteractor.requestFlattenRecommendations(survey),
                                            (lv, rv) -> new ReportBundle(lv.first, lv.second, rv)
                                    );

                            if (reportInteractor instanceof FsmReportInteractor) {
                                return single.zipWith(
                                        ((FsmReportInteractor) reportInteractor).getLevelObservable().firstOrError(),
                                        ReportBundle::setSchoolAccreditationLevel
                                );
                            } else if (reportInteractor instanceof RmiReportInteractor) {
                                return single.zipWith(
                                        ((RmiReportInteractor) reportInteractor).getLevelObservable().firstOrError(),
                                        ReportBundle::setSchoolAccreditationTallyLevel
                                );
                            } else {
                                throw new NotImplementedException();
                            }
                        }
                )
                .flatMap(it -> storage.exportToExcel(survey, it, exportType));
    }
}
