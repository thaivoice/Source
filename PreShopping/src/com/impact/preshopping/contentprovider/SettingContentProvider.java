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
package com.impact.preshopping.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import com.impact.preshopping.db.MySqlHelper;

import java.util.HashMap;

public class SettingContentProvider extends ContentProvider {

	private MySqlHelper dbHelper;
	private static HashMap<String, String> SETTING_PROJECTION_MAP;
	private static final String TABLE_NAME = "setting";
	private static final String AUTHORITY = "com.impact.preshopping.contentprovider.settingcontentprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	public static final Uri _ID_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase());
	public static final Uri DOWNLOADPERMISION_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/downloadpermision");
	public static final Uri HOMESCREENTYPE_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/homescreentype");
	public static final Uri PRODCATEGORIES_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/prodcategories");
	public static final Uri PRODGROUPS_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/prodgroups");
	public static final Uri PRODUCTS_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/products");
	public static final Uri NOTIFICATIONONOFF_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/notificationonoff");
	public static final Uri ALERTTYPE_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/alerttype");
	public static final Uri SOUNDFILE_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/soundfile");
	public static final Uri USERID_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/userid");
	public static final Uri OTHER1_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/other1");
	public static final Uri OTHER2_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/other2");

	public static final String DEFAULT_SORT_ORDER = "_id ASC";

	private static final UriMatcher URL_MATCHER;

	private static final int SETTING = 1;
	private static final int SETTING__ID = 2;
	private static final int SETTING_DOWNLOADPERMISION = 3;
	private static final int SETTING_HOMESCREENTYPE = 4;
	private static final int SETTING_PRODCATEGORIES = 5;
	private static final int SETTING_PRODGROUPS = 6;
	private static final int SETTING_PRODUCTS = 7;
	private static final int SETTING_NOTIFICATIONONOFF = 8;
	private static final int SETTING_ALERTTYPE = 9;
	private static final int SETTING_SOUNDFILE = 10;
	private static final int SETTING_USERID = 11;
	private static final int SETTING_OTHER1 = 12;
	private static final int SETTING_OTHER2 = 13;

	// Content values keys (using column names)
	public static final String _ID = "_id";
	public static final String DOWNLOADPERMISION = "downloadPermision";
	public static final String HOMESCREENTYPE = "homeScreenType";
	public static final String PRODCATEGORIES = "prodCategories";
	public static final String PRODGROUPS = "prodGroups";
	public static final String PRODUCTS = "products";
	public static final String NOTIFICATIONONOFF = "notificationOnOff";
	public static final String ALERTTYPE = "alertType";
	public static final String SOUNDFILE = "soundFile";
	public static final String USERID = "userId";
	public static final String OTHER1 = "other1";
	public static final String OTHER2 = "other2";

	public boolean onCreate() {
		dbHelper = MySqlHelper.getInstance(getContext());
		return (dbHelper == null) ? false : true;
	}

	public Cursor query(Uri url, String[] projection, String selection, String[] selectionArgs, String sort) {
		SQLiteDatabase mDB = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (URL_MATCHER.match(url)) {
		case SETTING:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(SETTING_PROJECTION_MAP);
			break;
		case SETTING__ID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("_id=" + url.getPathSegments().get(1));
			break;
		case SETTING_DOWNLOADPERMISION:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("downloadpermision='" + url.getPathSegments().get(2) + "'");
			break;
		case SETTING_HOMESCREENTYPE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("homescreentype='" + url.getPathSegments().get(2) + "'");
			break;
		case SETTING_PRODCATEGORIES:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("prodcategories='" + url.getPathSegments().get(2) + "'");
			break;
		case SETTING_PRODGROUPS:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("prodgroups='" + url.getPathSegments().get(2) + "'");
			break;
		case SETTING_PRODUCTS:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("products='" + url.getPathSegments().get(2) + "'");
			break;
		case SETTING_NOTIFICATIONONOFF:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("notificationonoff='" + url.getPathSegments().get(2) + "'");
			break;
		case SETTING_ALERTTYPE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("alerttype='" + url.getPathSegments().get(2) + "'");
			break;
		case SETTING_SOUNDFILE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("soundfile='" + url.getPathSegments().get(2) + "'");
			break;
		case SETTING_USERID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("userid='" + url.getPathSegments().get(2) + "'");
			break;
		case SETTING_OTHER1:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("other1='" + url.getPathSegments().get(2) + "'");
			break;
		case SETTING_OTHER2:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("other2='" + url.getPathSegments().get(2) + "'");
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
		String orderBy = "";
		if (TextUtils.isEmpty(sort)) {
			orderBy = DEFAULT_SORT_ORDER;
		} else {
			orderBy = sort;
		}
		Cursor c = qb.query(mDB, projection, selection, selectionArgs, null, null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), url);
		return c;
	}

	public String getType(Uri url) {
		switch (URL_MATCHER.match(url)) {
		case SETTING:
			return "vnd.android.cursor.dir/vnd.com.impact.preshopping.contentprovider.setting";
		case SETTING__ID:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.setting";
		case SETTING_DOWNLOADPERMISION:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.setting";
		case SETTING_HOMESCREENTYPE:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.setting";
		case SETTING_PRODCATEGORIES:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.setting";
		case SETTING_PRODGROUPS:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.setting";
		case SETTING_PRODUCTS:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.setting";
		case SETTING_NOTIFICATIONONOFF:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.setting";
		case SETTING_ALERTTYPE:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.setting";
		case SETTING_SOUNDFILE:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.setting";
		case SETTING_USERID:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.setting";
		case SETTING_OTHER1:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.setting";
		case SETTING_OTHER2:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.setting";

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
	}

	public Uri insert(Uri url, ContentValues initialValues) {
		SQLiteDatabase mDB = dbHelper.getWritableDatabase();
		long rowID;
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}
		if (URL_MATCHER.match(url) != SETTING) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		rowID = mDB.insert("setting", "setting", values);
		if (rowID > 0) {
			Uri uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(uri, null);
			return uri;
		}
		throw new SQLException("Failed to insert row into " + url);
	}

	public int delete(Uri url, String where, String[] whereArgs) {
		SQLiteDatabase mDB = dbHelper.getWritableDatabase();
		int count;
		String segment = "";
		switch (URL_MATCHER.match(url)) {
		case SETTING:
			count = mDB.delete(TABLE_NAME, where, whereArgs);
			break;
		case SETTING__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.delete(TABLE_NAME, "_id=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_DOWNLOADPERMISION:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "downloadpermision=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_HOMESCREENTYPE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "homescreentype=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_PRODCATEGORIES:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "prodcategories=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_PRODGROUPS:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "prodgroups=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_PRODUCTS:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "products=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_NOTIFICATIONONOFF:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "notificationonoff=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_ALERTTYPE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "alerttype=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_SOUNDFILE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "soundfile=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_USERID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "userid=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_OTHER1:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "other1=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_OTHER2:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "other2=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}

	public int update(Uri url, ContentValues values, String where, String[] whereArgs) {
		SQLiteDatabase mDB = dbHelper.getWritableDatabase();
		int count;
		String segment = "";
		switch (URL_MATCHER.match(url)) {
		case SETTING:
			count = mDB.update(TABLE_NAME, values, where, whereArgs);
			break;
		case SETTING__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.update(TABLE_NAME, values, "_id=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_DOWNLOADPERMISION:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "downloadpermision=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_HOMESCREENTYPE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "homescreentype=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_PRODCATEGORIES:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "prodcategories=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_PRODGROUPS:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "prodgroups=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_PRODUCTS:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "products=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_NOTIFICATIONONOFF:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "notificationonoff=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_ALERTTYPE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "alerttype=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_SOUNDFILE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "soundfile=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_USERID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "userid=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_OTHER1:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "other1=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case SETTING_OTHER2:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "other2=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}

	static {
		URL_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), SETTING);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/#", SETTING__ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/downloadpermision" + "/*", SETTING_DOWNLOADPERMISION);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/homescreentype" + "/*", SETTING_HOMESCREENTYPE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/prodcategories" + "/*", SETTING_PRODCATEGORIES);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/prodgroups" + "/*", SETTING_PRODGROUPS);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/products" + "/*", SETTING_PRODUCTS);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/notificationonoff" + "/*", SETTING_NOTIFICATIONONOFF);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/alerttype" + "/*", SETTING_ALERTTYPE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/soundfile" + "/*", SETTING_SOUNDFILE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/userid" + "/*", SETTING_USERID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/other1" + "/*", SETTING_OTHER1);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/other2" + "/*", SETTING_OTHER2);

		SETTING_PROJECTION_MAP = new HashMap<String, String>();
		SETTING_PROJECTION_MAP.put(_ID, "_id");
		SETTING_PROJECTION_MAP.put(DOWNLOADPERMISION, "downloadpermision");
		SETTING_PROJECTION_MAP.put(HOMESCREENTYPE, "homescreentype");
		SETTING_PROJECTION_MAP.put(PRODCATEGORIES, "prodcategories");
		SETTING_PROJECTION_MAP.put(PRODGROUPS, "prodgroups");
		SETTING_PROJECTION_MAP.put(PRODUCTS, "products");
		SETTING_PROJECTION_MAP.put(NOTIFICATIONONOFF, "notificationonoff");
		SETTING_PROJECTION_MAP.put(ALERTTYPE, "alerttype");
		SETTING_PROJECTION_MAP.put(SOUNDFILE, "soundfile");
		SETTING_PROJECTION_MAP.put(USERID, "userid");
		SETTING_PROJECTION_MAP.put(OTHER1, "other1");
		SETTING_PROJECTION_MAP.put(OTHER2, "other2");

	}
}
