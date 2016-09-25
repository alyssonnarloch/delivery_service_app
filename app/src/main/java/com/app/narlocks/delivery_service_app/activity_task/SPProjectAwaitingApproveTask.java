package com.app.narlocks.delivery_service_app.activity_task;

import android.os.AsyncTask;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity.SPProjectAwaitingFragment;
import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.service.ProjectService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class SPProjectAwaitingApproveTask extends AsyncTask<Integer, Void, Boolean> {

    private SPProjectAwaitingFragment fragment;
    private String errorMessage;

    public SPProjectAwaitingApproveTask(SPProjectAwaitingFragment fragment) {
        this.fragment = fragment;
        this.errorMessage = "";
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        boolean resultOk = true;

        try {
            ProjectService projectService = ServiceGenerator.createService(ProjectService.class);
            Call<ResponseBody> projectCall = projectService.approve(params[0]);
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
            this.fragment.backProjectsList();
        } else {
            Toast.makeText(fragment.getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}