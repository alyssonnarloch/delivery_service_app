package com.app.narlocks.delivery_service_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    void onClickUserSelection(View view) {
        Intent i = new Intent(LoginActivity.this, UserSelectionActivity.class);
        startActivity(i);
    }
}
