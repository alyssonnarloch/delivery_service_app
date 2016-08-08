package com.app.narlocks.delivery_service_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
    private EditText etNumber;
    private EditText etPassword;
    private EditText etPasswordConfirmation;
    AutoCompleteTextView acCity;
    private int selectedCityId = 0;
    private String selectedCityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client_main);

        Log.i("MAIN", "CREATE");

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
                Log.i("TEXTO INPUT", s + "");
                Log.i("TEXTO SELECIONADO", selectedCityName + "");
                if(selectedCityName != null && !selectedCityName.equals(s.toString())) {
                    Log.i("VERIFICAÇÃO", "ENTROU");
                    selectedCityId = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MAIN", "RESUME");
    }

    public void onClickProfileImage(View view) {
        Log.i("CIDADE ID", selectedCityId + "");
        Client client = getClientByView(view);

        if(validate(client)) {
            Intent i = new Intent(NewClientMainActivity.this, NewClientProfileActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            i.putExtra("clientObj", client);

            startActivity(i);
        }
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

    private Client getClientByView(View view) {
        getDataViewContent(view);

        Client client = new Client();

        client.setEmail(etEmail.getText().toString());
        client.setPhone(etPhone.getText().toString());
        client.setZipCode(etZipCode.getText().toString());
        client.setCityId(selectedCityId);
        client.setAddress(etAddress.getText().toString());

        int number;
        try {
            number = Integer.parseInt(etNumber.getText().toString());
        } catch (NumberFormatException ex) {
            number = 0;
        }
        client.setNumber(number);
        client.setPassword(etPassword.getText().toString());

        return client;
    }

    private boolean validate(Client client) {
        boolean valid = true;

        if(client.getName() == null || client.getName().equals("")) {
            valid = false;
            etName.setError("Obrigatório");
        }

        if(client.getEmail() == null || client.getEmail().equals("")) {
            valid = false;
            etEmail.setError("Obrigatório");
        }

        if(client.getPhone() == null || client.getPhone().equals("")) {
            valid = false;
            etPhone.setError("Obrigatório");
        }

        if(client.getZipCode() == null || client.getZipCode().equals("")) {
            valid = false;
            etZipCode.setError("Obrigatório");
        }

        if(client.getCityId() == 0) {
            valid = false;
            acCity.setError("Obrigatório");
        }

        return valid;
    }
}
