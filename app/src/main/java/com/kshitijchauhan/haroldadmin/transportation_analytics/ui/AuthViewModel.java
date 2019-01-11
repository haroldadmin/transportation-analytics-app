package com.kshitijchauhan.haroldadmin.transportation_analytics.ui;

import android.content.SharedPreferences;

import com.kshitijchauhan.haroldadmin.transportation_analytics.Constants;
import com.kshitijchauhan.haroldadmin.transportation_analytics.SingleLiveEvent;
import com.kshitijchauhan.haroldadmin.transportation_analytics.TransportationAnalyticsApp;
import com.kshitijchauhan.haroldadmin.transportation_analytics.remote.ApiManager;
import com.kshitijchauhan.haroldadmin.transportation_analytics.remote.AuthInterceptor;
import com.kshitijchauhan.haroldadmin.transportation_analytics.remote.service.user.request.UserLoginRequest;
import com.kshitijchauhan.haroldadmin.transportation_analytics.remote.service.user.response.UserLoginResponse;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AuthViewModel extends ViewModel {

    @Inject
    public ApiManager apiManager;
    @Inject
    public SharedPreferences sharedPreferences;
    @Inject
    public AuthInterceptor authInterceptor;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SingleLiveEvent<Boolean> mutableLoginSuccessState = new SingleLiveEvent<>();
    private SingleLiveEvent<String> mutableMessage = new SingleLiveEvent<>();
    private MutableLiveData<Boolean> mutableIsLoading = new MutableLiveData<>();

    public LiveData<Boolean> userLoginSuccessState = mutableLoginSuccessState;
    public LiveData<String> message = mutableMessage;
    public LiveData<Boolean> isLoading = mutableIsLoading;

    public AuthViewModel() {
        super();
        TransportationAnalyticsApp.appComponent.inject(this);
    }

    public void login(String email, String password) {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        apiManager.login(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    mutableIsLoading.postValue(true);
                })
                .subscribe(new SingleObserver<UserLoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(UserLoginResponse response) {
                        sharedPreferences
                                .edit()
                                .putString(Constants.KEY_JWT_TOKEN, response.getAccessToken())
                                .apply();

                        authInterceptor.setToken(response.getAccessToken());
                        mutableIsLoading.postValue(false);
                        mutableLoginSuccessState.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mutableLoginSuccessState.setValue(false);
                        mutableIsLoading.setValue(false);
                        mutableMessage.setValue("An error occurred");
                    }
                });
    }

}
