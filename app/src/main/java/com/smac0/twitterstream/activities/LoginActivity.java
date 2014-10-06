
package com.smac0.twitterstream.activities;

import com.smac0.twitterstream.application.AppPreferences;
import com.smac0.twitterstream.fragments.LoginFragment;

import android.os.Bundle;

/**
 * Initial activity providing functionality to allow user to login with his
 * twitter account. The activity in this case is a holder for the LoginFragment
 * which implements the actual functionality.
 *
 * @author rahulchaudhari
 */
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
