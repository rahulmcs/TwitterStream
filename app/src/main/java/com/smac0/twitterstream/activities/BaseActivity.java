
package com.smac0.twitterstream.activities;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

/**
 * Base activity containing some default behaviour to be shared across
 * activities.Activities can derive from this class to benefit from common
 * behavior.
 * 
 * @author rahulchaudhari
 */
public class BaseActivity extends ActionBarActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
