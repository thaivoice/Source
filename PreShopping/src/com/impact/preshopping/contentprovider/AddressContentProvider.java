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

public class AddressContentProvider extends ContentProvider {

	private MySqlHelper dbHelper;
	private static HashMap<String, String> ADDRESS_PROJECTION_MAP;
	private static final String TABLE_NAME = "address";
	private static final String AUTHORITY = "com.impact.preshopping.contentprovider.addresscontentprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	public static final Uri ADDRESS_ID_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/address_id");
	public static final Uri ADDRESS_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/address");
	public static final Uri ADDRESS2_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/address2");
	public static final Uri DISTRICT_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/district");
	public static final Uri CITY_ID_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/city_id");
	public static final Uri POSTAL_CODE_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/postal_code");
	public static final Uri PHONE_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/phone");
	public static final Uri LAST_UPDATE_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/last_update");
	public static final Uri OTHER1_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/other1");
	public static final Uri OTHER2_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/other2");

	public static final String DEFAULT_SORT_ORDER = "address_id ASC";

	private static final UriMatcher URL_MATCHER;

	private static final int ADDRESS = 1;
	private static final int ADDRESS_ADDRESS_ID = 2;
	private static final int ADDRESS_ADDRESS = 3;
	private static final int ADDRESS_ADDRESS2 = 4;
	private static final int ADDRESS_DISTRICT = 5;
	private static final int ADDRESS_CITY_ID = 6;
	private static final int ADDRESS_POSTAL_CODE = 7;
	private static final int ADDRESS_PHONE = 8;
	private static final int ADDRESS_LAST_UPDATE = 9;
	private static final int ADDRESS_OTHER1 = 10;
	private static final int ADDRESS_OTHER2 = 11;

	// Content values keys (using column names)
	public static final String ADDRESS_ID = "address_id";
	public static final String ADDRESS1 = "address";
	public static final String ADDRESS2 = "address2";
	public static final String DISTRICT = "district";
	public static final String CITY_ID = "city_id";
	public static final String POSTAL_CODE = "postal_code";
	public static final String PHONE = "phone";
	public static final String LAST_UPDATE = "last_update";
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
		case ADDRESS:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(ADDRESS_PROJECTION_MAP);
			break;
		case ADDRESS_ADDRESS_ID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("address_id='" + url.getPathSegments().get(2) + "'");
			break;
		case ADDRESS_ADDRESS:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("address='" + url.getPathSegments().get(2) + "'");
			break;
		case ADDRESS_ADDRESS2:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("address2='" + url.getPathSegments().get(2) + "'");
			break;
		case ADDRESS_DISTRICT:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("district='" + url.getPathSegments().get(2) + "'");
			break;
		case ADDRESS_CITY_ID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("city_id='" + url.getPathSegments().get(2) + "'");
			break;
		case ADDRESS_POSTAL_CODE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("postal_code='" + url.getPathSegments().get(2) + "'");
			break;
		case ADDRESS_PHONE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("phone='" + url.getPathSegments().get(2) + "'");
			break;
		case ADDRESS_LAST_UPDATE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("last_update='" + url.getPathSegments().get(2) + "'");
			break;
		case ADDRESS_OTHER1:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("other1='" + url.getPathSegments().get(2) + "'");
			break;
		case ADDRESS_OTHER2:
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
		case ADDRESS:
			return "vnd.android.cursor.dir/vnd.com.impact.preshopping.contentprovider.address";
		case ADDRESS_ADDRESS_ID:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.address";
		case ADDRESS_ADDRESS:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.address";
		case ADDRESS_ADDRESS2:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.address";
		case ADDRESS_DISTRICT:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.address";
		case ADDRESS_CITY_ID:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.address";
		case ADDRESS_POSTAL_CODE:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.address";
		case ADDRESS_PHONE:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.address";
		case ADDRESS_LAST_UPDATE:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.address";
		case ADDRESS_OTHER1:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.address";
		case ADDRESS_OTHER2:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.address";

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
		if (URL_MATCHER.match(url) != ADDRESS) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		rowID = mDB.insert("address", "address", values);
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
		case ADDRESS:
			count = mDB.delete(TABLE_NAME, where, whereArgs);
			break;
		case ADDRESS_ADDRESS_ID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "address_id=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case ADDRESS_ADDRESS:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "address=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case ADDRESS_ADDRESS2:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "address2=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case ADDRESS_DISTRICT:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "district=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case ADDRESS_CITY_ID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "city_id=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case ADDRESS_POSTAL_CODE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "postal_code=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case ADDRESS_PHONE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "phone=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case ADDRESS_LAST_UPDATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "last_update=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case ADDRESS_OTHER1:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "other1=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case ADDRESS_OTHER2:
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
		case ADDRESS:
			count = mDB.update(TABLE_NAME, values, where, whereArgs);
			break;
		case ADDRESS_ADDRESS_ID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "address_id=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case ADDRESS_ADDRESS:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "address=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case ADDRESS_ADDRESS2:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "address2=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case ADDRESS_DISTRICT:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "district=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case ADDRESS_CITY_ID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "city_id=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case ADDRESS_POSTAL_CODE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "postal_code=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case ADDRESS_PHONE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "phone=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case ADDRESS_LAST_UPDATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "last_update=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case ADDRESS_OTHER1:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "other1=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case ADDRESS_OTHER2:
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
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), ADDRESS);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/address_id" + "/*", ADDRESS_ADDRESS_ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/address" + "/*", ADDRESS_ADDRESS);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/address2" + "/*", ADDRESS_ADDRESS2);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/district" + "/*", ADDRESS_DISTRICT);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/city_id" + "/*", ADDRESS_CITY_ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/postal_code" + "/*", ADDRESS_POSTAL_CODE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/phone" + "/*", ADDRESS_PHONE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/last_update" + "/*", ADDRESS_LAST_UPDATE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/other1" + "/*", ADDRESS_OTHER1);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/other2" + "/*", ADDRESS_OTHER2);

		ADDRESS_PROJECTION_MAP = new HashMap<String, String>();
		ADDRESS_PROJECTION_MAP.put(ADDRESS_ID, "address_id");
		ADDRESS_PROJECTION_MAP.put(ADDRESS1, "address");
		ADDRESS_PROJECTION_MAP.put(ADDRESS2, "address2");
		ADDRESS_PROJECTION_MAP.put(DISTRICT, "district");
		ADDRESS_PROJECTION_MAP.put(CITY_ID, "city_id");
		ADDRESS_PROJECTION_MAP.put(POSTAL_CODE, "postal_code");
		ADDRESS_PROJECTION_MAP.put(PHONE, "phone");
		ADDRESS_PROJECTION_MAP.put(LAST_UPDATE, "last_update");
		ADDRESS_PROJECTION_MAP.put(OTHER1, "other1");
		ADDRESS_PROJECTION_MAP.put(OTHER2, "other2");

	}
}
