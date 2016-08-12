package com.app.narlocks.delivery_service_app.extras;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;

public class ErrorConversor {

    private Gson gson;

    public static String getErrorMessage(ResponseBody errorBody) {
        Gson gson = new Gson();
        String errorMessage = "";

        try {
            Map<String, String> errors = gson.fromJson(errorBody.string(), Map.class);

            for(Map.Entry<String, String> entry : errors.entrySet()) {
                errorMessage += entry.getValue() + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
            errorMessage = "Erro ao converter o retorno.";
        }

        return errorMessage;
    }
}
