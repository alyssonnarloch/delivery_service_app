package service;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class ServiceGenerator {

    private static final String baseUrl = "http://52.36.228.76:8080/delivery_service_api/api/";

    public static <S> S createService(Class<S> serviceClass, String path) {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(baseUrl + path)
                .setClient(new OkClient(new OkHttpClient()));

        RestAdapter adapter = builder.build();

        return adapter.create(serviceClass);
    }

}
