
package com.smac0.twitterstream.application;

import twitter4j.TwitterFactory;

import android.app.Application;

/**
 * @author rahulchaudhari
 */
public class TwitterStreamApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // set the oAuthConsumer details in the singleton twitterfactory
        // instance so that it can be used in different modules in the app
        TwitterFactory.getSingleton().setOAuthConsumer(AppPreferences.KEY_OAUTH_CONSUMER,
                AppPreferences.KEY_OAUTH_CONSUMER_SECRET);
    }

}
