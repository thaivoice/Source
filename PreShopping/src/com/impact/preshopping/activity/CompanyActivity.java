package com.impact.preshopping.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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
import com.impact.preshopping.PreShoppingApp;
import com.impact.preshopping.R;
import com.impact.preshopping.SyncDataService;
import com.impact.preshopping.adapter.ImageAdapter;
import com.impact.preshopping.db.MySqlHelper;

public class CompanyActivity extends BaseActivity implements IOnItemClicked{

	private GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_layout);
		gridView = (GridView) findViewById(R.id.gridView2);
		TextView title = (TextView) findViewById(R.id.textViewTitle);
		title.setText("Company List");
		
//		Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
//		intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//		startActivityForResult(intent, 0);
//		IntentIntegrator.initiateScan(this);    // `this` is the current Activity or Context
		
		scheduleAlarm();
	}

    private void scheduleAlarm() {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        int interval = Integer.parseInt(getString(R.string.sync_data_interval));
        am.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.currentThreadTimeMillis(), interval * 60 * 1000, getPendingIntent(getApplicationContext(), 1234));
    }
    
    private PendingIntent getPendingIntent(Context context, int id) {
        Intent intent =  new Intent(context, SyncDataService.class);
        return PendingIntent.getService(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		updateUi("*");
	}

	private void updateUi(String _id) {
		if (TextUtils.isEmpty(_id)) {
			return;
		}
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		try {
			SQLiteDatabase db = MySqlHelper.getInstance(getApplicationContext()).getReadableDatabase();
			db.beginTransaction();
			
//			Cursor c = db.rawQuery(String.format("SELECT %s from company WHERE iconFilePath is not null;", _id), null);
			Cursor c = db.rawQuery("SELECT * from company;", null);
			
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
		
		ImageAdapter adapter = new ImageAdapter(CompanyActivity.this, ImageAdapter.ItemType.COMPANY, list);
		adapter.setiOnItemClicked(CompanyActivity.this);
		gridView.setAdapter(adapter);	
	}
	
	
	@Override
	protected Intent getPreviousIntent() {
		return null;
	}

	@Override
	protected void addActivityToStack() {
		((PreShoppingApp) getApplication()).getActivityStack().clear();
		((PreShoppingApp) getApplication()).getActivityStack().add(new WeakReference<Activity>(this));
	}

	private Bundle map = new Bundle();
	@Override
	public void onItemClicked(String id) {
//		map.putString("COMPANY_ID", "*");
//		map.putString("CATEGORY_ID", id);
		map.putString("COMPANY_ID", id);
		
		((PreShoppingApp)getApplication()).setMap(map);
		
		Intent i = new Intent(getApplicationContext(), CategoryActivity.class);
		i.putExtra("ID", id);
		i.putExtra("EXTRA_INFO", map);
		
		startActivity(i);
		overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
	}



}
