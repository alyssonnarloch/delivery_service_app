package com.app.narlocks.delivery_service_app.activity_task;

import android.os.AsyncTask;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity.SPEvaluationsFragment;
import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.service.ProjectService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SPEvaluationsTask extends AsyncTask<Integer, Void, Boolean> {

    private List<Project> projects;
    private SPEvaluationsFragment fragment;
    private String errorMessage;

    public SPEvaluationsTask(SPEvaluationsFragment fragment) {
        this.fragment = fragment;
        this.errorMessage = "";
        this.projects = new ArrayList();
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        boolean resultOk = true;

        try {
            ProjectService projectService = ServiceGenerator.createService(ProjectService.class);
            Call<List<Project>> projectEvaluationCall = projectService.serviceProviderEvaluations(params[0]);
            Response response = projectEvaluationCall.execute();

            if (response.code() == 200) {
                projects = (List<Project>) response.body();
            } else {
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
            this.fragment.loadContentViewComponents(projects);
        } else {
            Toast.makeText(fragment.getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
