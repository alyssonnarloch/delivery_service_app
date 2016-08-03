package com.app.narlocks.delivery_service_app.service;

import com.app.narlocks.delivery_service_app.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TaskService {
    @GET("test/task/all")
    Call<List<Task>> getAll();

    @FormUrlEncoded
    @POST("test/task/save")
    Call<Task> save(@Field("name") String name);
}
