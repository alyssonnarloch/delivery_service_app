package com.app.narlocks.delivery_service_app.service;

import com.app.narlocks.delivery_service_app.model.ServiceType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceTypeService {

    @GET("service_type/all")
    Call<List<ServiceType>> getAll();
}
