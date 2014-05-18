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

public class UserContentProvider extends ContentProvider {

	private MySqlHelper dbHelper;
	private static HashMap<String, String> USER_PROJECTION_MAP;
	private static final String TABLE_NAME = "user";
	private static final String AUTHORITY = "com.impact.preshopping.contentprovider.usercontentprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	public static final Uri _ID_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase());
	public static final Uri SCREENNAME_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/screenname");
	public static final Uri PASSWORD_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/password");
	public static final Uri FIRSTNAME_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/firstname");
	public static final Uri LASTNAME_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/lastname");
	public static final Uri LASTLOGIN_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/lastlogin");
	public static final Uri CREATEDATE_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/createdate");
	public static final Uri EMAIL_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/email");
	public static final Uri OFFICEPHONE_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/officephone");
	public static final Uri MOBILEPHONE_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/mobilephone");
	public static final Uri FACEBOOK_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/facebook");
	public static final Uri INSTRAGRAM_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/instragram");
	public static final Uri TWEETER_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/tweeter");
	public static final Uri SKYPE_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/skype");
	public static final Uri LINE_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/line");
	public static final Uri PREFERREDLANG_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/preferredlang");
	public static final Uri IMEI_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/imei");
	public static final Uri ISACTIVATED_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/isactivated");
	public static final Uri ACTIVATEDDATE_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/activateddate");
	public static final Uri ADDRESSID_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/addressid");
	public static final Uri OTHER1_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/other1");
	public static final Uri OTHER2_FIELD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/other2");

	public static final String DEFAULT_SORT_ORDER = "_id ASC";

	private static final UriMatcher URL_MATCHER;

	private static final int USER = 1;
	private static final int USER__ID = 2;
	private static final int USER_SCREENNAME = 3;
	private static final int USER_PASSWORD = 4;
	private static final int USER_FIRSTNAME = 5;
	private static final int USER_LASTNAME = 6;
	private static final int USER_LASTLOGIN = 7;
	private static final int USER_CREATEDATE = 8;
	private static final int USER_EMAIL = 9;
	private static final int USER_OFFICEPHONE = 10;
	private static final int USER_MOBILEPHONE = 11;
	private static final int USER_FACEBOOK = 12;
	private static final int USER_INSTRAGRAM = 13;
	private static final int USER_TWEETER = 14;
	private static final int USER_SKYPE = 15;
	private static final int USER_LINE = 16;
	private static final int USER_PREFERREDLANG = 17;
	private static final int USER_IMEI = 18;
	private static final int USER_ISACTIVATED = 19;
	private static final int USER_ACTIVATEDDATE = 20;
	private static final int USER_ADDRESSID = 21;
	private static final int USER_OTHER1 = 22;
	private static final int USER_OTHER2 = 23;

	// Content values keys (using column names)
	public static final String _ID = "_id";
	public static final String SCREENNAME = "screenName";
	public static final String PASSWORD = "password";
	public static final String FIRSTNAME = "firstName";
	public static final String LASTNAME = "lastName";
	public static final String LASTLOGIN = "lastLogin";
	public static final String CREATEDATE = "createDate";
	public static final String EMAIL = "email";
	public static final String OFFICEPHONE = "officePhone";
	public static final String MOBILEPHONE = "mobilePhone";
	public static final String FACEBOOK = "facebook";
	public static final String INSTRAGRAM = "instragram";
	public static final String TWEETER = "tweeter";
	public static final String SKYPE = "skype";
	public static final String LINE = "line";
	public static final String PREFERREDLANG = "preferredLang";
	public static final String IMEI = "imei";
	public static final String ISACTIVATED = "isActivated";
	public static final String ACTIVATEDDATE = "activatedDate";
	public static final String ADDRESSID = "addressId";
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
		case USER:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(USER_PROJECTION_MAP);
			break;
		case USER__ID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("_id=" + url.getPathSegments().get(1));
			break;
		case USER_SCREENNAME:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("screenname='" + url.getPathSegments().get(2) + "'");
			break;
		case USER_PASSWORD:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("password='" + url.getPathSegments().get(2) + "'");
			break;
		case USER_FIRSTNAME:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("firstname='" + url.getPathSegments().get(2) + "'");
			break;
		case USER_LASTNAME:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("lastname='" + url.getPathSegments().get(2) + "'");
			break;
		case USER_LASTLOGIN:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("lastlogin='" + url.getPathSegments().get(2) + "'");
			break;
		case USER_CREATEDATE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("createdate='" + url.getPathSegments().get(2) + "'");
			break;
		case USER_EMAIL:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("email='" + url.getPathSegments().get(2) + "'");
			break;
		case USER_OFFICEPHONE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("officephone='" + url.getPathSegments().get(2) + "'");
			break;
		case USER_MOBILEPHONE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("mobilephone='" + url.getPathSegments().get(2) + "'");
			break;
		case USER_FACEBOOK:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("facebook='" + url.getPathSegments().get(2) + "'");
			break;
		case USER_INSTRAGRAM:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("instragram='" + url.getPathSegments().get(2) + "'");
			break;
		case USER_TWEETER:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("tweeter='" + url.getPathSegments().get(2) + "'");
			break;
		case USER_SKYPE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("skype='" + url.getPathSegments().get(2) + "'");
			break;
		case USER_LINE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("line='" + url.getPathSegments().get(2) + "'");
			break;
		case USER_PREFERREDLANG:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("preferredlang='" + url.getPathSegments().get(2) + "'");
			break;
		case USER_IMEI:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("imei='" + url.getPathSegments().get(2) + "'");
			break;
		case USER_ISACTIVATED:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("isactivated='" + url.getPathSegments().get(2) + "'");
			break;
		case USER_ACTIVATEDDATE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("activateddate='" + url.getPathSegments().get(2) + "'");
			break;
		case USER_ADDRESSID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("addressid='" + url.getPathSegments().get(2) + "'");
			break;
		case USER_OTHER1:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("other1='" + url.getPathSegments().get(2) + "'");
			break;
		case USER_OTHER2:
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
		case USER:
			return "vnd.android.cursor.dir/vnd.com.impact.preshopping.contentprovider.user";
		case USER__ID:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";
		case USER_SCREENNAME:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";
		case USER_PASSWORD:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";
		case USER_FIRSTNAME:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";
		case USER_LASTNAME:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";
		case USER_LASTLOGIN:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";
		case USER_CREATEDATE:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";
		case USER_EMAIL:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";
		case USER_OFFICEPHONE:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";
		case USER_MOBILEPHONE:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";
		case USER_FACEBOOK:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";
		case USER_INSTRAGRAM:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";
		case USER_TWEETER:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";
		case USER_SKYPE:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";
		case USER_LINE:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";
		case USER_PREFERREDLANG:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";
		case USER_IMEI:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";
		case USER_ISACTIVATED:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";
		case USER_ACTIVATEDDATE:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";
		case USER_ADDRESSID:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";
		case USER_OTHER1:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";
		case USER_OTHER2:
			return "vnd.android.cursor.item/vnd.com.impact.preshopping.contentprovider.user";

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
		if (URL_MATCHER.match(url) != USER) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		rowID = mDB.insert("user", "user", values);
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
		case USER:
			count = mDB.delete(TABLE_NAME, where, whereArgs);
			break;
		case USER__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.delete(TABLE_NAME, "_id=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_SCREENNAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "screenname=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_PASSWORD:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "password=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_FIRSTNAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "firstname=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_LASTNAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "lastname=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_LASTLOGIN:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "lastlogin=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_CREATEDATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "createdate=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_EMAIL:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "email=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_OFFICEPHONE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "officephone=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_MOBILEPHONE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "mobilephone=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_FACEBOOK:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "facebook=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_INSTRAGRAM:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "instragram=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_TWEETER:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "tweeter=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_SKYPE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "skype=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_LINE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "line=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_PREFERREDLANG:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "preferredlang=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_IMEI:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "imei=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_ISACTIVATED:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "isactivated=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_ACTIVATEDDATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "activateddate=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_ADDRESSID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "addressid=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_OTHER1:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME, "other1=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_OTHER2:
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
		case USER:
			count = mDB.update(TABLE_NAME, values, where, whereArgs);
			break;
		case USER__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.update(TABLE_NAME, values, "_id=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_SCREENNAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "screenname=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_PASSWORD:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "password=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_FIRSTNAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "firstname=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_LASTNAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "lastname=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_LASTLOGIN:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "lastlogin=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_CREATEDATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "createdate=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_EMAIL:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "email=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_OFFICEPHONE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "officephone=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_MOBILEPHONE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "mobilephone=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_FACEBOOK:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "facebook=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_INSTRAGRAM:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "instragram=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_TWEETER:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "tweeter=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_SKYPE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "skype=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_LINE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "line=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_PREFERREDLANG:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "preferredlang=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_IMEI:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "imei=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_ISACTIVATED:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "isactivated=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_ACTIVATEDDATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "activateddate=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_ADDRESSID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "addressid=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_OTHER1:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values, "other1=" + segment + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		case USER_OTHER2:
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
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), USER);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/#", USER__ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/screenname" + "/*", USER_SCREENNAME);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/password" + "/*", USER_PASSWORD);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/firstname" + "/*", USER_FIRSTNAME);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/lastname" + "/*", USER_LASTNAME);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/lastlogin" + "/*", USER_LASTLOGIN);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/createdate" + "/*", USER_CREATEDATE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/email" + "/*", USER_EMAIL);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/officephone" + "/*", USER_OFFICEPHONE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/mobilephone" + "/*", USER_MOBILEPHONE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/facebook" + "/*", USER_FACEBOOK);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/instragram" + "/*", USER_INSTRAGRAM);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/tweeter" + "/*", USER_TWEETER);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/skype" + "/*", USER_SKYPE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/line" + "/*", USER_LINE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/preferredlang" + "/*", USER_PREFERREDLANG);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/imei" + "/*", USER_IMEI);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/isactivated" + "/*", USER_ISACTIVATED);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/activateddate" + "/*", USER_ACTIVATEDDATE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/addressid" + "/*", USER_ADDRESSID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/other1" + "/*", USER_OTHER1);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/other2" + "/*", USER_OTHER2);

		USER_PROJECTION_MAP = new HashMap<String, String>();
		USER_PROJECTION_MAP.put(_ID, "_id");
		USER_PROJECTION_MAP.put(SCREENNAME, "screenname");
		USER_PROJECTION_MAP.put(PASSWORD, "password");
		USER_PROJECTION_MAP.put(FIRSTNAME, "firstname");
		USER_PROJECTION_MAP.put(LASTNAME, "lastname");
		USER_PROJECTION_MAP.put(LASTLOGIN, "lastlogin");
		USER_PROJECTION_MAP.put(CREATEDATE, "createdate");
		USER_PROJECTION_MAP.put(EMAIL, "email");
		USER_PROJECTION_MAP.put(OFFICEPHONE, "officephone");
		USER_PROJECTION_MAP.put(MOBILEPHONE, "mobilephone");
		USER_PROJECTION_MAP.put(FACEBOOK, "facebook");
		USER_PROJECTION_MAP.put(INSTRAGRAM, "instragram");
		USER_PROJECTION_MAP.put(TWEETER, "tweeter");
		USER_PROJECTION_MAP.put(SKYPE, "skype");
		USER_PROJECTION_MAP.put(LINE, "line");
		USER_PROJECTION_MAP.put(PREFERREDLANG, "preferredlang");
		USER_PROJECTION_MAP.put(IMEI, "imei");
		USER_PROJECTION_MAP.put(ISACTIVATED, "isactivated");
		USER_PROJECTION_MAP.put(ACTIVATEDDATE, "activateddate");
		USER_PROJECTION_MAP.put(ADDRESSID, "addressid");
		USER_PROJECTION_MAP.put(OTHER1, "other1");
		USER_PROJECTION_MAP.put(OTHER2, "other2");

	}
}
