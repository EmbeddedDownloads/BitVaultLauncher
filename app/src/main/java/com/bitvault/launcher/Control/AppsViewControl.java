package com.bitvault.launcher.Control;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bitvault.launcher.Adapter.AppsPageViewFragmentAdapter;
import com.bitvault.launcher.CustomView.CirclePageIndicator;
import com.bitvault.launcher.CustomView.PageIndicator;
import com.bitvault.launcher.Model.AppModel;
import com.bitvault.launcher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is using to manage header view page view and adapter and data
 * Created by Anshuman on 9/7/2016.
 */
public class AppsViewControl {

    /**
     * Activity Instance
     */
    private FragmentActivity mActivity;
    /**
     * View pager GUI
     */
    private ViewPager mPager;
    /**
     * View page indicator
     */
    private PageIndicator mIndicator;
    /**
     * View pager fragment adapter
     */
    private AppsPageViewFragmentAdapter mPagerAdapter;
    /**
     * Apps Data List Data
     */
    private List<List<AppModel>> mPageAppsData = new ArrayList<>();

    /*Today Date Position*/
    private int positionForCurrentDate = 0;

    private String TAG = AppsViewControl.class.getSimpleName();

    /**
     * Default Constructor of this class
     *
     * @param mActivity
     * @param appsListForPageView
     */
    public AppsViewControl(FragmentActivity mActivity, List<List<AppModel>> appsListForPageView, View view) {
        this.mActivity = mActivity;
        this.mPageAppsData = appsListForPageView;
        initViews(view);
        handleHeaderMove();
    }

    /**
     * Initial View of th header view
     * @param view
     */
    private void initViews(View view) {
        mPager = (ViewPager) view.findViewById(R.id.appsPager);
        mIndicator = (CirclePageIndicator) view.findViewById(R.id.appsIndicator);
        updateAppsDataOnGUI();

    }


    /**
     * set date data Activity Set On GUI
     */
    private void updateAppsDataOnGUI() {
        // Set adapter
        mPagerAdapter = new AppsPageViewFragmentAdapter(
                mActivity.getSupportFragmentManager(), mPageAppsData,
                mActivity);
        mPager.setAdapter(mPagerAdapter);
        mIndicator.setViewPager(mPager);
        /*Set Pageview to current date*/
        mPager.setCurrentItem(positionForCurrentDate);
    }


    /**
     * Handler Move Listener
     */
    private void handleHeaderMove() {
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                // Check if this is the page you want.
            }
        });
    }

}
