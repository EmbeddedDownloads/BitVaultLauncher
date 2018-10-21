package com.bitvault.launcher.preference;

/**
 * Preference Helper Class
 *
 * @author Anshuman
 */
public interface PreferenceHelper {
    // Shared Preferences file name
    String PREFERENCE_NAME = "BITVAULT_LAUNCHER_APP_PREFERENCE";
    // Shared Preferences mode
    int PRIVATE_MODE = 0;
    // User Id
    String KEY_USER_PASSWORD = "USER_PASSWORD";
    //User email id
    String KEY_USER_EMAIL = "USER_EMAIL";
    //User user name
    String KEY_USER_NAME = "USER_NAME";
    // Login Shared Preferences Keys
    String IS_LOGIN = "IS_LOGGED_IN";
    // Password protection Shared Preferences Keys
    String IS_PROTECTION_ENABLED = "IS_PROTECTION_ENABLED";
    // Password protection
    String KEY_PROTECTION_PASSWORD = "PROTECTION_PASSWORD";
    // Password protection hint
    String KEY_PROTECTION_PASSWORD_HINT = "PROTECTION_PASSWORD_HINT";
    // User Image Data
    String KEY_USER_IMAGE = "USER_IMAGE";

}
