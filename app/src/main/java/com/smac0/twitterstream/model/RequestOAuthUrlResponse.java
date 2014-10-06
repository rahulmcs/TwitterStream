
package com.smac0.twitterstream.model;

import twitter4j.auth.RequestToken;

/**
 * OAUthUrl response holder object received from Twitter.
 * 
 * @author rahulchaudhari
 */
public class RequestOAuthUrlResponse {
    private RequestToken mRequestToken;

    private String mOAuthUrl;

    public RequestOAuthUrlResponse(RequestToken requestToken, String oAuthUrl) {
        mRequestToken = requestToken;
        mOAuthUrl = oAuthUrl;
    }

    public RequestToken getRequestToken() {
        return mRequestToken;
    }

    public void setRequestToken(RequestToken requestToken) {
        mRequestToken = mRequestToken;
    }

    public String getOAuthUrl() {
        return mOAuthUrl;
    }

    public void setOAuthUrl(String mOAuthUrl) {
        mOAuthUrl = mOAuthUrl;
    }

}
