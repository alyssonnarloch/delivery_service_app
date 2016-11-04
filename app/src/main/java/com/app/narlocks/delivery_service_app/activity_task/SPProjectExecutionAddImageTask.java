package com.app.narlocks.delivery_service_app.activity_task;

import android.os.AsyncTask;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity.R;
import com.app.narlocks.delivery_service_app.activity.SPProjectExecutionFragment;
import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.model.ProjectPortfolio;
import com.app.narlocks.delivery_service_app.service.ProjectPortfolioService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SPProjectExecutionAddImageTask extends AsyncTask<Integer, Void, Boolean> {

    private List<ProjectPortfolio> projectPortfolio;
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
                Call<List<ProjectPortfolio>> projectPortfolioCall = service.getProjectPortfolio(params[0]);
                Response projectPortfolioResponse = projectPortfolioCall.execute();

                if (projectPortfolioResponse.code() == 200) {
                    projectPortfolio = (List<ProjectPortfolio>) projectPortfolioResponse.body();
                } else {
                    resultOk = false;
                    errorMessage += ErrorConversor.getErrorMessage(projectPortfolioResponse.errorBody());
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
            fragment.realoadGridImages(projectPortfolio);
        } else {
            Toast.makeText(fragment.getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
