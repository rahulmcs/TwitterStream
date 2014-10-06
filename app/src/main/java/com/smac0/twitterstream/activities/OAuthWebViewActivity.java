
package com.smac0.twitterstream.activities;

import com.smac0.twitterstream.R;
import com.smac0.twitterstream.fragments.OAuthWebViewFragment;

import android.os.Bundle;

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
