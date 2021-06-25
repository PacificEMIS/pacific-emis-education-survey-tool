package org.pacific_emis.surveys.core.data.remote_data_source;

import org.pacific_emis.surveys.core.data.remote_data_source.models.Core;
import org.pacific_emis.surveys.core.data.remote_data_source.models.School;
import org.pacific_emis.surveys.core.data.remote_data_source.models.Subject;
import org.pacific_emis.surveys.core.data.remote_data_source.models.Teachers;
import org.pacific_emis.surveys.core.data.remote_data_source.models.Token;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface EmisApi {
    @GET("schools")
    Single<List<School>> getSchools(
            @Query("PageNo") int page,
            @Query("PageSize") int pageSize,
            @Header("Authorization") String token
    );

    @GET("teachers")
    Single<Teachers> getTeachers(
            @Query("PageNo") int page,
            @Query("PageSize") int pageSize,
            @Header("Authorization") String token
    );

    @GET("subjects")
    Single<List<Subject>> getSubjects(
            @Header("Authorization") String token
    );

    @GET("lookups/collection/core")
    Single<Core> getCore();

    @FormUrlEncoded
    @POST("token")
    Single<Token> getToken(
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grantType
    );
}

