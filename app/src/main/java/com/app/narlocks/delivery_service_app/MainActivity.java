package com.app.narlocks.delivery_service_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import model.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.ServiceGenerator;
import service.TaskService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TaskService client = ServiceGenerator.createService(TaskService.class, "TOKEN_LOCO_123");
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
