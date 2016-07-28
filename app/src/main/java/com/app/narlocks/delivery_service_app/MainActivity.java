package com.app.narlocks.delivery_service_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import model.Task;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import service.ServiceGenerator;
import service.TaskService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String URL_PATH = "task/all";

        TaskService client = ServiceGenerator.createService(TaskService.class, URL_PATH);
        client.getAll(new Callback<List<Task>>() {
            @Override
            public void success(List<Task> tasks, Response response) {
                for(Task t : tasks) {
                   Log.v("TASK", t.getId() + " - " + t.getName());
                }
                Log.v("IRRRAAAAAAAAAAAA", "FOOOOOOOOOOOOOOOOI");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.v("IRRRAAAAAAAAAAAA", "NÃO FOI =(");
            }
        });

    }
}
