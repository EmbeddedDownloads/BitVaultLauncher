package com.bitvault.launcher.Control;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bitvault.launcher.HomeViewFragment;
import com.bitvault.launcher.R;
import com.bitvault.launcher.Views.HideAppsFragment;
import com.bitvault.launcher.Views.LauncherHomeFragment;


/**
 * Manage Left Side Sliding Menu
 *
 * @author Anshuman
 */
public class LeftSlidingMenuControl implements OnClickListener {
    /**
     * Instance of Activity
     */
    private static Activity mActivity;
    /**
     * Manage Menu Slider Text view object
     */
    private TextView hideAppsMenu;
    public static TextView homeMenu;
    /**
     * Instance of this class
     */
    public static LeftSlidingMenuControl mLeftSlidingMenuControl;
    /**
     * Currently Selected TAG
     */
    public static int currentSelectedTAG = 0;

    /**
     * Debugging TAG
     */
    @SuppressWarnings("unused")
    private String TAG = LeftSlidingMenuControl.class.getSimpleName();

    /**
     * Left Side Sliding Menu Control constructor
     */
    public LeftSlidingMenuControl(Activity mAct) {
        mActivity = mAct;
        mLeftSlidingMenuControl = this;
        initSlidingView();
        assignClicks();
        assignTags();
    }


    /**
     * Manage Sliding View object
     */
    private void initSlidingView() {
        homeMenu = (TextView) mActivity
                .findViewById(R.id.homeMenu);
        hideAppsMenu = (TextView) mActivity
                .findViewById(R.id.hideAppsMenu);


    }

    /**
     * Assign click to View objects
     */
    private void assignClicks() {
        homeMenu.setOnClickListener(this);
        hideAppsMenu.setOnClickListener(this);
    }

    /**
     * Assign TAG on GUI
     */
    private void assignTags() {
        currentSelectedTAG = homeMenu.getId();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.homeMenu:
                HomeViewFragment.getInstance().closeSlideMenu();
                if (currentSelectedTAG != homeMenu.getId()) {
                    currentSelectedTAG = homeMenu.getId();
                    HomeViewFragment.getInstance().removeAllOldFragment();
                    HomeViewFragment.getInstance().pushFragment(new LauncherHomeFragment());
                }
                break;

            case R.id.hideAppsMenu:
                HomeViewFragment.getInstance().closeSlideMenu();
                if (currentSelectedTAG != hideAppsMenu.getId()) {
                    currentSelectedTAG = hideAppsMenu.getId();
                    HomeViewFragment.getInstance().removeAllOldFragment();
                    HomeViewFragment.getInstance().pushFragment(new HideAppsFragment());
                }
                break;

            default:
                break;
        }

    }


}
