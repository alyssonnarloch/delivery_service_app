package com.app.narlocks.delivery_service_app.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.adapter.AutocompleteCityAdapter;
import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.extras.Extra;
import com.app.narlocks.delivery_service_app.model.City;
import com.app.narlocks.delivery_service_app.model.Client;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.model.ServiceProvider;
import com.app.narlocks.delivery_service_app.service.ProjectService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;
import com.app.narlocks.delivery_service_app.session.SessionManager;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientMakeContractFragment extends Fragment {

    private TextView tvName;
    private RatingBar rbStars;
    private TextView tvEvaluation;
    private EditText etTitle;
    private EditText etDescription;
    private CheckBox cbUseMyAddress;
    private EditText etZipCode;
    private AutoCompleteTextView acCity;
    private EditText etAddress;
    private EditText etNumber;
    private ImageButton ibStartCalendar;
    private ImageButton ibEndCalendar;
    private EditText dtStart;
    private EditText dtEnd;
    private Button btFinish;

    private int serviceProviderId;
    private double serviceProviderQualification;
    private String serviceProviderName;
    private int serviceProviderNumEvaluations;

    private int selectedCityId = 0;
    private String selectedCityName;

    private SessionManager session;
    Resources res;

    public ClientMakeContractFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        getActivity().setTitle(R.string.title_make_contract);

        View view = inflater.inflate(R.layout.fragment_client_make_contract, container, false);

        res = getResources();
        session = new SessionManager(getActivity());

        final AutocompleteCityAdapter cityAdapter = new AutocompleteCityAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line);

        serviceProviderId = getArguments().getInt("serviceProviderId");
        serviceProviderQualification = getArguments().getDouble("serviceProviderQualification");
        serviceProviderName = getArguments().getString("serviceProviderName");
        serviceProviderNumEvaluations = getArguments().getInt("serviceProviderNumEvaluations");

        tvName = (TextView) view.findViewById(R.id.tvName);
        tvName.setText(serviceProviderName);

        rbStars = (RatingBar) view.findViewById(R.id.rbStars);
        rbStars.setRating((float) serviceProviderQualification);

        tvEvaluation = (TextView) view.findViewById(R.id.tvEvaluation);
        String evaluationLabel = "";

        if (serviceProviderNumEvaluations > 1 || serviceProviderNumEvaluations == 0) {
            evaluationLabel = res.getString(R.string.evaluations);
        } else {
            evaluationLabel = res.getString(R.string.evaluation);
        }
        tvEvaluation.setText("(" + serviceProviderNumEvaluations + " " + evaluationLabel + ")");

        etTitle = (EditText) view.findViewById(R.id.etTitle);
        etDescription = (EditText) view.findViewById(R.id.etDescription);
        cbUseMyAddress = (CheckBox) view.findViewById(R.id.cbUseMyAddress);
        etZipCode = (EditText) view.findViewById(R.id.etZipCode);
        acCity = (AutoCompleteTextView) view.findViewById(R.id.acCity);
        etAddress = (EditText) view.findViewById(R.id.etAddress);
        etNumber = (EditText) view.findViewById(R.id.etNumber);
        ibStartCalendar = (ImageButton) view.findViewById(R.id.ibStartCalendar);
        dtStart = (EditText) view.findViewById(R.id.dtStart);
        ibEndCalendar = (ImageButton) view.findViewById(R.id.ibEndCalendar);
        dtEnd = (EditText) view.findViewById(R.id.dtEnd);
        btFinish = (Button) view.findViewById(R.id.btFinish);

        cbUseMyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbUseMyAddress.isChecked()) {
                    etZipCode.setEnabled(false);
                    acCity.setEnabled(false);
                    etAddress.setEnabled(false);
                    etNumber.setEnabled(false);

                    etZipCode.setText(session.getUserZipCode());
                    acCity.setText(session.getUserCityName());
                    selectedCityId = session.getUserCityId();
                    selectedCityName = session.getUserCityName();
                    etAddress.setText(session.getUserAddress());
                    etNumber.setText(session.getUserNumber() + "");

                    etZipCode.setError(null);
                    acCity.setError(null);
                    etAddress.setError(null);
                    etNumber.setError(null);
                } else {
                    etZipCode.setEnabled(true);
                    acCity.setEnabled(true);
                    etAddress.setEnabled(true);
                    etNumber.setEnabled(true);

                    etZipCode.setText("");
                    acCity.setText("");
                    selectedCityId = 0;
                    selectedCityName = "";
                    etAddress.setText("");
                    etNumber.setText("");
                }
            }
        });

        acCity.setInputType(InputType.TYPE_CLASS_TEXT);
        acCity.setAdapter(cityAdapter);

        acCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City selectedCity = cityAdapter.getItem(position);

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

        ibStartCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragment = new DatePickerFragment() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dtStart.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                };
                datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        ibEndCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragment = new DatePickerFragment() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dtEnd.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                };
                datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Project project = getProjectByView();
                if(validate(project)) {
                    save(project);
                }
            }
        });

        return view;
    }

    private Project getProjectByView() {
        Project project = new Project();

        project.setTitle(etTitle.getText().toString());
        project.setDescription(etDescription.getText().toString());

        if(cbUseMyAddress.isChecked()) {
            project.setZipCode(session.getUserZipCode());
            project.setCity(new City(session.getUserCityId()));
            project.setAddress(session.getUserAddress());
            project.setNumber(session.getUserNumber());
        } else {
            project.setZipCode(etZipCode.getText().toString());
            project.setCity(new City(selectedCityId));
            project.setAddress(etAddress.getText().toString());

            int number;
            try {
                number = Integer.parseInt(etNumber.getText().toString());
            } catch (NumberFormatException ex) {
                number = 0;
            }
            project.setNumber(number);
        }

        project.setClient(new Client(session.getUserId()));
        project.setServiceProvider(new ServiceProvider(serviceProviderId));
        project.setStartAt(Extra.toDate(dtStart.getText().toString(), "dd/MM/yyyy"));
        project.setEndAt(Extra.toDate(dtEnd.getText().toString(), "dd/MM/yyyy"));

        return project;
    }

    private boolean validate(Project project) {
        boolean isValid = true;

        if (project.getTitle() == null || project.getTitle().isEmpty()) {
            isValid = false;
            etTitle.setError(res.getString(R.string.validation_required));
        }

        if (project.getDescription() == null || project.getDescription().isEmpty()) {
            isValid = false;
            etDescription.setError(res.getString(R.string.validation_required));
        }

        if (project.getZipCode() == null || project.getZipCode().isEmpty()) {
            isValid = false;
            etZipCode.setError(res.getString(R.string.validation_required));
        }

        if (project.getCity().getId() == 0) {
            isValid = false;
            acCity.setError(res.getString(R.string.validation_required));
        }

        if (project.getAddress() == null || project.getAddress().isEmpty()) {
            isValid = false;
            etAddress.setError(res.getString(R.string.validation_required));
        }

        if (project.getNumber() == 0) {
            etNumber.setError(res.getString(R.string.validation_required));
        }

        if (project.getStartAt() == null) {
            isValid = false;
            dtStart.setError(res.getString(R.string.validation_required));
        }

        if (project.getEndAt() == null) {
            isValid = false;
            dtEnd.setError(res.getString(R.string.validation_required));
        }

        return isValid;
    }

    private void save(Project project) {
        ProjectService projectService = ServiceGenerator.createService(ProjectService.class);
        Call<ResponseBody> projectCall = projectService.save(project.getTitle(),
                project.getDescription(),
                project.getClient().getId(),
                project.getServiceProvider().getId(),
                project.getAddress(),
                project.getNumber(),
                project.getZipCode(),
                project.getCity().getId(),
                Extra.dateToString(project.getStartAt(), "yyyy-MM-dd"),
                Extra.dateToString(project.getEndAt(), "yyyy-MM-dd")
        );

        projectCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Toast.makeText(getActivity(), res.getString(R.string.project_save_ok), Toast.LENGTH_LONG).show();

                    Fragment fragment = new ClientProjectsFragment();

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_client, fragment).commit();

                    //DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_client_layout);
                    //drawer.closeDrawer(GravityCompat.START);

                } else {
                    Toast.makeText(getActivity(), ErrorConversor.getErrorMessage(response.errorBody()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), res.getString(R.string.service_project_fail), Toast.LENGTH_LONG).show();
            }
        });
    }
}
