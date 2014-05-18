package com.impact.preshopping.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

import com.impact.preshopping.BaseActivity;
import com.impact.preshopping.PreShoppingApp;
import com.impact.preshopping.R;
import com.impact.preshopping.CompanyFragmentActivity.IOnItemClicked;
import com.impact.preshopping.adapter.ImageAdapter;
import com.impact.preshopping.db.MySqlHelper;

public class GroupActivity extends BaseActivity implements IOnItemClicked{

	private GridView gridView;
	private String id;
	private String categoryId;
	private Bundle map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_layout);
		gridView = (GridView) findViewById(R.id.gridView2);

		try {
			map = getIntent().getBundleExtra("EXTRA_INFO");
			id = map.getString("CATEGORY_ID");
		} catch (Exception e) {
			Log.e("ERR", "id can't be null");
		}

//		if (TextUtils.isEmpty(id)) {
//			try {
//				id = getIntent().getExtras().getString("GROUP_ID");
//			} catch (Exception e) {
//				Log.e("ERR", "id can't be null");
//			}
//		}
		
		TextView title = (TextView) findViewById(R.id.textViewTitle);
		title.setText("Product Group List");
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if (!TextUtils.isEmpty(id)) {
			updateUi(id);	
		}
	}

	private void updateUi(String _id) {
		if (TextUtils.isEmpty(_id)) {
			return;
		}
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		try {
			SQLiteDatabase db = MySqlHelper.getInstance(getApplicationContext()).getReadableDatabase();
			db.beginTransaction();
			
			Cursor c = db.rawQuery("SELECT * from prodGroup WHERE categoryId = " + _id, null);
			
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
		
		ImageAdapter adapter = new ImageAdapter(GroupActivity.this, ImageAdapter.ItemType.COMPANY, list);
		adapter.setiOnItemClicked(GroupActivity.this);
		gridView.setAdapter(adapter);
	}
	
	
	@Override
	protected Intent getPreviousIntent() {
		Intent i = new Intent(getApplicationContext(), CategoryActivity.class);
		i.putExtra("EXTRA_INFO", map);
		
		return i;
	}

	@Override
	protected void addActivityToStack() {
		((PreShoppingApp) getApplication()).getActivityStack().add(new WeakReference<Activity>(this));
	}

	@Override
	public void onItemClicked(String id) {
		
		Intent i = new Intent(getApplicationContext(), ProductActivity.class);
		map.putString("GROUP_ID", id);
		i.putExtra("EXTRA_INFO", map);
		startActivity(i);
		overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
		finish();
	}
}
