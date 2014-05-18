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

public class PushnotificationContentProvider extends ContentProvider {

	private MySqlHelper dbHelper;
	private static HashMap<String, String> PUSHNOTIFICATION_PROJECTION_MAP;
	private static final String TABLE_NAME = "pushnotification";
	private static final String AUTHORITY = "com.impact.preshopping.contentprovider.pushnotificationcontentprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	public static final Uri _ID_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase());
	public static final Uri PUSHSUCCESS_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/pushsuccess");
	public static final Uri STARTDATE_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/startdate");
	public static final Uri ENDDATE_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/enddate");
	public static final Uri DEVIDS_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/devids");
	public static final Uri PRODUCTID_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/productid");
	public static final Uri OTHER1_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/other1");
	public static final Uri OTHER2_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/other2");

	public static final String DEFAULT_SORT_ORDER = "_id ASC";

	private static final UriMatcher URL_MATCHER;

	private static final int PUSHNOTIFICATION = 1;
	private static final int PUSHNOTIFICATION__ID = 2;
	private static final int PUSHNOTIFICATION_PUSHSUCCESS = 3;
	private static final int PUSHNOTIFICATION_STARTDATE = 4;
	private static final int PUSHNOTIFICATION_ENDDATE = 5;
	private static final int PUSHNOTIFICATION_DEVIDS = 6;
	private static final int PUSHNOTIFICATION_PRODUCTID = 7;
	private static final int PUSHNOTIFICATION_OTHER1 = 8;
	private static final int PUSHNOTIFICATION_OTHER2 = 9;

	// Content values keys (using column names)
	public static final String _ID = "_id";
	public static final String PUSHSUCCESS = "pushSuccess";
	public static final String STARTDATE = "startDate";
	public static final String ENDDATE = "endDate";
	public static final String DEVIDS = "devIds";
	public static final String PRODUCTID = "productId";
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
		case PUSHNOTIFICATION:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(PUSHNOTIFICATION_PROJECTION_MAP);
			break;
		case PUSHNOTIFICATION__ID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("_id=" + url.getPathSegments().get(1));
			break;
		case PUSHNOTIFICATION_PUSHSUCCESS:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("pushsuccess='" + url.getPathSegments().get(2) + "'");
			break;
		case PUSHNOTIFICATION_STARTDATE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("startdate='" + url.getPathSegments().get(2) + "'");
			break;
		case PUSHNOTIFICATION_ENDDATE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("enddate='" + url.getPathSegments().get(2) + "'");
			break;
		case PUSHNOTIFICATION_DEVIDS:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("devids='" + url.getPathSegments().get(2) + "'");
			break;
		case PUSHNOTIFICATION_PRODUCTID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("productid='" + url.getPathSegments().get(2) + "'");
			break;
		case PUSHNOTIFICATION_OTHER1:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("other1='" + url.getPathSegments().get(2) + "'");
			break;
		case PUSHNOTIFICATION_OTHER2:
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
		case PUSHNOTIFICATION:
			return "vnd.android.cursor.dir/vnd.com.impact.preshopping.contentprovider.pushnotification";
		case PUSHNOTIFICATION__ID:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.pushnotification";
		case PUSHNOTIFICATION_PUSHSUCCESS:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.pushnotification";
		case PUSHNOTIFICATION_STARTDATE:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.pushnotification";
		case PUSHNOTIFICATION_ENDDATE:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.pushnotification";
		case PUSHNOTIFICATION_DEVIDS:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.pushnotification";
		case PUSHNOTIFICATION_PRODUCTID:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.pushnotification";
		case PUSHNOTIFICATION_OTHER1:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.pushnotification";
		case PUSHNOTIFICATION_OTHER2:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.pushnotification";

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
		if (URL_MATCHER.match(url) != PUSHNOTIFICATION) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		rowID = mDB.insert("pushnotification", "pushnotification", values);
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
		case PUSHNOTIFICATION:
			count = mDB.delete(TABLE_NAME, where, whereArgs);
			break;
		case PUSHNOTIFICATION__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.delete(TABLE_NAME, "_id=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PUSHNOTIFICATION_PUSHSUCCESS:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "pushsuccess=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PUSHNOTIFICATION_STARTDATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "startdate=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PUSHNOTIFICATION_ENDDATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "enddate=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PUSHNOTIFICATION_DEVIDS:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "devids=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PUSHNOTIFICATION_PRODUCTID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "productid=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PUSHNOTIFICATION_OTHER1:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "other1=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PUSHNOTIFICATION_OTHER2:
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
		case PUSHNOTIFICATION:
			count = mDB.update(TABLE_NAME, values, where, whereArgs);
			break;
		case PUSHNOTIFICATION__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.update(TABLE_NAME, values, "_id=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PUSHNOTIFICATION_PUSHSUCCESS:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "pushsuccess=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PUSHNOTIFICATION_STARTDATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "startdate=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PUSHNOTIFICATION_ENDDATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "enddate=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PUSHNOTIFICATION_DEVIDS:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "devids=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PUSHNOTIFICATION_PRODUCTID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "productid=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PUSHNOTIFICATION_OTHER1:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "other1=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PUSHNOTIFICATION_OTHER2:
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
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), PUSHNOTIFICATION);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/#", PUSHNOTIFICATION__ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/pushsuccess" + "/*", PUSHNOTIFICATION_PUSHSUCCESS);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/startdate" + "/*", PUSHNOTIFICATION_STARTDATE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/enddate" + "/*", PUSHNOTIFICATION_ENDDATE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/devids" + "/*", PUSHNOTIFICATION_DEVIDS);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/productid" + "/*", PUSHNOTIFICATION_PRODUCTID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/other1" + "/*", PUSHNOTIFICATION_OTHER1);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/other2" + "/*", PUSHNOTIFICATION_OTHER2);

		PUSHNOTIFICATION_PROJECTION_MAP = new HashMap<String, String>();
		PUSHNOTIFICATION_PROJECTION_MAP.put(_ID, "_id");
		PUSHNOTIFICATION_PROJECTION_MAP.put(PUSHSUCCESS, "pushsuccess");
		PUSHNOTIFICATION_PROJECTION_MAP.put(STARTDATE, "startdate");
		PUSHNOTIFICATION_PROJECTION_MAP.put(ENDDATE, "enddate");
		PUSHNOTIFICATION_PROJECTION_MAP.put(DEVIDS, "devids");
		PUSHNOTIFICATION_PROJECTION_MAP.put(PRODUCTID, "productid");
		PUSHNOTIFICATION_PROJECTION_MAP.put(OTHER1, "other1");
		PUSHNOTIFICATION_PROJECTION_MAP.put(OTHER2, "other2");

	}
}
