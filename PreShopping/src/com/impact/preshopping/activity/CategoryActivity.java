package com.impact.preshopping.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

import com.impact.preshopping.BaseActivity;
import com.impact.preshopping.CompanyFragmentActivity.IOnItemClicked;
import com.impact.preshopping.DeviceManagerIntentService;
import com.impact.preshopping.PreLoginActivity;
import com.impact.preshopping.PreShoppingApp;
import com.impact.preshopping.R;
import com.impact.preshopping.SyncDataService;
import com.impact.preshopping.adapter.ImageAdapter;
import com.impact.preshopping.db.MySqlHelper;

public class CategoryActivity extends BaseActivity implements IOnItemClicked{

	private GridView gridView;
	private String id;
	private Bundle map;

	
    private void scheduleAlarm() {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        int interval = Integer.parseInt(getString(R.string.sync_data_interval));

    	PendingIntent devMgr = PendingIntent.getService(getApplicationContext(), 3333, new Intent(getApplicationContext(), DeviceManagerIntentService.class), 0);
    	PendingIntent syncData = PendingIntent.getService(getApplicationContext(), 5555, new Intent(getApplicationContext(), SyncDataService.class), 0);

        am.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.currentThreadTimeMillis(), interval * 60 * 1000, syncData);
        am.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.currentThreadTimeMillis(), (interval) * 60 * 1000, devMgr);
    }
    
    private void cancelAlarms() {
    	AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
    	PendingIntent devMgr = PendingIntent.getService(getApplicationContext(), 3333, new Intent(getApplicationContext(), DeviceManagerIntentService.class), 0);
    	PendingIntent syncData = PendingIntent.getService(getApplicationContext(), 5555, new Intent(getApplicationContext(), SyncDataService.class), 0);
    	am.cancel(devMgr);
    	am.cancel(syncData);
    }	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_layout);
		
		try {
			boolean forceExit = getIntent().getBooleanExtra("FORCE_EXIT", false);
			if (forceExit) {
				cancelAlarms();
				Intent preLogin = new Intent(getApplicationContext(), PreLoginActivity.class);
				startActivity(preLogin);
				finish();
				return;
			}
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}
		
		gridView = (GridView) findViewById(R.id.gridView2);
		if (Boolean.valueOf(getString(R.string.app_no_company_preset))) {
			map = new Bundle();
			id = "";
		} else {
			try {
//				id = getIntent().getExtras().getString("ID");
				map = getIntent().getBundleExtra("EXTRA_INFO");
				id = map.getString("COMPANY_ID");
			} catch (Exception e) {
				Log.e("ERR", "id can't be null");
			}			
		}
		
		TextView title = (TextView) findViewById(R.id.textViewTitle);
		title.setText("Product Category List");
		
		if (Boolean.valueOf(getString(R.string.app_no_company_preset))) {
			scheduleAlarm();	
		}
		
	}
    
	@Override
	protected void onResume() {
		super.onResume();
//		if (!TextUtils.isEmpty(id)) {
//			updateUi(id);
//		}
		updateUi(id);
	}

	private void updateUi(String _id) {
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		try {
			SQLiteDatabase db = MySqlHelper.getInstance(getApplicationContext()).getReadableDatabase();
			db.beginTransaction();
			
			Cursor c = null;
			
			if (TextUtils.isEmpty(_id)) {
				c = db.rawQuery("SELECT * from prodCategory", null);
			} else {
				c = db.rawQuery("SELECT * from prodCategory WHERE company_id = " + _id, null);
			}
			
			if (c != null && c.getCount() > 0) {
				if (c.moveToFirst()) {
					do {
						String name = c.getString(c.getColumnIndex("name"));
						String filePath = c.getString(c.getColumnIndex("iconFilePath"));
						String id = c.getString(c.getColumnIndex("_id"));
						String url = c.getString(c.getColumnIndex("icon"));
						
						HashMap<String, String> data = new HashMap<String, String>();
						data.put(ImageAdapter.DESC, TextUtils.isEmpty(name) ? "" : name);
						data.put(ImageAdapter.NAME, TextUtils.isEmpty(name) ? "" : name);
						data.put(ImageAdapter.IMG_FILE_PATH, filePath);
						data.put(ImageAdapter.ID, id);
						data.put(ImageAdapter.IMG_URL_PATH, url);
						
						list.add(data);
						
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
		
		ImageAdapter adapter = new ImageAdapter(CategoryActivity.this, ImageAdapter.ItemType.COMPANY, list);
		adapter.setiOnItemClicked(CategoryActivity.this);
		if (gridView != null) {
			gridView.setAdapter(adapter);	
		}
	}
	
	
	@Override
	protected Intent getPreviousIntent() {
		
		Intent i = null;
		
		if (Boolean.valueOf(getString(R.string.app_no_company_preset))) {
			i = null;
		} else {
			i = new Intent(getApplicationContext(), CompanyActivity.class);
			i.putExtra("EXTRA_INFO", map);
		}
		
		return i;
	}

	@Override
	protected void addActivityToStack() {
		
		((PreShoppingApp) getApplication()).getActivityStack().add(new WeakReference<Activity>(this));
	}

	@Override
	public void onItemClicked(String id) {
		
		Intent i = new Intent(getApplicationContext(), GroupActivity.class);
		
		map.putString("CATEGORY_ID", id);
		i.putExtra("EXTRA_INFO", map);
		
		startActivity(i);
		overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
		
		if (!Boolean.valueOf(getString(R.string.app_no_company_preset))) {
			finish();	
		}
	}


}
