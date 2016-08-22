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
import android.widget.CheckBox;
import android.widget.GridView;

import com.app.narlocks.delivery_service_app.adapter.AutocompleteCityAdapter;
import com.app.narlocks.delivery_service_app.adapter.GridViewTextRemoveAdapter;
import com.app.narlocks.delivery_service_app.model.City;
import com.app.narlocks.delivery_service_app.model.NameItem;
import com.app.narlocks.delivery_service_app.model.ServiceProvider;

import java.util.ArrayList;
import java.util.List;

public class NewServiceProviderAreasActivity extends AppCompatActivity {

    private AutoCompleteTextView acCity;
    private CheckBox cbAvailable;
    private GridView gvSelectedCities;
    private Resources res;

    private AutocompleteCityAdapter acCityAdapter;
    private GridViewTextRemoveAdapter gvTextAdapter;
    private List<NameItem> cities;

    private ServiceProvider serviceProvider;
    private int selectedCityId = 0;
    private String selectedCityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_service_provider_areas);

        res = getResources();
        this.serviceProvider = (ServiceProvider) getIntent().getSerializableExtra("serviceProviderObj");
        cities = new ArrayList();

        gvSelectedCities = (GridView) findViewById(R.id.gvSelectedCities);

        acCityAdapter = new AutocompleteCityAdapter(this, R.layout.autocomplete_layout);
        gvTextAdapter = new GridViewTextRemoveAdapter(this, R.layout.text_remove_layout, cities);
        gvSelectedCities.setAdapter(gvTextAdapter);

        acCity = (AutoCompleteTextView) findViewById(R.id.acCity);
        acCity.setInputType(InputType.TYPE_CLASS_TEXT);
        acCity.setAdapter(acCityAdapter);

        acCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City selectedCity = acCityAdapter.getItem(position);

                selectedCityId = selectedCity.getId();
                selectedCityName = selectedCity.getName();
                acCity.setText(selectedCity.getName());

                cities.add(new NameItem(selectedCityId, selectedCityName));
                gvTextAdapter = new GridViewTextRemoveAdapter(NewServiceProviderAreasActivity.this, R.layout.text_remove_layout, cities);
                gvSelectedCities.setAdapter(gvTextAdapter);

                acCity.setText(null);
            }
        });

        acCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (selectedCityName != null && !selectedCityName.equals(s.toString())) {
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

        serviceProvider = (ServiceProvider) getIntent().getSerializableExtra("serviceProviderObj");
    }

    public void onClickNext(View view) {
        if(validate()) {
            cbAvailable = (CheckBox) findViewById(R.id.cbAvailable);
            serviceProvider.setAvailable(cbAvailable.isChecked());

            for(NameItem nameItem : cities) {
                serviceProvider.addOccupationAreaId(nameItem.getId());
            }

            Intent i = new Intent(NewServiceProviderAreasActivity.this, NewServiceProviderPortfolioActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            i.putExtra("serviceProviderObj", serviceProvider);

            startActivity(i);
        }
    }

    public void onClickBack(View view) {
        Intent i = new Intent(NewServiceProviderAreasActivity.this, NewServiceProviderServicesActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        i.putExtra("serviceProviderObj", serviceProvider);

        startActivity(i);
    }

    private boolean validate() {
        boolean isValid = true;

        if(cities.size() == 0) {
            isValid = false;
            acCity.setError(res.getString(R.string.service_provider_areas_required));
        }

        return isValid;
    }

}
