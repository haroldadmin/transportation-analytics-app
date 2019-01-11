package com.kshitijchauhan.haroldadmin.transportation_analytics;

import android.app.Application;

import com.kshitijchauhan.haroldadmin.transportation_analytics.di.component.AppComponent;
import com.kshitijchauhan.haroldadmin.transportation_analytics.di.component.DaggerAppComponent;
import com.kshitijchauhan.haroldadmin.transportation_analytics.di.module.ContextModule;
import com.kshitijchauhan.haroldadmin.transportation_analytics.di.module.SharedPreferencesModule;

public class TransportationAnalyticsApp extends Application {

    public static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent
                .builder()
                .contextModule(new ContextModule(this.getApplicationContext()))
                .sharedPreferencesModule(new SharedPreferencesModule(Constants.SHARED_PREFS_NAME))
                .build();
    }
}
