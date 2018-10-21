package com.bitvault.launcher.Views;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitvault.launcher.R;
import com.bitvault.launcher.Utils.AndroidAppUtils;
import com.bitvault.launcher.Utils.AppDialogUtils;
import com.bitvault.launcher.preference.SessionManager;

/**
 * Created by Anshuman on 3/15/2017.
 */

public class PasswordLockScreenControl implements View.OnClickListener {
    /**
     * Debuggable TAG
     */
    private static final String TAG = PasswordLockScreenControl.class.getSimpleName();
    /**
     * Activity reference object
     */
    private Activity mActivity;
    /**
     * View Instance
     */
    private EditText unlock_password_et;
    private TextInputLayout unlock_pwd_txtinpt;
    private Button applyPassword;
    private TextView hintText;
    private RelativeLayout enterPasswordView;
    private View mView;

    /**
     * Constructor of this class
     *
     * @param mActivity
     * @param enterPasswordView
     * @param view
     */
    PasswordLockScreenControl(Activity mActivity, RelativeLayout enterPasswordView, View view) {
        this.mActivity = mActivity;
        this.enterPasswordView = enterPasswordView;
        this.mView = view;
        initViews();
    }

    /**
     * init Views
     */
    private void initViews() {
        unlock_pwd_txtinpt = (TextInputLayout) mView.findViewById(R.id.unlock_pwd_txtinpt);
        unlock_password_et = (EditText) mView.findViewById(R.id.unlock_password_et);
        hintText = (TextView) mView.findViewById(R.id.hintText);
        applyPassword = (Button) mView.findViewById(R.id.applyPassword);
        applyPassword.setOnClickListener(this);
        /*Set Hint Text but set invisible*/
        hintText.setText("Hint :" + SessionManager.getInstance(mActivity).getProtectionPasswordHint());
        hintText.setVisibility(View.INVISIBLE);

        if (!SessionManager.getInstance(mActivity).isPasswordProtectionEnabled() && enterPasswordView != null)
            enterPasswordView.setVisibility(View.GONE);

//        AndroidAppUtils.updateGUIViewsTheme(applyPassword, null, null);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.applyPassword:
                hintText.setVisibility(View.INVISIBLE);
                if (validatePassword()) {
                    checkPassword();
                }
                break;
        }
    }

    /**
     * Save User Preference Data
     */
    private void checkPassword() {
        String enteredPassword = unlock_password_et.getText().toString();
        String originalPassword = SessionManager.getInstance(mActivity).getProtectionPassword();

        if (enteredPassword.equals(originalPassword)) {
            AndroidAppUtils.keyboardDown(mActivity);
            enterPasswordView.setVisibility(View.GONE);
        } else {
            /*Now Show Hint to enter password*/
            hintText.setVisibility(View.VISIBLE);
            /*Plan to add shake animation on textview*/
            AppDialogUtils.showAlertDialog(mActivity, mActivity.getResources().getString(R.string.wrong_password),
                    mActivity.getResources().getString(R.string.ok), null);
        }

    }

    /*******************************************************************************
     * Function name - validatePassword
     * Description - validate Password entered by user
     *
     * @return - returns true if Password is valid
     * return false if Password is not valid
     ******************************************************************************/
    private boolean validatePassword() {
        String pwd = unlock_password_et.getText().toString().trim();
        if (pwd.isEmpty()) {
            unlock_pwd_txtinpt.setError(mActivity.getString(R.string.err_msg_password));
            return false;
        } else {
            unlock_pwd_txtinpt.setErrorEnabled(false);
        }
        return true;
    }
}
