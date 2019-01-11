package com.kshitijchauhan.haroldadmin.transportation_analytics.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.kshitijchauhan.haroldadmin.transportation_analytics.R;

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.auth_fragment_container, LoginFragment.newInstance())
                    .commit();
        }
    }
}
