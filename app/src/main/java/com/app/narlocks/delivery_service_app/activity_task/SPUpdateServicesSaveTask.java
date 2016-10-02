package com.app.narlocks.delivery_service_app.activity_task;

import android.os.AsyncTask;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity.R;
import com.app.narlocks.delivery_service_app.activity.SPUpdateServicesFragment;
import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.model.ServiceProvider;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;
import com.app.narlocks.delivery_service_app.service.ServiceProviderService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class SPUpdateServicesSaveTask extends AsyncTask<Void, Void, Boolean> {

    private ServiceProvider serviceProvider;
    private String errorMessage;
    private SPUpdateServicesFragment fragment;

    public SPUpdateServicesSaveTask(SPUpdateServicesFragment fragment, ServiceProvider serviceProvider) {
        this.fragment = fragment;
        this.errorMessage = "";
        this.serviceProvider = serviceProvider;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean resultOk = true;

        try {
            ServiceProviderService service = ServiceGenerator.createService(ServiceProviderService.class);
            Call<ResponseBody> call = service.updateServices(serviceProvider.getId(),
                    serviceProvider.getServiceTypeIds(),
                    serviceProvider.getExperienceDescription(),
                    serviceProvider.isAvailable());
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
            this.fragment.backDashboard();
            Toast.makeText(fragment.getActivity(), R.string.update_data_success, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(fragment.getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}



