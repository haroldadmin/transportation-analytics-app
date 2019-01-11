package com.kshitijchauhan.haroldadmin.transportation_analytics;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kshitijchauhan.haroldadmin.transportation_analytics.models.RouteRequest;
import com.kshitijchauhan.haroldadmin.transportation_analytics.remote.AuthInterceptor;
import com.kshitijchauhan.haroldadmin.transportation_analytics.remote.service.route_requests.RequestsService;
import com.kshitijchauhan.haroldadmin.transportation_analytics.remote.service.user.UserService;
import com.kshitijchauhan.haroldadmin.transportation_analytics.remote.service.user.request.UserLoginRequest;
import com.kshitijchauhan.haroldadmin.transportation_analytics.remote.service.user.request.UserRegisterRequest;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);

        Gson gson = new GsonBuilder()
                .create();

        AuthInterceptor authInterceptor = new AuthInterceptor(sharedPreferences.getString
                (Constants.KEY_JWT_TOKEN, ""));

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("http://192.168.43.11:3000/api/")
                .client(okHttpClient)
                .build();

        UserService service = retrofit.create(UserService.class);
        RequestsService routeRequestService = retrofit.create(RequestsService.class);

        routeRequestService.getAllRouteRequests()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(list -> {
                    for (RouteRequest routeRequest : list) {
                        Log.d(TAG, String.valueOf(routeRequest.getStartPointLat()));

                    }
                })
                .subscribe();

        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setEmail("user@test2.com");
        registerRequest.setPassword("testpassword");
        registerRequest.setName("Test User");

        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setEmail("user@test2.com");
        loginRequest.setPassword("testpassword");
//
//        compositeDisposable.add(service.registerUser(registerRequest)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSuccess(response -> {
//                    Log.d(TAG, response.getMessage());
//                }).subscribe()
//        );
//
//        compositeDisposable.add(
//
//                service.loginUser(loginRequest)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .doOnSuccess(response -> {
//                            Log.d(TAG, response.getAccessToken());
//                            sharedPreferences
//                                    .edit()
//                                    .putString(KEY_JWT_TOKEN, response.getAccessToken())
//                                    .apply();
//
//                            authInterceptor.setToken(response.getAccessToken());
//                        })
//                        .subscribe()
//
//
//        );
//
//        compositeDisposable.add(service.getAllUsers()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSuccess(userCollectionResponse -> {
//                    for (User user : userCollectionResponse) {
//                        Log.d(TAG, user.getName());
//                    }
//                })
//                .subscribe()
//        );
//
//        compositeDisposable.add(service.getUser(1)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSuccess(response -> {
//                    Log.d(TAG, response.getName());
//                })
//                .subscribe()
//        );
//
//        compositeDisposable.add(service.getRoutesForUser(1)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSuccess(response -> {
//                    for (RouteRequest req : response) {
//                        Log.d(TAG, String.valueOf(req.getEndPointLat()));
//                    }
//                })
//                .subscribe()
//        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
