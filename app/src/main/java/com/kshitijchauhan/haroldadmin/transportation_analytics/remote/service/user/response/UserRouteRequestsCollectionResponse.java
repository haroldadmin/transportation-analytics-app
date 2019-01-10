package com.kshitijchauhan.haroldadmin.transportation_analytics.remote.service.user.response;

import com.kshitijchauhan.haroldadmin.transportation_analytics.models.RouteRequest;

import java.util.List;

public class UserRouteRequestsCollectionResponse {
    private List<RouteRequest> routeRequestList;

    public List<RouteRequest> getRouteRequestList() {
        return routeRequestList;
    }

    public void setRouteRequestList(List<RouteRequest> routeRequestList) {
        this.routeRequestList = routeRequestList;
    }
}
