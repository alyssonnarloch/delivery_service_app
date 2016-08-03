package com.app.narlocks.delivery_service_app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.app.narlocks.delivery_service_app.R;
import com.app.narlocks.delivery_service_app.model.Task;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;
import com.app.narlocks.delivery_service_app.service.TaskService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String token = "eyJhbGciOiJIUzUxMiJ9.eyJlbWFpbCI6ImFseXNzb24ubmFybG9jaEBnbWFpbC5jb20iLCJwYXNzd29yZCI6IjE1MjAzNiIsImV4cGlyZVRpbWUiOjE0Njk5MDQzOTM1NjF9.Hh1BoJHxrV2DZ7GxqxFjOZKwRnngEcGfluRaDsx_efeta7j7uy7wUnoXuMTo9GNrDRmhgzsvWlRHeINMn2qHWw";

        TaskService client = ServiceGenerator.createService(TaskService.class, token);
        Call<List<Task>> call = client.getAll();
        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful()) {
                    for(Task task : response.body()) {
                        Log.i("TASK", task.getName());
                    }
                } else {
                    Log.i("TASK ERROR", "Não deuuuuuuuuuuuuuuuuuuu");
                    Log.i("TASK ERROR", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
            }
        });

    }
}
