package com.kshitijchauhan.haroldadmin.transportation_analytics.remote.service.user;

import com.kshitijchauhan.haroldadmin.transportation_analytics.remote.common.CustomResponse;
import com.kshitijchauhan.haroldadmin.transportation_analytics.remote.service.user.request.UserLoginRequest;
import com.kshitijchauhan.haroldadmin.transportation_analytics.remote.service.user.request.UserRegisterRequest;
import com.kshitijchauhan.haroldadmin.transportation_analytics.remote.service.user.response.UserCollectionResponse;
import com.kshitijchauhan.haroldadmin.transportation_analytics.remote.service.user.response.UserProfileResponse;
import com.kshitijchauhan.haroldadmin.transportation_analytics.remote.service.user.response.UserRouteRequestsCollectionResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {

    @GET("/users/")
    Single<UserCollectionResponse> getAllUsers();

    @GET("/users/{userId}")
    Single<UserProfileResponse> getUser(@Path("userId") int userId);

    @GET("/users/{userId}/requests")
    Single<UserRouteRequestsCollectionResponse> getRoutesForUser(@Path("userId") int userId);

    @POST("/users/register")
    Single<CustomResponse> registerUser(@Body UserRegisterRequest request);

    @POST("/users/register")
    Single<CustomResponse> loginUser(@Body UserLoginRequest request);

}
