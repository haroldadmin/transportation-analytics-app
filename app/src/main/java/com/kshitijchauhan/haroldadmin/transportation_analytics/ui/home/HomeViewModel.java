package com.kshitijchauhan.haroldadmin.transportation_analytics.ui.home;

import com.kshitijchauhan.haroldadmin.transportation_analytics.models.RouteRequest;
import com.kshitijchauhan.haroldadmin.transportation_analytics.remote.ApiManager;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    @Inject public ApiManager apiManager;

    private MutableLiveData<List<RouteRequest>> mutableRouteRequestsList = new MutableLiveData<>();

    public LiveData<List<RouteRequest>> routeRequestsList = mutableRouteRequestsList;

    public HomeViewModel() {
        super();
    }

    public void getRouteRequests() {
//        apiManager
    }

}
