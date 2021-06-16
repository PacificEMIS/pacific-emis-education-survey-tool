package org.pacific_emis.surveys.remote_data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface EmisApi {
    @GET("schools")
    Call<List<School>> getSchools(@Query("Authorization") String token);

    @GET("teachers")
    Call<List<School>> getTeachers(@Query("Authorization") String token);

    @GET("lookups/collection/core")
    Call<Core> getCore();

    @FormUrlEncoded
    @POST("token")
    Call<Token> getToken(
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grantType
    );
}

