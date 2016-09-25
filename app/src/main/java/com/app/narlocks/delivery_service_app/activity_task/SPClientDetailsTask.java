package com.app.narlocks.delivery_service_app.activity_task;


import android.os.AsyncTask;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity.SPClientDetailsFragment;
import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.model.Client;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.service.ClientService;
import com.app.narlocks.delivery_service_app.service.ProjectService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SPClientDetailsTask extends AsyncTask<Integer, Void, Boolean> {

    private Client client;
    private List<Project> projects;
    private SPClientDetailsFragment fragment;
    private String errorMessage;

    public SPClientDetailsTask(SPClientDetailsFragment fragment) {
        this.fragment = fragment;
        this.errorMessage = "";
        this.projects = new ArrayList();
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        boolean resultOk = true;

        try {
            ClientService clientService = ServiceGenerator.createService(ClientService.class);
            ProjectService projectService = ServiceGenerator.createService(ProjectService.class);
            Call<Client> clientCall = clientService.getById(params[0]);
            Response clientResponse = clientCall.execute();

            if (clientResponse.code() == 200) {
                client = (Client) clientResponse.body();
            } else {
                resultOk = false;
                errorMessage += ErrorConversor.getErrorMessage(clientResponse.errorBody());
            }

            if (client != null) {
                Call<List<Project>> projectEvaluationCall = projectService.clientEvaluations(client.getId());
                Response projectResponse = projectEvaluationCall.execute();

                if (projectResponse.code() == 200) {
                    projects = (List<Project>) projectResponse.body();
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
            this.fragment.loadContentViewComponents(client, projects);
        } else {
            Toast.makeText(fragment.getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
