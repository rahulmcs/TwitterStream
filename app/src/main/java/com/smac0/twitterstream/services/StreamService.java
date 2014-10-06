
package com.smac0.twitterstream.services;

import com.smac0.twitterstream.BuildConfig;
import com.smac0.twitterstream.application.AppPreferences;
import com.smac0.twitterstream.model.StreamStatus;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * Background service used to connect to the twitter streaming api using
 * twitter4j. The service streams status tweets for a tracked keyword and
 * broadcasts them using the {@link StreamService#BROADCAST_ACTION_STATUS}.
 * Clients can register for this broadcast and consume the status tweets when
 * they are available. The twitter api connection is terminated when the service
 * is destroyed. It is expected for clients to bind and unbind with this service
 * without having to explicitly start the service using Service#startService.
 * 
 * @author rahulchaudhari
 */
public class StreamService extends Service implements StatusListener {

    // Defines a custom Intent action
    public static final String BROADCAST_ACTION_STATUS = StreamService.class.getName()
            + ".BROADCAST_ACTION_STATUS";

    // Defines the key for the status "extra" in the Intent
    public static final String KEY_STATUS = StreamService.class.getName() + ".KEY_STATUS";

    private static final String TAG = StreamService.class.getName();

    // Binder given to clients
    private final IBinder mBinder = new StreamServiceBinder();

    private Object mTwitterStreamLock = new Object();

    private TwitterStream mTwitterStream;

    private boolean mTwitterStreamingStarted;

    private String mTrackKeyword;

    public StreamService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG)
            Log.d(TAG, "onDestroy");

        // create a new thread to destroy the stream which can sometimes be a
        // blocking
        // operation - we do not want to block the UI thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (mTwitterStreamLock) {
                    destroyTwitterStream();
                }
            }
        }).start();
    }

    public void start(String trackKeyword) {

        if (TextUtils.isEmpty(trackKeyword) || trackKeyword.equals(mTrackKeyword)) {
            return;
        }

        mTrackKeyword = trackKeyword;
        mTwitterStreamingStarted = false;

        if (BuildConfig.DEBUG)
            Log.d(TAG, "start");

        // start a new thread to create the stream since that can be a blocking
        // call and we do not want to block
        // the main thread
        // TODO handle the case where start() gets called while an instance of
        // the thread is
        // already executing however
        // at the moment this use case is not present in the app.
        new Thread(new Runnable() {
            @Override
            public void run() {

                synchronized (mTwitterStreamLock) {
                    // destroy any previous stream
                    destroyTwitterStream();

                    // create a new stream instance
                    createTwitterStream();

                    // filter() method internally creates a thread which
                    // manipulates TwitterStream and calls these adequate
                    // listener
                    // methods continuously.
                    String trackArray[] = new String[] {
                        mTrackKeyword
                    };
                    mTwitterStream.filter(new FilterQuery().track(trackArray));
                    mTwitterStreamingStarted = true;
                }
            }
        }).start();
    }

    /**
     * Temporarily Pause the streaming.Should be used for disabling the stream
     * status broadcasts
     */
    public void pause() {
        mTwitterStreamingStarted = false;
    }

    /**
     * Resume the status broadcasts
     */
    public void resume() {
        if (mTwitterStream != null && !mTwitterStreamingStarted) {
            mTwitterStreamingStarted = true;
        }
    }

    private void createTwitterStream() {
        if (BuildConfig.DEBUG)
            Log.d(TAG, "createTwitterStream");
        mTwitterStream = new TwitterStreamFactory().getInstance();
        mTwitterStream.setOAuthConsumer(AppPreferences.KEY_OAUTH_CONSUMER,
                AppPreferences.KEY_OAUTH_CONSUMER_SECRET);
        AccessToken accessToken = AppPreferences.getOAuthAccessToken(getApplicationContext());
        mTwitterStream.setOAuthAccessToken(accessToken);
        mTwitterStream.addListener(this);
        mTwitterStreamingStarted = false;
    }

    private void destroyTwitterStream() {
        if (mTwitterStream != null) {
            if (BuildConfig.DEBUG)
                Log.d(TAG, "destroyTwitterStream started");
            mTwitterStreamingStarted = false;
            mTwitterStream.clearListeners();
            mTwitterStream.cleanUp(); // can potentially block - avoid on main
                                      // thread
            mTwitterStream.shutdown(); // can potentially block - avoid on main
                                       // thread
            mTwitterStream = null;
            if (BuildConfig.DEBUG)
                Log.d(TAG, "destroyTwitterStream complete");

        }
    }

    // Twitter Stream Status listener callbacks
    @Override
    public void onStatus(Status status) {
        // Log.d(TAG, status.getUser().getName() + " " + status.getText());
        if (mTwitterStreamingStarted) {
            StreamStatus streamStatus = new StreamStatus(status.getUser().getScreenName(),
                    status.getText());

            Intent localIntent = new Intent(BROADCAST_ACTION_STATUS).putExtra(KEY_STATUS,
                    streamStatus);
            // Broadcasts the Intent to receivers in this app.
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        }
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, "onDeletionNotice : " + statusDeletionNotice.getStatusId());
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, "onTrackLimitationNotice : " + numberOfLimitedStatuses);
    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
    }

    @Override
    public void onStallWarning(final StallWarning warning) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, "onStallWarning : " + warning.getMessage());
    }

    @Override
    public void onException(Exception ex) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, "onException: " + ex.getMessage());
    }

    /**
     * Class used for the client Binder.
     */
    public class StreamServiceBinder extends Binder {
        public StreamService getService() {
            // Return this instance of StreamService so clients can call public
            // methods
            return StreamService.this;
        }
    }
}
