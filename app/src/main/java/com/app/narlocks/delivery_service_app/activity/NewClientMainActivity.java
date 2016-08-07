package com.app.narlocks.delivery_service_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.app.narlocks.delivery_service_app.adapter.AutocompleteCityAdapter;
import com.app.narlocks.delivery_service_app.model.City;
import com.app.narlocks.delivery_service_app.model.Client;

public class NewClientMainActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etZipCode;
    private EditText etAddress;
    private EditText etPassword;
    private EditText etPasswordConfirmation;
    private int selectedCityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client_main);

        Log.i("MAIN", "CREATE");

        final AutoCompleteTextView acCity = (AutoCompleteTextView) findViewById(R.id.acCity);
        final AutocompleteCityAdapter adapter = new AutocompleteCityAdapter(this,android.R.layout.simple_dropdown_item_1line);
        acCity.setAdapter(adapter);

        acCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City selectedCity = adapter.getItem(position);

                selectedCityId = selectedCity.getId();
                acCity.setText(selectedCity.getName());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MAIN", "RESUME");
    }

    private void onClickProfileImage(View view) {
        Client client = getClientByView(view);

        Intent i = new Intent(NewClientMainActivity.this, NewClientProfileActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }

    private void getDataViewContent(View view) {
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etZipCode = (EditText) findViewById(R.id.etZipCode);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordConfirmation = (EditText) findViewById(R.id.etPasswordConfirmation);
    }

    private Client getClientByView(View view) {
        getDataViewContent(view);

        Client client = new Client();

        client.setName(etName.getText().toString());
        client.setEmail(etEmail.getText().toString());
        client.setPhone(etPhone.getText().toString());
        client.setZipCode(etZipCode.getText().toString());
        client.setCityId(selectedCityId);
        client.setAddress(etAddress.getText().toString());
        client.setPassword(etPassword.getText().toString());

        return client;
    }
}
