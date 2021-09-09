package org.pacific_emis.surveys.core.data.remote_data_source;

import org.pacific_emis.surveys.core.data.local_data_source.DataSource;
import org.pacific_emis.surveys.core.data.model.Photo;
import org.pacific_emis.surveys.core.data.model.School;
import org.pacific_emis.surveys.core.data.model.Subject;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.data.model.Teacher;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoreRemoteDataSource implements DataSource {
    private final static String UNSUPPORTED_FOR_THIS_DATASOURCE = "This operation is unsupported for remote data source.";
    private final static String API_URL_FEDEMIS = "https://fedemis.doe.fm/api/";
    private final static String API_URL_MIEMIS = "http://data.pss.edu.mh/miemis/api/";

    private final static String TOKEN_GRANT_TYPE = "password";

    private EmisApi emisApi;
    private String username;
    private String password;
    private final AppRegion appRegion;

    public static CoreRemoteDataSource createByAppRegion(AppRegion appRegion, String apiUrl, String emisUser, String emisPassword) {
        if (apiUrl != null) {
            return new CoreRemoteDataSource(apiUrl, emisUser, emisPassword, appRegion);
        } else switch (appRegion) {
            case RMI:
                return new CoreRemoteDataSource(API_URL_MIEMIS, emisUser, emisPassword, appRegion);
            case FSM:
                return new CoreRemoteDataSource(API_URL_FEDEMIS, emisUser, emisPassword, appRegion);
            default:
                return null;
        }
    }

    public CoreRemoteDataSource(String emisApiUrl, String username, String password, AppRegion appRegion) {
        this.appRegion = appRegion;
        emisApi = initEmisApi(emisApiUrl);
        this.username = username;
        this.password = password;
    }

    private EmisApi initEmisApi(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(EmisApi.class);
    }

    @Override
    public Single<List<School>> loadSchools() {
        return emisApi.getCore().map(core -> core.schoolCodes.stream().map(school -> {
            school.appRegion = appRegion;
            return (School) school;
        }).collect(Collectors.toList()));
    }

    @Override
    public Single<List<Teacher>> loadTeachers() {
        return emisApi.getToken(username, this.password, TOKEN_GRANT_TYPE).map(
                token -> emisApi
                        .getTeachers(
                                1,
                                emisApi.getTeachers(1, 1, token.toString()).blockingGet().fullAmountOfTeachers,
                                token.toString()
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
    public Single<List<Subject>> loadSubjects() {
        return emisApi.getToken(username, password, TOKEN_GRANT_TYPE).map(
                token -> emisApi
                        .getSubjects(token.toString())
                        .blockingGet()
                        .stream()
                        .map(it -> (Subject) it)
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
    public Single<Survey> getTemplateSurvey() {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }

    @Override
    public Single<Survey> loadSurvey(long surveyId) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }

    @Override
    public Single<List<Survey>> loadAllSurveys() {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }

    @Override
    public Single<List<Survey>> loadSurveys(String schoolId, AppRegion appRegion, String surveyTag) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_THIS_DATASOURCE);
    }

    @Override
    public Single<Survey> createSurvey(String schoolId, String schoolName, Date createDate, String surveyTag, String userEmail) {
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
    public Completable createPartiallySavedSurvey(Survey survey) {
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
}
