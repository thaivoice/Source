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

public class CompanyContentProvider extends ContentProvider {

	private MySqlHelper dbHelper;
	private static HashMap<String, String> COMPANY_PROJECTION_MAP;
	private static final String TABLE_NAME = "company";
	private static final String AUTHORITY = "com.impact.preshopping.contentprovider.companycontentprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	public static final Uri _ID_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase());
	public static final Uri NAME_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/name");
	public static final Uri TAXID_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/taxid");
	public static final Uri CONTACTPERSON_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/contactperson");
	public static final Uri PHONENUMBER_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/phonenumber");
	public static final Uri ICON_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/icon");
	public static final Uri IMAGE_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/image");
	public static final Uri BEGINDATE_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/begindate");
	public static final Uri ENDDATE_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/enddate");
	public static final Uri OTHER1_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/other1");
	public static final Uri OTHER2_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/other2");

	public static final String DEFAULT_SORT_ORDER = "_id ASC";

	private static final UriMatcher URL_MATCHER;

	private static final int COMPANY = 1;
	private static final int COMPANY__ID = 2;
	private static final int COMPANY_NAME = 3;
	private static final int COMPANY_TAXID = 4;
	private static final int COMPANY_CONTACTPERSON = 5;
	private static final int COMPANY_PHONENUMBER = 6;
	private static final int COMPANY_ICON = 7;
	private static final int COMPANY_IMAGE = 8;
	private static final int COMPANY_BEGINDATE = 9;
	private static final int COMPANY_ENDDATE = 10;
	private static final int COMPANY_OTHER1 = 11;
	private static final int COMPANY_OTHER2 = 12;

	// Content values keys (using column names)
	public static final String _ID = "_id";
	public static final String NAME = "name";
	public static final String TAXID = "taxId";
	public static final String CONTACTPERSON = "contactPerson";
	public static final String PHONENUMBER = "phoneNumber";
	public static final String ICON = "icon";
	public static final String IMAGE = "image";
	public static final String BEGINDATE = "beginDate";
	public static final String ENDDATE = "endDate";
	public static final String OTHER1 = "other1";
	public static final String OTHER2 = "other2";

	public boolean onCreate() {
		dbHelper = MySqlHelper.getInstance(getContext());
		return (dbHelper == null) ? false : true;
	}

	public Cursor query(Uri url, String[] projection, String selection, String[] selectionArgs, String sort) {
		SQLiteDatabase mDB = MySqlHelper.getInstance(getContext()).getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (URL_MATCHER.match(url)) {
		case COMPANY:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(COMPANY_PROJECTION_MAP);
			break;
		case COMPANY__ID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("_id=" + url.getPathSegments().get(1));
			break;
		case COMPANY_NAME:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("name='" + url.getPathSegments().get(2) + "'");
			break;
		case COMPANY_TAXID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("taxid='" + url.getPathSegments().get(2) + "'");
			break;
		case COMPANY_CONTACTPERSON:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("contactperson='" + url.getPathSegments().get(2) + "'");
			break;
		case COMPANY_PHONENUMBER:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("phonenumber='" + url.getPathSegments().get(2) + "'");
			break;
		case COMPANY_ICON:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("icon='" + url.getPathSegments().get(2) + "'");
			break;
		case COMPANY_IMAGE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("image='" + url.getPathSegments().get(2) + "'");
			break;
		case COMPANY_BEGINDATE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("begindate='" + url.getPathSegments().get(2) + "'");
			break;
		case COMPANY_ENDDATE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("enddate='" + url.getPathSegments().get(2) + "'");
			break;
		case COMPANY_OTHER1:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("other1='" + url.getPathSegments().get(2) + "'");
			break;
		case COMPANY_OTHER2:
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
		case COMPANY:
			return "vnd.android.cursor.dir/vnd.com.impact.preshopping.contentprovider.company";
		case COMPANY__ID:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.company";
		case COMPANY_NAME:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.company";
		case COMPANY_TAXID:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.company";
		case COMPANY_CONTACTPERSON:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.company";
		case COMPANY_PHONENUMBER:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.company";
		case COMPANY_ICON:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.company";
		case COMPANY_IMAGE:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.company";
		case COMPANY_BEGINDATE:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.company";
		case COMPANY_ENDDATE:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.company";
		case COMPANY_OTHER1:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.company";
		case COMPANY_OTHER2:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.company";

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
	}

	public Uri insert(Uri url, ContentValues initialValues) {
		SQLiteDatabase mDB = MySqlHelper.getInstance(getContext()).getWritableDatabase();
		long rowID;
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}
		if (URL_MATCHER.match(url) != COMPANY) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		rowID = mDB.insert("company", "company", values);
		if (rowID > 0) {
			Uri uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(uri, null);
			return uri;
		}
		throw new SQLException("Failed to insert row into " + url);
	}

	public int delete(Uri url, String where, String[] whereArgs) {
		SQLiteDatabase mDB = MySqlHelper.getInstance(getContext()).getWritableDatabase();
		int count;
		String segment = "";
		switch (URL_MATCHER.match(url)) {
		case COMPANY:
			count = mDB.delete(TABLE_NAME, where, whereArgs);
			break;
		case COMPANY__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.delete(TABLE_NAME, "_id=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case COMPANY_NAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "name=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case COMPANY_TAXID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "taxid=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case COMPANY_CONTACTPERSON:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "contactperson=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case COMPANY_PHONENUMBER:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "phonenumber=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case COMPANY_ICON:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "icon=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case COMPANY_IMAGE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "image=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case COMPANY_BEGINDATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "begindate=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case COMPANY_ENDDATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "enddate=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case COMPANY_OTHER1:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "other1=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case COMPANY_OTHER2:
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
		SQLiteDatabase mDB = MySqlHelper.getInstance(getContext()).getWritableDatabase();
		int count;
		String segment = "";
		switch (URL_MATCHER.match(url)) {
		case COMPANY:
			count = mDB.update(TABLE_NAME, values, where, whereArgs);
			break;
		case COMPANY__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.update(TABLE_NAME, values, "_id=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case COMPANY_NAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "name=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case COMPANY_TAXID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "taxid=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case COMPANY_CONTACTPERSON:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "contactperson=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case COMPANY_PHONENUMBER:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "phonenumber=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case COMPANY_ICON:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "icon=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case COMPANY_IMAGE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "image=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case COMPANY_BEGINDATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "begindate=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case COMPANY_ENDDATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "enddate=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case COMPANY_OTHER1:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "other1=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case COMPANY_OTHER2:
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
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), COMPANY);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/#", COMPANY__ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/name" + "/*", COMPANY_NAME);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/taxid" + "/*", COMPANY_TAXID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/contactperson" + "/*", COMPANY_CONTACTPERSON);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/phonenumber" + "/*", COMPANY_PHONENUMBER);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/icon" + "/*", COMPANY_ICON);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/image" + "/*", COMPANY_IMAGE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/begindate" + "/*", COMPANY_BEGINDATE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/enddate" + "/*", COMPANY_ENDDATE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/other1" + "/*", COMPANY_OTHER1);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/other2" + "/*", COMPANY_OTHER2);

		COMPANY_PROJECTION_MAP = new HashMap<String, String>();
		COMPANY_PROJECTION_MAP.put(_ID, "_id");
		COMPANY_PROJECTION_MAP.put(NAME, "name");
		COMPANY_PROJECTION_MAP.put(TAXID, "taxid");
		COMPANY_PROJECTION_MAP.put(CONTACTPERSON, "contactperson");
		COMPANY_PROJECTION_MAP.put(PHONENUMBER, "phonenumber");
		COMPANY_PROJECTION_MAP.put(ICON, "icon");
		COMPANY_PROJECTION_MAP.put(IMAGE, "image");
		COMPANY_PROJECTION_MAP.put(BEGINDATE, "begindate");
		COMPANY_PROJECTION_MAP.put(ENDDATE, "enddate");
		COMPANY_PROJECTION_MAP.put(OTHER1, "other1");
		COMPANY_PROJECTION_MAP.put(OTHER2, "other2");

	}
}
