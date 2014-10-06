
package com.smac0.twitterstream.fragments;

import com.smac0.twitterstream.R;
import com.smac0.twitterstream.application.AppPreferences;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Fragment implementing functionality to allow user to login using his twitter
 * account via oAuth authorization on the web interface.
 *
 * @author rahulchaudhari
 */
public class OAuthWebViewFragment extends Fragment {

    public static final String TAG = OAuthWebViewFragment.class.getName();

    public static final int RESULT_SUCCESS = 1;

    public static final int RESULT_FAILURE = -1;

    public static final String ARG_OAUTH_URL = "oauth_url";

    public static final String ARG_OAUTH_PIN = "oauth_pin";

    private String mOAuthUrl;

    private WebView mWebView;

    private ProgressBar mProgressBar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OAuthWebViewFragment() {
    }

    public static OAuthWebViewFragment newInstance(String oAuthUrl) {
        OAuthWebViewFragment fragment = new OAuthWebViewFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_OAUTH_URL, oAuthUrl);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOAuthUrl = getArguments().getString(ARG_OAUTH_URL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_twitter_oauth_webview, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mOAuthUrl);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);

                if (shouldOverrideUrlLoading(view, url)) {
                    getActivity().finish();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "shouldOverrideUrlLoading");

                // intercept the urls and check for presence of oAuth_verifier
                String oAuthPin = "";
                if (url.startsWith(AppPreferences.KEY_OAUTH_CALLBACK_URL)) {
                    if (url.contains("oauth_verifier")) {
                        Uri uri = Uri.parse(url);
                        oAuthPin = uri.getQueryParameter("oauth_verifier");
                        getActivity().setResult(RESULT_SUCCESS,
                                new Intent().putExtra(ARG_OAUTH_PIN, oAuthPin));
                    } else if (url.contains("denied")) {
                        getActivity().setResult(RESULT_FAILURE);
                    }
                    getActivity().finish();
                    return true;
                }

                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient());

    }

    private void initView(View view) {
        mWebView = (WebView)view.findViewById(R.id.oauth_webView);
        mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar);
    }
}
