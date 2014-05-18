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

public class ProductContentProvider extends ContentProvider {

	private MySqlHelper dbHelper;
	private static HashMap<String, String> PRODUCT_PROJECTION_MAP;
	private static final String TABLE_NAME = "product";
	private static final String AUTHORITY = "com.impact.preshopping.contentprovider.productcontentprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	public static final Uri _ID_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase());
	public static final Uri SHORTNAME_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/shortname");
	public static final Uri LONGNAME_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/longname");
	public static final Uri SHORTDESC_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/shortdesc");
	public static final Uri LONGDESC_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/longdesc");
	public static final Uri RATING_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/rating");
	public static final Uri ICON_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/icon");
	public static final Uri QRDATA_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/qrdata");
	public static final Uri MAKER_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/maker");
	public static final Uri MODEL_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/model");
	public static final Uri CREATEDBY_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/createdby");
	public static final Uri GROUPID_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/groupid");
	public static final Uri OTHER1_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/other1");
	public static final Uri OTHER2_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/other2");

	public static final String DEFAULT_SORT_ORDER = "_id ASC";

	private static final UriMatcher URL_MATCHER;

	private static final int PRODUCT = 1;
	private static final int PRODUCT__ID = 2;
	private static final int PRODUCT_SHORTNAME = 3;
	private static final int PRODUCT_LONGNAME = 4;
	private static final int PRODUCT_SHORTDESC = 5;
	private static final int PRODUCT_LONGDESC = 6;
	private static final int PRODUCT_RATING = 7;
	private static final int PRODUCT_ICON = 8;
	private static final int PRODUCT_QRDATA = 9;
	private static final int PRODUCT_MAKER = 10;
	private static final int PRODUCT_MODEL = 11;
	private static final int PRODUCT_CREATEDBY = 12;
	private static final int PRODUCT_GROUPID = 13;
	private static final int PRODUCT_OTHER1 = 14;
	private static final int PRODUCT_OTHER2 = 15;

	// Content values keys (using column names)
	public static final String _ID = "_id";
	public static final String SHORTNAME = "shortName";
	public static final String LONGNAME = "longName";
	public static final String SHORTDESC = "shortDesc";
	public static final String LONGDESC = "longDesc";
	public static final String RATING = "rating";
	public static final String ICON = "icon";
	public static final String QRDATA = "qrData";
	public static final String MAKER = "maker";
	public static final String MODEL = "model";
	public static final String CREATEDBY = "createdBy";
	public static final String GROUPID = "groupId";
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
		case PRODUCT:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(PRODUCT_PROJECTION_MAP);
			break;
		case PRODUCT__ID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("_id=" + url.getPathSegments().get(1));
			break;
		case PRODUCT_SHORTNAME:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("shortname='" + url.getPathSegments().get(2) + "'");
			break;
		case PRODUCT_LONGNAME:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("longname='" + url.getPathSegments().get(2) + "'");
			break;
		case PRODUCT_SHORTDESC:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("shortdesc='" + url.getPathSegments().get(2) + "'");
			break;
		case PRODUCT_LONGDESC:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("longdesc='" + url.getPathSegments().get(2) + "'");
			break;
		case PRODUCT_RATING:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("rating='" + url.getPathSegments().get(2) + "'");
			break;
		case PRODUCT_ICON:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("icon='" + url.getPathSegments().get(2) + "'");
			break;
		case PRODUCT_QRDATA:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("qrdata='" + url.getPathSegments().get(2) + "'");
			break;
		case PRODUCT_MAKER:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("maker='" + url.getPathSegments().get(2) + "'");
			break;
		case PRODUCT_MODEL:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("model='" + url.getPathSegments().get(2) + "'");
			break;
		case PRODUCT_CREATEDBY:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("createdby='" + url.getPathSegments().get(2) + "'");
			break;
		case PRODUCT_GROUPID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("groupid='" + url.getPathSegments().get(2) + "'");
			break;
		case PRODUCT_OTHER1:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("other1='" + url.getPathSegments().get(2) + "'");
			break;
		case PRODUCT_OTHER2:
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
		case PRODUCT:
			return "vnd.android.cursor.dir/vnd.com.impact.preshopping.contentprovider.product";
		case PRODUCT__ID:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.product";
		case PRODUCT_SHORTNAME:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.product";
		case PRODUCT_LONGNAME:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.product";
		case PRODUCT_SHORTDESC:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.product";
		case PRODUCT_LONGDESC:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.product";
		case PRODUCT_RATING:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.product";
		case PRODUCT_ICON:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.product";
		case PRODUCT_QRDATA:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.product";
		case PRODUCT_MAKER:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.product";
		case PRODUCT_MODEL:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.product";
		case PRODUCT_CREATEDBY:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.product";
		case PRODUCT_GROUPID:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.product";
		case PRODUCT_OTHER1:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.product";
		case PRODUCT_OTHER2:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.product";

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
		if (URL_MATCHER.match(url) != PRODUCT) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		rowID = mDB.insert("product", "product", values);
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
		case PRODUCT:
			count = mDB.delete(TABLE_NAME, where, whereArgs);
			break;
		case PRODUCT__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.delete(TABLE_NAME, "_id=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_SHORTNAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "shortname=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_LONGNAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "longname=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_SHORTDESC:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "shortdesc=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_LONGDESC:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "longdesc=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_RATING:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "rating=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_ICON:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "icon=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_QRDATA:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "qrdata=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_MAKER:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "maker=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_MODEL:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "model=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_CREATEDBY:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "createdby=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_GROUPID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "groupid=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_OTHER1:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "other1=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_OTHER2:
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
		case PRODUCT:
			count = mDB.update(TABLE_NAME, values, where, whereArgs);
			break;
		case PRODUCT__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.update(TABLE_NAME, values, "_id=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_SHORTNAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "shortname=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_LONGNAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "longname=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_SHORTDESC:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "shortdesc=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_LONGDESC:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "longdesc=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_RATING:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "rating=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_ICON:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "icon=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_QRDATA:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "qrdata=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_MAKER:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "maker=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_MODEL:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "model=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_CREATEDBY:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "createdby=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_GROUPID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "groupid=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_OTHER1:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "other1=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case PRODUCT_OTHER2:
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
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), PRODUCT);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/#", PRODUCT__ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/shortname" + "/*", PRODUCT_SHORTNAME);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/longname" + "/*", PRODUCT_LONGNAME);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/shortdesc" + "/*", PRODUCT_SHORTDESC);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/longdesc" + "/*", PRODUCT_LONGDESC);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/rating" + "/*", PRODUCT_RATING);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/icon" + "/*", PRODUCT_ICON);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/qrdata" + "/*", PRODUCT_QRDATA);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/maker" + "/*", PRODUCT_MAKER);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/model" + "/*", PRODUCT_MODEL);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/createdby" + "/*", PRODUCT_CREATEDBY);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/groupid" + "/*", PRODUCT_GROUPID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/other1" + "/*", PRODUCT_OTHER1);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/other2" + "/*", PRODUCT_OTHER2);

		PRODUCT_PROJECTION_MAP = new HashMap<String, String>();
		PRODUCT_PROJECTION_MAP.put(_ID, "_id");
		PRODUCT_PROJECTION_MAP.put(SHORTNAME, "shortname");
		PRODUCT_PROJECTION_MAP.put(LONGNAME, "longname");
		PRODUCT_PROJECTION_MAP.put(SHORTDESC, "shortdesc");
		PRODUCT_PROJECTION_MAP.put(LONGDESC, "longdesc");
		PRODUCT_PROJECTION_MAP.put(RATING, "rating");
		PRODUCT_PROJECTION_MAP.put(ICON, "icon");
		PRODUCT_PROJECTION_MAP.put(QRDATA, "qrdata");
		PRODUCT_PROJECTION_MAP.put(MAKER, "maker");
		PRODUCT_PROJECTION_MAP.put(MODEL, "model");
		PRODUCT_PROJECTION_MAP.put(CREATEDBY, "createdby");
		PRODUCT_PROJECTION_MAP.put(GROUPID, "groupid");
		PRODUCT_PROJECTION_MAP.put(OTHER1, "other1");
		PRODUCT_PROJECTION_MAP.put(OTHER2, "other2");

	}
}
