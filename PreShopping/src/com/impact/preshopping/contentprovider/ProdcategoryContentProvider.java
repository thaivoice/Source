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

public class ProdcategoryContentProvider extends ContentProvider {

	private MySqlHelper dbHelper;
	private static HashMap<String, String> PRODCATEGORY_PROJECTION_MAP;
	private static final String TABLE_NAME = "prodcategory";
	private static final String AUTHORITY = "com.impact.preshopping.contentprovider.prodcategorycontentprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	public static final Uri _ID_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase());
	public static final Uri NAME_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/name");
	public static final Uri ICON_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/icon");
	public static final Uri IMAGE_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/image");
	public static final Uri COMPANY_ID_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/company_id");
	public static final Uri OTHER1_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/other1");
	public static final Uri OTHER2_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/other2");

	public static final String DEFAULT_SORT_ORDER = "_id ASC";

	private static final UriMatcher URL_MATCHER;

	private static final int PRODCATEGORY = 1;
	private static final int PRODCATEGORY__ID = 2;
	private static final int PRODCATEGORY_NAME = 3;
	private static final int PRODCATEGORY_ICON = 4;
	private static final int PRODCATEGORY_IMAGE = 5;
	private static final int PRODCATEGORY_COMPANY_ID = 6;
	private static final int PRODCATEGORY_OTHER1 = 7;
	private static final int PRODCATEGORY_OTHER2 = 8;

	// Content values keys (using column names)
	public static final String _ID = "_id";
	public static final String NAME = "name";
	public static final String ICON = "icon";
	public static final String IMAGE = "image";
	public static final String COMPANY_ID = "company_id";
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
		case PRODCATEGORY:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(PRODCATEGORY_PROJECTION_MAP);
			break;
		case PRODCATEGORY__ID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("_id=" + url.getPathSegments().get(1));
			break;
		case PRODCATEGORY_NAME:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("name='" + url.getPathSegments().get(2) + "'");
			break;
		case PRODCATEGORY_ICON:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("icon='" + url.getPathSegments().get(2) + "'");
			break;
		case PRODCATEGORY_IMAGE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("image='" + url.getPathSegments().get(2) + "'");
			break;
		case PRODCATEGORY_COMPANY_ID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("company_id='" + url.getPathSegments().get(2) + "'");
			break;
		case PRODCATEGORY_OTHER1:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("other1='" + url.getPathSegments().get(2) + "'");
			break;
		case PRODCATEGORY_OTHER2:
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
		case PRODCATEGORY:
			return "vnd.android.cursor.dir/vnd.com.impact.preshopping.contentprovider.prodcategory";
		case PRODCATEGORY__ID:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.prodcategory";
		case PRODCATEGORY_NAME:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.prodcategory";
		case PRODCATEGORY_ICON:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.prodcategory";
		case PRODCATEGORY_IMAGE:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.prodcategory";
		case PRODCATEGORY_COMPANY_ID:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.prodcategory";
		case PRODCATEGORY_OTHER1:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.prodcategory";
		case PRODCATEGORY_OTHER2:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.prodcategory";

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
		if (URL_MATCHER.match(url) != PRODCATEGORY) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		rowID = mDB.insert("prodcategory", "prodcategory", values);
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
		case PRODCATEGORY:
			count = mDB.delete(TABLE_NAME, where, whereArgs);
			break;
		case PRODCATEGORY__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.delete(TABLE_NAME, "_id=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODCATEGORY_NAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "name=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODCATEGORY_ICON:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "icon=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODCATEGORY_IMAGE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "image=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODCATEGORY_COMPANY_ID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "company_id=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODCATEGORY_OTHER1:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "other1=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODCATEGORY_OTHER2:
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
		case PRODCATEGORY:
			count = mDB.update(TABLE_NAME, values, where, whereArgs);
			break;
		case PRODCATEGORY__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.update(TABLE_NAME, values, "_id=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODCATEGORY_NAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "name=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODCATEGORY_ICON:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "icon=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODCATEGORY_IMAGE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "image=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODCATEGORY_COMPANY_ID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "company_id=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODCATEGORY_OTHER1:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "other1=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODCATEGORY_OTHER2:
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
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), PRODCATEGORY);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/#", PRODCATEGORY__ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/name" + "/*", PRODCATEGORY_NAME);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/icon" + "/*", PRODCATEGORY_ICON);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/image" + "/*", PRODCATEGORY_IMAGE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/company_id" + "/*", PRODCATEGORY_COMPANY_ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/other1" + "/*", PRODCATEGORY_OTHER1);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/other2" + "/*", PRODCATEGORY_OTHER2);

		PRODCATEGORY_PROJECTION_MAP = new HashMap<String, String>();
		PRODCATEGORY_PROJECTION_MAP.put(_ID, "_id");
		PRODCATEGORY_PROJECTION_MAP.put(NAME, "name");
		PRODCATEGORY_PROJECTION_MAP.put(ICON, "icon");
		PRODCATEGORY_PROJECTION_MAP.put(IMAGE, "image");
		PRODCATEGORY_PROJECTION_MAP.put(COMPANY_ID, "company_id");
		PRODCATEGORY_PROJECTION_MAP.put(OTHER1, "other1");
		PRODCATEGORY_PROJECTION_MAP.put(OTHER2, "other2");

	}
}
