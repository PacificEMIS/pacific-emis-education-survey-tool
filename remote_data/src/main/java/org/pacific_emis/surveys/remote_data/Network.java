package org.pacific_emis.surveys.remote_data;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private final static String ERROR_INCORRECT_API_CONTEXT = "Cannot get list of schools from API with such context.";
    private final static String ERROR_API_INTERACT = "Error while interacting with API.";

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
}
