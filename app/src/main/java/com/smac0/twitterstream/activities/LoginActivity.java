
package com.smac0.twitterstream.activities;

import com.smac0.twitterstream.application.AppPreferences;
import com.smac0.twitterstream.fragments.LoginFragment;

import android.os.Bundle;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppPreferences.isAuthorized(this)) {
            StreamSearchActivity.startActivity(this);
            finish();
        } else {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, LoginFragment.newInstance(), LoginFragment.TAG)
                        .commit();
            }
        }
    }

}
