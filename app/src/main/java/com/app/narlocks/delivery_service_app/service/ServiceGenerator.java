package com.app.narlocks.delivery_service_app.service;


import android.util.Log;

import com.app.narlocks.delivery_service_app.model.Task;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final String API_BASE_URL = "http://52.36.228.76:8080/delivery_service_api/api/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        if (authToken != null) {
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();
                    Request newRequest = original;

                    Log.i("TOKEN MAROTO", authToken);

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", "bearer " + authToken)
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    Response response = chain.proceed(request);

                    Log.i("URL******", request.url().toString());

                    if (response.code() == 200 && !request.url().toString().contains("save")) {
                        TaskService client = ServiceGenerator.createService(TaskService.class, "");
                        Call<Task> callTask = client.save("ALYSSON MUITO LOUCO");
                        Task task = callTask.execute().body();

                        Log.i("NEW TASK", task.getName() + " foi salvo marotamente!");

                        Request.Builder newRequestBuilder = newRequest.newBuilder()
                                .header("Authorization", "bearer " + task.getName())
                                .method(newRequest.method(), newRequest.body());

                        return chain.proceed(newRequestBuilder.build());
                    }

                    return response;
                }
            });
        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }

}
