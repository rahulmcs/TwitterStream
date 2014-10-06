
package com.smac0.twitterstream.activities;

import com.smac0.twitterstream.R;
import com.smac0.twitterstream.fragments.StreamListFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class StreamListActivity extends BaseActivity {

    public static void startActivity(final Activity parent, final String searchKeyword) {
        parent.startActivity(new Intent(parent, StreamListActivity.class).putExtra(
                StreamListFragment.ARG_TRACK_KEYWORD, searchKeyword));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String trackKeyword = getIntent().getStringExtra(StreamListFragment.ARG_TRACK_KEYWORD);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content,
                            StreamListFragment.newInstance(getIntent().getStringExtra(
                                    StreamListFragment.ARG_TRACK_KEYWORD)), StreamListFragment.TAG)
                    .commit();
        }
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.twitter_stream_list_title, trackKeyword));
    }

}
