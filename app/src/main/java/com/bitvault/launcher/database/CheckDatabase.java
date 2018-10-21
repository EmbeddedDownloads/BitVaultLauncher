package com.bitvault.launcher.database;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.bitvault.launcher.Utils.AndroidAppUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CheckDatabase {
    Activity mActivity;
    String DB_PATH;
    /**
     * Tag for debugging purpose
     */
    String TAG = CheckDatabase.class.getSimpleName();

    public CheckDatabase(Activity mActivity) {
        // TODO Auto-generated constructor stub
        this.mActivity = mActivity;
        copyDatabase();
    }

    @SuppressWarnings("static-access")
    @SuppressLint({"SdCardPath", "WorldWriteableFiles"})
    private void copyDatabase() {
        // TODO Auto-generated method stub
        @SuppressWarnings("deprecation")
        SharedPreferences settings = mActivity.getSharedPreferences(
                DatabaseIhelper.DATABASE_PREFS_NAME,
                mActivity.MODE_PRIVATE);
        boolean silent = settings.getBoolean("install", false);
        if (!silent) {
            DB_PATH = "/data/data/"
                    + mActivity.getApplicationContext().getPackageName()
                    + "/databases/";
            boolean dbexist = checkdatabase();
            if (dbexist) {
                AndroidAppUtils.showLog(TAG, "Database exists");
            } else {
                copyAssets();
            }
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("install", true);
            editor.commit();
            AndroidAppUtils.showLog(TAG, "Copy Database Complete");
        }
    }

    private boolean checkdatabase() {
        // TODO Auto-generated method stub
        boolean checkdb = false;
        try {
            String myPath = DB_PATH + DatabaseIhelper.DATABASE_NAME;
            File dbfile = new File(myPath);

            checkdb = dbfile.exists();
        } catch (SQLiteException e) {
            AndroidAppUtils.showLog(TAG, "Database doesn't exist");
        }
        return checkdb;
    }

    @SuppressLint("SdCardPath")
    private void copyAssets() {
        // TODO Auto-generated method stub
        String DB_PATH = "/data/data/"
                + mActivity.getApplicationContext().getPackageName()
                + "/databases/";

        AssetManager assetManager = mActivity.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        for (String filename : files) {
            if (filename.equals(DatabaseIhelper.DATABASE_NAME)) {
                DB_PATH = "/data/data/"
                        + mActivity.getApplicationContext().getPackageName()
                        + "/databases/";

                InputStream in = null;
                OutputStream out = null;
                try {
                    in = assetManager.open(filename);

                    File dir = new File(DB_PATH);
                    if (!dir.exists())
                        dir.mkdir();

                    out = new FileOutputStream(DB_PATH + filename);
                    copyFile(in, out);

                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) {
        // TODO Auto-generated method stub
        byte[] buffer = new byte[1024];
        int read;
        try {
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
