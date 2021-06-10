package org.pacific_emis.surveys.remote_data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    public static FedEmisApi initFedEmis() {
        return new Retrofit.Builder()
                .baseUrl("https://fedemis.doe.fm/api")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FedEmisApi.class);
    }
}
