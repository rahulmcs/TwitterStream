
package com.smac0.twitterstream.activities;

import com.smac0.twitterstream.R;
import com.smac0.twitterstream.utils.NetworkUtils;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * Activity providing keyword search interface for twitter streams.
 * 
 * @author rahulchaudhari
 */

public class StreamSearchActivity extends BaseActivity {

    public static void startActivity(Activity parent) {
        parent.startActivity(new Intent(parent, StreamSearchActivity.class));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_stream);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu_twitter_stream, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)MenuItemCompat
                .getActionView(menu.findItem(R.id.search));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            if (!NetworkUtils.isNetworkAvailable(this)) {
                NetworkUtils.showNoNetworkToast(this);
            } else {
                String query = intent.getStringExtra(SearchManager.QUERY);
                StreamListActivity.startActivity(this, query);
            }

        }
    }
}
