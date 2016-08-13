package com.app.narlocks.delivery_service_app.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.app.narlocks.delivery_service_app.adapter.AutocompleteCityAdapter;
import com.app.narlocks.delivery_service_app.model.City;
import com.app.narlocks.delivery_service_app.model.ServiceProvider;

public class NewServiceProviderMainActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etZipCode;
    private EditText etAddress;
    private EditText etNumber;
    private EditText etPassword;
    private EditText etPasswordConfirmation;
    AutoCompleteTextView acCity;

    private int selectedCityId = 0;
    private String selectedCityName;

    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_service_provider_main);

        res = getResources();

        final AutocompleteCityAdapter adapter = new AutocompleteCityAdapter(this,android.R.layout.simple_dropdown_item_1line);

        acCity = (AutoCompleteTextView) findViewById(R.id.acCity);
        acCity.setInputType(InputType.TYPE_CLASS_TEXT);
        acCity.setAdapter(adapter);

        acCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City selectedCity = adapter.getItem(position);

                selectedCityId = selectedCity.getId();
                selectedCityName = selectedCity.getName();
                acCity.setText(selectedCity.getName());
            }
        });

        acCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(selectedCityName != null && !selectedCityName.equals(s.toString())) {
                    selectedCityId = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onClickNext(View view) {
        ServiceProvider serviceProvider = getServiceProviderByView(view);

        if(validate(serviceProvider)) {
            Intent i = new Intent(NewServiceProviderMainActivity.this, NewServiceProviderProfileActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            i.putExtra("serviceProviderObj", serviceProvider);

            startActivity(i);
        }
    }

    public void onClickCancel(View view) {
        Intent i = new Intent(NewServiceProviderMainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void getDataViewContent(View view) {
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etZipCode = (EditText) findViewById(R.id.etZipCode);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etNumber = (EditText) findViewById(R.id.etNumber);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordConfirmation = (EditText) findViewById(R.id.etPasswordConfirmation);
    }

    private ServiceProvider getServiceProviderByView(View view) {
        getDataViewContent(view);

        ServiceProvider serviceProvider = new ServiceProvider();

        serviceProvider.setName(etName.getText().toString());
        serviceProvider.setEmail(etEmail.getText().toString());
        serviceProvider.setPhone(etPhone.getText().toString());
        serviceProvider.setZipCode(etZipCode.getText().toString());
        serviceProvider.setCityId(selectedCityId);
        serviceProvider.setAddress(etAddress.getText().toString());

        int number;
        try {
            number = Integer.parseInt(etNumber.getText().toString());
        } catch (NumberFormatException ex) {
            number = 0;
        }
        serviceProvider.setNumber(number);
        serviceProvider.setPassword(etPassword.getText().toString());
        serviceProvider.setPasswordConfirmation(etPasswordConfirmation.getText().toString());

        return serviceProvider;
    }
    
    private boolean validate(ServiceProvider serviceProvider) {
        boolean valid = true;

        if(serviceProvider.getName() == null || serviceProvider.getName().equals("")) {
            valid = false;
            etName.setError(res.getString(R.string.validation_required));
        }

        if(serviceProvider.getEmail() == null || serviceProvider.getEmail().equals("")) {
            valid = false;
            etEmail.setError(res.getString(R.string.validation_required));
        }

        if(serviceProvider.getPhone() == null || serviceProvider.getPhone().equals("")) {
            valid = false;
            etPhone.setError(res.getString(R.string.validation_required));
        }

        if(serviceProvider.getZipCode() == null || serviceProvider.getZipCode().equals("")) {
            valid = false;
            etZipCode.setError(res.getString(R.string.validation_required));
        }

        if(serviceProvider.getCityId() == 0) {
            valid = false;
            acCity.setError(res.getString(R.string.validation_required));
        }

        if(serviceProvider.getAddress() == null || serviceProvider.getAddress().equals("")) {
            valid = false;
            etAddress.setError(res.getString(R.string.validation_required));
        }

        if(serviceProvider.getNumber() == 0) {
            valid = false;
            etNumber.setError(res.getString(R.string.validation_required));
        }

        if(serviceProvider.getPassword() == null || serviceProvider.getPassword().equals("")) {
            valid = false;
            etPassword.setError(res.getString(R.string.validation_required));
        } else if(serviceProvider.getPasswordConfirmation() == null || serviceProvider.getPasswordConfirmation().equals("")) {
            valid = false;
            etPasswordConfirmation.setError(res.getString(R.string.validation_required));
        } else if(!serviceProvider.getPassword().equals(serviceProvider.getPasswordConfirmation())) {
            valid = false;
            etPasswordConfirmation.setError(res.getString(R.string.validation_password_confirmation));
        }

        return valid;
    }
}
