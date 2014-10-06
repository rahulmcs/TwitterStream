
package com.smac0.twitterstream.fragments;

import com.smac0.twitterstream.R;
import com.smac0.twitterstream.activities.OAuthWebViewActivity;
import com.smac0.twitterstream.activities.StreamSearchActivity;
import com.smac0.twitterstream.loaders.LoginLoaderFactory;
import com.smac0.twitterstream.model.RequestOAuthUrlResponse;
import com.smac0.twitterstream.utils.NetworkUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Fragment providing functionality to allow user to login with his twitter
 * account.
 * 
 * @author rahulchaudhari
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = LoginFragment.class.getName();

    private static int OAUTH_PIN_REQUEST_CODE = 100;

    private RequestOAuthUrlResponse mRequestOAuthUrlResponse;

    private Button mLoginButton;

    private ProgressBar mProgressBar;

    private String mOAuthPin;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_twitter_login, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == mLoginButton.getId()) {
            // There are steps to login using twitter and authorize user:
            // 1. Fetch the request token and authorisation url
            // 2. Take the user to the authorization url using a webview and
            // allow the user to login and authorize.
            // 3. Use the returned authorisation token (pin) to fetch the access
            // token and access secret which should also complete the
            // authorisation
            if (!NetworkUtils.isNetworkAvailable(getActivity())) {
                NetworkUtils.showNoNetworkToast(getActivity());
            } else {
                getLoaderManager().initLoader(LoginLoaderFactory.LOADER_ID_REQUEST_OAUTH_URL, null,
                        new RequestOAuthUrlLoader());
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OAUTH_PIN_REQUEST_CODE) {
            if (resultCode == OAuthWebViewFragment.RESULT_SUCCESS) {
                // now use the oauth pin to request the access token and secret
                // to complete authorization
                mOAuthPin = data.getStringExtra(OAuthWebViewFragment.ARG_OAUTH_PIN);
                if (!NetworkUtils.isNetworkAvailable(getActivity())) {
                    NetworkUtils.showNoNetworkToast(getActivity());
                } else {
                    getLoaderManager().initLoader(
                            LoginLoaderFactory.LOADER_ID_REQUEST_ACCESS_TOKEN, null,
                            new RequestOAuthAccessTokenLoader());
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.twitter_login_unsuccessful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView(View view) {
        mLoginButton = (Button)view.findViewById(R.id.twitter_login_button);
        mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        mLoginButton.setOnClickListener(this);
    }

    /**
     * Loader callback implementation for requesting OAuth Url
     */
    private class RequestOAuthUrlLoader implements
            LoaderManager.LoaderCallbacks<RequestOAuthUrlResponse> {
        @Override
        public Loader<RequestOAuthUrlResponse> onCreateLoader(int id, Bundle args) {
            mProgressBar.setVisibility(View.VISIBLE);
            return LoginLoaderFactory.requestOAuthUrl(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<RequestOAuthUrlResponse> loader,
                RequestOAuthUrlResponse requestOAuthUrlResponse) {
            mProgressBar.setVisibility(View.GONE);

            if (requestOAuthUrlResponse != null) {
                mRequestOAuthUrlResponse = requestOAuthUrlResponse;
                // redirect user to login using twitter
                startActivityForResult(
                        new Intent(getActivity(), OAuthWebViewActivity.class).putExtra(
                                OAuthWebViewFragment.ARG_OAUTH_URL,
                                requestOAuthUrlResponse.getOAuthUrl()), OAUTH_PIN_REQUEST_CODE);
            } else {
                Toast.makeText(getActivity(), getString(R.string.twitter_login_unsuccessful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onLoaderReset(Loader<RequestOAuthUrlResponse> loader) {
        }
    }

    /**
     * Loader callback implementation for requesting access token
     */
    private class RequestOAuthAccessTokenLoader implements LoaderManager.LoaderCallbacks<Boolean> {

        @Override
        public Loader<Boolean> onCreateLoader(int i, Bundle bundle) {
            mProgressBar.setVisibility(View.VISIBLE);
            return LoginLoaderFactory.requestAccessToken(getActivity(),
                    mRequestOAuthUrlResponse.getRequestToken(), mOAuthPin);
        }

        @Override
        public void onLoadFinished(Loader<Boolean> accessTokenLoader, Boolean authorized) {
            mProgressBar.setVisibility(View.GONE);
            if (authorized) {
                StreamSearchActivity.startActivity(getActivity());
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), getString(R.string.twitter_login_unsuccessful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onLoaderReset(Loader<Boolean> accessTokenLoader) {
        }
    }

}
