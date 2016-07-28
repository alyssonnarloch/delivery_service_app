package service;

import java.util.List;

import model.Task;
import retrofit.Callback;
import retrofit.http.GET;

public interface TaskService {
    @GET("/task/all")
    void getAll(Callback<List<Task>> cb);
}
