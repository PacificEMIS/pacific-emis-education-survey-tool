package org.pacific_emis.surveys.core.data.remote_data_source;

import org.pacific_emis.surveys.core.BuildConfig;
import org.pacific_emis.surveys.core.data.data_repository.Result;
import org.pacific_emis.surveys.core.data.local_data_source.DataSource;
import org.pacific_emis.surveys.core.data.model.Photo;
import org.pacific_emis.surveys.core.data.model.School;
import org.pacific_emis.surveys.core.data.model.Subject;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.data.model.Teacher;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.preferences.entities.UploadState;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoreRemoteDataSource implements DataSource {
    private final static String UNSUPPORTED_FOR_THIS_DATASOURCE = "This operation is unsupported for remote data source.";
    private final static String TOKEN_GRANT_TYPE = "password";

    private final Map<AppRegion, String> mTokenMap = new HashMap<>();
    private final Map<AppRegion, EmisApi> emisApiMap = new ConcurrentHashMap<>();
    private final LocalSettings localSettings;

    public CoreRemoteDataSource(LocalSettings localSettings) {
        emisApiMap.put(localSettings.getCurrentAppRegion(), initEmisApi(localSettings.getEmisApiUrl()));
        this.localSettings = localSettings;
    }

    private EmisApi initEmisApi(String baseUrl) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        return new Retrofit.Builder()
                .client(getUnsafeOkHttpClient())
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(EmisApi.class);
    }

    private OkHttpClient getUnsafeOkHttpClient() {
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        // nothing
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        // nothing
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };

        SSLSocketFactory sslSocketFactory = null;
        try {
            sslSocketFactory = getSocketFactory(trustAllCerts);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (sslSocketFactory != null) {
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);

        }
        return builder.build();
    }

    private SSLSocketFactory getSocketFactory(TrustManager[] trustAllCerts) throws KeyManagementException,
            NoSuchAlgorithmException {
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new SecureRandom());
        return sslContext.getSocketFactory();
    }

    private Single<String> getToken(AppRegion appRegion) {
        String emisUser = localSettings.getEmisUser();
        String emisPassword = localSettings.getEmisPassword();
        EmisApi emisApi = getOrCreateEmisApi(appRegion);
        return Single.fromCallable(() -> Optional.ofNullable(mTokenMap.get(appRegion)))
                .flatMap(optional ->
                        optional.map(Single::just).orElseGet(()
                                -> emisApi.getToken(emisUser, emisPassword, TOKEN_GRANT_TYPE)
                                .map(token -> {
                                    mTokenMap.put(appRegion, token.toString());
                                    return token.toString();
                                })));
    }

    private synchronized EmisApi getOrCreateEmisApi(AppRegion appRegion) {
        EmisApi emisApi = emisApiMap.get(appRegion);
        if (emisApi == null) {
            if (localSettings.getCurrentAppRegion() != appRegion) {
                throw new IllegalArgumentException("Current appRegion != " + appRegion + " (current = " + localSettings.getCurrentAppRegion() + ")");
            }
            emisApi = initEmisApi(localSettings.getEmisApiUrl());
            emisApiMap.put(appRegion, emisApi);
        }
        return emisApi;
    }

    @Override
    public Single<Result<List<School>>> loadSchools(AppRegion appRegion) {
        EmisApi emisApi = getOrCreateEmisApi(appRegion);
        return emisApi.getCore().map(core -> core.schoolCodes.stream().map(school -> {
            school.appRegion = appRegion;
            return (School) school;
        })
                .collect(Collectors.toList()))
                .map(item -> new Result<>(item, null));
    }

    @Override
    public Single<List<Teacher>> loadTeachers(AppRegion appRegion) {
        EmisApi emisApi = getOrCreateEmisApi(appRegion);

        return getToken(appRegion).map(
                token -> emisApi
                        .getTeachers(
                                1,
                                Integer.MAX_VALUE,
                                token
                        )
                        .blockingGet()
                        .teachers
                        .stream()
                        .map(teacher -> {
                            teacher.appRegion = appRegion;
                            return (Teacher) teacher;
                        })
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Single<List<Subject>> loadSubjects(AppRegion appRegion) {
        EmisApi emisApi = getOrCreateEmisApi(appRegion);

        return getToken(appRegion).map(
                token -> emisApi
                        .getSubjects(token)
                        .blockingGet()
                        .stream()
                        .map(subject -> {
                            subject.appRegion = appRegion;
                            return (Subject) subject;
                        })
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Completable rewriteAllSchools(List<org.pacific_emis.surveys.core.data.model.School> schools) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }

    @Override
    public Completable rewriteAllTeachers(List<Teacher> teachers) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }

    @Override
    public Completable rewriteAllSubjects(List<Subject> subjects) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }

    @Override
    public Completable rewriteTemplateSurvey(Survey survey) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }

    @Override
    public Single<Survey> getTemplateSurvey(AppRegion appRegion) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }

    @Override
    public Single<Survey> loadSurvey(AppRegion appRegion, long surveyId) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }

    @Override
    public Single<List<Survey>> loadAllSurveys(AppRegion appRegion) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }

    @Override
    public Single<List<Survey>> loadSurveys(String schoolId, AppRegion appRegion, String surveyTag) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }

    @Override
    public Single<Survey> createSurvey(
            String schoolId, String schoolName, Date createDate, String surveyTag, String userEmail, AppRegion appRegion, String tabletId
    ) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }

    @Override
    public Completable deleteSurvey(long surveyId) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }

    @Override
    public Single<Photo> createPhoto(Photo photo, long answerId) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }

    @Override
    public Completable deletePhoto(long photoId) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }

    @Override
    public Completable deleteCreatedSurveys() {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }

    @Override
    public Completable createPartiallySavedSurvey(AppRegion appRegion, Survey survey) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }

    @Override
    public void updateSurvey(Survey survey) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }

    @Override
    public List<Photo> getPhotos(Survey survey) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }

    @Override
    public Completable updatePhotoWithRemote(Photo photo, String remoteFileId) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }

    @Override
    public void setSurveyUploadState(Survey survey, UploadState uploadState) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }
}
