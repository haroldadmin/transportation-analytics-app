package com.kshitijchauhan.haroldadmin.transportation_analytics.ui.create_request;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.kshitijchauhan.haroldadmin.transportation_analytics.R;

public class CreateRouteRequestFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = CreateRouteRequestFragment.class.getSimpleName();

    private CreateRouteRequestViewModel createRouteRequestViewModel;
    private MapView mapView;
    private TextInputEditText startEditText;
    private TextInputEditText endEditText;
    private MaterialButton btSendRequest;

    private LatLng start;
    private LatLng end;

    public static CreateRouteRequestFragment newInstance() {
        return new CreateRouteRequestFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_route_request, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createRouteRequestViewModel = ViewModelProviders.of(this).get(CreateRouteRequestViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.mvCreateRequest);
        startEditText = view.findViewById(R.id.etStartPoint);
        endEditText = view.findViewById(R.id.etEndPoint);
        btSendRequest = view.findViewById(R.id.btSendRequest);
        mapView.onCreate(null);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMapClickListener(latLng -> {
            Log.d(TAG, "Clicked on: " + latLng.toString() );
            if (start == null) {
                start = latLng;
                createMarker(latLng, googleMap);
                updateOverlay(latLng, startEditText);
            } else {
                end = latLng;
                createMarker(latLng, googleMap);
                updateOverlay(latLng, endEditText);
            }
        });
    }

    private void createMarker(LatLng point, GoogleMap map) {
        map.addMarker(new MarkerOptions().position(point));
    }

    private void updateOverlay(LatLng point, TextInputEditText editText) {
        if (editText != null) {
            String text = String.format("%f, %f", point.latitude, point.longitude);
            editText.setText(text);
        }

        if (this.start != null && this.end != null) {
            btSendRequest.setVisibility(View.VISIBLE);
        } else {
            btSendRequest.setVisibility(View.GONE);
        }
    }
}
