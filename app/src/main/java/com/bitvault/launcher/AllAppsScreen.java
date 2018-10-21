package com.bitvault.launcher;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.bitvault.launcher.Adapter.AppsListAdapter;
import com.bitvault.launcher.Control.WelcomeBitVaultViewControl;
import com.bitvault.launcher.Model.AppModel;
import com.bitvault.launcher.Utils.AndroidAppUtils;
import com.bitvault.launcher.Utils.GlobalConfig;
import com.bitvault.launcher.Views.LauncherHomeFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * BibleZon Load Shop website on GUI
 *
 * @author Anshuman
 */
public class AllAppsScreen extends Activity {

    /**
     * View fields instances
     */
    @SuppressWarnings("unused")
    private String TAG = AllAppsScreen.class.getSimpleName();
    private Activity mActivity;

    /**
     * Apps List Model
     */
    private List<AppModel> mAppsData;
    /**
     * RecyclerView View
     */
    private RecyclerView all_apps_grid_recycler_view;
    /**
     * Add List Adapter.
     */
    private AppsListAdapter mAdapter;
    private EditText searchView_et;
    private TextInputLayout searchView_txtinpt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_apps_grid_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initViews();
        onSearchTextChange();
        new WelcomeBitVaultViewControl(mActivity, null);
    }


    /**
     * init View of the application view
     */
    private void initViews() {
        mActivity = AllAppsScreen.this;
        mAppsData = new ArrayList<>();
        mAppsData = LauncherHomeFragment.mSortAppsData;
        /**
         * Image View Instance && Heading & SubHeading Text
         */
        all_apps_grid_recycler_view = (RecyclerView) findViewById(R.id.all_apps_grid_recycler_view);
        searchView_et = (EditText) findViewById(R.id.searchView_et);
        searchView_txtinpt = (TextInputLayout) findViewById(R.id.searchView_txtinpt);
        showAppsDataOnGUI(mAppsData);
        GlobalConfig.isNeedToShowLoading = false;

    }

    /**
     * From Backpress dnt need to show loading.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GlobalConfig.isNeedToShowLoading = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * Show Apps Data on GUI
     *
     * @param mApps
     */
    private void showAppsDataOnGUI(List<AppModel> mApps) {
        if (mApps != null) {
            //Set Adapter on Grid
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mActivity, 3, GridLayoutManager.VERTICAL, false);
            all_apps_grid_recycler_view.setLayoutManager(mLayoutManager);

            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.vertical_grid_layout_margin);
            all_apps_grid_recycler_view.addItemDecoration(new GridSpacingItemDecoration(3, 0, true));
//            apps_grid_recycler_view.addItemDecoration(new GridSpacingItemDecoration(3, 0/*dpToPx(5)*/, true));
            all_apps_grid_recycler_view.setItemAnimator(new DefaultItemAnimator());

            mAdapter = new AppsListAdapter(mActivity, mApps, TAG);
            all_apps_grid_recycler_view.setAdapter(mAdapter);
        } else
            AndroidAppUtils.showErrorLog(TAG, "Apps List is null.");
    }

    private void onSearchTextChange() {
         /*Text Change Listener for Search Edittext*/
        searchView_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchApps(searchView_et.getText().toString());
            }
        });
    }

    /**
     * Search Apps As per user input
     */
    private void searchApps(String enteredAppName) {
        ArrayList<AppModel> appSearchList = new ArrayList<>();
        for (AppModel mAppModel :
                mAppsData) {
            if ((mAppModel.getmAppName().toLowerCase()).contains(enteredAppName.toLowerCase()))
                appSearchList.add(mAppModel);
        }
        showAppsDataOnGUI(appSearchList);
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
