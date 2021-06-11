package org.pacific_emis.surveys.remote_data;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
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
}
