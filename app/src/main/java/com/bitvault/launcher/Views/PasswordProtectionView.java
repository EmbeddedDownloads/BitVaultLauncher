package com.bitvault.launcher.Views;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.bitvault.launcher.Control.HeaderViewManager;
import com.bitvault.launcher.Control.WelcomeBitVaultViewControl;
import com.bitvault.launcher.HomeViewFragment;
import com.bitvault.launcher.R;
import com.bitvault.launcher.Utils.AndroidAppUtils;
import com.bitvault.launcher.Utils.AppDialogUtils;
import com.bitvault.launcher.Utils.GlobalConfig;
import com.bitvault.launcher.iHelper.AlertDialogClickListener;
import com.bitvault.launcher.iHelper.HeaderViewClickListener;
import com.bitvault.launcher.preference.SessionManager;

import static com.bitvault.launcher.Control.LeftSlidingMenuControl.currentSelectedTAG;
import static com.bitvault.launcher.Control.LeftSlidingMenuControl.homeMenu;

/**
 * Created by Anshuman on 3/12/2017.
 */

public class PasswordProtectionView extends Fragment implements View.OnClickListener {
    /**
     * Debuggint TAG
     */
    private String TAG = PasswordProtectionView.class.getSimpleName();
    /**
     * Activity Instance
     */
    private Activity mActivity;
    /**
     * View Instance
     */
    private EditText password_et, confirm_password_et, passwordHint_et;
    private TextInputLayout pwd_txtinpt, confirm_password_txtinpt, passwordHint_txtinpt;
    private CheckBox showPasswordCheckBox;
    private RelativeLayout enterPasswordView;
    private Button savePassword;
    /**
     * View reference object
     */
    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.password_protection_screen, container, false);
        initViews();
        assignClick();
        manageHeaderView();
        return view;
    }

    /**
     * init Views
     */
    private void initViews() {
        this.mActivity = getActivity();
        pwd_txtinpt = (TextInputLayout) view.findViewById(R.id.pwd_txtinpt);
        confirm_password_txtinpt = (TextInputLayout) view.findViewById(R.id.confirm_password_txtinpt);
        passwordHint_txtinpt = (TextInputLayout) view.findViewById(R.id.passwordHint_txtinpt);

        password_et = (EditText) view.findViewById(R.id.password_et);
        confirm_password_et = (EditText) view.findViewById(R.id.confirm_password_et);
        passwordHint_et = (EditText) view.findViewById(R.id.passwordHint_et);
        enterPasswordView = (RelativeLayout) view.findViewById(R.id.enterPasswordView);

        savePassword = (Button) view.findViewById(R.id.savePassword);

        showPasswordCheckBox = (CheckBox) view.findViewById(R.id.showPasswordCheckBox);
/**
 * on Show and hide password
 */
        showPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    password_et.setTransformationMethod(null);
                    confirm_password_et.setTransformationMethod(null);
                } else {
                    password_et.setTransformationMethod(new PasswordTransformationMethod());
                    confirm_password_et.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        new PasswordLockScreenControl(mActivity, enterPasswordView, view);
        GlobalConfig.isNeedToShowLoading = false;

//        AndroidAppUtils.updateGUIViewsTheme(savePassword, null, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        new WelcomeBitVaultViewControl(mActivity, view);
    }

    /**
     * Assign Click on GUI
     */
    private void assignClick() {
        savePassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.savePassword:
                if (validatePassword() && validateConfirmPassword() && validatePasswordHint()) {
                    savePasswordPreferenceData();
                }
                break;
        }
    }

    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    public void manageHeaderView() {
        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick(), mActivity);
        HeaderViewManager.getInstance().setHeading(true, mActivity.getResources().getString(R.string.password_protection));
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

    /**
     * Save Password Preference Data
     */
    private void savePasswordPreferenceData() {
        SessionManager.getInstance(mActivity).createPasswordProtectionSession(password_et.getText().toString(),
                passwordHint_et.getText().toString().trim());

        AppDialogUtils.showAlertDialog(mActivity, mActivity.getResources().getString(R.string.password_protection_enabled),
                mActivity.getResources().getString(R.string.ok), redirectUserToHomeScreen());

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

    /*******************************************************************************
     * Function name - validateUsername
     * Description - validate User entered by user
     *
     * @return - returns true if user name is valid
     * return false if user name is not valid
     ******************************************************************************/
    private boolean validatePasswordHint() {
        String passwordHint = passwordHint_et.getText().toString().trim();

        if (passwordHint.isEmpty()) {
            passwordHint_txtinpt.setError(mActivity.getString(R.string.err_msg_password_hint));
            AndroidAppUtils.requestFocus(mActivity, passwordHint_et);
            return false;
        } else {
            passwordHint_txtinpt.setErrorEnabled(false);
        }

        return true;
    }

    /*******************************************************************************
     * Function name - validatePassword
     * Description - validate Password entered by user
     *
     * @return - returns true if Password is valid
     * return false if Password is not valid
     ******************************************************************************/
    private boolean validateConfirmPassword() {
        String confirmPwd = confirm_password_et.getText().toString();
        if (confirmPwd.isEmpty()) {
            confirm_password_txtinpt.setError(mActivity.getString(R.string.err_confirm_msg_password));
            return false;
        } else if (!((password_et.getText().toString()).equals(confirmPwd))) {
            confirm_password_txtinpt.setError(mActivity.getString(R.string.err_password_not_match));
            return false;
        } else {
            confirm_password_txtinpt.setErrorEnabled(false);
        }

        return true;

    }

    /*******************************************************************************
     * Function name - validatePassword
     * Description - validate Password entered by user
     *
     * @return - returns true if Password is valid
     * return false if Password is not valid
     ******************************************************************************/
    private boolean validatePassword() {
        String pwd = password_et.getText().toString();
        if (pwd.isEmpty()) {
            pwd_txtinpt.setError(mActivity.getString(R.string.err_msg_password));
            return false;
        } else {
            pwd_txtinpt.setErrorEnabled(false);
        }
        return true;

    }
}
