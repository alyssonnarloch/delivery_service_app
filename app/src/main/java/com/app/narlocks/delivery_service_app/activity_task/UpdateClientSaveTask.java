package com.app.narlocks.delivery_service_app.activity_task;

import android.os.AsyncTask;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity.UpdateClientFragment;
import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.model.Client;
import com.app.narlocks.delivery_service_app.service.ClientService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class UpdateClientSaveTask extends AsyncTask<Void, Void, Boolean> {

    private Client client;
    private UpdateClientFragment fragment;
    private String errorMessage;

    public UpdateClientSaveTask(UpdateClientFragment fragment, Client client) {
        this.fragment = fragment;
        this.errorMessage = "";
        this.client = client;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean resultOk = true;

        try {
            ClientService clientService = ServiceGenerator.createService(ClientService.class);
            Call<ResponseBody> clientCall = clientService.update(client.getId(),
                    client.getName(),
                    client.getEmail(),
                    client.getPhone(),
                    client.getZipCode(),
                    client.getCityId(),
                    client.getAddress(),
                    client.getNumber(),
                    client.getProfileImage());
            Response clientResponse = clientCall.execute();

            if (clientResponse.code() != 200) {
                resultOk = false;
                errorMessage += ErrorConversor.getErrorMessage(clientResponse.errorBody());
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
            this.fragment.backDetails();
        } else {
            Toast.makeText(fragment.getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}


