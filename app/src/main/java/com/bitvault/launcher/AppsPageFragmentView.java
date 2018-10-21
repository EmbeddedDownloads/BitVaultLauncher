package com.bitvault.launcher;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitvault.launcher.Adapter.AppsListAdapter;
import com.bitvault.launcher.Model.AppModel;
import com.bitvault.launcher.Utils.AndroidAppUtils;

import java.util.List;


/**
 * Header Page View Row
 *
 * @author Anshuman
 */
public class AppsPageFragmentView extends Fragment {
    private static Activity mActivity;
    /**
     * Mass Activity Of Mass Data Model
     */
    private List<AppModel> mAppsData;
    /**
     * RecyclerView View
     */
    private RecyclerView apps_grid_recycler_view;
    /**
     * Add List Adapter.
     */
    private AppsListAdapter mAdapter;
    /**
     * Debugging TAG
     */
    private String TAG = AppsPageFragmentView.class.getSimpleName();

    /**
     * This method return instance of the same fragment
     *
     * @return
     */
    public static Fragment getInstance() {
        /* Create new instance of fragment */
        Fragment fragment = new AppsPageFragmentView();
        return fragment;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("unused")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.apps_grid_page,
                container, false);
        /**
         * Image View Instance && Heading & SubHeading Text
         */
        apps_grid_recycler_view = (RecyclerView) view.findViewById(R.id.apps_grid_recycler_view);

        if (mAppsData != null) {
            //Set Adapter on Grid
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mActivity, 3, GridLayoutManager.HORIZONTAL, false);
            apps_grid_recycler_view.setLayoutManager(mLayoutManager);

            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_layout_margin);
            apps_grid_recycler_view.addItemDecoration(new GridSpacingItemDecoration(3, 0, true));
//            apps_grid_recycler_view.addItemDecoration(new GridSpacingItemDecoration(3, 0/*dpToPx(5)*/, true));
            apps_grid_recycler_view.setItemAnimator(new DefaultItemAnimator());

            mAdapter = new AppsListAdapter(mActivity, mAppsData, TAG);
            apps_grid_recycler_view.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else
            AndroidAppUtils.showErrorLog(TAG, "Apps List is null.");

        return view;
    }

    /**
     * Set product image on GUI @Setter
     *
     * @param mAppsData
     */
    public void setItem(Activity mActivity, List<AppModel> mAppsData,
                        String current_url) {
        AndroidAppUtils.showLog(TAG, "set apps item on pageview :" + mAppsData.size());
        this.mActivity = mActivity;
        this.mAppsData = mAppsData;

    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
