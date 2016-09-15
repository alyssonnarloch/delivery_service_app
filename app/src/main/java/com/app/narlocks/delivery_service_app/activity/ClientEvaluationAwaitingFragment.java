package com.app.narlocks.delivery_service_app.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.narlocks.delivery_service_app.session.SessionManager;

public class ClientEvaluationAwaitingFragment extends Fragment {

    private TextView tvTitle;
    private TextView tvStatus;
    private TextView tvServiceProviderName;
    private EditText etDescription;
    private Button btFinish;

    private SessionManager session;
    private Resources res;

    public ClientEvaluationAwaitingFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.res = getResources();
        this.session = new SessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_client_evaluation_awaiting, container, false);

        loadViewComponents(view);
        loadContentViewComponents();

        return view;
    }

    private void loadViewComponents(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        tvServiceProviderName = (TextView) view.findViewById(R.id.tvServiceProviderName);
        etDescription = (EditText) view.findViewById(R.id.etDescription);
        btFinish = (Button) view.findViewById(R.id.btFinish);
    }

    private void loadContentViewComponents() {
        tvTitle.setText(getArguments().getString("title"));
        tvStatus.setText(getArguments().getString("status"));
        tvServiceProviderName.setText(getArguments().getString("serviceProviderName"));
    }
}
