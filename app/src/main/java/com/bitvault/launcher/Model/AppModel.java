package com.bitvault.launcher.Model;

import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import java.util.Comparator;

/**
 * @credit http://developer.android.com/reference/android/content/AsyncTaskLoader.html
 */
public class AppModel {
    private boolean isHidden = false;
    private ResolveInfo mResolveInfo = null;
    private String mAppName = "", mAppPackage = "";
    private Drawable mAppIcon;
    private int appPriorityCount = 0;

    public ResolveInfo getmResolveInfo() {
        return mResolveInfo;
    }

    public void setmResolveInfo(ResolveInfo mResolveInfo) {
        this.mResolveInfo = mResolveInfo;
    }

    public int getAppPriorityCount() {
        return appPriorityCount;
    }

    public void setAppPriorityCount(int appPriorityCount) {
        this.appPriorityCount = appPriorityCount;
    }

    public String getmAppName() {
        return mAppName;
    }

    public void setmAppName(String mAppName) {
        this.mAppName = mAppName;
    }

    public String getmAppPackage() {
        return mAppPackage;
    }

    public void setmAppPackage(String mAppPackage) {
        this.mAppPackage = mAppPackage;
    }

    public Drawable getmAppIcon() {
        return mAppIcon;
    }

    public void setmAppIcon(Drawable mAppIcon) {
        this.mAppIcon = mAppIcon;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public ResolveInfo getAppInfo() {
        return mResolveInfo;
    }

    public void setAppInfo(ResolveInfo mResolveInfo) {
        this.mResolveInfo = mResolveInfo;
    }


    public static class AppsPriorityComparator implements Comparator<AppModel> {
        public int compare(AppModel app_1, AppModel app_2) {
            return app_1.getAppPriorityCount() - app_2.getAppPriorityCount();
        }
    }
}
