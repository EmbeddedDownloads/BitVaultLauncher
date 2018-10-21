package com.bitvault.launcher.WebService.control;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bitvault.launcher.R;
import com.bitvault.launcher.Utils.AndroidAppUtils;

/**
 * Webservice API Error Handler
 *
 * @author Shruti
 */
public class WebserviceAPIErrorHandler {
    /**
     * Instance of This class
     */
    public static WebserviceAPIErrorHandler mErrorHandler;
    /**
     * Debugging TAG
     */
    private String TAG = WebserviceAPIErrorHandler.class.getSimpleName();

    private WebserviceAPIErrorHandler() {
    }

    /**
     * Get Instance of this class
     *
     * @return
     */
    public static WebserviceAPIErrorHandler getInstance() {
        if (mErrorHandler == null)
            mErrorHandler = new WebserviceAPIErrorHandler();
        return mErrorHandler;

    }

    /**
     * Volley Error Handler
     *
     * @param mError
     */
    public void VolleyErrorHandler(VolleyError mError, Activity mActivity) {
        AndroidAppUtils.showErrorLog(TAG, "VolleyError :" + mError);
        if (mError instanceof NoConnectionError) {
            AndroidAppUtils.showToast(mActivity, mActivity.getResources()
                    .getString(R.string.network_error));
        } else if (mError instanceof TimeoutError) {
            AndroidAppUtils.showToast(mActivity, mActivity.getResources()
                    .getString(R.string.network_slow_error));
            // TODO
        } else if (mError instanceof AuthFailureError) {
            // TODO
        } else if (mError instanceof ServerError) {
            AndroidAppUtils.showToast(mActivity, mActivity.getResources()
                    .getString(R.string.server_error));
            // TODO
        } else if (mError instanceof NetworkError) {
            AndroidAppUtils.showToast(mActivity, mActivity.getResources()
                    .getString(R.string.network_error));
            // TODO
        } else if (mError instanceof ParseError) {
            // TODO
        }
    }


}
