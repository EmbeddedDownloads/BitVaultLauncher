package com.bitvault.launcher.Views;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitvault.launcher.Adapter.HiddenAppsAdapter;
import com.bitvault.launcher.Control.HeaderViewManager;
import com.bitvault.launcher.Control.WelcomeBitVaultViewControl;
import com.bitvault.launcher.HomeViewFragment;
import com.bitvault.launcher.Model.AppModel;
import com.bitvault.launcher.Model.HiddenAppModel;
import com.bitvault.launcher.R;
import com.bitvault.launcher.Utils.AndroidAppUtils;
import com.bitvault.launcher.Utils.AppDialogUtils;
import com.bitvault.launcher.Utils.GlobalConfig;
import com.bitvault.launcher.database.DBManager;
import com.bitvault.launcher.database.DatabaseIhelper;
import com.bitvault.launcher.iHelper.AlertDialogClickListener;
import com.bitvault.launcher.iHelper.HeaderViewClickListener;
import com.bitvault.launcher.preference.SessionManager;

import java.util.ArrayList;

import static com.bitvault.launcher.Control.LeftSlidingMenuControl.currentSelectedTAG;
import static com.bitvault.launcher.Control.LeftSlidingMenuControl.homeMenu;


/****************************************************************
 * Class Name: HideAppsFragment
 * Description: This screen will show the feast day data
 ****************************************************************/
public class HideAppsFragment extends Fragment {
    /**
     * Debuggable TAG
     */
    private static final String TAG = HideAppsFragment.class.getSimpleName();
    /**
     * Activity reference object
     */
    private Activity mActivity;
    /**
     * View reference object
     */
    private View view;
    /**
     * View Objects
     */
    private ListView hideShowAppsList;
    private TextView hideAppsSaveButton;
    private RelativeLayout enterPasswordView;
    /**
     * Hidden List Adapter
     */
    private HiddenAppsAdapter mHiddenAppsAdapter;
    private ArrayList<AppModel> mAppsArrayListTemp;

    /**
     * On Create of this view
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.hide_apps_screen, container, false);
        initializeView();
        manageHeaderView();
        manageClicks();
        loadContentOnGUI();
        return view;
    }

    /**
     * Initialize the view component object
     */
    private void initializeView() {


        mActivity = getActivity();
        hideShowAppsList = (ListView) view.findViewById(R.id.hideShowAppsList);
        hideAppsSaveButton = (TextView) view.findViewById(R.id.hideAppsSaveButton);
        enterPasswordView = (RelativeLayout) view.findViewById(R.id.enterPasswordView);

        mHiddenAppsAdapter = new HiddenAppsAdapter(mActivity);
        hideShowAppsList.setAdapter(mHiddenAppsAdapter);

        new PasswordLockScreenControl(mActivity, enterPasswordView, view);
        GlobalConfig.isNeedToShowLoading = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        new WelcomeBitVaultViewControl(mActivity, view);
    }

    /**
     * Load Content on GUI
     */
    private void loadContentOnGUI() {
        mAppsArrayListTemp = new ArrayList<AppModel>();

        for (AppModel mData :
                GlobalConfig.appsModelList) {
            AppModel mTmp = new AppModel();
            mTmp.setmAppPackage(mData.getmAppPackage());
            mTmp.setmAppIcon(mData.getmAppIcon());
            mTmp.setmAppName(mData.getmAppName());
            mTmp.setAppInfo(mData.getAppInfo());
            mTmp.setHidden(mData.isHidden());
            mAppsArrayListTemp.add(mTmp);
        }
        FilterHiddenApps(mAppsArrayListTemp);
        mHiddenAppsAdapter.addUpdateDataIntoList(mAppsArrayListTemp);
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
        return appsList;
    }

    /****************************************************
     * Function Name : manageClicks
     * Description : This function will manage the clicks on views
     */
    private void manageClicks() {
        hideAppsSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Database Instance*/
                DBManager dbManager = new DBManager(mActivity);
                dbManager.open();
                /*Clear all old Apps List*/
                dbManager.emptyTable(DatabaseIhelper.DB_HIDDEN_APPS_TABLE);

                /*Make List of Hidden Apps*/
                for (AppModel model :
                        mHiddenAppsAdapter.mHiddenAppsList) {
                    if (model.isHidden()) {
                        AndroidAppUtils.showLog(TAG, "Hidden Apps Name :" + model.getmAppName());
                        dbManager.insertHiddenAppsIntoDB(model.getmAppName(),
                                model.getmAppPackage());
                    }
                }
                /*Close Database*/
                dbManager.close();
                ForceUserToTurnONPasswordProtection();
            }
        });
    }

    /**
     * Force user to turn on Password Protection
     */
    private void ForceUserToTurnONPasswordProtection() {
        if (!SessionManager.getInstance(mActivity).isPasswordProtectionEnabled())
            AppDialogUtils.showAlertDialog(mActivity, mActivity.getResources().getString(R.string.force_user_to_set_pass),
                    mActivity.getResources().getString(R.string.ok), redirectUserToPasswordProtection());
        else
            AppDialogUtils.showAlertDialog(mActivity, mActivity.getResources().getString(R.string.hide_apps_success),
                    mActivity.getResources().getString(R.string.ok), redirectUserToHomeScreen());
    }

    /**
     * Redirect User to password protection view
     *
     * @return
     */
    private AlertDialogClickListener redirectUserToPasswordProtection() {
        AlertDialogClickListener mAlertDialogClickListener = new AlertDialogClickListener() {
            @Override
            public void onClickOfAlertDialogPositive() {
                currentSelectedTAG = 0;
                GlobalConfig.isNeedToShowLoading = false;
                HomeViewFragment.getInstance().removeAllOldFragment();
                HomeViewFragment.getInstance().pushFragment(new PasswordProtectionView());
            }
        };
        return mAlertDialogClickListener;
    }

    /**
     * Redirect User to Launcher view
     *
     * @return
     */
    private AlertDialogClickListener redirectUserToHomeScreen() {
        AlertDialogClickListener mAlertDialogClickListener = new AlertDialogClickListener() {
            @Override
            public void onClickOfAlertDialogPositive() {
                GlobalConfig.isNeedToShowLoading = false;
                if (currentSelectedTAG != homeMenu.getId()) {
                    currentSelectedTAG = homeMenu.getId();
                    HomeViewFragment.getInstance().removeAllOldFragment();
                    HomeViewFragment.getInstance().pushFragment(new LauncherHomeFragment());
                }
            }
        };
        return mAlertDialogClickListener;
    }


    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    public void manageHeaderView() {
        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick(), mActivity);
        HeaderViewManager.getInstance().setHeading(true, mActivity.getResources().getString(R.string.hide_apps));
        HeaderViewManager.getInstance().setLeftSideHeaderView(true, R.drawable.menu_icon, true, false);
    }

    /*****************************************************************************
     * Function name - manageHeaderClick
     * Description - manage the click on the left and right image view of header
     *****************************************************************************/
    private HeaderViewClickListener manageHeaderClick() {
        HeaderViewClickListener headerViewClickListener = new HeaderViewClickListener() {
            @Override
            public void onClickOfHeaderLeftView() {
                HomeViewFragment.getInstance().openSliderMenu();
            }

            @Override
            public void onClickOfHeaderRightView() {
            }
        };
        return headerViewClickListener;
    }
}
