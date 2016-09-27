package com.app.narlocks.delivery_service_app.activity_task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity.R;
import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.service.ProjectPortfolioService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class SPProjectExecutionDeleteImageTask extends AsyncTask<Integer, Void, Boolean> {

    private Project project;
    private Context fragment;
    private String errorMessage;

    public SPProjectExecutionDeleteImageTask(Context fragment) {
        this.fragment = fragment;
        this.errorMessage = "";
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        boolean resultOk = true;

        try {
            ProjectPortfolioService service = ServiceGenerator.createService(ProjectPortfolioService.class);
            Call<ResponseBody> call = service.delete(params[0]);
            Response response = call.execute();

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
            Toast.makeText(fragment, R.string.project_portfolio_image_delete_ok, Toast.LENGTH_LONG).show();
        }
    }
}
