package com.app.narlocks.delivery_service_app.service;

import com.app.narlocks.delivery_service_app.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthService {
    @FormUrlEncoded
    @POST("auth/login")
    String authenticate(@Field("token") String token);

    @FormUrlEncoded
    @POST("auth/login_aux")
    Call<User> authenticateAux(@Field("email") String email, @Field("password") String password);
}
