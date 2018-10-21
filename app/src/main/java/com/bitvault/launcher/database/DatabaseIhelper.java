package com.bitvault.launcher.database;

/**
 * Store app database table name and colum names
 *
 * @author Anshuman
 */
public interface DatabaseIhelper {

    /* Database details */
    String DATABASE_NAME = "bitvaultapps.db";
    int DATABASE_VERSION = 1;
    String DATABASE_PREFS_NAME = "InstallDatabase";

    /*Apps Data Table*/
    String DB_APPS_TABLE = "apps";
    String DB_APPS_ROW_NAME = "app_name";
    String DB_APPS_ROW_PACKAGE_NAME = "app_package";
    String DB_APPS_ROW_PRIORITY_COUNT = "app_priority_count";

    /*Hidden Apps Table*/
    String DB_HIDDEN_APPS_TABLE = "hidden_apps";
    String DB_HIDDEN_APPS_ROW_NAME = "app_name";
    String DB_HIDDEN_APPS_ROW_PACKAGE = "app_package";


}
