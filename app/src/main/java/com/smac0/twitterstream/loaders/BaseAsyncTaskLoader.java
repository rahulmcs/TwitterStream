
package com.smac0.twitterstream.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Class providing a way to perform an asynchronous request on a background
 * thread. Subclasses need to implement the {@link #loadInBackground()} method.
 */
public abstract class BaseAsyncTaskLoader<D> extends AsyncTaskLoader<D> {

    /**
     * Optional parameters passed to the network request
     */
    protected Bundle mArgs;

    private D mData;

    public BaseAsyncTaskLoader(Context context, Bundle args) {
        super(context);
        mArgs = args;
    }

    public BaseAsyncTaskLoader(Context context) {
        this(context, null);
    }

    @Override
    public void deliverResult(D data) {
        mData = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (mData != null) {
            deliverResult(mData);
        } else {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
        super.onStopLoading();
    }

    @Override
    protected void onReset() {
        cancelLoad();
        mData = null;
        super.onReset();
    }
}
