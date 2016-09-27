package com.app.narlocks.delivery_service_app.service;


import com.app.narlocks.delivery_service_app.model.ProjectPortfolio;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProjectPortfolioService {

    @GET("project_portfolio/")
    Call<ProjectPortfolio> getById(@Query("project_portfolio_id") int id);

    @GET("project_portfolio/project")
    Call<List<ProjectPortfolio>> getProjectPortfolio(@Query("project_id") int id);

    @PUT("project_portfolio/")
    Call<ResponseBody> approveRejectImage(@Query("project_portfolio_id") int projectPortfolioId,
                              @Query("approve") boolean newStatus);

    @FormUrlEncoded
    @POST("project_portfolio/save")
    Call<ProjectPortfolio> save(@Field("project_id") int projectId,
                            @Field("image") String image);

    @DELETE("project_portfolio/delete/{id}")
    Call<ResponseBody> delete(@Path("id") int id);
}
