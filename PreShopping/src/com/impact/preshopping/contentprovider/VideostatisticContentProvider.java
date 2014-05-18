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

public class VideostatisticContentProvider extends ContentProvider {

	private MySqlHelper dbHelper;
	private static HashMap<String, String> VIDEOSTATISTIC_PROJECTION_MAP;
	private static final String TABLE_NAME = "videostatistic";
	private static final String AUTHORITY = "com.impact.preshopping.contentprovider.videostatisticcontentprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	public static final Uri WATCHEDTYPE_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/watchedtype");
	public static final Uri USERID_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/userid");
	public static final Uri PUSHID_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/pushid");
	public static final Uri OTHER1_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/other1");
	public static final Uri OTHER2_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/other2");

	public static final String DEFAULT_SORT_ORDER = "watchedType ASC";

	private static final UriMatcher URL_MATCHER;

	private static final int VIDEOSTATISTIC = 1;
	private static final int VIDEOSTATISTIC_WATCHEDTYPE = 2;
	private static final int VIDEOSTATISTIC_USERID = 3;
	private static final int VIDEOSTATISTIC_PUSHID = 4;
	private static final int VIDEOSTATISTIC_OTHER1 = 5;
	private static final int VIDEOSTATISTIC_OTHER2 = 6;

	// Content values keys (using column names)
	public static final String WATCHEDTYPE = "watchedType";
	public static final String USERID = "userId";
	public static final String PUSHID = "pushId";
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
		case VIDEOSTATISTIC:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(VIDEOSTATISTIC_PROJECTION_MAP);
			break;
		case VIDEOSTATISTIC_WATCHEDTYPE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("watchedtype='" + url.getPathSegments().get(2) + "'");
			break;
		case VIDEOSTATISTIC_USERID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("userid='" + url.getPathSegments().get(2) + "'");
			break;
		case VIDEOSTATISTIC_PUSHID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("pushid='" + url.getPathSegments().get(2) + "'");
			break;
		case VIDEOSTATISTIC_OTHER1:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("other1='" + url.getPathSegments().get(2) + "'");
			break;
		case VIDEOSTATISTIC_OTHER2:
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
		case VIDEOSTATISTIC:
			return "vnd.android.cursor.dir/vnd.com.impact.preshopping.contentprovider.videostatistic";
		case VIDEOSTATISTIC_WATCHEDTYPE:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.videostatistic";
		case VIDEOSTATISTIC_USERID:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.videostatistic";
		case VIDEOSTATISTIC_PUSHID:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.videostatistic";
		case VIDEOSTATISTIC_OTHER1:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.videostatistic";
		case VIDEOSTATISTIC_OTHER2:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.videostatistic";

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
		if (URL_MATCHER.match(url) != VIDEOSTATISTIC) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		rowID = mDB.insert("videostatistic", "videostatistic", values);
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
		case VIDEOSTATISTIC:
			count = mDB.delete(TABLE_NAME, where, whereArgs);
			break;
		case VIDEOSTATISTIC_WATCHEDTYPE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "watchedtype=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case VIDEOSTATISTIC_USERID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "userid=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case VIDEOSTATISTIC_PUSHID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "pushid=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case VIDEOSTATISTIC_OTHER1:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "other1=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case VIDEOSTATISTIC_OTHER2:
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
		case VIDEOSTATISTIC:
			count = mDB.update(TABLE_NAME, values, where, whereArgs);
			break;
		case VIDEOSTATISTIC_WATCHEDTYPE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "watchedtype=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case VIDEOSTATISTIC_USERID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "userid=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case VIDEOSTATISTIC_PUSHID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "pushid=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case VIDEOSTATISTIC_OTHER1:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "other1=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case VIDEOSTATISTIC_OTHER2:
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
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), VIDEOSTATISTIC);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/watchedtype" + "/*", VIDEOSTATISTIC_WATCHEDTYPE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/userid" + "/*", VIDEOSTATISTIC_USERID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/pushid" + "/*", VIDEOSTATISTIC_PUSHID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/other1" + "/*", VIDEOSTATISTIC_OTHER1);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/other2" + "/*", VIDEOSTATISTIC_OTHER2);

		VIDEOSTATISTIC_PROJECTION_MAP = new HashMap<String, String>();
		VIDEOSTATISTIC_PROJECTION_MAP.put(WATCHEDTYPE, "watchedtype");
		VIDEOSTATISTIC_PROJECTION_MAP.put(USERID, "userid");
		VIDEOSTATISTIC_PROJECTION_MAP.put(PUSHID, "pushid");
		VIDEOSTATISTIC_PROJECTION_MAP.put(OTHER1, "other1");
		VIDEOSTATISTIC_PROJECTION_MAP.put(OTHER2, "other2");

	}
}
