package com.dudev.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.dudev.util.DownloadImageThread.ImageType;
import com.impact.preshopping.GcmIntentService;
import com.impact.preshopping.R;
import com.impact.preshopping.adapter.ImageAdapter;
import com.impact.preshopping.adapter.ProductItemAdapter;
import com.impact.preshopping.db.MySqlHelper;
import com.impact.preshopping.model.Category;
import com.impact.preshopping.model.Company;
import com.impact.preshopping.model.Group;
import com.impact.preshopping.model.ImMedia;
import com.impact.preshopping.model.Product;

public class Utilities {

	// 1 for Configuration.ORIENTATION_PORTRAIT
	// 2 for Configuration.ORIENTATION_LANDSCAPE
	// 0 for Configuration.ORIENTATION_SQUARE
	public static final int ORIENTATION_PORTRAIT = 1;
	public static final int ORIENTATION_LANDSCAPE = 2;
	public static final int ORIENTATION_SQUARE = 0;

	public static final String TAG = Utilities.class.getSimpleName();

	// address
	public static final String _ID = "_id";

	public static final String ADDRESS = "address";

	public static final String ADDRESS2 = "address2";

	public static final String DISTRICT = "district";

	public static final String CITY_ID = "city_id";

	public static final String POSTAL_CODE = "postal_code";

	public static final String PHONE = "phone";

	public static final String LAST_UPDATE = "last_update";

	public static final String OTHER1 = "other1";

	public static final String OTHER2 = "other2";

	public static final String OTHER3 = "other3";
	public static final String NAME = "name";
	public static final String TAX_ID = "taxId";
	public static final String CONTACT_PERSON = "contactPerson";
	public static final String PHONE_NUMBER = "phoneNumber";
	public static final String ICON_URL = "icon";
	public static final String IMAGE = "image";
	public static final String BEGIN_DATE = "beginDate";
	public static final String END_DATE = "endDate";
	public static final String ICON_FILE_PATH = "iconFilePath";
	// ....
	public static final String URL = "url";

	// ..

	public static final String FILE_PATH = "filePath";
	public static final String FILE_SIZE = "fileSize";
	public static final String SHORT_DESC = "shortDesc";
	public static final String LONG_DESC = "longDesc";
	public static final String TYPE = "type";
	public static final String PROD_ID = "productId";
	// ...
	public static final String COMPANY_ID = "company_id";
	// ...
	public static final String CATEGORY_ID = "categoryId";
	// ....
	public static final String SHORT_NAME = "shortName";

	public static final String LONG_NAME = "longName";
	public static final String RATING = "rating";
	public static final String BARCODE_DATA = "barcodeData";
	public static final String MAKER = "maker";
	public static final String MODEL = "model";
	public static final String CREATED_BY = "createdBy";
	public static final String GROUP_ID = "groupId";

	// ...
	public static final String PUSH_SUCCESS = "pushSuccess";

	public static final String START_DATE = "startDate";

	public static final String DEV_ID = "devIds";
	// ...
	public static final String DOWNLOAD_PERMISION = "downloadPermision";
	public static final String HOME_SCREEN_TYPE = "homeScreenType";
	public static final String PROD_CATEGORIES = "prodCategories";
	public static final String PROD_GROUPS = "prodGroups";
	public static final String PRODUCTS = "products";
	public static final String NOTIFICATION_ON_OFF = "notificationOnOff";
	public static final String ALERT_TYPE = "alertType";

	public static final String SOUND_FILE = "soundFile";
	// ..
	public static final String SCREEN_NAME = "screenName";
	public static final String PASSWORD = "password";

	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String LAST_LOGIN = "lastLogin";
	public static final String CREATE_DATE = "createDate";
	public static final String EMAIL = "email";
	public static final String OFFICE_PHONE = "officePhone";
	public static final String MOBILE_PHONE = "mobilePhone";
	public static final String FACEBOOK = "facebook";

	public static final String INSTRAGRAM = "instragram";
	public static final String TWEETER = "tweeter";
	public static final String SKYPE = "skype";
	public static final String LINE = "line";
	public static final String PREFERRED_LANG = "preferredLang";
	public static final String IMEI = "imei";
	public static final String IS_ACTIVATED = "isActivated";
	public static final String ACTIVATED_DATE = "activatedDate";
	public static final String ADDRESS_ID = "addressId";
	// ...
	public static final String WATCHED_TYPE = "watchedType";
	public static final String USER_ID = "userId";
	public static final String PUSH_ID = "pushId";
	public static final String TBL_VIDEO_STATISTIC = "videoStatistic";
	public static final String TBL_USER = "user";
	public static final String TBL_SETTING = "setting";
	public static final String TBL_PUSH_NOTIFICATION = "pushNotification";
	public static final String TBL_PRODUCT = "product";
	public static final String TBL_GROUP = "prodGroup";
	public static final String TBL_CATEGORY = "prodCategory";

	public static final String TBL_MEDIA = "media";
	public static final String TBL_COMPANY = "company";
	public static final String TBL_ADDRESS = "address";

	public static final String REG_SCREEN_NAME = "screenName";
	public static final String REG_PASSWORD = "password";
	public static final String REG_FIRST_NAME = "firstName";
	public static final String REG_LAST_NAME = "lastName";
	public static final String REG_IS_ACTIVE = "isActive";
	public static final String REG_EMAIL = "email";
	public static final String REG_OFFICE_PHONE = "officePhone";
	public static final String REG_MOBILE_PHONE = "mobilePhone";
	public static final String REG_FACEBOOK = "facebook";
	public static final String REG_INSTRAGRAM = "instragram";
	public static final String REG_TWEETER = "tweeter";
	public static final String REG_SKYPE = "skype";
	public static final String REG_LINE = "line";
	public static final String REG_PREGERRED_LANG = "preferredLang";
	public static final String REG_IMEI = "imei";
	public static final String REG_DEVICE_ID = "deviceID";
	public static final String REG_DEVICE_TYPE = "deviceType";
	public static final String REG_USER_AVATAR = "avatar";
	public static final String REG_USER_AVATAR_TEXT = "avatarText";

	// Setting table.
	public static final String SETTING_DOWNLOAD_PERMISION = "downloadPermision";
	public static final String SETTING_HOME_SCREEN_TYPE = "homeScreenType";
	public static final String SETTING_COMPANY = "Companies";
	public static final String SETTING_PROD_CATEGORY = "prodCategories";
	public static final String SETTING_PROD_GROUP = "prodGroups";
	public static final String SETTING_PROD = "products";
	public static final String SETTING_NOTIFICATION_ONOFF = "notificationOnOff";
	public static final String SETTING_ALERT_TYPE = "alertType";
	public static final String SETTING_SOUND_FILE = "soundFile";
    public static final float ICON_MAX_SIZE = 150; // in dp

	public enum MediaType {
		IMAGE(1), MP3(2), MP4(3), OTHER(4);

		int code;

		MediaType(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
	}

	/**
	 * This method converts dp unit to equivalent pixels, depending on device
	 * density.
	 * 
	 * @param dp
	 *            A value in dp (density independent pixels) unit. Which we need
	 *            to convert into pixels
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent px equivalent to dp depending on
	 *         device density
	 */
	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	/**
	 * This method converts device specific pixels to density independent
	 * pixels.
	 * 
	 * @param px
	 *            A value in px (pixels) unit. Which we need to convert into db
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}

	public static String getExternalStorage() {
		File file = new File("/system/etc/vold.fstab");
		FileReader fr = null;
		BufferedReader br = null;
		String path = "/mnt/sdcard";
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			if (fr != null) {
				br = new BufferedReader(fr);
				String s = br.readLine();
				while (s != null) {
					if (s.startsWith("dev_mount")) {
						String[] tokens = s.split("\\s");
						path = tokens[2]; // mount_point
						if (!Environment.getExternalStorageDirectory().getAbsolutePath().equals(path)) {
							break;
						}
					}
					s = br.readLine();
					Log.w(TAG, "storage: " + s);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fr != null) {
					fr.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
			path = Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		return path;
	}

	// get screen height in px.
	public static int getHeight(Context mContext) {
		int height = 0;
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		if (Build.VERSION.SDK_INT > 12) {
			Point size = new Point();
			display.getSize(size);
			height = size.y;
		} else {
			height = display.getHeight(); // Deprecated
		}
		return height;
	}

	public static int getScreenOrientation(Context context) {
		int width = Utilities.getWidth(context);
		int height = Utilities.getHeight(context);

		if (width == height) {
			return Configuration.ORIENTATION_SQUARE;
		} else {
			if (width < height) {
				return Configuration.ORIENTATION_PORTRAIT;
			} else {
				return Configuration.ORIENTATION_LANDSCAPE;
			}
		}
	}

	// get screen width in px.
	public static int getWidth(Context mContext) {
		int width = 0;
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		if (Build.VERSION.SDK_INT > 12) {
			Point size = new Point();
			display.getSize(size);
			width = size.x;
		} else {
			width = display.getWidth(); // Deprecated
		}
		return width;
	}

	public static long insertNewCompanies(List<Company> list, Context context) {
		long total = 0;
		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		db.beginTransaction();
		db.delete(TBL_COMPANY, null, null);
		for (int i = 0; i < list.size(); i++) {
			Company comp = list.get(i);
			ContentValues v = new ContentValues();
			v.put(_ID, Integer.valueOf(comp.getCompanyId()));
			v.put(NAME, comp.getCompanyName());
			v.put(ICON_URL, comp.getLogoIconUrl());

			long num = db.insert(TBL_COMPANY, null, v);
			if (num > 0) {
				total = total + 1;
			}
		}

		db.setTransactionSuccessful();
		db.endTransaction();

		return total;
	}

	public static HashMap<String, String> getCompanyIconUrl(Context context) {
		HashMap<String, String> result = new HashMap<String, String>();

		try {
			SQLiteDatabase db = MySqlHelper.getInstance(context).getReadableDatabase();
			db.beginTransaction();
			Cursor c = db.rawQuery(context.getString(R.string.db_select_all_icon_url_company), null);
			if (c != null && c.getCount() > 0) {
				if (c.moveToFirst()) {
					do {
						result.put(String.valueOf(c.getInt(c.getColumnIndex(_ID))), c.getString(c.getColumnIndex(ICON_URL)));
					} while (c.moveToNext());
				}
			}

			if (c != null) {
				c.close();
				c = null;
			}

			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}

		return result;
	}

	public static long insertNewGroups(List<List<Group>> groups, Context context) {

		long total = 0;
		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		db.beginTransaction();
		db.delete(TBL_GROUP, null, null);
		for (int i = 0; i < groups.size(); i++) {
			List<Group> grps = groups.get(i);
			for (int a = 0; a < grps.size(); a++) {
				Group g = grps.get(a);
				ContentValues v = new ContentValues();
				v.put(_ID, Integer.valueOf(g.getGroupId()));
				v.put(CATEGORY_ID, g.getCategoryId());
				v.put(ICON_URL, g.getGroupIconUrl());
				v.put(NAME, g.getGroupName());
				long num = db.insert(TBL_GROUP, null, v);
				if (num > 0) {
					total = total + 1;
				}
			}
		}
		db.setTransactionSuccessful();
		db.endTransaction();

		return total;
	}

	public static HashMap<String, String> getGroupIconUrl(Context context) {
		HashMap<String, String> result = new HashMap<String, String>();

		try {
			SQLiteDatabase db = MySqlHelper.getInstance(context).getReadableDatabase();
			db.beginTransaction();
			Cursor c = db.rawQuery(context.getString(R.string.db_select_all_icon_url_prodGroup), null);
			if (c != null && c.getCount() > 0) {
				if (c.moveToFirst()) {
					do {
						result.put(String.valueOf(c.getInt(c.getColumnIndex(_ID))), c.getString(c.getColumnIndex(ICON_URL)));
					} while (c.moveToNext());
				}
			}

			if (c != null) {
				c.close();
				c = null;
			}

			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}

		return result;
	}

	public static long insertNewMedias(Context context, List<List<ImMedia>> list) {
		long total = 0;
		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		db.beginTransaction();
		db.delete(TBL_MEDIA, null, null);
		for (int i = 0; i < list.size(); i++) {
			List<ImMedia> medias = list.get(i);
			for (int a = 0; a < medias.size(); a++) {
				ImMedia m = medias.get(a);
				ContentValues v = new ContentValues();
				v.put(_ID, Integer.valueOf(m.getMediaId()));
				v.put(PROD_ID, m.getProductId());
				v.put(SHORT_DESC, m.getShortDesc());
				v.put(LONG_DESC, m.getLongDesc());
				v.put(FILE_SIZE, m.getFileSize());
				v.put(TYPE, m.getType());
				v.put(URL, m.getMediaUrl());

				String filePath = getFilePath(context, m.getProductId(), m.getType(), m.getMediaUrl());
				File f = new File(filePath);
				if (f.exists()) {
					v.put(FILE_PATH, Uri.fromFile(f).toString());
				}

				long num = db.insert(TBL_MEDIA, null, v);
				if (num > 0) {
					total = total + 1;
				}

			}
		}
		db.setTransactionSuccessful();
		db.endTransaction();

		return total;
	}

	private static String getFilePath(Context context, String id, String type, String mediaUrl) {
		char c = mediaUrl.charAt(mediaUrl.length() - 1);

		String filename = "";
		if (c == '/') {
			int index = mediaUrl.lastIndexOf(c);
			filename = mediaUrl.substring(index);
		} else {
			int index = mediaUrl.lastIndexOf("/");
			filename = mediaUrl.substring(index + 1);
		}

		String filePath = getAppFolder_ExtSd(context);

		// IMAGE(1), MP3(2), MP4(3);
		MediaType t;
		if (String.valueOf(MediaType.MP3.code).equals(type)) {
			t = MediaType.MP3;
		} else if (String.valueOf(MediaType.IMAGE.code).equals(type)) {
			t = MediaType.IMAGE;
		} else {
			t = MediaType.MP4;
		}
		// filePath = filePath + "/" + t.name() + "/" + id + "/";
		filePath = filePath + "/" + t.name() + "/";

		return (filePath + filename);
	}

	public static long insertNewProducts(List<List<Product>> list, Context context) {
		long total = 0;
		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		db.beginTransaction();
		db.delete(TBL_PRODUCT, null, null);
		for (int i = 0; i < list.size(); i++) {
			List<Product> products = list.get(i);
			for (int a = 0; a < products.size(); a++) {
				Product prod = products.get(a);
				ContentValues v = new ContentValues();
				v.put(_ID, Integer.valueOf(prod.getProductId()));
				v.put(SHORT_NAME, prod.getProductName());
				v.put(LONG_DESC, prod.getProductLongDesc());
				v.put(SHORT_DESC, prod.getProductShortDesc());
				v.put(ICON_URL, prod.getProductIconUrl());
				v.put(GROUP_ID, prod.getGroupId());
				v.put(BARCODE_DATA, prod.getBarcodeData());

				long num = db.insert(TBL_PRODUCT, null, v);
				if (num > 0) {
					total = total + 1;
				}
			}
		}

		db.setTransactionSuccessful();
		db.endTransaction();
		return total;
	}

	public static HashMap<String, String> getProductIconUrl(Context context) {
		HashMap<String, String> result = new HashMap<String, String>();

		try {
			SQLiteDatabase db = MySqlHelper.getInstance(context).getReadableDatabase();
			db.beginTransaction();
			Cursor c = db.rawQuery(context.getString(R.string.db_select_all_icon_url_product), null);
			if (c != null && c.getCount() > 0) {
				if (c.moveToFirst()) {
					do {
						result.put(String.valueOf(c.getInt(c.getColumnIndex(_ID))), c.getString(c.getColumnIndex(ICON_URL)));
					} while (c.moveToNext());
				}
			}

			if (c != null) {
				c.close();
				c = null;
			}

			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}

		return result;
	}

	public static long interNewCategories(List<List<Category>> list, Context context) {

		long total = 0;
		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		db.beginTransaction();
		db.delete(TBL_CATEGORY, null, null);
		for (int i = 0; i < list.size(); i++) {
			List<Category> cList = list.get(i);
			for (int a = 0; a < cList.size(); a++) {
				Category c = cList.get(a);

				ContentValues values = new ContentValues();
				values.put(COMPANY_ID, Integer.valueOf(c.getCompanyId()));
				values.put(_ID, c.getCategoryId());
				values.put(ICON_URL, c.getCategoryIconUrl());
				values.put(NAME, c.getCategoryName());

				long num = db.insert(TBL_CATEGORY, null, values);
				if (num > 0) {
					total = total + 1;
				}
			}
		}
		db.setTransactionSuccessful();
		db.endTransaction();

		return total;
	}

	public static long updateIconOrImageFilePath(Context context, String belongTo, String id, String filePath) {
		long total = 0;
		if (TextUtils.isEmpty(filePath)) {
			return total;
		}
		String tblName = getTblName(belongTo);

		try {
			SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
			db.beginTransaction();

			ContentValues values = new ContentValues();
			values.put("iconFilePath", filePath);
			db.update(tblName, values, "_id = ?", new String[] { id });
			db.setTransactionSuccessful();
			db.endTransaction();
			total = 1;
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}

		return total;
	}

	private static String getTblName(String belongTo) {
		String tblName = "";

		if (belongTo.equals(ImageType.CATEGORY.name())) {
			tblName = TBL_CATEGORY;
		} else if (belongTo.equals(ImageType.COMPANY.name())) {
			tblName = TBL_COMPANY;
		} else if (belongTo.equals(ImageType.GROUP.name())) {
			tblName = TBL_GROUP;
		} else if (belongTo.equals(ImageType.PRODUCT.name())) {
			tblName = TBL_PRODUCT;
		}

		return tblName;
	}

	public static HashMap<String, String> getCategoryIconUrl(Context context) {
		HashMap<String, String> result = new HashMap<String, String>();

		try {
			SQLiteDatabase db = MySqlHelper.getInstance(context).getReadableDatabase();
			db.beginTransaction();
			Cursor c = db.rawQuery(context.getString(R.string.db_select_all_icon_url_prodCategory), null);
			if (c != null && c.getCount() > 0) {
				if (c.moveToFirst()) {
					do {
						result.put(String.valueOf(c.getInt(c.getColumnIndex(_ID))), c.getString(c.getColumnIndex(ICON_URL)));
					} while (c.moveToNext());
				}
			}

			if (c != null) {
				c.close();
				c = null;
			}

			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}

		return result;
	}

	public static String getAppFolder(Context context) {
		String path = "";
		try {
			path = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.dataDir;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}

		return path;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		System.out.println("inSampleSize=" + inSampleSize);
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filename, options);
	}

	public static List<HashMap<String, String>> getProdList(Context context, String grpId) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		SQLiteDatabase db = MySqlHelper.getInstance(context).getReadableDatabase();
		if (db == null) {
			Log.e(TAG, "Couldn't get a valid reference to database.");
			return list;
		}

		try {
			Cursor c = db.rawQuery(String.format(context.getString(R.string.db_select_all_icon_filepath_product_by_grp_id), grpId), null);
			if (c != null && c.getCount() > 0) {
				if (c.moveToFirst()) {
					do {
						String id = c.getString(c.getColumnIndex(_ID));
						String name = c.getString(c.getColumnIndex(SHORT_NAME));
						String desc = c.getString(c.getColumnIndex(SHORT_DESC));
						String filePath = c.getString(c.getColumnIndex(ICON_FILE_PATH));
						String details = c.getString(c.getColumnIndex(LONG_DESC));
						String url = c.getString(c.getColumnIndex("icon"));

						HashMap<String, String> map = new HashMap<String, String>();
						map.put(ProductItemAdapter.PROD_ID, id);
						map.put(ProductItemAdapter.PROD_TITLE, name);
						map.put(ProductItemAdapter.PROD_DESC, desc);
						map.put(ProductItemAdapter.PROD_DETAILS, details);
						map.put(ImageAdapter.IMG_URL_PATH, url);

						if ((new File(Uri.parse(filePath).getPath())).exists()) {
							map.put(ProductItemAdapter.PROD_FILE_PATH, filePath);
						} else {
							map.put(ProductItemAdapter.PROD_FILE_PATH, "");
						}

						list.add(map);

					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}

		return list;
	}

	public static List<String> getMediaUrl(Context context, String prodId) {
		List<String> urls = new ArrayList<String>();
		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		if (db == null) {
			Log.e(TAG, "Couldn't get a valid reference to database.");
			return urls;
		}

		try {
			Cursor c = db.rawQuery(String.format(context.getString(R.string.db_select_all_url_media_by_prod_id), prodId), null);
			if (c != null && c.getCount() > 0) {
				if (c.moveToFirst()) {
					do {
						urls.add(c.getString(c.getColumnIndex(Utilities.URL)));
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}

		return urls;
	}

	public static int getTotalNumImageByProdId (Context context, String prodId) {
		int total = 0;
		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		if (db == null) {
			Log.e(TAG, "Couldn't get a valid reference to database.");
			return 0;
		}
		Cursor count = db.rawQuery("SELECT COUNT(*) from media where productId = " + prodId + " AND type = 1", null);
		if (count != null && count.getCount() > 0) {
			if (count.moveToFirst()) {
				total = count.getInt(0);	
			}
		}
		
		if (count != null) {
			count.close();
			count = null;
		}
		
		return total;
	}

	public static int getTotalNumVideoByProdId (Context context, String prodId) {
		int total = 0;
		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		if (db == null) {
			Log.e(TAG, "Couldn't get a valid reference to database.");
			return 0;
		}
		Cursor count = db.rawQuery("SELECT COUNT(*) from media where productId = " + prodId + " AND (type = 3 Or type = 2)", null);
		if (count != null && count.getCount() > 0) {
			if (count.moveToFirst()) {
				total = count.getInt(0);	
			}
		}
		
		if (count != null) {
			count.close();
			count = null;
		}
		
		return total;
	}
	
	public static List<String> getMediaFilePath(Context context, String prodId) {
		List<String> urls = new ArrayList<String>();
		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		if (db == null) {
			Log.e(TAG, "Couldn't get a valid reference to database.");
			return urls;
		}

		try {
			Cursor c = db.rawQuery(String.format(context.getString(R.string.db_select_all_filepath_media_by_prod_id), prodId), null);
			if (c != null && c.getCount() > 0) {
				if (c.moveToFirst()) {
					do {
						String filePath = c.getString(c.getColumnIndex(Utilities.FILE_PATH));
						if (!TextUtils.isEmpty(filePath)) {
							if ((new File(Uri.parse(filePath).getPath()).exists())) {
								urls.add(filePath);
							} else {
								urls.add("0");
								ContentValues values = new ContentValues();
								values.put(FILE_PATH, "0");
								int row = db.update(TBL_MEDIA, values, FILE_PATH + "='" + filePath + "'", null);
								System.out.println("updated row = " + row);
							}
						} else {
							urls.add("0");
						}
					} while (c.moveToNext());
				}
			}

			if (c != null) {
				c.close();
				c = null;
			}

		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}

		return urls;
	}

	public static Uri download(Context context, String id, String downloadUrl, MediaType type) {

		Uri uri = null;

		OutputStream out = null;
		InputStream in = null;
		int downloaded = 0;

		try {
			URL url = new URL(downloadUrl);

			char c = downloadUrl.charAt(downloadUrl.length() - 1);

			String filename = "";
			if (c == '/') {
				int index = downloadUrl.lastIndexOf(c);
				filename = downloadUrl.substring(index);
			} else {
				int index = downloadUrl.lastIndexOf("/");
				filename = downloadUrl.substring(index + 1);
			}

			// File destFile = new File(filePath + "/" + filename); //
			// /storage/extSdCard/com.impact.preshopping/media/forbes.gif
			String filePath = getAppFolder_ExtSd(context);
			// filePath = filePath + "/" + type.name() + "/" + id + "/";
			filePath = filePath + "/" + type.name() + "/";
			File dir = new File(filePath);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			File destFile = new File(filePath + filename);
			if (!destFile.exists()) {
				destFile.createNewFile();
			} else {
				downloaded = (int) destFile.length();
			}

			out = downloaded == 0 ? new FileOutputStream(destFile) : new FileOutputStream(destFile, true);

			// ...
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			int total = conn.getContentLength();
			conn.disconnect();

			if (total > downloaded) {
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setDoOutput(true);
				conn.setRequestProperty("Range", "bytes=" + downloaded + "-");
				conn.setConnectTimeout(120 * 1000);
				conn.setReadTimeout(120 * 1000);
				conn.connect();
				in = conn.getInputStream();

				byte[] buffer = new byte[1024];
				int length = 0;
				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}

				conn.disconnect();
			}
			// conn.setRequestMethod("GET");
			// conn.setDoOutput(true);
			// conn.setConnectTimeout(120 * 1000);
			// conn.setReadTimeout(120 * 1000);
			// in = conn.getInputStream();
			//
			// byte[] buffer = new byte[1024];
			// int length = 0;
			// while ((length = in.read(buffer)) > 0) {
			// out.write(buffer, 0, length);
			// }
			//
			// conn.disconnect();

			uri = Uri.fromFile(destFile);

		} catch (IOException e) {
			Log.e(TAG, "" + e);

		} catch (ClassCastException e) {
			Log.e(TAG, "" + e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					Log.e(TAG, "Cannot close outputstream");
				}
			}

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				in = null;
			}
		}

		return uri;
	}

	public static String getExtSd(Context context) {
		File file = new File("/system/etc/vold.fstab");
		FileReader fr = null;
		BufferedReader br = null;
		String path = "/mnt/sdcard";
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			if (fr != null) {
				br = new BufferedReader(fr);
				String s = br.readLine();
				while (s != null) {
					if (s.startsWith("dev_mount")) {
						String[] tokens = s.split("\\s");
						path = tokens[2]; // mount_point
						if (!Environment.getExternalStorageDirectory().getAbsolutePath().equals(path)) {
							break;
						}
					}
					s = br.readLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fr != null) {
					fr.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
			path = Environment.getExternalStorageDirectory().getAbsolutePath();
		}

		Log.i(TAG, "path = " + path);

		return path;
	}

	public static String getAppFolder_ExtSd(Context context) {
		File file = new File("/system/etc/vold.fstab");
		FileReader fr = null;
		BufferedReader br = null;
		String path = "/mnt/sdcard";
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			if (fr != null) {
				br = new BufferedReader(fr);
				String s = br.readLine();
				while (s != null) {
					if (s.startsWith("dev_mount")) {
						String[] tokens = s.split("\\s");
						path = tokens[2]; // mount_point
						if (!Environment.getExternalStorageDirectory().getAbsolutePath().equals(path)) {
							break;
						}
					}
					s = br.readLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fr != null) {
					fr.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// if (Environment.getExternalStorageState() !=
		// Environment.MEDIA_MOUNTED) {
		// Log.e(TAG, "external media not mounted...");
		// path = Environment.getExternalStorageDirectory().getAbsolutePath();
		// }

		// path = path + "/" + context.getPackageName() + "/media";
//		Log.i(TAG, "path = " + path);
		// /Android/data/
//		path = path + "/Android/data/" + context.getPackageName() + "/media";
		
		path = getObbFolder(context);

		return path;
	}

	private static String getObbFolder(Context context) {
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		path = path + "/Android/obb/" + context.getPackageName();
		return path;
	}

	public static List<HashMap<String, Object>> getVideoInfo(Context context, String prodId) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		SQLiteDatabase db = MySqlHelper.getInstance(context).getReadableDatabase();
		if (db == null) {
			Log.e(TAG, "Couldn't get a valid reference to database.");
			return list;
		}

		db.beginTransaction();
		try {
			Cursor c = db.rawQuery(String.format(context.getString(R.string.db_select_all_video_info_media_by_prod_id), prodId), null);
			if (c != null && c.getCount() > 0) {
				if (c.moveToFirst()) {
					do {
						HashMap<String, Object> data = new HashMap<String, Object>();
						String remote = c.getString(c.getColumnIndex(Utilities.URL));
						if (!TextUtils.isEmpty(remote)) {
							data.put("URI", Uri.parse(remote));
						}

						String filePath = c.getString(c.getColumnIndex(Utilities.FILE_PATH));
						if (!TextUtils.isEmpty(filePath)) {
							data.put("FILE_PATH", Uri.parse(filePath));
						} else {
							data.put("FILE_PATH", "");
						}

						data.put("TITLE", c.getString(c.getColumnIndex(Utilities.SHORT_DESC)));
						data.put("DESC", c.getString(c.getColumnIndex(Utilities.LONG_DESC)));
						data.put("FILE_SIZE", c.getString(c.getColumnIndex(Utilities.FILE_SIZE)));
						data.put("ID", c.getString(c.getColumnIndex(Utilities._ID)));
						data.put("TYPE", c.getString(c.getColumnIndex(Utilities.TYPE)));

						list.add(data);

					} while (c.moveToNext());
				}
			}

			if (c != null) {
				c.close();
				c = null;
			}

		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}

		db.setTransactionSuccessful();
		db.endTransaction();
		return list;
	}

	public static List<String> getProdInfo(Context context, String prodId) {
		List<String> result = new ArrayList<String>();

		SQLiteDatabase db = MySqlHelper.getInstance(context).getReadableDatabase();
		if (db == null) {
			Log.e(TAG, "Couldn't get a valid reference to database.");
			return result;
		}

		db.beginTransaction();
		String desc = "";
		String shortName = "";
		String iconFilePath = "";
		try {
			Cursor c = db.rawQuery(String.format(context.getString(R.string.db_select_prod_lng_desc), prodId), null);
			if (c != null && c.getCount() > 0) {
				if (c.moveToFirst()) {
					desc = c.getString(c.getColumnIndex(LONG_DESC));
					shortName = c.getString(c.getColumnIndex(SHORT_NAME));
					iconFilePath = c.getString(c.getColumnIndex(ICON_FILE_PATH));
				}
			}

			if (c != null) {
				c.close();
				c = null;
			}

		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}

		result.add(shortName);
		result.add(desc);
		result.add(iconFilePath);

		db.setTransactionSuccessful();
		db.endTransaction();

		return result;
	}

	public static long updateDbVersion(Context context, String version, String lastUpdateTime) {
		long total = 0;
		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		db.beginTransaction();
		db.delete("dbVersion", null, null);

		ContentValues v = new ContentValues();
		v.put("version", version);
		v.put("lastUpdateTime", lastUpdateTime);

		long num = db.insert("dbVersion", null, v);
		if (num > 0) {
			total = total + 1;
		}

		db.setTransactionSuccessful();
		db.endTransaction();

		return total;
	}

	public static String getDbVersion(Context context) {
		String version = "0";
		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		db.beginTransaction();

		Cursor c = db.rawQuery(context.getString(R.string.db_select_dbversion), null);
		if (c != null && c.getCount() > 0) {
			if (c.moveToFirst()) {
				version = c.getString(c.getColumnIndex("version"));
			}
		}

		if (c != null) {
			c.close();
			c = null;
		}

		db.setTransactionSuccessful();
		db.endTransaction();

		return version;
	}

	public static synchronized long updateMediaTbl(Context context, String id, String filePath) {
		long affectedRow = 0;

		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		db.beginTransaction();
		ContentValues v = new ContentValues();
		v.put("filePath", filePath);
		affectedRow = (long) db.update("media", v, "_id = ?", new String[] { id });
		db.setTransactionSuccessful();
		db.endTransaction();

		return affectedRow;
	}

	public static long insertNewRegInfo(Context context, HashMap<String, String> info) {
		long affectedRow = 0;

		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		db.beginTransaction();
		db.delete("registrationInfo", null, null);
		ContentValues v = new ContentValues();

		for (String field : info.keySet()) {
			if (field.equals(Utilities.REG_USER_AVATAR)) {

				if (!TextUtils.isEmpty(info.get(field))) {
					try {
						byte[] avatar = Base64.decode(info.get(field), 0);
						v.put(Utilities.REG_USER_AVATAR, avatar);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} else {
				v.put(field, TextUtils.isEmpty(info.get(field)) ? "" : info.get(field));
			}
		}

		affectedRow = db.insert("registrationInfo", null, v);
		db.setTransactionSuccessful();
		db.endTransaction();

		return affectedRow;
	}

	public static long insertNewSettings(Context context, HashMap<String, String> settingInfo) {
		long affectedRow = 0;

		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		db.beginTransaction();
		db.delete("setting", null, null);
		ContentValues v = new ContentValues();

		for (String field : settingInfo.keySet()) {
			v.put(field, TextUtils.isEmpty(settingInfo.get(field)) ? "" : settingInfo.get(field));
		}

		affectedRow = db.insert("setting", null, v);
		db.setTransactionSuccessful();
		db.endTransaction();

		return affectedRow;
	}

	public static long updateRegInfo(Context context, HashMap<String, Object> info) {
		long affectedRow = 0;

		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		db.beginTransaction();
		ContentValues v = new ContentValues();

		Uri uri = (Uri) info.get("AVATAR_URI");
		if (uri != null) {
			info.remove("AVATAR_URI");
			float width = Utilities.convertDpToPixel(ICON_MAX_SIZE, context);
			Options options = Utilities.getOptions(uri.getPath(), (int) width, (int) width);
			Bitmap b = BitmapFactory.decodeFile(uri.getPath(), options);

			if (b != null) {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				b.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArr = stream.toByteArray();

				if (byteArr != null && byteArr.length > 0) {
					v.put("avatar", byteArr);
				}
			}
		}

		for (String field : info.keySet()) {
			v.put(field, info.get(field) == null ? "" : info.get(field).toString());
		}
		affectedRow = db.update("registrationInfo", v, null, null);
		db.setTransactionSuccessful();
		db.endTransaction();

		return affectedRow;
	}

	public static HashMap<String, Object> getRegInfo(Context context) {

		HashMap<String, Object> info = new HashMap<String, Object>();

		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		db.beginTransaction();

		Cursor c = db.rawQuery("SELECT * FROM registrationInfo", null);
		if (c != null && c.getCount() > 0) {
			if (c.moveToFirst()) {

				info.put(REG_SCREEN_NAME, c.getString(c.getColumnIndex(REG_SCREEN_NAME)));
				info.put(REG_PASSWORD, c.getString(c.getColumnIndex(REG_PASSWORD)));
				info.put(REG_FIRST_NAME, c.getString(c.getColumnIndex(REG_FIRST_NAME)));
				info.put(REG_LAST_NAME, c.getString(c.getColumnIndex(REG_LAST_NAME)));
				info.put(REG_EMAIL, c.getString(c.getColumnIndex(REG_EMAIL)));
				info.put(REG_OFFICE_PHONE, c.getString(c.getColumnIndex(REG_OFFICE_PHONE)));
				info.put(REG_MOBILE_PHONE, c.getString(c.getColumnIndex(REG_MOBILE_PHONE)));
				info.put(REG_FACEBOOK, c.getString(c.getColumnIndex(REG_FACEBOOK)));
				info.put(REG_INSTRAGRAM, c.getString(c.getColumnIndex(REG_INSTRAGRAM)));
				info.put(REG_TWEETER, c.getString(c.getColumnIndex(REG_TWEETER)));
				info.put(REG_SKYPE, c.getString(c.getColumnIndex(REG_SKYPE)));
				info.put(REG_LINE, c.getString(c.getColumnIndex(REG_LINE)));
				info.put(REG_PREGERRED_LANG, c.getString(c.getColumnIndex(REG_PREGERRED_LANG)));
				info.put(REG_IMEI, c.getString(c.getColumnIndex(REG_IMEI)));
				info.put(REG_DEVICE_ID, c.getString(c.getColumnIndex(REG_DEVICE_ID)));

				byte[] arr = c.getBlob(c.getColumnIndex(REG_USER_AVATAR));
				Bitmap b = null;
				if (arr != null && arr.length > 0) {
					b = BitmapFactory.decodeByteArray(arr, 0, arr.length);
				}

				info.put(REG_USER_AVATAR, b);
			}
		}

		if (c != null) {
			c.close();
			c = null;
		}

		db.setTransactionSuccessful();
		db.endTransaction();

		return info;
	}

	public static HashMap<String, byte[]> getAvatar(Context context) {

		HashMap<String, byte[]> info = new HashMap<String, byte[]>();

		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		db.beginTransaction();

		Cursor c = db.rawQuery("SELECT * FROM registrationInfo", null);
		if (c != null && c.getCount() > 0) {
			if (c.moveToFirst()) {
				byte[] arr = c.getBlob(c.getColumnIndex(REG_USER_AVATAR));
				info.put(REG_USER_AVATAR, arr);
			}
		}

		if (c != null) {
			c.close();
			c = null;
		}

		db.setTransactionSuccessful();
		db.endTransaction();

		return info;
	}

	public static synchronized long updateMediaTblByProdId(Context context, String prodId, String url, String filePath) {
		long affectedRow = 0;

		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		db.beginTransaction();
		ContentValues v = new ContentValues();
		v.put("filePath", filePath);
		affectedRow = (long) db.update("media", v, "productId = ? AND type = ? AND url = ?", new String[] { prodId, String.valueOf(MediaType.IMAGE.getCode()), url });
		db.setTransactionSuccessful();
		db.endTransaction();

		return affectedRow;
	}

	/**
	 * Checking for all possible internet providers
	 * **/
	public static boolean isConnectingToInternet(Context _context) {
		ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	public static String getImei(Context context) {
		TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(context);

		String imeiSIM1 = telephonyInfo.getImeiSIM1();
		String imeiSIM2 = telephonyInfo.getImeiSIM2();

		boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
		boolean isSIM2Ready = telephonyInfo.isSIM2Ready();

		boolean isDualSIM = telephonyInfo.isDualSIM();
		Log.i("Dual = ", " IME1 : " + imeiSIM1 + "\n" + " IME2 : " + imeiSIM2 + "\n" + " IS DUAL SIM : " + isDualSIM + "\n" + " IS SIM1 READY : " + isSIM1Ready + "\n"
				+ " IS SIM2 READY : " + isSIM2Ready + "\n");

		// Hard coded to get only imei of the first slot.
		String imei = imeiSIM1;

		return imei;
	}

	public static boolean isValidEmail(String email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	public static Object deserializeBytes(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bytesIn);
		Object obj = ois.readObject();
		ois.close();
		return obj;
	}

	public static byte[] serializeObject(Object obj) throws IOException {
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bytesOut);
		oos.writeObject(obj);
		oos.flush();
		byte[] bytes = bytesOut.toByteArray();
		bytesOut.close();
		oos.close();
		return bytes;
	}

	public static BitmapFactory.Options getOptions(String filePath, int requiredWidth, int requiredHeight) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		// setting inJustDecodeBounds to true
		// ensures that we are able to measure
		// the dimensions of the image,without
		// actually allocating it memory
		options.inJustDecodeBounds = true;

		// decode the file for measurement
		BitmapFactory.decodeFile(filePath, options);

		// obtain the inSampleSize for loading a
		// scaled down version of the image.
		// options.outWidth and options.outHeight
		// are the measured dimensions of the
		// original image
		options.inSampleSize = getScale(options.outWidth, options.outHeight, requiredWidth, requiredHeight);
		options.inPurgeable = true;
		Log.i(TAG, "inSampleSize=" + String.valueOf(options.inSampleSize));

		// set inJustDecodeBounds to false again
		// so that we can now actually allocate the
		// bitmap some memory
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		options.inDither = true;

		return options;

	}

	public static int getScale(int originalWidth, int originalHeight, final int requiredWidth, final int requiredHeight) {

		// a scale of 1 means the original dimensions
		// of the image are maintained
		int scale = 1;

		// calculate scale only if the height or width of
		// the image exceeds the required value.
		if ((originalWidth > requiredWidth) || (originalHeight > requiredHeight)) {
			// calculate scale with respect to
			// the smaller dimension
			if (originalWidth < originalHeight)
				scale = Math.round((float) originalWidth / requiredWidth);
			else
				scale = Math.round((float) originalHeight / requiredHeight);

		}
		// This offers some additional logic in case the image has a strange
		// aspect ratio. For example, a panorama may have a much larger
		// width than height. In these cases the total pixels might still
		// end up being too large to fit comfortably in memory, so we should
		// be more aggressive with sample down the image (=larger
		// inSampleSize).

		final float totalPixels = originalWidth * originalHeight;

		// Anything more than 2x the requested pixels we'll sample down
		// further.
		final float totalReqPixelsCap = requiredWidth * requiredHeight * 2;

		while (totalPixels / (scale * scale) > totalReqPixelsCap) {
			scale++;
		}

		return scale;
	}

	public static HashMap<String, String> getAllCompanies(Context context) {

		HashMap<String, String> info = new HashMap<String, String>();

		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		db.beginTransaction();

		Cursor c = db.rawQuery("SELECT _id, name FROM company", null);
		if (c != null && c.getCount() > 0) {
			if (c.moveToFirst()) {
				do {
					String id = c.getString(c.getColumnIndex(_ID));
					String name = c.getString(c.getColumnIndex(NAME));

					info.put(id, name);

				} while (c.moveToNext());
			}
		}

		if (c != null) {
			c.close();
			c = null;
		}

		db.setTransactionSuccessful();
		db.endTransaction();

		return info;
	}

	public static HashMap<String, String> getAllCategories(Context context) {

		HashMap<String, String> info = new HashMap<String, String>();

		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		db.beginTransaction();

		Cursor c = db.rawQuery("SELECT _id, name FROM prodCategory", null);
		if (c != null && c.getCount() > 0) {
			if (c.moveToFirst()) {
				do {
					String id = c.getString(c.getColumnIndex(_ID));
					String name = c.getString(c.getColumnIndex(NAME));

					info.put(id, name);

				} while (c.moveToNext());
			}
		}

		if (c != null) {
			c.close();
			c = null;
		}

		db.setTransactionSuccessful();
		db.endTransaction();

		return info;
	}

	public static HashMap<String, String> getAllGroups(Context context) {

		HashMap<String, String> info = new HashMap<String, String>();

		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		db.beginTransaction();

		Cursor c = db.rawQuery("SELECT _id, name FROM prodGroup", null);
		if (c != null && c.getCount() > 0) {
			if (c.moveToFirst()) {
				do {
					String id = c.getString(c.getColumnIndex(_ID));
					String name = c.getString(c.getColumnIndex(NAME));

					info.put(id, name);

				} while (c.moveToNext());
			}
		}

		if (c != null) {
			c.close();
			c = null;
		}

		db.setTransactionSuccessful();
		db.endTransaction();

		return info;
	}

	public static HashMap<String, String> getAllProducts(Context context) {

		HashMap<String, String> info = new HashMap<String, String>();

		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		db.beginTransaction();

		Cursor c = db.rawQuery("SELECT _id, shortName FROM product", null);
		if (c != null && c.getCount() > 0) {
			if (c.moveToFirst()) {
				do {
					String id = c.getString(c.getColumnIndex(_ID));
					String name = c.getString(c.getColumnIndex(SHORT_NAME));

					info.put(id, name);

				} while (c.moveToNext());
			}
		}

		if (c != null) {
			c.close();
			c = null;
		}

		db.setTransactionSuccessful();
		db.endTransaction();

		return info;
	}

	public static void insertOrUpdate(Context context, HashMap<String, String> data) {

		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		db.beginTransaction();
		db.delete(TBL_SETTING, null, null);
		ContentValues values = new ContentValues();
		for (String key : data.keySet()) {
			values.put(key, data.get(key));
		}

		long row = db.insert(TBL_SETTING, null, values);

		db.setTransactionSuccessful();
		db.endTransaction();

	}

	public static HashMap<String, String> getSettings(Context context) {
		HashMap<String, String> result = new HashMap<String, String>();
		SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
		db.beginTransaction();

		Cursor c = db.rawQuery(context.getString(R.string.db_select_settings), null);
		if (c != null && c.getCount() > 0) {
			if (c.moveToFirst()) {
				for (int i = 0; i < c.getColumnCount(); i++) {
					result.put(c.getColumnName(i), c.getString(i));
				}
			}
		}

		if (c != null) {
			c.close();
			c = null;
		}

		db.setTransactionSuccessful();
		db.endTransaction();

		return result;
	}

	public static String getReadableSize(long bytes) {

		if (bytes < 1024)
			return bytes + " B";

		int exp = (int) (Math.log(bytes) / Math.log(1024));
		String pre = ("KMGTPE").charAt(exp - 1) + "";

		return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
	}

	/**
	 * @return Number of bytes available on internal storage
	 */
	public static long getInternalFreeSpace() {
		long availableSpace = -1L;
		try {
			StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
			stat.restat(Environment.getDataDirectory().getPath());
			availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Free space.
		return availableSpace;
	}

	public static boolean isAppInForeground(Context context) {
		List<RunningTaskInfo> task = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1);
		if (task.isEmpty()) {
			return false;
		}
		return task.get(0).topActivity.getPackageName().equalsIgnoreCase(context.getPackageName());
	}

	public static String getMediaShortDesc(Context context, String mediaId) {
		String shortDesc = "";

		SQLiteDatabase db = MySqlHelper.getInstance(context).getReadableDatabase();
		db.beginTransaction();

		Cursor c = db.rawQuery("SELECT shortDesc from media WHERE _id = " + mediaId, null);
		if (c != null && c.getCount() > 0) {
			if (c.moveToFirst()) {
				shortDesc = c.getString(0);
			}
		}

		if (c != null) {
			c.close();
			c = null;
		}

		db.setTransactionSuccessful();
		db.endTransaction();

		return shortDesc;

	}

	public static String getGroupIdByProdId(Context context, String prodId) {
		String groupId = "";

		SQLiteDatabase db = MySqlHelper.getInstance(context).getReadableDatabase();
		db.beginTransaction();

		Cursor c = db.rawQuery("SELECT groupId from product WHERE _id = " + prodId, null);
		if (c != null && c.getCount() > 0) {
			if (c.moveToFirst()) {
				groupId = c.getString(0);
			}
		}

		if (c != null) {
			c.close();
			c = null;
		}

		db.setTransactionSuccessful();
		db.endTransaction();

		return groupId;
	}

	public static String getCategoryIdByGroupId(Context applicationContext, String groupId) {
		String categoryId = "";

		SQLiteDatabase db = MySqlHelper.getInstance(applicationContext).getReadableDatabase();
		db.beginTransaction();

		Cursor c = db.rawQuery("SELECT categoryId from prodGroup WHERE _id = " + groupId, null);
		if (c != null && c.getCount() > 0) {
			if (c.moveToFirst()) {
				categoryId = c.getString(0);
			}
		}

		if (c != null) {
			c.close();
			c = null;
		}

		db.setTransactionSuccessful();
		db.endTransaction();

		return categoryId;
	}

	public static String getCompanyIdByCategoryId(Context applicationContext, String categoryId) {
		String companyId = "";

		SQLiteDatabase db = MySqlHelper.getInstance(applicationContext).getReadableDatabase();
		db.beginTransaction();

		Cursor c = db.rawQuery("SELECT company_id from prodCategory WHERE _id = " + categoryId, null);
		if (c != null && c.getCount() > 0) {
			if (c.moveToFirst()) {
				companyId = c.getString(0);
			}
		}

		if (c != null) {
			c.close();
			c = null;
		}

		db.setTransactionSuccessful();
		db.endTransaction();

		return companyId;
	}

	public static boolean doesProdExist(Context applicationContext, String prodId) {
		boolean exists = false;

		SQLiteDatabase db = MySqlHelper.getInstance(applicationContext).getReadableDatabase();
		db.beginTransaction();

		Cursor c = db.rawQuery("SELECT _id from product WHERE _id = " + prodId, null);
		if (c != null && c.getCount() > 0) {
			exists = true;
		}

		if (c != null) {
			c.close();
			c = null;
		}

		db.setTransactionSuccessful();
		db.endTransaction();

		return exists;
	}

	public static String getTotalExtMemory()
	{
	    StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());   
	    long Total = (statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;

	    String strI = Long.toString(Total);
	    return strI;
	}

	public static String getFreeExtMemory()
	{
	    StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
	    long Free  = (statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
	    String strI = Long.toString(Free);
	    return strI;
	}

	public static String getBusyExtMemory()
	{
	    StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());   
	    int Total = (statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
	    int Free  = (statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
	    int Busy  = Total - Free;
	    String strI = Integer.toString(Busy);
	    return strI;
	}

	public static boolean doesProdExist_Barcode(Context applicationContext,
			String barcode) {
		
		boolean exists = false;

		SQLiteDatabase db = MySqlHelper.getInstance(applicationContext).getReadableDatabase();
		db.beginTransaction();

		Cursor c = db.rawQuery("SELECT _id from product WHERE barcodeData = '" + barcode + "'", null);
		if (c != null && c.getCount() > 0) {
			exists = true;
		}

		if (c != null) {
			c.close();
			c = null;
		}

		db.setTransactionSuccessful();
		db.endTransaction();

		return exists;	
		
	}

	public static String getProdIdByBarcode(Context applicationContext, String barcode) {
		
		String prodId = "";
		
		SQLiteDatabase db = MySqlHelper.getInstance(applicationContext).getReadableDatabase();
		db.beginTransaction();

		Cursor c = db.rawQuery("SELECT _id from product WHERE barcodeData = '" + barcode + "'", null);
		if (c != null && c.getCount() > 0) {
			if (c.moveToFirst()) {
				prodId = c.getString(0);
			}
		}

		if (c != null) {
			c.close();
			c = null;
		}

		db.setTransactionSuccessful();
		db.endTransaction();
		
		return prodId;
	}

    public static long insertNewPromotion(Context context, Bundle data) {
        long total = 0;
        SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put("end_date", data.getString(GcmIntentService.PROMOTION_END_DATE));
        values.put("start_date", data.getString(GcmIntentService.PROMOTION_START_DATE));
        values.put("promotion_url", data.getString(GcmIntentService.PROMOTION_URL));
        values.put("promotion_desc", data.getString(GcmIntentService.PROMOTION_DESC));
        values.put("company_id", data.getString(GcmIntentService.PROMOTION_COMPANY_ID));
       
        total = db.insert("push_history", null, values);
        
        db.setTransactionSuccessful();
        db.endTransaction();

        return total;
    }

    public static List<HashMap<String, String>> getAllValidPromotion(Context context) {
        
        // Don't forget to take this opportunity to cleanup old promotion...
        // ....
        List<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
        
        SQLiteDatabase db = MySqlHelper.getInstance(context).getWritableDatabase();
        db.beginTransaction();

        Cursor c = db.rawQuery("SELECT * FROM push_history", null);
        if (c != null && c.getCount() > 0) {
            if (c.moveToFirst()) {
                
                do {
                    HashMap<String, String> promotions = new HashMap<String, String>();
                    promotions.put(GcmIntentService.PROMOTION_END_DATE, c.getString(c.getColumnIndex("end_date")));
                    promotions.put(GcmIntentService.PROMOTION_START_DATE, c.getString(c.getColumnIndex("start_date")));
                    promotions.put(GcmIntentService.PROMOTION_URL, c.getString(c.getColumnIndex("promotion_url")));
                    promotions.put(GcmIntentService.PROMOTION_DESC, c.getString(c.getColumnIndex("promotion_desc")));
                    promotions.put(GcmIntentService.PROMOTION_COMPANY_ID, c.getString(c.getColumnIndex("company_id")));
                    
                    result.add(promotions);
                } while (c.moveToNext());
            }
        }
        
        db.setTransactionSuccessful();
        db.endTransaction();
        
        return result;
    }
    
    public static String reformEndpoint(Context applicationContext, String methodName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        String endpoint = prefs.getString("pref_app_endoint", applicationContext.getString(R.string.application_endpoint));
        endpoint = endpoint + methodName;
        
        return endpoint;
    }
    
    public static boolean isValidPromotion (Context context, String companyId) {
        boolean isValid = false;
        SQLiteDatabase db = MySqlHelper.getInstance(context).getReadableDatabase();
        db.beginTransaction();

        Cursor c = db.rawQuery("SELECT Companies from setting", null);
        if (c != null && c.getCount() > 0) {
            if (c.moveToFirst()) {
                String comanies = c.getString(0);
                
                if (TextUtils.isEmpty(comanies)) {
                    isValid = true;
                } else {
                    String[] comArr = comanies.split(",");
                    for (String str : comArr) {
                        if (str.equals(companyId)) {
                            isValid = true;
                            break;
                        }
                    }                    
                }
            }
        }

        if (c != null) {
            c.close();
            c = null;
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        
        return isValid;
    }

    public static String getCompanyName(Context applicationContext, String companyId) {
        String name = "";
        SQLiteDatabase db = MySqlHelper.getInstance(applicationContext).getReadableDatabase();
        db.beginTransaction();

        Cursor c = db.rawQuery("SELECT name FROM company WHERE _id = " + companyId, null);
        if (c != null && c.getCount() > 0) {
            if (c.moveToFirst()) {
                name = c.getString(0);
            }
        }

        if (c != null) {
            c.close();
            c = null;
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        
        return name;
    }

    public static String getAlertType(Context applicationContext, String companyId) {
        String type = Constants.FLAG_ALERT_TYPE_MSG_ONLY;
        
        SQLiteDatabase db = MySqlHelper.getInstance(applicationContext).getReadableDatabase();
        db.beginTransaction();

        Cursor c = db.rawQuery("SELECT alertType from setting", null);
        if (c != null && c.getCount() > 0) {
            if (c.moveToFirst()) {
                type = c.getString(0);
            }
        }

        if (c != null) {
            c.close();
            c = null;
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        
        return type;
    }
    
    public static boolean isNotificationOn (Context context) {
        
        boolean isOn = true;
        
        SQLiteDatabase db = MySqlHelper.getInstance(context).getReadableDatabase();
        db.beginTransaction();

        Cursor c = db.rawQuery("SELECT notificationOnOff from setting", null);
        if (c != null && c.getCount() > 0) {
            if (c.moveToFirst()) {
                isOn = (c.getString(0).equals("1") ? true : false);
            }
        }

        if (c != null) {
            c.close();
            c = null;
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        
        return isOn;
        
    }
}
