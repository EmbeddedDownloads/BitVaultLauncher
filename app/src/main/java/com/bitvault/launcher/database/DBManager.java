package com.bitvault.launcher.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bitvault.launcher.Model.AppModel;
import com.bitvault.launcher.Model.HiddenAppModel;
import com.bitvault.launcher.Utils.AndroidAppUtils;

import java.util.ArrayList;

import static com.bitvault.launcher.database.DatabaseIhelper.DATABASE_NAME;


public class DBManager {
    public static SQLiteDatabase sqliteDatabase;
    static String TAG = DBManager.class.getSimpleName();
    Context mCtx;
    DatabaseHelper databasehelper;

    /**
     * Initialize database manager
     *
     * @param ctx
     */
    public DBManager(Context ctx) {

        this.mCtx = ctx;
        databasehelper = new DatabaseHelper(mCtx);
    }

    /**
     * Open Database
     *
     * @return
     * @throws SQLException
     */
    public DBManager open() throws SQLException {

        sqliteDatabase = databasehelper.getWritableDatabase();
        return this;
    }

    /**
     * Close database
     */
    public void close() {
        // TODO Auto-generated method stub
        databasehelper.close();
    }

    /**
     * Insert Hidden App Data
     *
     * @param appName
     * @param appPackage
     */
    public void insertAppsIntoDB(String appName, String appPackage, int priorityCount) {
        appName = appName.replaceAll("\\s+", "-").replaceAll("'", "");
        if (!checkIsAppAlreadyIntoDB(appPackage, appName)) {
            ContentValues initialValues = new ContentValues();
        /*Add Application Name*/
            initialValues.put(DatabaseIhelper.DB_APPS_ROW_NAME,
                    appName);
        /*add Application package*/
            initialValues.put(DatabaseIhelper.DB_APPS_ROW_PACKAGE_NAME,
                    appPackage);
         /*add Application priorityCount*/
            initialValues.put(DatabaseIhelper.DB_APPS_ROW_PRIORITY_COUNT,
                    priorityCount);

            sqliteDatabase.insert(DatabaseIhelper.DB_APPS_TABLE, null,
                    initialValues);
        } else AndroidAppUtils.showInfoLog(TAG, "This App Package is alreday into database");

    }

    /**
     * Check that this app is already into database or not.
     *
     * @param appPackage
     * @return
     */
    private boolean checkIsAppAlreadyIntoDB(String appPackage, String appName) {

        appName = appName.replaceAll("\\s+", "-").replaceAll("'", "");
        String query = "SELECT * FROM " + DatabaseIhelper.DB_APPS_TABLE + " WHERE "
                + DatabaseIhelper.DB_APPS_ROW_PACKAGE_NAME + "='" + appPackage + "'"
                + " AND "
                + DatabaseIhelper.DB_APPS_ROW_NAME + "='" + appName + "'";
        Cursor cursor = sqliteDatabase.rawQuery(query, null);
        try {
            if (cursor.getCount() > 0)
                return true;
            else return false;
            // do some work with the cursor here.
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }
    }

    /**
     * Get List of app Installed Apps
     */
    public ArrayList<AppModel> getListOfApps() {
        String query = "SELECT * FROM " + DatabaseIhelper.DB_APPS_TABLE + " ORDER BY "
                + DatabaseIhelper.DB_APPS_ROW_PRIORITY_COUNT + " DESC";
        Cursor cursor = sqliteDatabase.rawQuery(query, new String[]{});
        ArrayList<AppModel> mListOfAppModels = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                AppModel model = new AppModel();
                model.setmAppName(cursor.getString(0));
                model.setmAppPackage(cursor.getString(1));
                model.setAppPriorityCount(cursor.getInt(2));

                mListOfAppModels.add(model);
            } while (cursor.moveToNext());
        }
        try {
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mListOfAppModels;
    }


    /**
     * Get List of app Installed Apps
     */
    public AppModel getAppsPriority(String appName, String appPackage) {
        appName = appName.replaceAll("\\s+", "-").replaceAll("'", "");
        String query = "SELECT * FROM " + DatabaseIhelper.DB_APPS_TABLE + " WHERE "
                + DatabaseIhelper.DB_APPS_ROW_PACKAGE_NAME + "='" + appPackage + "'"
                + " AND "
                + DatabaseIhelper.DB_APPS_ROW_NAME + "='" + appName + "'";
        Cursor cursor = sqliteDatabase.rawQuery(query, null);
        AppModel model = new AppModel();
        if (cursor.moveToFirst()) {
            do {
                model = new AppModel();
                model.setmAppName(cursor.getString(0));
                model.setmAppPackage(cursor.getString(1));
                model.setAppPriorityCount(cursor.getInt(2));
            } while (cursor.moveToNext());
        }
        try {
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }


    /**
     * Increase app Priority according to uses
     */
    public void updateAppPriorities(String appName, String appPackage) {
        appName = appName.replaceAll("\\s+", "-").replaceAll("'", "");
        AppModel mData = getAppsPriority(appName, appPackage);
        mData.setAppPriorityCount(mData.getAppPriorityCount() + 1);
        ContentValues initialValues = new ContentValues();
        /*Add Application Name*/
        initialValues.put(DatabaseIhelper.DB_APPS_ROW_NAME,
                appName);
        /*add Application package*/
        initialValues.put(DatabaseIhelper.DB_APPS_ROW_PACKAGE_NAME,
                appPackage);
         /*add Application priorityCount*/
        initialValues.put(DatabaseIhelper.DB_APPS_ROW_PRIORITY_COUNT,
                mData.getAppPriorityCount());
        sqliteDatabase.update(DatabaseIhelper.DB_APPS_TABLE, initialValues,
                DatabaseIhelper.DB_APPS_ROW_PACKAGE_NAME + " = ? AND " + DatabaseIhelper.DB_APPS_ROW_NAME + " = ?",
                new String[]{appPackage, appName});

    }


    /**
     * Insert Hidden App Data
     *
     * @param appName
     * @param appPackage
     */
    public void insertHiddenAppsIntoDB(String appName, String appPackage) {

        appName = appName.replaceAll("\\s+", "-").replaceAll("'", "");

        ContentValues initialValues = new ContentValues();

        initialValues.put(DatabaseIhelper.DB_HIDDEN_APPS_ROW_NAME,
                appName);
            /*add Application package*/
        initialValues.put(DatabaseIhelper.DB_HIDDEN_APPS_ROW_PACKAGE,
                appPackage);

        sqliteDatabase.insert(DatabaseIhelper.DB_HIDDEN_APPS_TABLE, null,
                initialValues);

    }

    /**
     * get Hidden Apps List
     */
    public ArrayList<HiddenAppModel> getHiddenAppsData() {
        String query = "SELECT * FROM " + DatabaseIhelper.DB_HIDDEN_APPS_TABLE;
        Cursor cursor = sqliteDatabase.rawQuery(query, null);
        ArrayList<HiddenAppModel> mListOfHiddenAppModels = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                HiddenAppModel model = new HiddenAppModel();
                model.setAppName(cursor.getString(0));
                model.setAppPackage(cursor.getString(1));

                mListOfHiddenAppModels.add(model);
            } while (cursor.moveToNext());
        }
        try {
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mListOfHiddenAppModels;
    }

    /**
     * Empty Any Table
     *
     * @return
     */
    public void emptyTable(String tableName) {
        try {
            sqliteDatabase.delete(tableName, "1", null);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * This is class for database helper and update
     */
    public class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null,
                    DatabaseIhelper.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onUpgrade(SQLiteDatabase paramSQLiteDatabase,
                              int paramInt1, int paramInt2) {

            AndroidAppUtils.showLog(TAG, "onUpgrade :" + paramInt1 + " and " + paramInt2);


        }
    }
}
