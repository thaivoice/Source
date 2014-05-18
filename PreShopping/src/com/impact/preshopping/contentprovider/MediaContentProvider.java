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

public class MediaContentProvider extends ContentProvider {

	private MySqlHelper dbHelper;
	private static HashMap<String, String> MEDIA_PROJECTION_MAP;
	private static final String TABLE_NAME = "media";
	private static final String AUTHORITY = "com.impact.preshopping.contentprovider.mediacontentprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	public static final Uri _ID_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase());
	public static final Uri URL_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/url");
	public static final Uri FILESIZE_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/filesize");
	public static final Uri SHORTDESC_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/shortdesc");
	public static final Uri LONGDESC_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/longdesc");
	public static final Uri TYPE_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/type");
	public static final Uri PRODUCTID_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/productid");
	public static final Uri OTHER1_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/other1");
	public static final Uri OTHER2_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/other2");

	public static final String DEFAULT_SORT_ORDER = "_id ASC";

	private static final UriMatcher URL_MATCHER;

	private static final int MEDIA = 1;
	private static final int MEDIA__ID = 2;
	private static final int MEDIA_URL = 3;
	private static final int MEDIA_FILESIZE = 4;
	private static final int MEDIA_SHORTDESC = 5;
	private static final int MEDIA_LONGDESC = 6;
	private static final int MEDIA_TYPE = 7;
	private static final int MEDIA_PRODUCTID = 8;
	private static final int MEDIA_OTHER1 = 9;
	private static final int MEDIA_OTHER2 = 10;

	// Content values keys (using column names)
	public static final String _ID = "_id";
	public static final String URL = "url";
	public static final String FILESIZE = "fileSize";
	public static final String SHORTDESC = "shortDesc";
	public static final String LONGDESC = "longDesc";
	public static final String TYPE = "type";
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
		case MEDIA:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(MEDIA_PROJECTION_MAP);
			break;
		case MEDIA__ID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("_id=" + url.getPathSegments().get(1));
			break;
		case MEDIA_URL:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("url='" + url.getPathSegments().get(2) + "'");
			break;
		case MEDIA_FILESIZE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("filesize='" + url.getPathSegments().get(2) + "'");
			break;
		case MEDIA_SHORTDESC:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("shortdesc='" + url.getPathSegments().get(2) + "'");
			break;
		case MEDIA_LONGDESC:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("longdesc='" + url.getPathSegments().get(2) + "'");
			break;
		case MEDIA_TYPE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("type='" + url.getPathSegments().get(2) + "'");
			break;
		case MEDIA_PRODUCTID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("productid='" + url.getPathSegments().get(2) + "'");
			break;
		case MEDIA_OTHER1:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("other1='" + url.getPathSegments().get(2) + "'");
			break;
		case MEDIA_OTHER2:
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
		case MEDIA:
			return "vnd.android.cursor.dir/vnd.com.impact.preshopping.contentprovider.media";
		case MEDIA__ID:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.media";
		case MEDIA_URL:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.media";
		case MEDIA_FILESIZE:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.media";
		case MEDIA_SHORTDESC:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.media";
		case MEDIA_LONGDESC:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.media";
		case MEDIA_TYPE:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.media";
		case MEDIA_PRODUCTID:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.media";
		case MEDIA_OTHER1:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.media";
		case MEDIA_OTHER2:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.media";

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
		if (URL_MATCHER.match(url) != MEDIA) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		rowID = mDB.insert("media", "media", values);
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
		case MEDIA:
			count = mDB.delete(TABLE_NAME, where, whereArgs);
			break;
		case MEDIA__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.delete(TABLE_NAME, "_id=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case MEDIA_URL:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "url=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case MEDIA_FILESIZE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "filesize=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case MEDIA_SHORTDESC:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "shortdesc=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case MEDIA_LONGDESC:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "longdesc=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case MEDIA_TYPE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "type=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case MEDIA_PRODUCTID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "productid=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case MEDIA_OTHER1:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "other1=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case MEDIA_OTHER2:
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
		case MEDIA:
			count = mDB.update(TABLE_NAME, values, where, whereArgs);
			break;
		case MEDIA__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.update(TABLE_NAME, values, "_id=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case MEDIA_URL:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "url=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case MEDIA_FILESIZE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "filesize=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case MEDIA_SHORTDESC:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "shortdesc=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case MEDIA_LONGDESC:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "longdesc=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case MEDIA_TYPE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "type=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case MEDIA_PRODUCTID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "productid=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case MEDIA_OTHER1:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "other1=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case MEDIA_OTHER2:
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
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), MEDIA);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/#", MEDIA__ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/url" + "/*", MEDIA_URL);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/filesize" + "/*", MEDIA_FILESIZE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/shortdesc" + "/*", MEDIA_SHORTDESC);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/longdesc" + "/*", MEDIA_LONGDESC);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/type" + "/*", MEDIA_TYPE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/productid" + "/*", MEDIA_PRODUCTID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/other1" + "/*", MEDIA_OTHER1);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/other2" + "/*", MEDIA_OTHER2);

		MEDIA_PROJECTION_MAP = new HashMap<String, String>();
		MEDIA_PROJECTION_MAP.put(_ID, "_id");
		MEDIA_PROJECTION_MAP.put(URL, "url");
		MEDIA_PROJECTION_MAP.put(FILESIZE, "filesize");
		MEDIA_PROJECTION_MAP.put(SHORTDESC, "shortdesc");
		MEDIA_PROJECTION_MAP.put(LONGDESC, "longdesc");
		MEDIA_PROJECTION_MAP.put(TYPE, "type");
		MEDIA_PROJECTION_MAP.put(PRODUCTID, "productid");
		MEDIA_PROJECTION_MAP.put(OTHER1, "other1");
		MEDIA_PROJECTION_MAP.put(OTHER2, "other2");

	}
}
