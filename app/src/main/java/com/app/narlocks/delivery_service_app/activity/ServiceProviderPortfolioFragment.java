package com.app.narlocks.delivery_service_app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.app.narlocks.delivery_service_app.activity_task.ServiceProviderPortfolioTask;
import com.app.narlocks.delivery_service_app.adapter.ProjectPortfolioGridViewAdapter;
import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.ImageItem;
import com.app.narlocks.delivery_service_app.model.ServiceProviderPortfolio;

import java.util.ArrayList;
import java.util.List;

public class ServiceProviderPortfolioFragment extends Fragment {

    private GridView gvImages;

    public ServiceProviderPortfolioFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.title_service_provider_portfolio);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_project_portfolio, container, false);

        gvImages = (GridView) view.findViewById(R.id.gvImages);

        new ServiceProviderPortfolioTask(this).execute(getArguments().getInt("serviceProviderId"));

        return view;
    }

    public void loadContentViewComponents(List<ServiceProviderPortfolio> serviceProviderPortfolio) {
        ProjectPortfolioGridViewAdapter adapter = new ProjectPortfolioGridViewAdapter(getActivity(), R.layout.gridview_image_layout, getImageItems(serviceProviderPortfolio), getActivity().getSupportFragmentManager());
        gvImages.setAdapter(adapter);
    }

    private List<ImageItem> getImageItems(List<ServiceProviderPortfolio> serviceProviderPortfolio) {
        List<ImageItem> imageItems = new ArrayList();

        for(ServiceProviderPortfolio p : serviceProviderPortfolio) {
            imageItems.add(new ImageItem(Image.base64ToBitmap(p.getImage())));
        }

        return imageItems;
    }
}
