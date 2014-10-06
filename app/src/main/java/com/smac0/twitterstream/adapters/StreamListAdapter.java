
package com.smac0.twitterstream.adapters;

import com.smac0.twitterstream.R;
import com.smac0.twitterstream.model.StreamStatus;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Adapter to hold the latest tweets with a maximum capacity as specified by
 * {@link StreamListAdapter#mLatestTweetsSize}.Oldest entries are discarded to
 * make room for new ones.
 * 
 * @author rahulchaudhari
 */
public class StreamListAdapter extends BaseAdapter {

    private final int mLatestTweetsSize;

    private LinkedList<StreamStatus> mLatestTweetsList;

    public StreamListAdapter(int latestTweetsSize) {
        mLatestTweetsList = new LinkedList<StreamStatus>();
        mLatestTweetsSize = latestTweetsSize;
    }

    /**
     * Appends the latest tweet to the list. If the maximum capacity has reached
     * the oldest tweet will be deleted to make room for the new one
     * 
     * @note This method should always be called on the UI thread
     * @param status The latest tweet to be added
     */
    public synchronized void addLatest(final StreamStatus status) {
        if (getCount() == mLatestTweetsSize) {
            // remove the oldest status to make room for the new one
            mLatestTweetsList.remove(0);
        }
        mLatestTweetsList.addLast(status);

        // refresh list
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mLatestTweetsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mLatestTweetsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_list_twitter_stream,
                    null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        // set the twitter user name and status
        StreamStatus twitterStatus = mLatestTweetsList.get(position);
        viewHolder.mTwitterUserIdView.setText("@" + twitterStatus.getUserName());
        viewHolder.mTwitterUserStatusView.setText(twitterStatus.getStatus());
        return convertView;
    }

    private static class ViewHolder {
        private TextView mTwitterUserIdView;

        private TextView mTwitterUserStatusView;

        ViewHolder(View rowLayout) {
            mTwitterUserIdView = (TextView)rowLayout.findViewById(R.id.twitter_user_id);
            mTwitterUserStatusView = (TextView)rowLayout.findViewById(R.id.twitter_user_status);
        }
    }
}
