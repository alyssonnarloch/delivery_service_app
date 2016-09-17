package com.app.narlocks.delivery_service_app.activity_task;

import android.os.AsyncTask;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity.ClientProjectRefusedFragment;
import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.service.ProjectService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ClientProjectRefusedTask  extends AsyncTask<Integer, Void, Boolean> {

    private Project project;
    private List<Project> projects;
    private ClientProjectRefusedFragment fragment;
    private String errorMessage;

    public ClientProjectRefusedTask(ClientProjectRefusedFragment fragment) {
        this.fragment = fragment;
        this.errorMessage = "";
        this.projects = new ArrayList();
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        boolean resultOk = true;

        try {
            ProjectService projectService = ServiceGenerator.createService(ProjectService.class);
            Call<Project> projectCall = projectService.getById(params[0]);
            Response projectResponse = projectCall.execute();

            if (projectResponse.code() == 200) {
                project = (Project) projectResponse.body();
            } else {
                resultOk = false;
                errorMessage += ErrorConversor.getErrorMessage(projectResponse.errorBody());
            }

            if (project != null) {
                Call<List<Project>> projectEvaluationCall = projectService.serviceProviderEvaluations(project.getServiceProvider().getId());
                Response projectEvaluation = projectEvaluationCall.execute();

                if (projectEvaluation.code() == 200) {
                    projects = (List<Project>) projectEvaluation.body();
                } else {
                    resultOk = false;
                    errorMessage += ErrorConversor.getErrorMessage(projectResponse.errorBody());
                }

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
            this.fragment.loadContentViewComponents(project, projects);
        } else {
            Toast.makeText(fragment.getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
