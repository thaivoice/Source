package com.impact.preshopping;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.GridView;

import com.dudev.util.RequestType;
import com.dudev.util.RestClient;
import com.dudev.util.SyncDataThread;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.impact.preshopping.adapter.ImageAdapter;
import com.impact.preshopping.adapter.ImageAdapter.ItemType;
import com.impact.preshopping.db.MySqlHelper;
import com.impact.preshopping.model.PreShopping;

public class GridViewCompanyActivity extends Activity {

public static final String TAG = GridViewCompanyActivity.class.getSimpleName();
	
	
	private String getRemoteImage(URL aURL) {
		try {

			URLConnection conn = aURL.openConnection();

			conn.connect();

			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());

			Bitmap bm = BitmapFactory.decodeStream(bis);
			File f = new File(UUID.randomUUID() + ".jpg");
			OutputStream out = openFileOutput(f.getName(), 0);
			bm.compress(CompressFormat.JPEG, 100, out);
			bm.recycle();
			bis.close();

			return Uri.fromFile(f).toString();

		} catch (IOException e) {

		}
		return null;
	}

	private List<WeakReference<GridViewCompanyActivity>> weakRef = new ArrayList<WeakReference<GridViewCompanyActivity>>();
	private ProgressDialog dialog;
	public enum ActivityMode {
		NORMAL,
		DOWNLOAD,
		LOGGING_IN,
		NONE
	}
	public static final String ACTIVITY_MODE = "activityMode";
	
	private Handler uiHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SyncDataThread.FLAG_DISMISS_PROGRESS_DIALOG_AND_START_DOWNLOAD:
				mode = ActivityMode.DOWNLOAD;
				if (weakRef.get(0).get() != null && !weakRef.get(0).get().isFinishing()) {
					if (dialog != null && !dialog.isShowing()) {
						dialog.setMessage("Downloading icons...");
						if (!dialog.isShowing()) {
							dialog.show();
						}
					} else {
						dialog = new ProgressDialog(GridViewCompanyActivity.this);
						dialog.setMessage("Downloading icons...");	
						dialog.setIndeterminate(true);
						dialog.setCancelable(false);
						dialog.setCanceledOnTouchOutside(false);
						dialog.show();
					}
				}

				if (msg != null && msg.obj != null) {
//					new DownloadImageThread(getApplicationContext(), 1, "http://mobile.bgnsolutions.com/media/image/20a.jpg", this).start();
				}

				break;

			case SyncDataThread.FLAG_SHOW_PROGRESS_DIALOG_LOGIN:
				if (weakRef.get(0).get() != null && !weakRef.get(0).get().isFinishing()) {
					dialog = new ProgressDialog(GridViewCompanyActivity.this);
					dialog.setMessage("Logging in...");
					dialog.setIndeterminate(true);
					dialog.setCancelable(false);
					dialog.setCanceledOnTouchOutside(false);
					dialog.show();
					mode = ActivityMode.LOGGING_IN;
				}
				
			case SyncDataThread.FLAG_SHOW_IMAGE:
				mode = ActivityMode.NORMAL;
				if (weakRef.get(0).get() != null && !weakRef.get(0).get().isFinishing()) {
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();	
					}
				}
				
				if (msg != null && msg.obj != null) {
					HashMap<String, Object> result = (HashMap<String, Object>)msg.obj;
					Uri uri = (Uri)result.get("URI");
					List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
					HashMap<String, String> data = new HashMap<String, String>();
					data.put(ImageAdapter.DESC, "SUCCESS");
					data.put(ImageAdapter.NAME, "NIKE");
					
					data.put(ImageAdapter.IMG_FILE_PATH, uri.toString());
					
					
					list.add(data);
					list.add(data);
					list.add(data);
					list.add(data);
					
//					TextView desc = (TextView) gridView.findViewById(R.id.desc);
//					desc.setText(data.get(position).get(DESC));
//					ImageView imageView = (ImageView) gridView.findViewById(R.id.image1);
//					imageView.setImageURI(Uri.parse(data.get(position).get(IMG_FILE_PATH)));
					
					ImageAdapter adapter = new ImageAdapter(GridViewCompanyActivity.this, ImageAdapter.ItemType.COMPANY, list);
					gridView.setAdapter(adapter);
				}
			default:
				break;
			}
		}
	};

	
	class Downloader extends AsyncTask<ArrayList<LinkedTreeMap>, Void, List<HashMap<String, String>>> {

		@Override
		protected List<HashMap<String, String>> doInBackground(ArrayList<LinkedTreeMap>... params) {
			try {
				List<HashMap<String, String>> result = downloadIconOrImage(params[0]);
				return result;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {
			
			super.onPostExecute(result);
			gridView.setAdapter(new ImageAdapter(GridViewCompanyActivity.this, ItemType.COMPANY, result));
		}
		
		
		
		
		
	}
	
	
	private List<HashMap<String, String>> downloadIconOrImage(ArrayList<LinkedTreeMap> obj) throws MalformedURLException {

		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		for(int i = 0; i < obj.size(); i++) {
			LinkedTreeMap data = (LinkedTreeMap) obj.get(i);
			HashMap<String, String> company = new HashMap<String, String>();
			company.put(ImageAdapter.ID, data.get("companyID").toString());
			company.put(ImageAdapter.NAME, data.get("comName").toString());
			company.put(ImageAdapter.IMG_FILE_PATH, getRemoteImage(new URL(data.get("logoIcon").toString())));

			result.add(company);
		}

		return result;

	}

	private void syncData() {
		String url = "http://mobile.bgnsolutions.com/getdata.php";
		RestClient rest = new RestClient(url);
		try {
			rest.Execute(RequestType.POST);

			String data = rest.GetResponse();
			// String sampleData =
			// MainActivity.this.getString(R.string.sample_data);
			// StringBuilder b = new StringBuilder(data);
			// b.deleteCharAt(0);
			// b.deleteCharAt(b.length() - 1);
			// data = b.toString();
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new StringReader(data));
			reader.setLenient(true);
			PreShopping preShopping = gson.fromJson(reader, PreShopping.class);

			System.out.println("Result: " + preShopping);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private GridView gridView;
	private GridLayout layout;
	private ActivityMode mode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_layout);

		layout = (GridLayout) findViewById(R.id.prod_list_layout_container);
		gridView = (GridView) findViewById(R.id.gridView2);
		
		List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
//		HashMap<String, String> data = new HashMap<String, String>();
//		data.put(ImageAdapter.DESC, "SUCCESS");
//		data.put(ImageAdapter.NAME, "NIKE");
//		data.put(ImageAdapter.IMG_FILE_PATH, "");
//		list.add(data);
		
		weakRef.add(new WeakReference<GridViewCompanyActivity>(this));
		
		if (savedInstanceState != null) {
			mode = ActivityMode.valueOf(savedInstanceState.getString(ACTIVITY_MODE));
		} else {
			mode = ActivityMode.NORMAL;
		}
		if (mode == ActivityMode.NORMAL) {
			String url = "http://mobile.bgnsolutions.com/getdata.php";
//			new SyncDataThread(this, uiHandler, url, "syncData", null).start();
				
		} else {
			Message msg = null;
			if (mode == ActivityMode.LOGGING_IN) {
				msg = Message.obtain(uiHandler, SyncDataThread.FLAG_SHOW_PROGRESS_DIALOG_LOGIN);	
			} else {
				msg = Message.obtain(uiHandler, SyncDataThread.FLAG_DISMISS_PROGRESS_DIALOG_AND_START_DOWNLOAD);
			}
			
			uiHandler.sendMessage(msg);
		}
		
		try {
			SQLiteDatabase db = MySqlHelper.getInstance(getApplicationContext()).getReadableDatabase();
			db.beginTransaction();
			
			Cursor c = db.rawQuery("SELECT * from prodGroup WHERE iconFilePath is not null;", null);
			

//			list.add(data);
//			list.add(data);
//			list.add(data);
//			list.add(data);
//			List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
			if (c != null && c.getCount() > 0) {
				if (c.moveToFirst()) {
					do {
						String name = c.getString(c.getColumnIndex("name"));
						String filePath = c.getString(c.getColumnIndex("iconFilePath"));
						String id = c.getString(c.getColumnIndex("_id"));
						
						HashMap<String, String> data = new HashMap<String, String>();
						data.put(ImageAdapter.DESC, TextUtils.isEmpty(name) ? "" : name);
						data.put(ImageAdapter.NAME, TextUtils.isEmpty(name) ? "" : name);
						data.put(ImageAdapter.IMG_FILE_PATH, filePath);
						data.put(ImageAdapter.ID, id);
						list.add(data);
					} while (c.moveToNext());
				}
			}
			
			
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}
		
		ImageAdapter adapter = new ImageAdapter(GridViewCompanyActivity.this, ImageAdapter.ItemType.COMPANY, list);
		gridView.setAdapter(adapter);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		outState.putString(ACTIVITY_MODE, mode.name());
		super.onSaveInstanceState(outState);
	}
	
	
	

}