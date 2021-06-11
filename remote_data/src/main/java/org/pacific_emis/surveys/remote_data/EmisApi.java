package org.pacific_emis.surveys.remote_data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface EmisApi {
    @GET("schools")
    public Call<List<School>> getSchools(@Query("Authorization") String token);

    @GET("teachers")
    public Call<List<School>> getTeachers(@Query("Authorization") String token);

    @GET("lookups/collection/core")
    public Call<Core> getCore();
}

