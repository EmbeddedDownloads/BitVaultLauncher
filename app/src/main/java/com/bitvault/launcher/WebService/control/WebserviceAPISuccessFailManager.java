package com.bitvault.launcher.WebService.control;

/**
 * Webservice API Response is Success OR Fail
 *
 * @author Shruti
 */
public class WebserviceAPISuccessFailManager {
    /**
     * Instance of This class
     */
    public static WebserviceAPISuccessFailManager mApiSuccessFailManager;
    /**
     * Debugging TAG
     */
    @SuppressWarnings("unused")
    private String TAG = WebserviceAPISuccessFailManager.class.getSimpleName();

    private WebserviceAPISuccessFailManager() {
    }

    /**
     * Get Instance of this class
     *
     * @return
     */
    public static WebserviceAPISuccessFailManager getInstance() {
        if (mApiSuccessFailManager == null)
            mApiSuccessFailManager = new WebserviceAPISuccessFailManager();
        return mApiSuccessFailManager;

    }

}
