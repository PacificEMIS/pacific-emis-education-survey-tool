package org.pacific_emis.surveys.remote_data;

import org.pacific_emis.surveys.remote_data.models.Core;
import org.pacific_emis.surveys.remote_data.models.School;
import org.pacific_emis.surveys.remote_data.models.Teachers;
import org.pacific_emis.surveys.remote_data.models.Token;

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
    Call<Teachers> getTeachers(@Query("PageNo") int page, @Query("PageSize") int pageSize, @Query("Authorization") String token);

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

