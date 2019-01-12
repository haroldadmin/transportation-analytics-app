package com.kshitijchauhan.haroldadmin.transportation_analytics.ui.home;

import androidx.lifecycle.ViewModelProviders;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kshitijchauhan.haroldadmin.transportation_analytics.R;
import com.kshitijchauhan.haroldadmin.transportation_analytics.TransportationAnalyticsApp;
import com.kshitijchauhan.haroldadmin.transportation_analytics.ui.MainViewModel;
import com.kshitijchauhan.haroldadmin.transportation_analytics.utilities.Constants;
import com.kshitijchauhan.haroldadmin.transportation_analytics.utilities.State;

import javax.inject.Inject;

public class HomeFragment extends Fragment {

    @Inject public SharedPreferences sharedPreferences;

    private HomeViewModel homeViewModel;
    private MainViewModel mainViewModel;
    private RoutesAdapter routesAdapter;
    private ProgressBar pbLoading;
    private RecyclerView routesRecyclerView;
    private FloatingActionButton fabAddRequest;
    private int userId;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        TransportationAnalyticsApp
                .appComponent
                .inject(this);

        userId = sharedPreferences.getInt(Constants.KEY_USER_ID, -1);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        homeViewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                pbLoading.setVisibility(View.VISIBLE);
            } else {
                pbLoading.setVisibility(View.GONE);
            }
        });

        homeViewModel.message.observe(getViewLifecycleOwner(), message -> {
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        });

        homeViewModel.routeRequestsList.observe(getViewLifecycleOwner(), routeRequests -> {
            routesAdapter.submitList(routeRequests);
        });

        homeViewModel.getRouteRequests(userId);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pbLoading = view.findViewById(R.id.pbLoading);
        routesRecyclerView = view.findViewById(R.id.rvRouteRequests);
        fabAddRequest = view.findViewById(R.id.fabAddRouteRequest);

        routesAdapter = new RoutesAdapter(new RouteRequestsDiffItemCallback());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration divider = new DividerItemDecoration(getContext(),
                linearLayoutManager.getOrientation());

        routesRecyclerView.setAdapter(routesAdapter);
        routesRecyclerView.setLayoutManager(linearLayoutManager);
        routesRecyclerView.addItemDecoration(divider);

        fabAddRequest.setOnClickListener(v -> {
            mainViewModel.updateState(State.CREATE_REQUEST_STATE);
        });
    }
}
