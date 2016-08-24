package com.app.narlocks.delivery_service_app.service;

import com.app.narlocks.delivery_service_app.model.ServiceProvider;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceProviderService {

    @GET("service_provider/{id}")
    Call<List<ServiceProvider>> getById(@Path("id") int id);

    @FormUrlEncoded
    @POST("service_provider/new")
    Call<ResponseBody> save(@Field("name") String name,
                            @Field("email") String email,
                            @Field("phone") String phone,
                            @Field("zipcode") String zipCode,
                            @Field("city_id") int cityId,
                            @Field("address") String address,
                            @Field("number") int number,
                            @Field("password") String password,
                            @Field("profile_image") String profileImage,
                            @Field("service_type[]") List<Integer> serviceTypesId,
                            //@FieldMap Map<String, String> serviceTypesId,
                            @Field("experience_description") String experienceDescription,
                            @Field("available") boolean available,
                            @Field("occupation_area[]") List<Integer> occupationAreas,
                            //@FieldMap Map<String, String> occupationAreas,
                            @Field("profile_portfolio[]") List<String> profilePortfolio);
                            //@FieldMap Map<String, String> profilePortfolio);

    @FormUrlEncoded
    @PUT("service_provider/edit/main")
    Call<ResponseBody> editMain(@Field("id") int id,
                                @Field("name") String name,
                                @Field("email") String email,
                                @Field("phone") String phone,
                                @Field("zipcode") String zipCode,
                                @Field("city_id") int cityId,
                                @Field("address") String address,
                                @Field("number") int number,
                                @Field("profile_image") String profileImage);

    @FormUrlEncoded
    @PUT("service_provider/edit/services")
    Call<ResponseBody> editServices(@Field("id") int id,
                                    @Field("service_type") List<Integer> serviceTypesId,
                                    @Field("experience_description") String experienceDescription,
                                    @Field("available") boolean available);

    @FormUrlEncoded
    @PUT("service_provider/edit/areas")
    Call<ResponseBody> editAreas(@Field("id") int id,
                                 @Field("occupation_area") List<Integer> occupationAreas);

    @FormUrlEncoded
    @PUT("service_provider/edit/portfolio")
    Call<ResponseBody> editPortfolio(@Field("id") int id,
                                     @Field("profile_portfolio") List<String> profilePortfolio);

    @FormUrlEncoded
    @POST("service_provider/search")
    Call<ResponseBody> search(@Field("name") String name,
                              @Field("service_type") List<Integer> serviceTypeIds,
                              @Field("city_id") int cityId,
                              @Field("available") boolean available);
}
