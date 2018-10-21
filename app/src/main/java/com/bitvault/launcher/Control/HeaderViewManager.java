package com.bitvault.launcher.Control;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitvault.launcher.R;
import com.bitvault.launcher.Utils.AndroidAppUtils;
import com.bitvault.launcher.iHelper.HeaderViewClickListener;


/****************************************************************************
 * Class Name - HeaderViewManager
 * Description - This class is used to manage the header view of
 * every activity. Basically this is the controller class which will
 * be binded to every class which will be containing header.
 *****************************************************************************/
public class HeaderViewManager {

    /**
     * Instance of this class
     */
    public static HeaderViewManager mHeaderManagerInstance;
    /**
     * Debugging TAG
     */
    private String TAG = HeaderViewManager.class.getSimpleName();

    /**
     * Header View Instance
     */
    private RelativeLayout headerLeftView;
    private TextView headerHeadingText;
    private ImageView headerLeftImage;
    private Activity mActivity;

    /**
     * Instance of Header View Manager
     *
     * @return
     */
    public static HeaderViewManager getInstance() {
        if (mHeaderManagerInstance == null) {
            mHeaderManagerInstance = new HeaderViewManager();
        }

        return mHeaderManagerInstance;
    }

    /**
     * Initialize Header View
     *
     * @param mActivity
     * @param mView
     * @param headerViewClickListener
     * @param activity
     */
    public void InitializeHeaderView(Activity activity, View mView,
                                     HeaderViewClickListener headerViewClickListener, Activity mActivity) {
        this.mActivity = mActivity;
        if (activity != null) {
            headerLeftView = (RelativeLayout) activity
                    .findViewById(R.id.headerLeftView);
            headerHeadingText = (TextView) activity
                    .findViewById(R.id.header_text);
            headerLeftImage = (ImageView) activity
                    .findViewById(R.id.headerLeftImage);
        } else if (mView != null) {
            headerLeftView = (RelativeLayout) mView
                    .findViewById(R.id.headerLeftView);
            headerHeadingText = (TextView) mView
                    .findViewById(R.id.header_text);
            headerLeftImage = (ImageView) mView
                    .findViewById(R.id.headerLeftImage);
        }
        manageClickOnViews(headerViewClickListener);
    }


    /**
     * ManageClickOn Header view
     *
     * @param headerViewClickListener
     */
    private void manageClickOnViews(
            final HeaderViewClickListener headerViewClickListener) {
        // Click on Header Left View
        headerLeftView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                headerViewClickListener.onClickOfHeaderLeftView();
            }
        });
    }

    /**
     * Set Heading View Text
     *
     * @param isVisible
     * @param headingStr
     */
    public void setHeading(boolean isVisible, String headingStr) {
        if (headerHeadingText != null) {
            if (isVisible) {
                headerHeadingText.setVisibility(View.VISIBLE);
                headerHeadingText.setText(headingStr);
            } else {
                headerHeadingText.setVisibility(View.GONE);
            }
        } else {
            AndroidAppUtils.showErrorLog(TAG,
                    "Header Heading Text View is null");
        }
    }

    /**
     * Manage Header Left View
     *
     * @param isVisibleImage
     * @param ImageId
     * @param isMenuOption
     * @param isBackOption
     */
    public void setLeftSideHeaderView(boolean isVisibleImage, int ImageId, boolean isMenuOption, boolean isBackOption) {
        if (!isVisibleImage) {
            headerLeftView.setVisibility(View.INVISIBLE);
        } else if (headerLeftView == null || headerLeftImage == null) {
            AndroidAppUtils.showErrorLog(TAG, "Header Left View is null");
        } else if (isVisibleImage) {
            headerLeftView.setVisibility(View.VISIBLE);
            headerLeftImage.setVisibility(View.VISIBLE);
            if (ImageId > 0) {
                if (isMenuOption) {
                        /*Other Theme*/
                    headerLeftImage.setImageResource(R.drawable.menu_icon);
                } else if (isBackOption) {
                        /*Other Theme*/
                    headerLeftImage.setImageResource(R.drawable.back_icon);
                } else headerLeftImage.setImageResource(ImageId);
            } else {
                AndroidAppUtils.showErrorLog(TAG,
                        "Header left image id is null");
            }
        }
    }
}

