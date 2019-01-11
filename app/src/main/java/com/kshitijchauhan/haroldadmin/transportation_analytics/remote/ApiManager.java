package com.kshitijchauhan.haroldadmin.transportation_analytics.remote;

import com.kshitijchauhan.haroldadmin.transportation_analytics.remote.service.route_requests.RequestsService;
import com.kshitijchauhan.haroldadmin.transportation_analytics.remote.service.user.UserService;
import com.kshitijchauhan.haroldadmin.transportation_analytics.remote.service.user.request.UserLoginRequest;
import com.kshitijchauhan.haroldadmin.transportation_analytics.remote.service.user.response.UserLoginResponse;

import javax.inject.Inject;

import io.reactivex.Single;

public class ApiManager {

    private UserService userService;
    private RequestsService requestsService;

    @Inject
    public ApiManager(UserService userService, RequestsService requestsService) {
        this.userService = userService;
        this.requestsService = requestsService;
    }

    public Single<UserLoginResponse> login(UserLoginRequest userLoginRequest) {
        return userService.loginUser(userLoginRequest);
    }

}
