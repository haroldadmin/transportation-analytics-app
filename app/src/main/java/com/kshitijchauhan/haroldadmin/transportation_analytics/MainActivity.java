package com.kshitijchauhan.haroldadmin.transportation_analytics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.kshitijchauhan.haroldadmin.transportation_analytics.ui.auth.AuthenticationActivity;
import com.kshitijchauhan.haroldadmin.transportation_analytics.ui.home.HomeFragment;
import com.kshitijchauhan.haroldadmin.transportation_analytics.utilities.Constants;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject public SharedPreferences sharedPreferences;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        TransportationAnalyticsApp.appComponent
                .inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        boolean isAuthenticated = sharedPreferences.getBoolean(Constants.KEY_IS_AUTHENTICATED,
                false);

        if (!isAuthenticated) {
            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivity(intent);
            finish();
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, HomeFragment.newInstance())
                    .commit();
        }

    }
}
