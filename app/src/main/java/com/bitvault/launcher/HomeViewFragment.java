package com.bitvault.launcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;

import com.bitvault.launcher.Control.LeftSlidingMenuControl;
import com.bitvault.launcher.Utils.AndroidAppUtils;
import com.bitvault.launcher.Views.LauncherHomeFragment;
import com.bitvault.launcher.database.CheckDatabase;

import java.util.Stack;

/**
 * Created by Anshuman Sharma
 */
public class HomeViewFragment extends FragmentActivity {
    /**
     * Activity Instance
     */
    private Activity mActivity;

    /**
     * Debugging TAG
     */
    private String TAG = HomeViewFragment.class.getSimpleName();
    /**
     * Fragment showing and opening Stack
     */
    private Stack<Fragment> mFragmentStack = new Stack<Fragment>();
    private DrawerLayout mSlideMenu;

    /**
     * Instance of this class
     */
    public static HomeViewFragment mHomeViewFragment;

    /**
     * ON Create of this class
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        initViews();
        new CheckDatabase(this);
        new LeftSlidingMenuControl(this);
        NavigationDrawnListner();
        pushFragment(new LauncherHomeFragment());
    }

    /**
     * Instance of this class
     *
     * @return
     */
    public static HomeViewFragment getInstance() {

        return mHomeViewFragment;
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (mFragmentStack != null && mFragmentStack.size() > 0) {
            mFragmentStack.pop();
        }
    }

    /**
     * Init Views
     */
    private void initViews() {
        mActivity = this;
        mHomeViewFragment = HomeViewFragment.this;
        mFragmentStack = new Stack<Fragment>();

        mSlideMenu = (DrawerLayout) findViewById(R.id.drawer_layout);

        mSlideMenu.setFocusableInTouchMode(false);
    }

    private void NavigationDrawnListner() {
        mSlideMenu.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }


    /**
     * Push Fragment into fragment Stack and show top fragment to GUI
     */
    public void pushFragment(Fragment mFragment) {
        try {
            if (mFragment != null) {
                // Begin the transaction
                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction();
                // add fragment on frame-layout
                transaction.add(R.id.SliderMainLayout, mFragment);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commitAllowingStateLoss();
                // add fragment into flow stack
                mFragmentStack.add(mFragment);
            } else
                AndroidAppUtils.showErrorLog(TAG, "Push Fragment is null.");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    /**
     * Pop Fragment from fragment stack and remove latest one and show Pervious
     * attached fragment
     */
    public void popFragment(Fragment mFragment) {
        try {
            if (mFragment != null) {
                // Begin the transaction
                FragmentTransaction ft = getSupportFragmentManager()
                        .beginTransaction();
                // remove fragment from view
                if (mFragment != null) {
                    ft.remove(mFragment);
                    ft.detach(mFragment);
                }
                ft.commit();
                getSupportFragmentManager().popBackStack();
                mFragmentStack.remove(mFragmentStack.lastElement());
            } else
                AndroidAppUtils.showErrorLog(TAG, "Pop fragment is null.");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    /**
     * Remove all attached old fragments
     */
    public void removeAllOldFragment() {
        try {
            for (int i = 0; i < mFragmentStack.size(); i++) {
                popFragment(mFragmentStack.get(i));
            }
            mFragmentStack.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Close Slide Menu
     */
    public void closeSlideMenu() {
        if (mSlideMenu != null) {
            mSlideMenu.closeDrawers();
        } else {
            AndroidAppUtils.showErrorLog(TAG, "mSlideMenu is null");
        }

    }

    /**
     * Open Slide Menu
     */
    public void openSliderMenu() {
        if (mSlideMenu != null) {
            mSlideMenu.openDrawer(Gravity.LEFT);
        } else {
            AndroidAppUtils.showErrorLog(TAG, "mSlideMenu is null");
        }

    }


    /**
     * Start Left Menu
     */
    private void startLeftSlidingMenu() {
        try {
            mSlideMenu.setDrawerLockMode(mSlideMenu.LOCK_MODE_UNLOCKED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop Left Menu
     */
    private void StopLeftSlidingMenu() {
        try {
            mSlideMenu.setDrawerLockMode(mSlideMenu.LOCK_MODE_LOCKED_CLOSED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
