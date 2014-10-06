
package com.smac0.twitterstream.fragments;

import com.smac0.twitterstream.BuildConfig;
import com.smac0.twitterstream.R;
import com.smac0.twitterstream.adapters.StreamListAdapter;
import com.smac0.twitterstream.model.StreamStatus;
import com.smac0.twitterstream.services.StreamService;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * A fragment representing a list of latest
 * {@link StreamListFragment#MAX_LATEST_TWEETS} tweets as streamed from the
 * twitter api.
 *
 * @author rahulchaudhari
 */
public class StreamListFragment extends ListFragment {

    public static final String TAG = StreamListFragment.class.getName();

    public static final String ARG_TRACK_KEYWORD = "arg_track_keyword";

    private static final int MAX_LATEST_TWEETS = 10;

    // The filter's action is BROADCAST_ACTION_STATUS
    IntentFilter mStatusIntentFilter = new IntentFilter(StreamService.BROADCAST_ACTION_STATUS);

    private String mTrackKeyword;

    private StreamListAdapter mLatestTweetsListAdapter;

    private boolean mBound;

    private StreamService mService;

    private BroadcastReceiver mStreamStatusBroacastReceiver = new StreamStatusBroadcastReceiver();

    private ServiceConnection mStreamServiceConnection = new StreamServiceConnection();;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StreamListFragment() {
    }

    public static StreamListFragment newInstance(String trackKeyword) {
        StreamListFragment fragment = new StreamListFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_TRACK_KEYWORD, trackKeyword);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG)
            Log.d(TAG, "onCreate");

        mTrackKeyword = getArguments().getString(ARG_TRACK_KEYWORD);

        mLatestTweetsListAdapter = new StreamListAdapter(MAX_LATEST_TWEETS);
        setListAdapter(mLatestTweetsListAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (BuildConfig.DEBUG)
            Log.d(TAG, "onActivityCreated");

        setEmptyText(getText(R.string.twitter_stream_empty_text));
        setListShownNoAnimation(false);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (BuildConfig.DEBUG)
            Log.d(TAG, "onStart");

        // Bind to Stream
        Intent intent = new Intent(getActivity(), StreamService.class);
        getActivity().bindService(intent, mStreamServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (BuildConfig.DEBUG)
            Log.d(TAG, "onStop");

        // Unbind from the service
        if (mBound) {
            getActivity().unbindService(mStreamServiceConnection);
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
                    mStreamStatusBroacastReceiver);
            mBound = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (BuildConfig.DEBUG)
            Log.d(TAG, "onPause");

        if (mService != null && mBound) {
            mService.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (BuildConfig.DEBUG)
            Log.d(TAG, "onResume");

        if (mService != null && mBound) {
            mService.resume();
        }
    }

    private boolean canUpdateList() {
        return getActivity() != null && !getActivity().isFinishing()
                && mLatestTweetsListAdapter != null;
    }

    /**
     * Status stream broadcast receiver
     */
    private class StreamStatusBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (canUpdateList()) {
                // run on UI thread to update the list adapter
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (canUpdateList()) {
                            setListShown(true);
                            mLatestTweetsListAdapter.addLatest((StreamStatus) intent
                                    .getParcelableExtra(StreamService.KEY_STATUS));
                        }
                    }
                });
            }
        }
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private class StreamServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            if (BuildConfig.DEBUG)
                Log.d(TAG, "onServiceConnected");

            // We've bound to StreamService, cast the IBinder and get
            // LocalService instance
            StreamService.StreamServiceBinder binder = (StreamService.StreamServiceBinder)service;
            mService = binder.getService();
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                    mStreamStatusBroacastReceiver, mStatusIntentFilter);
            mService.start(mTrackKeyword);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            if (BuildConfig.DEBUG)
                Log.d(TAG, "onServiceDisconnected");
            mBound = false;
        }
    };

}
