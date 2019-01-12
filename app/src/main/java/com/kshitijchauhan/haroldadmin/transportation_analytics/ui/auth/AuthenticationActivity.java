package com.kshitijchauhan.haroldadmin.transportation_analytics.ui.auth;

import android.os.Bundle;

import com.kshitijchauhan.haroldadmin.transportation_analytics.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class AuthenticationActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.auth_fragment_container, LoginFragment.newInstance())
                    .commit();
        }

        authViewModel.state.observe(this, newState -> {
            switch (newState) {
                case LOGIN_STATE:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.auth_fragment_container, LoginFragment.newInstance())
                            .addToBackStack(null)
                            .commit();
                    break;
                case REGISTER_STATE:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.auth_fragment_container, RegisterFragment.newInstance())
                            .addToBackStack(null)
                            .commit();
                    break;
                default:
                    throw new IllegalStateException("Unable to update to state " +
                            newState.name() + " from AuthenticationActivity");
            }
        });
    }
}
