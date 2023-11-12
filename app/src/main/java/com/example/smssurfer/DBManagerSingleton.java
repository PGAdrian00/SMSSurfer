package com.example.smssurfer;

import android.content.Context;

public class DBManagerSingleton {
    private static DBManager dbManager;

    public static synchronized DBManager getDBManager(Context context) {
        if (dbManager == null) {
            dbManager = new DBManager(context);
        }
        return dbManager;
    }
}
