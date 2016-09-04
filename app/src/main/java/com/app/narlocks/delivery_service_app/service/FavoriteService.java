package com.app.narlocks.delivery_service_app.service;

import com.app.narlocks.delivery_service_app.model.ClientServiceProviderFavorite;
import com.app.narlocks.delivery_service_app.model.ServiceProvider;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FavoriteService {

    @GET("favorite/services_provider/{client_id}")
    Call<List<ServiceProvider>> getServicesProvider(@Path("client_id") int clientId);

    @GET("favorite/{client_id}/{service_provider_id}")
    Call<ClientServiceProviderFavorite> getFavorite(@Path("client_id") int clientId,
                                                    @Path("service_provider_id") int serviceProviderId);

    @FormUrlEncoded
    @POST("favorite/new")
    Call<ClientServiceProviderFavorite> save(@Field("client_id") int clientId,
                            @Field("service_provider_id") int serviceProviderId);

    @DELETE("favorite/delete/{id}")
    Call<ResponseBody> delete(@Path("id") int id);
}
