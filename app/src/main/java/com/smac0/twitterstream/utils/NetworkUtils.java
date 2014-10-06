
package com.smac0.twitterstream.utils;

import com.smac0.twitterstream.R;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * This helper class provides network related utility methods
 *
 * @author rahulchaudhari
 */
public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getName();

    /**
     * Determine if there is an active network connection
     *
     * @param context Context
     * @return true if network available, false otherwise
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // check if network available
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static void showNoNetworkToast(Context context) {
        Toast.makeText(context, R.string.twitter_no_network_available, Toast.LENGTH_SHORT).show();
    }
}
