package com.bitvault.launcher.Adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bitvault.launcher.AppsPageFragmentView;
import com.bitvault.launcher.Model.AppModel;

import java.util.List;


/**
 * Page view adapter to show data on the page view (product image on page view)
 *
 * @author Anshuman
 */

public class AppsPageViewFragmentAdapter extends FragmentStatePagerAdapter {
    /**
     * List which content page view data
     */
    private List<List<AppModel>> mPageAppsData;
    Activity mActivity;

    /**
     * Constructor of this adapter class
     *
     * @param fm
     */
    public AppsPageViewFragmentAdapter(FragmentManager fm,
                                       List<List<AppModel>> mPageAppsData,
                                       Activity mActivity) {
        super(fm);
        this.mPageAppsData = mPageAppsData;
        this.mActivity = mActivity;
    }

    /**
     * Update new page list on GUI
     */
    public void updatePagerImage(List<List<AppModel>> mPageAppsData) {
        this.mPageAppsData = mPageAppsData;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.view.PagerAdapter#getCount()
     */
    @Override
    public int getCount() {
        return mPageAppsData.size();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentStatePagerAdapter#getItem(int)
     */
    @Override
    public Fragment getItem(int position) {
        // Create a new instance of the fragment and return it.
        AppsPageFragmentView sampleFragment = (AppsPageFragmentView) AppsPageFragmentView
                .getInstance();
        sampleFragment.setItem(mActivity, mPageAppsData.get(position),
                "_" + (position + 1));
        return sampleFragment;
    }

    /**
     * This method is only gets called when we invoke
     * {@link #notifyDataSetChanged()} on this adapter. Returns the index of the
     * currently active fragments. There could be minimum two and maximum three
     * active fragments(suppose we have 3 or more fragments to show). If there
     * is only one fragment to show that will be only active fragment. If there
     * are only two fragments to show, both will be in active state.
     * PagerAdapter keeps left and right fragments of the currently visible
     * fragment in ready/active state so that it could be shown immediate on
     * swiping. Currently Active Fragments means one which is currently visible
     * one is before it and one is after it.
     *
     * @param object Active Fragment reference
     * @return Returns the index of the currently active fragments.
     */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
