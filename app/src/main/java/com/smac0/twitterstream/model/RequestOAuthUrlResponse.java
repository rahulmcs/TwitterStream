
package com.smac0.twitterstream.model;

import twitter4j.auth.RequestToken;

/**
 * Created by rahulchaudhari on 04/10/2014.
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
