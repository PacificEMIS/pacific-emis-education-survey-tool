package org.pacific_emis.surveys.remote_data;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private final static String ERROR_API_INTERACT = "Error while interacting with API.";

    private EmisApi initEmisApi(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(EmisApi.class);
    }

    public List<School> getListOfSchoolNamesAndCodesFrom(String apiURL) throws IOException {
        EmisApi api = initEmisApi(apiURL);
        Core preResult = api.getCore().execute().body();
        if (preResult != null) return preResult.schoolCodes;
        else throw new IOException(ERROR_API_INTERACT);
    }
}
