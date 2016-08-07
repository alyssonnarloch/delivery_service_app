package com.app.narlocks.delivery_service_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class UserSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);
    }

    void onClickTypeClient(View view) {
        Intent i = new Intent(UserSelectionActivity.this, NewClientMainActivity.class);
        startActivity(i);
    }

    void onClickTypeServiceProvider(View view) {

    }
}
