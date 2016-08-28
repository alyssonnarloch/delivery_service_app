package com.app.narlocks.delivery_service_app.service;

import com.app.narlocks.delivery_service_app.model.Project;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ProjectService {

    @FormUrlEncoded
    @POST("project/new")
    Call<ResponseBody> save(@Field("title") String title,
                            @Field("description") String description,
                            @Field("client_id") int clientId,
                            @Field("service_provider_id") int serviceProviderId,
                            @Field("address") String address,
                            @Field("number") int number,
                            @Field("zip_code") String zipcode,
                            @Field("city_id") int cityId,
                            @Field("start_at") String startAt,
                            @Field("end_at") String endAt);

    @GET("project/client")
    Call<List<Project>> clientProjects(@Query("client_id") int clientId,
                                       @Query("status") int status);

    @GET("project/client/evaluations")
    Call<List<Project>> clientEvaluations(@Query("client_id") int clientId);

    @GET("project/service_provider")
    Call<List<Project>> serviceProviderProjects(@Query("service_provider_id") int serviceProviderId,
                                       @Query("status") int status);

    @GET("project/service_provider/evaluations")
    Call<List<Project>> serviceProviderEvaluations(@Query("service_provider_id") int serviceProviderId);

    @FormUrlEncoded
    @PUT("project/close")
    Call<ResponseBody> update(@Field("project_id") int projectId,
                              @Field("qualification") int qualification,
                              @Field("description") String description,
                              @Field("profile_id") int profileId);
}
