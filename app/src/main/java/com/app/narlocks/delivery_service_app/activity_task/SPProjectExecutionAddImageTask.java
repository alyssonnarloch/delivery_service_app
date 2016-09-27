package com.app.narlocks.delivery_service_app.activity_task;

import android.os.AsyncTask;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity.R;
import com.app.narlocks.delivery_service_app.activity.SPProjectExecutionFragment;
import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.model.ProjectPortfolio;
import com.app.narlocks.delivery_service_app.service.ProjectPortfolioService;
import com.app.narlocks.delivery_service_app.service.ProjectService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Response;

public class SPProjectExecutionAddImageTask extends AsyncTask<Integer, Void, Boolean> {

    private Project project;
    private String imageSrc;
    private SPProjectExecutionFragment fragment;
    private String errorMessage;

    public SPProjectExecutionAddImageTask(SPProjectExecutionFragment fragment, String imageSrc) {
        this.imageSrc = imageSrc;
        this.fragment = fragment;
        this.errorMessage = "";
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        boolean resultOk = true;

        try {
            ProjectPortfolioService service = ServiceGenerator.createService(ProjectPortfolioService.class);
            Call<ProjectPortfolio> call = service.save(params[0], imageSrc);
            Response response = call.execute();

            if (response.code() == 200) {
                ProjectService projectService = ServiceGenerator.createService(ProjectService.class);
                Call<Project> projectCall = projectService.getById(params[0]);
                Response projectResponse = projectCall.execute();

                if (projectResponse.code() == 200) {
                    project = (Project) projectResponse.body();
                } else {
                    resultOk = false;
                    errorMessage += ErrorConversor.getErrorMessage(projectResponse.errorBody());
                }
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
            Toast.makeText(fragment.getActivity(), R.string.project_portfolio_image_add_ok, Toast.LENGTH_LONG).show();
            fragment.realoadGridImages(project);
        } else {
            Toast.makeText(fragment.getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
