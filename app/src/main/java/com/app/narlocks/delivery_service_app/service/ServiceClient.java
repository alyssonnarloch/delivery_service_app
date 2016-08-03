package com.app.narlocks.delivery_service_app.service;

import com.app.narlocks.delivery_service_app.model.Client;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceClient {

    @GET("client/{id}")
    Call<Client> getById(@Path("id") int id);

    @FormUrlEncoded
    @POST("client/new")
    Call<ResponseBody> save(@Field("name") String name,
                            @Field("email") String email,
                            @Field("phone") String phone,
                            @Field("zipcode") String zipCode,
                            @Field("city_id") int cityId,
                            @Field("address") String address,
                            @Field("number") int number,
                            @Field("password") String password,
                            @Field("profile_image") String profileImage);
    @FormUrlEncoded
    @PUT("client/edit")
    Call<ResponseBody> update(@Field("name") String name,
                              @Field("email") String email,
                              @Field("phone") String phone,
                              @Field("zipcode") String zipCode,
                              @Field("city_id") int cityId,
                              @Field("address") String address,
                              @Field("number") int number,
                              @Field("password") String password,
                              @Field("profile_image") String profileImage);


}
