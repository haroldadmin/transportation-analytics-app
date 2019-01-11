package com.kshitijchauhan.haroldadmin.transportation_analytics.ui.auth;

import android.content.SharedPreferences;

import com.kshitijchauhan.haroldadmin.transportation_analytics.models.User;
import com.kshitijchauhan.haroldadmin.transportation_analytics.utilities.Constants;
import com.kshitijchauhan.haroldadmin.transportation_analytics.utilities.SingleLiveEvent;
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
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
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
                .doOnSubscribe(disposable -> {
                    mutableIsLoading.postValue(true);
                })
                .doOnSuccess(userLoginResponse -> {
                    sharedPreferences
                            .edit()
                            .putString(Constants.KEY_JWT_TOKEN, userLoginResponse.getAccessToken())
                            .apply();

                    authInterceptor.setToken(userLoginResponse.getAccessToken());
                })
                .flatMap((Function<UserLoginResponse, SingleSource<User>>) userLoginResponse ->
                        apiManager.getUserProfile())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(User user) {
                        sharedPreferences.edit()
                                .putInt(Constants.KEY_USER_ID, user.getId())
                                .apply();
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
