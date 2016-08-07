package com.app.narlocks.delivery_service_app.service;

import com.app.narlocks.delivery_service_app.model.City;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CityService {

    @GET("city/{name}")
    Call<List<City>> getByName(@Path("name") String name);

}
