package com.bitvault.launcher.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
    // make private static instance of session manager class
    private static SessionManager sessionManager;
    // Shared Preferences
    private SharedPreferences pref;
    // Editor for Shared preferences
    private Editor editor;
    // Context
    private Context mContext;

    // Constructor
    @SuppressLint("CommitPrefEdits")
    private SessionManager(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PreferenceHelper.PREFERENCE_NAME,
                PreferenceHelper.PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * getInstance method is used to initialize SessionManager singelton
     * instance
     *
     * @param context context instance
     * @return Singelton session manager instance
     */
    public static SessionManager getInstance(Context context) {
        if (sessionManager == null) {
            sessionManager = new SessionManager(context);
        }
        return sessionManager;
    }

    /**
     * Create login session
     */
    public void createLoginSession(String password, String email, String userName) {
        // Storing login value as TRUE
        editor.putBoolean(PreferenceHelper.IS_LOGIN, true);
        // Storing User name and email in pref
        editor.putString(PreferenceHelper.KEY_USER_PASSWORD, password);
        editor.putString(PreferenceHelper.KEY_USER_EMAIL, email);
        editor.putString(PreferenceHelper.KEY_USER_NAME, userName);
        // commit changes
        editor.commit();
    }

    /**
     * Create login session
     */
    public void createPasswordProtectionSession(String password, String hint) {
        // Storing login value as TRUE
        editor.putBoolean(PreferenceHelper.IS_PROTECTION_ENABLED, true);
        // Storing protection password in pref
        editor.putString(PreferenceHelper.KEY_PROTECTION_PASSWORD, password);
        editor.putString(PreferenceHelper.KEY_PROTECTION_PASSWORD_HINT, hint);
        // commit changes
        editor.commit();
    }

    /**
     * Create login session
     */
    public void storeUserImage(String imageData) {
        // Storing User Image
        editor.putString(PreferenceHelper.KEY_USER_IMAGE, imageData);
        // commit changes
        editor.commit();
    }

    /**
     * Get User Image From prefernce
     *
     * @return
     */
    public String getUserImage() {
        return pref.getString(PreferenceHelper.KEY_USER_IMAGE, "");
    }

    /**
     * Get Login User name
     *
     * @return
     */
    public String getUserPassword() {
        return pref.getString(PreferenceHelper.KEY_USER_PASSWORD, null);
    }

    /**
     * Get Login User Email
     *
     * @return
     */
    public String getUserEmail() {
        return pref.getString(PreferenceHelper.KEY_USER_EMAIL, null);
    }

    /**
     * Get PasswordHint
     *
     * @return
     */
    public String getProtectionPasswordHint() {
        return pref.getString(PreferenceHelper.KEY_PROTECTION_PASSWORD_HINT, "");
    }

    /**
     * Get Protection Password
     *
     * @return
     */
    public String getProtectionPassword() {
        return pref.getString(PreferenceHelper.KEY_PROTECTION_PASSWORD, "");
    }

    /**
     * Get User Name
     *
     * @return
     */
    public String getUserName() {
        return pref.getString(PreferenceHelper.KEY_USER_NAME, "");
    }


    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    /**
     * Quick check for login status
     **/
    public boolean isLoggedIn() {
        return pref.getBoolean(PreferenceHelper.IS_LOGIN, false);
    }

    /**
     * Quick check for login status
     **/
    public boolean isPasswordProtectionEnabled() {
        return pref.getBoolean(PreferenceHelper.IS_PROTECTION_ENABLED, false);
    }

}
