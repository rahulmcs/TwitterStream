
package com.smac0.twitterstream.activities;

import com.smac0.twitterstream.R;
import com.smac0.twitterstream.fragments.OAuthWebViewFragment;

import android.os.Bundle;

/**
 * Fragment providing user to login using his twitter account via oAuth
 * authorization on the web interface. The activity in this case is a holder for
 * the OAuthWebViewFragment which implements the actual functionality.
 *
 * @author rahulchaudhari
 */
public class OAuthWebViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content,
                            OAuthWebViewFragment.newInstance(getIntent().getStringExtra(
                                    OAuthWebViewFragment.ARG_OAUTH_URL)), OAuthWebViewFragment.TAG)
                    .commit();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.twitter_oauth_signin_title);
    }
}
