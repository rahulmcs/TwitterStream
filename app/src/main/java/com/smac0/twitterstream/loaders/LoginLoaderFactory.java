
package com.smac0.twitterstream.loaders;

import com.smac0.twitterstream.application.AppPreferences;
import com.smac0.twitterstream.model.RequestOAuthUrlResponse;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import android.content.Context;
import android.util.Log;

/**
 * Factory providing implementation of the loaders for twitter api asynchronus
 * network communication
 * 
 * @author rahulchaudhari
 */
public class LoginLoaderFactory {
    private static final String TAG = LoginLoaderFactory.class.getName();

    public static int LOADER_ID_REQUEST_OAUTH_URL = 1000;

    public static int LOADER_ID_REQUEST_ACCESS_TOKEN = 2000;

    /**
     * Creates a loader to asynchronously fetch the OAuthUrl and request token.
     * 
     * @param context
     * @return BaseAsyncTaskLoader<RequestOAuthUrlResponse>
     */
    public static BaseAsyncTaskLoader<RequestOAuthUrlResponse> requestOAuthUrl(final Context context) {
        return new BaseAsyncTaskLoader<RequestOAuthUrlResponse>(context) {

            @Override
            public RequestOAuthUrlResponse loadInBackground() {
                String oAuthUrl;
                RequestOAuthUrlResponse requestOAuthUrlResponse = null;
                Twitter twitter = TwitterFactory.getSingleton();
                try {
                    RequestToken requestToken = twitter.getOAuthRequestToken();
                    oAuthUrl = requestToken.getAuthorizationURL();
                    requestOAuthUrlResponse = new RequestOAuthUrlResponse(requestToken, oAuthUrl);
                } catch (TwitterException e) {
                    Log.d(TAG, "requestOAuthUrl : " + e.getMessage());
                }
                return requestOAuthUrlResponse;
            }
        };
    }

    /**
     * Create a loader to asynchronously fetch the oAuth access token
     * 
     * @param context
     * @param requestToken
     * @param oAuthPin
     * @return BaseAsyncTaskLoader<Boolean>
     */
    public static BaseAsyncTaskLoader<Boolean> requestAccessToken(final Context context,
            final RequestToken requestToken, final String oAuthPin) {
        return new BaseAsyncTaskLoader<Boolean>(context) {

            @Override
            public Boolean loadInBackground() {
                AccessToken accessToken = null;
                boolean authorized = false;
                Twitter twitter = TwitterFactory.getSingleton();
                try {
                    accessToken = twitter.getOAuthAccessToken(requestToken, oAuthPin);
                    AppPreferences.saveOAuthAccessToken(getContext(), accessToken);
                    authorized = true;
                } catch (TwitterException e) {
                    Log.d(TAG, "requestAccessToken : " + e.getMessage());
                }
                return authorized;
            }
        };
    }

}
