package service;

import java.util.List;

import model.Task;
import retrofit2.Call;
import retrofit2.http.GET;

public interface TaskService {
    @GET("test/task/all")
    Call<List<Task>> getAll();
}
