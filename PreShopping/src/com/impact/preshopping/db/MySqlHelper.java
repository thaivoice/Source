/*
 ******************************************************************************
 * Parts of this code sample are licensed under Apache License, Version 2.0   *
 * Copyright (c) 2009, Android Open Handset Alliance. All rights reserved.    *
 *																			  *																			*
 * Except as noted, this code sample is offered under a modified BSD license. *
 * Copyright (C) 2010, Motorola Mobility, Inc. All rights reserved.           *
 * 																			  *
 * For more details, see MOTODEV_Studio_for_Android_LicenseNotices.pdf        * 
 * in your installation folder.                                               *
 ******************************************************************************
 */
package com.impact.preshopping.db;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.impact.preshopping.R;


public class MySqlHelper extends SQLiteOpenHelper {

    private static String DB_PATH;
	private static String DB_NAME = "preshopping.db";
	private final Context myContext;

    private MySqlHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
        try {
            DB_PATH  = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.dataDir + "/databases/";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static MySqlHelper helper;
    public static synchronized MySqlHelper getInstance(Context context) {

        if (helper == null) {
            helper = new MySqlHelper(context);
        }

        return helper;
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			Log.w("db", "Creating db...");
			db.execSQL(myContext.getString(R.string.db_create_address));
			db.execSQL(myContext.getString(R.string.db_create_company));
			db.execSQL(myContext.getString(R.string.db_create_media));
			db.execSQL(myContext.getString(R.string.db_create_prodCategory));
			db.execSQL(myContext.getString(R.string.db_create_prodGroup));
			db.execSQL(myContext.getString(R.string.db_create_product));
			db.execSQL(myContext.getString(R.string.db_create_pushNotification));
			db.execSQL(myContext.getString(R.string.db_create_setting));
			db.execSQL(myContext.getString(R.string.db_create_user));
			db.execSQL(myContext.getString(R.string.db_create_videoStatistic));
			db.execSQL(myContext.getString(R.string.db_create_dbversion));
			db.execSQL(myContext.getString(R.string.db_create_registration_info));
			db.execSQL(myContext.getString(R.string.db_create_push_tbl));
			
			Log.w("db", "Creating db...END...");
		} catch (Exception e) {
			Log.e("db", "" + e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// fill in your code here
	}
}
