package com.app.narlocks.delivery_service_app.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.extras.Extra;
import com.app.narlocks.delivery_service_app.model.User;
import com.app.narlocks.delivery_service_app.service.AuthService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;
import com.app.narlocks.delivery_service_app.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;

    private SessionManager session;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(getApplicationContext());
        if(session.isLoggedIn()){
            goToUserEnvironment(session.getUserProfileId());
        }

        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        res = getResources();
    }

    public void onClickLogin(View view) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (validate(email, password)) {
            AuthService service = ServiceGenerator.createService(AuthService.class);

            Call<User> call = service.authenticateAux(email, Extra.getMD5(password));
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        User user = response.body();

                        session.createLoginSession(user.getId(), user.getName(), user.getEmail(), user.getProfileImage(), user.getZipCode(), user.getCity().getId(), user.getCity().getName(), user.getAddress(), user.getNumber(), user.getProfileId());

                        goToUserEnvironment(user.getProfileId());
                    } else {
                        try {
                            Toast.makeText(LoginActivity.this, ErrorConversor.getErrorMessage(response.errorBody()), Toast.LENGTH_LONG).show();
                        } catch (Exception ex) {
                            Toast.makeText(LoginActivity.this, res.getString(R.string.login_failure), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, res.getString(R.string.login_failure), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void onClickUserSelection(View view) {
        Intent i = new Intent(LoginActivity.this, UserSelectionActivity.class);
        startActivity(i);
    }

    private boolean validate(String email, String password) {
        boolean isValid = true;

        if (email == null || email.equals("")) {
            isValid = false;
            etEmail.setError(res.getString(R.string.validation_required));
        }

        if (password == null || password.equals("")) {
            isValid = false;
            etPassword.setError(res.getString(R.string.validation_required));
        }

        return isValid;
    }

    private void goToUserEnvironment(String profileId) {
        Intent i = null;
        if (profileId != null && profileId.equals("1")) {
            i = new Intent(LoginActivity.this, ClientDefaultActivity.class);
        } else {
            i = new Intent(LoginActivity.this, SPDefaultActivity.class);
        }
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
