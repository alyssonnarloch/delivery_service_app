package com.app.narlocks.delivery_service_app.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.TextView;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity_task.UpdateClientLoadTask;
import com.app.narlocks.delivery_service_app.activity_task.UpdateClientSaveTask;
import com.app.narlocks.delivery_service_app.adapter.AutocompleteCityAdapter;
import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.City;
import com.app.narlocks.delivery_service_app.model.Client;
import com.app.narlocks.delivery_service_app.session.SessionManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ClientUpdateFragment extends Fragment {

    private ImageView ivProfilePicture;
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

    public static final int IMAGE_GALLERY_REQUEST = 20;

    public ClientUpdateFragment() {

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
        ivProfilePicture = (ImageView) view.findViewById(R.id.ivProfilePicture);
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

        ivChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String pictureDirectoryPath = pictureDirectory.getPath();
                Uri data = Uri.parse(pictureDirectoryPath);

                photoPickerIntent.setDataAndType(data, "image/*");
                startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upadte(v);
            }
        });
    }

    public void loadContentViewComponents(Client client) {
        ivProfilePicture.setImageBitmap(Image.base64ToBitmap(client.getProfileImage()));
        etName.setText(client.getName());
        etEmail.setText(client.getEmail());
        etPhone.setText(client.getPhone());
        etZipCode.setText(client.getZipCode());
        acCity.setText(client.getCity().getName());
        etAddress.setText(client.getAddress());
        etNumber.setText(client.getNumber() + "");

        selectedCityId = client.getCity().getId();
    }

    public void backDetails() {
        Fragment fragment = new ClientDetailsFragment();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_client, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                Uri imageUri = data.getData();

                InputStream inputStream;

                try {
                    inputStream = getActivity().getContentResolver().openInputStream(imageUri);

                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    ivProfilePicture.setImageBitmap(image);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getActivity(), res.getString(R.string.image_upload_fail), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private Client getClientByView(View view) {
        Client client = new Client();

        client.setId(session.getUserId());
        client.setName(etName.getText().toString());
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

        BitmapDrawable drawable = (BitmapDrawable) ivProfilePicture.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        client.setProfileImage(Image.bitmapToBase64(bitmap));

        return client;
    }

    private void upadte(View view) {
        Client client = getClientByView(view);

        if(validate(client)) {
            new UpdateClientSaveTask(this, client).execute();
            SessionManager session = new SessionManager(getActivity());
            session.createLoginSession(client.getId(), client.getName(), client.getEmail(), client.getProfileImage(), client.getZipCode(), selectedCityId, selectedCityName, client.getAddress(), client.getNumber());

            // Atualiza os dados do menu
            ((ImageView) getActivity().findViewById(R.id.ivProfileImage)).setImageBitmap(Image.base64ToBitmap(client.getProfileImage()));
            ((TextView) getActivity().findViewById(R.id.tvUserName)).setText(client.getName());
            ((TextView) getActivity().findViewById(R.id.tvUserEmail)).setText(client.getEmail());
        }
    }

    private boolean validate(Client client) {
        boolean valid = true;

        if (client.getName() == null || client.getName().equals("")) {
            valid = false;
            etName.setError(res.getString(R.string.validation_required));
        }

        if (client.getEmail() == null || client.getEmail().equals("")) {
            valid = false;
            etEmail.setError(res.getString(R.string.validation_required));
        }

        if (client.getPhone() == null || client.getPhone().equals("")) {
            valid = false;
            etPhone.setError(res.getString(R.string.validation_required));
        }

        if (client.getZipCode() == null || client.getZipCode().equals("")) {
            valid = false;
            etZipCode.setError(res.getString(R.string.validation_required));
        }

        if (client.getCityId() == 0) {
            valid = false;
            acCity.setError(res.getString(R.string.validation_required));
        }

        if (client.getAddress() == null || client.getAddress().equals("")) {
            valid = false;
            etAddress.setError(res.getString(R.string.validation_required));
        }

        if (client.getNumber() == 0) {
            valid = false;
            etNumber.setError(res.getString(R.string.validation_required));
        }

        return valid;
    }
}
