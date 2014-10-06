
package com.smac0.twitterstream.application;

import twitter4j.auth.AccessToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Utility methods to save and retrieve various shared preferences.
 * 
 * @author rahulchaudhari
 */
public final class AppPreferences {

    /**
     * Name of the preferences file being used for general data storage in the
     * application.
     */
    public static final String PREFERENCES_FILENAME = "twitter_stream_preferences";

    public static final String KEY_OAUTH_ACCESS_TOKEN = "key_oauth_access_token";

    public static final String KEY_OAUTH_ACCESS_TOKEN_SECRET = "key_oauth_access_token_secret";

    public static final String KEY_OAUTH_IS_AUTHORIZED = "key_oauth_is_authorized";

    // Specific OAuth consumer key for this application as retrieved from
    // "dev.twitter.com"
    public static final String KEY_OAUTH_CONSUMER = "dsBMDYWHO7rLXAladOmUNmcrz";

    // Specific OAuth consumer key secretfor this application as retrieved from
    // "dev.twitter.com"
    public static final String KEY_OAUTH_CONSUMER_SECRET = "fqOPNWMKE2Vefsni8ralGzM6aXKAWdP7GpmSKxQP7xhpbmaKod";

    // Specific OAuth callback url associated with this application as retrieved
    // from
    // "dev.twitter.com"
    public static final String KEY_OAUTH_CALLBACK_URL = "http://www.smac0.com";

    private static final String TAG = "AppDataManager";

    private AppPreferences() {
    }

    /**
     * Stores the twitter OAuth access token: specifically the access token and
     * access token secret alongwith an indication that the user is now
     * authorized.
     * 
     * @param context context
     * @param accessToken Access token
     */
    public static void saveOAuthAccessToken(final Context context, final AccessToken accessToken) {
        if (!TextUtils.isEmpty(accessToken.getToken())
                && !TextUtils.isEmpty(accessToken.getTokenSecret())) {
            final SharedPreferences prefs = context.getSharedPreferences(
                    AppPreferences.PREFERENCES_FILENAME, Context.MODE_PRIVATE);
            prefs.edit()
                    .putString(AppPreferences.KEY_OAUTH_ACCESS_TOKEN, accessToken.getToken())
                    .putString(AppPreferences.KEY_OAUTH_ACCESS_TOKEN_SECRET,
                            accessToken.getTokenSecret()).putBoolean(KEY_OAUTH_IS_AUTHORIZED, true)
                    .apply();
        }
    }

    /**
     * Retrieves the access token contining just the accessToken and accessToken
     * secret.
     * 
     * @param context context
     * @return the accessToken if one exists, null otherwise
     */
    public static AccessToken getOAuthAccessToken(final Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILENAME,
                Context.MODE_PRIVATE);
        String accessToken = prefs.getString(KEY_OAUTH_ACCESS_TOKEN, "");
        String accessTokenSecret = prefs.getString(KEY_OAUTH_ACCESS_TOKEN_SECRET, "");
        if (TextUtils.isEmpty(accessToken) || TextUtils.isEmpty(accessTokenSecret)) {
            return null;
        }
        return new AccessToken(accessToken, accessTokenSecret);
    }

    /**
     * Check if the user is already authorized due to a previous successful
     * attempt
     * 
     * @param context
     * @return true if authorized, false otherwise
     */
    public static boolean isAuthorized(final Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILENAME,
                Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_OAUTH_IS_AUTHORIZED, false);
    }

}
