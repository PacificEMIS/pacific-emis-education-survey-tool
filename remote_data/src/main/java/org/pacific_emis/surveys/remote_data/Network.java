package org.pacific_emis.surveys.remote_data;

import org.pacific_emis.surveys.remote_data.models.Core;
import org.pacific_emis.surveys.remote_data.models.School;
import org.pacific_emis.surveys.remote_data.models.Teacher;
import org.pacific_emis.surveys.remote_data.models.Teachers;
import org.pacific_emis.surveys.remote_data.models.Token;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private final static String ERROR_INCORRECT_API_CONTEXT = "Cannot get list of schools from API with such context.";
    private final static String ERROR_API_INTERACT = "Error while interacting with API.";
    private final static String USERNAME_FEDEMIS = "elena.zagainova@omega-r.com";
    private final static String USERNAME_MIEMIS = "elena.zagainova@omega-r.com";
    private final static String PASSWORD_FEDEMIS = "Omega!2019";
    private final static String PASSWORD_MIEMIS = "Omega!2019";

    EmisApi fedEmisApi;
    EmisApi miEmisApi;

    public Network() {
        fedEmisApi = initEmisApi(ApiContext.FEDEMIS);
        miEmisApi = initEmisApi(ApiContext.MIEMIS);
    }

    private EmisApi initEmisApi(ApiContext context) {
        return new Retrofit.Builder()
                .baseUrl(context.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(EmisApi.class);
    }

    public List<School> getListOfSchoolNamesAndCodesFrom(ApiContext context) throws IOException {
        EmisApi api;
        switch (context) {
            case MIEMIS:
                api = miEmisApi;
                break;
            case FEDEMIS:
                api = fedEmisApi;
                break;
            default:
                throw new IOException(ERROR_INCORRECT_API_CONTEXT);
        }
        Core preResult = api.getCore().execute().body();
        if (preResult != null) return preResult.schoolCodes;
        else throw new IOException(ERROR_API_INTERACT);
    }

    public List<Teacher> getListOfTeacherNamesAndIdsFrom(ApiContext context) throws IOException {
        EmisApi api;
        String username = "";
        String password = "";
        switch (context) {
            case MIEMIS:
                api = miEmisApi;
                username = USERNAME_MIEMIS;
                password = PASSWORD_MIEMIS;
                break;
            case FEDEMIS:
                username = USERNAME_FEDEMIS;
                password = PASSWORD_FEDEMIS;
                api = fedEmisApi;
                break;
            default:
                throw new IOException(ERROR_INCORRECT_API_CONTEXT);
        }
        Token token = api.getToken(username, password, "password").execute().body();
        if (token == null) throw new IOException(ERROR_API_INTERACT);
        Teachers preResult = api.getTeachers(1, 5000, token.toString()).execute().body();
        if (preResult == null) throw new IOException(ERROR_API_INTERACT);
        return preResult.teachers;
    }
}
