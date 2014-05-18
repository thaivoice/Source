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
import com.impact.preshopping.PreShoppingApp;
import com.impact.preshopping.R;
import com.impact.preshopping.SyncDataService;
import com.impact.preshopping.CompanyFragmentActivity.IOnItemClicked;
import com.impact.preshopping.adapter.ImageAdapter;
import com.impact.preshopping.db.MySqlHelper;

public class CategoryActivity extends BaseActivity implements IOnItemClicked{

	private GridView gridView;
	private String id;
	private Bundle map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_layout);
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
		
		scheduleAlarm();
	}
    private PendingIntent getPendingIntent(Context context, int id) {
        Intent intent =  new Intent(context, SyncDataService.class);
        return PendingIntent.getService(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    private void scheduleAlarm() {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.currentThreadTimeMillis(), 30 * 1000, getPendingIntent(getApplicationContext(), 1234));
    }
    
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
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
		finish();
	}


}
