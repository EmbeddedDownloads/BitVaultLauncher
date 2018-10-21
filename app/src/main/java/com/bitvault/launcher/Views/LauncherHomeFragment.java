package com.bitvault.launcher.Views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitvault.launcher.AllAppsScreen;
import com.bitvault.launcher.Control.AppsLoader;
import com.bitvault.launcher.Control.AppsViewControl;
import com.bitvault.launcher.Control.WelcomeBitVaultViewControl;
import com.bitvault.launcher.HomeViewFragment;
import com.bitvault.launcher.Model.AppModel;
import com.bitvault.launcher.Model.HiddenAppModel;
import com.bitvault.launcher.R;
import com.bitvault.launcher.Utils.AndroidAppUtils;
import com.bitvault.launcher.Utils.GlobalConfig;
import com.bitvault.launcher.database.DBManager;
import com.bitvault.launcher.iHelper.Validation;
import com.google.common.collect.Lists;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


/****************************************************************
 * Class Name: FeastDayFragment
 * Description: This screen will show the feast day data
 ****************************************************************/
public class LauncherHomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<AppModel>> {
    /**
     * Debuggable TAG
     */
    private static final String TAG = LauncherHomeFragment.class.getSimpleName();
    /**
     * Activity reference object
     */
    private Activity mActivity;
    /**
     * View reference object
     */
    private View view;
    /**
     * Message View
     */
    private TextView messageView, headerDayDate;

    /**
     * App Loading Progress Bar
     */
    private ProgressBar appLoading;
    /**
     * Menu Button
     */
    private RelativeLayout menuIconView, deleteEnableDisableIconView, searchAppsOption,
            searchHeader, headerView, closeIconView, all_apps;
    private ImageView menuIcon, allAppsIcon, searchAppsIcon, deleteAppsIcon, closeIcon;
    private EditText searchView_et;
    private TextInputLayout searchView_txtinpt;

    public static ArrayList<AppModel> mSortAppsData = new ArrayList<>();

    public static LauncherHomeFragment mLauncherHomeFragment;


    /**
     * ON Create On view
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.launcher_base_view, container, false);
        initializeView();
        assignClick();
        new WelcomeBitVaultViewControl(mActivity, view);
        return view;
    }

    /**
     * Update Date when launcher is get started
     */
    @Override
    public void onResume() {
        super.onResume();

        updateHeaderDayDate();
        updateHeaderDateTheme();
    }

    /**
     * Initialize the view component object
     */
    private void initializeView() {
        mActivity = getActivity();
        mLauncherHomeFragment = this;
        messageView = (TextView) view.findViewById(R.id.messagetxt);
        headerDayDate = (TextView) view.findViewById(R.id.headerDayDate);
        appLoading = (ProgressBar) view.findViewById(R.id.appLoading);
        menuIconView = (RelativeLayout) view.findViewById(R.id.menuIconView);
        searchAppsOption = (RelativeLayout) view.findViewById(R.id.searchAppsOption);
        searchHeader = (RelativeLayout) view.findViewById(R.id.searchHeader);
        headerView = (RelativeLayout) view.findViewById(R.id.headerView);
        closeIconView = (RelativeLayout) view.findViewById(R.id.closeIconView);
        all_apps = (RelativeLayout) view.findViewById(R.id.all_apps);

        menuIcon = (ImageView) view.findViewById(R.id.menuIcon);
        allAppsIcon = (ImageView) view.findViewById(R.id.allAppsIcon);
        searchAppsIcon = (ImageView) view.findViewById(R.id.searchAppsIcon);
        deleteAppsIcon = (ImageView) view.findViewById(R.id.deleteAppsIcon);
        closeIcon = (ImageView) view.findViewById(R.id.closeIcon);

        searchView_et = (EditText) view.findViewById(R.id.searchView_et);
        searchView_txtinpt = (TextInputLayout) view.findViewById(R.id.searchView_txtinpt);

        deleteEnableDisableIconView = (RelativeLayout) view.findViewById(R.id.deleteEnableDisableIconView);
        appLoading.setVisibility(View.VISIBLE);
        // create the loader to load the apps list in background
        HomeViewFragment.getInstance().getSupportLoaderManager().initLoader(0, null, this);

        /*Show Normal Date Header*/
        searchHeader.setVisibility(View.GONE);
        headerView.setVisibility(View.VISIBLE);

    }

    /**
     * Update Views  GUI Theme
     */
    private void updateHeaderDateTheme() {
            /*Normal Theme*/
        headerDayDate.setTextColor(mActivity.getResources().getColor(R.color.colorWhite));
        menuIcon.setImageResource(R.drawable.menu_icon);
        deleteAppsIcon.setImageResource(R.drawable.delete_icon);
        searchAppsIcon.setImageResource(R.drawable.search_icon);
        allAppsIcon.setImageResource(R.drawable.apps_icon);
    }

    /**
     * Assign Click on GUI
     */
    private void assignClick() {
/**
 * Click on Menu
 */
        menuIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeViewFragment.getInstance().openSliderMenu();
            }
        });

        deleteEnableDisableIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalConfig.isDeleteFeatureEnable)
                    GlobalConfig.isDeleteFeatureEnable = false;
                else
                    GlobalConfig.isDeleteFeatureEnable = true;

                updateDataOnGUI(GlobalConfig.appsModelList);
            }
        });
        /*Search option click*/
        searchAppsOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Show Search Header*/
                searchHeader.setVisibility(View.VISIBLE);
                headerView.setVisibility(View.GONE);
            }
        });

        /*Close of search Option*/
        closeIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Show Normal Header*/
                searchHeader.setVisibility(View.GONE);
                headerView.setVisibility(View.VISIBLE);
                /*Show All Apps Now*/
                updateDataOnGUI(GlobalConfig.appsModelList);
                 /*Remove all String from edittext*/
                searchView_et.getText().clear();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /*Hide Keyboard*/
                        AndroidAppUtils.keyboardDown(mActivity);
                    }
                }, 500);

            }
        });
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

        /*Show all apps on same page*/
        all_apps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalConfig.isDeleteFeatureEnable) {
                    GlobalConfig.isDeleteFeatureEnable = false;
                    updateDataOnGUI(GlobalConfig.appsModelList);
                }
                Intent mIntent = new Intent(mActivity, AllAppsScreen.class);
                mActivity.startActivity(mIntent);
            }
        });

    }

    /**
     * Search Apps As per user input
     */
    private void searchApps(String enteredAppName) {
        ArrayList<AppModel> appSearchList = new ArrayList<>();
        for (AppModel mAppModel :
                GlobalConfig.appsModelList) {
            if ((mAppModel.getmAppName().toLowerCase()).contains(enteredAppName.toLowerCase()))
                appSearchList.add(mAppModel);
        }
        updateDataOnGUI(appSearchList);
    }

    /*******************************************************************************
     * Function name - validateAppSearch
     * Description - validate App Name entered by user
     *
     * @return - returns true if App Name is valid
     * return false if App Name is not valid
     ******************************************************************************/
    private boolean validateAppSearch() {
        String appName = searchView_et.getText().toString().trim();

        if (appName.isEmpty() || !Validation.isEmailAddress(searchView_et, true) || !Validation.isEmailValidForDot(searchView_et)) {
            searchView_txtinpt.setError(mActivity.getString(R.string.please_enter_name_app));
            AndroidAppUtils.requestFocus(mActivity, searchView_et);
            return false;
        } else {
            searchView_txtinpt.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * Update Header Day & date
     */
    private void updateHeaderDayDate() {
         /*Date Format for Design Look*/
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        /*For Database format*/
        SimpleDateFormat mDbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        /*Calendar Instance*/
        Calendar cal = Calendar.getInstance();
        String mTodayDate = sdf.format(cal.getTime());
        if (headerDayDate != null)
            headerDayDate.setText(mTodayDate);
    }


    /**
     * Update Apps Data on GUI
     */
    public void updateDataOnGUI(ArrayList<AppModel> appsList) {
        // Create new List with same capacity as original (for efficiency).
        ArrayList<AppModel> mAppsArrayList = new ArrayList<AppModel>();
        for (AppModel mData :
                appsList) {
            AppModel mTmp = new AppModel();
            mTmp.setmAppPackage(mData.getmAppPackage());
            mTmp.setmAppIcon(mData.getmAppIcon());
            mTmp.setmAppName(mData.getmAppName());
            mTmp.setAppInfo(mData.getAppInfo());
            mTmp.setHidden(mData.isHidden());
            mAppsArrayList.add(mTmp);
        }

        if (mAppsArrayList != null) {
            ArrayList<AppModel> mFilterAppsData = FilterHiddenApps(mAppsArrayList);
            appLoading.setVisibility(View.GONE);
            /*Now Sort Apps According to Use of Apps */
            mSortAppsData = new ArrayList<>();
            mSortAppsData = sortAppsAccordingToUses(mFilterAppsData);

            List<List<AppModel>> appsListForPageView = Lists.partition(mSortAppsData, 9);
            new AppsViewControl(HomeViewFragment.getInstance(), appsListForPageView, view);
        } else
            AndroidAppUtils.showErrorLog(TAG, "Apps List is null.");
    }

    /**
     * Sort Final list of apps
     *
     * @param AppsData
     */
    private ArrayList<AppModel> sortAppsAccordingToUses(ArrayList<AppModel> AppsData) {
        Collections.reverse(AppsData);
        /*Database Instance*/
        DBManager dbManager = new DBManager(mActivity);
        dbManager.open();
        for (int i = 0; i < AppsData.size(); i++) {
            /*Store Apps Data into database*/
            AppsData.get(i).setAppPriorityCount(dbManager.getAppsPriority(AppsData.get(i).getmAppName(),
                    AppsData.get(i).getmAppPackage()).getAppPriorityCount());
        }
        // Sort by priorities :
        Collections.sort(AppsData, new AppModel.AppsPriorityComparator());
        Collections.reverse(AppsData);
        dbManager.close();
        return AppsData;
    }

    /**
     * Filter Hidden Apps
     *
     * @param appsList
     */
    private ArrayList<AppModel> FilterHiddenApps(ArrayList<AppModel> appsList) {
        /*Database Instance*/
        DBManager dbManager = new DBManager(mActivity);
        dbManager.open();
                /*Get List of hidden apps*/
        ArrayList<HiddenAppModel> mHiddenApps = dbManager.getHiddenAppsData();
        dbManager.close();
        for (int i = 0; i < appsList.size(); i++) {
            for (HiddenAppModel hiddenAppModel :
                    mHiddenApps) {
                if (appsList.get(i).getmAppPackage().equalsIgnoreCase(hiddenAppModel.getAppPackage()))
                    appsList.get(i).setHidden(true);
            }
        }
        ArrayList<AppModel> mHinddenAppsTemp = new ArrayList<>();
        for (AppModel mData : appsList) {
            if (mData.isHidden())
                mHinddenAppsTemp.add(mData);
        }
        appsList.removeAll(mHinddenAppsTemp);
        return appsList;
    }

    @Override
    public Loader<ArrayList<AppModel>> onCreateLoader(int id, Bundle bundle) {
        return new AppsLoader(mActivity);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<AppModel>> loader, ArrayList<AppModel> apps) {
        /*Store apps into DB*/
        prepareAppsForDB(apps);
        /*Get apps From Database */
        GlobalConfig.appsModelList = new ArrayList<>();
        GlobalConfig.appsModelList.addAll(apps);
        /*Update Apps on GUI*/
        updateDataOnGUI(apps);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<AppModel>> loader) {
        updateDataOnGUI(null);
    }

    /**
     * Prepare Apps for inserting for database
     *
     * @param apps
     */
    private void prepareAppsForDB(ArrayList<AppModel> apps) {
        ArrayList<String> mDefaultPriorityApps = getDefaultHighPriorityApps();
        for (AppModel model :
                apps) {
            if (mDefaultPriorityApps.contains(model.getmAppName().toUpperCase()))
                model.setAppPriorityCount(1);
             /*Database Instance*/
            DBManager dbManager = new DBManager(mActivity);
            dbManager.open();
            /*Store Apps Data into database*/
            dbManager.insertAppsIntoDB(model.getmAppName(), model.getmAppPackage(), model.getAppPriorityCount());
            dbManager.close();
        }
    }

    /**
     * List of Default Apps for Launcher Apps Priority
     */
    private ArrayList<String> getDefaultHighPriorityApps() {
        ArrayList<String> mApps = new ArrayList<>();
        mApps.add("PHONE");
        mApps.add("CONTACTS");
        mApps.add("CHROME");
        mApps.add("CAMERA");
        mApps.add("FACEBOOK");
        mApps.add("GMAIL");
        mApps.add("INSTAGRAM");
        mApps.add("MY FILES");
        mApps.add("PHOTOS");
        mApps.add("PLAY STORE");
        mApps.add("WHATSAPP");
        mApps.add("YOUTUBE");
        mApps.add("SKYPE");

        return mApps;

    }


}
