package com.app.narlocks.delivery_service_app.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.narlocks.delivery_service_app.activity_task.UpdateClientLoadTask;
import com.app.narlocks.delivery_service_app.adapter.AutocompleteCityAdapter;
import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.City;
import com.app.narlocks.delivery_service_app.model.Client;
import com.app.narlocks.delivery_service_app.session.SessionManager;

public class UpdateClientFragment extends Fragment {

    private ImageView ivProfileImage;
    private ImageView ivChangeImage;
    private EditText etName;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etZipCode;
    private AutoCompleteTextView acCity;
    private EditText etAddress;
    private EditText etNumber;
    private Button btUpdate;

    private AutocompleteCityAdapter adapter;
    private int selectedCityId = 0;
    private String selectedCityName;

    private SessionManager session;
    private Resources res;

    public UpdateClientFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.res = getResources();
        this.session = new SessionManager(getActivity());
        this.adapter = new AutocompleteCityAdapter(getActivity(), R.layout.autocomplete_layout);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_update_client, container, false);

        loadViewComponents(view);
        new UpdateClientLoadTask(this).execute(getArguments().getInt("clientId"));

        return view;
    }

    private void loadViewComponents(View view) {
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        ivChangeImage = (ImageView) view.findViewById(R.id.ivChangeImage);
        etName = (EditText) view.findViewById(R.id.etName);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etPhone = (EditText) view.findViewById(R.id.etPhone);
        etZipCode = (EditText) view.findViewById(R.id.etZipCode);
        acCity = (AutoCompleteTextView) view.findViewById(R.id.acCity);
        etAddress = (EditText) view.findViewById(R.id.etAddress);
        etNumber = (EditText) view.findViewById(R.id.etNumber);
        btUpdate = (Button) view.findViewById(R.id.btUpdate);

        acCity.setAdapter(adapter);

        loadViewListeners(view);
    }

    private void loadViewListeners(View view) {
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
                if (selectedCityName != null && !selectedCityName.equals(s.toString())) {
                    selectedCityId = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void loadContentViewComponents(Client client) {
        ivProfileImage.setImageBitmap(Image.base64ToBitmap(client.getProfileImage()));
        etName.setText(client.getName());
        etEmail.setText(client.getEmail());
        etPhone.setText(client.getPhone());
        etZipCode.setText(client.getZipCode());
        acCity.setText(client.getCity().getName());
        etAddress.setText(client.getAddress());
        etNumber.setText(client.getNumber() + "");
    }
}
