package com.app.narlocks.delivery_service_app.activity_task;

import android.os.AsyncTask;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity.SPProjectEvaluationAwaitingFragment;
import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.service.ProjectService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class SPProjectEvaluationAwaitingTask extends AsyncTask<Void, Void, Boolean> {

    private int projectId;
    private int qualification;
    private int profileId;
    private int status;
    private String description;

    private SPProjectEvaluationAwaitingFragment fragment;
    private String errorMessage;

    public SPProjectEvaluationAwaitingTask(SPProjectEvaluationAwaitingFragment fragment, int projectId, int qualification, String description, int profileId, int status) {
        this.fragment = fragment;
        this.errorMessage = "";

        this.projectId = projectId;
        this.qualification = qualification;
        this.profileId = profileId;
        this.status = status;
        this.description = description;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean resultOk = true;

        try {
            ProjectService projectService = ServiceGenerator.createService(ProjectService.class);
            Call<ResponseBody> projectCall = projectService.update(projectId, qualification, description, profileId, status);
            Response response = projectCall.execute();

            if (response.code() != 200) {
                resultOk = false;
                errorMessage += ErrorConversor.getErrorMessage(response.errorBody());
            }

        } catch (Exception ex) {
            resultOk = false;
            ex.getStackTrace();
            errorMessage += ex.getMessage();
        }

        return resultOk;
    }

    @Override
    protected void onPostExecute(Boolean ok) {
        if (ok) {
            this.fragment.loadProjects();
        } else {
            Toast.makeText(fragment.getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
