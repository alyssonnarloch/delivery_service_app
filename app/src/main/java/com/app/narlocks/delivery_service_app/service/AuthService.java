package com.app.narlocks.delivery_service_app.service;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthService {
    @FormUrlEncoded
    @POST("login")
    String authenticate(@Field("token") String token);
}
