package com.bitvault.launcher.Control;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import com.bitvault.launcher.R;
import com.bitvault.launcher.Utils.AndroidAppUtils;
import com.bitvault.launcher.Utils.GlobalConfig;

/**
 * Created by Anshuman on 7/6/2017.
 */

public class WelcomeBitVaultViewControl {
    /**
     * Relative layout
     */
    private RelativeLayout loadingBitvault;
    /**
     * Debugging TAG
     */
    private String TAG = WelcomeBitVaultViewControl.class.getSimpleName();

    /**
     * Contructor of this class
     *
     * @param mActivity
     */

    public WelcomeBitVaultViewControl(Activity mActivity, View mView) {
        if (mActivity != null)
            if (mView == null)
                loadingBitvault = (RelativeLayout) mActivity.findViewById(R.id.loadingBitvault);
            else loadingBitvault = (RelativeLayout) mView.findViewById(R.id.loadingBitvault);
        else AndroidAppUtils.showErrorLog(TAG, "mActivity is null.");
        if (GlobalConfig.isNeedToShowLoading)
            ShowLoadingImage();
        else {
            loadingBitvault.setVisibility(View.GONE);
            GlobalConfig.isNeedToShowLoading = true;
            AndroidAppUtils.showErrorLog(TAG, "Loading Not required.");
        }
    }

    /**
     * Show loading image
     */
    private void ShowLoadingImage() {
        if (loadingBitvault != null) {
            loadingBitvault.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingBitvault.setVisibility(View.GONE);
                }
            }, 3000);
        } else AndroidAppUtils.showErrorLog(TAG, "loadingBitvault is null.");
    }

}
