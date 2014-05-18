package com.impact.preshopping.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.impact.preshopping.R;
import com.impact.preshopping.CompanyFragmentActivity.IUpdateFragment;
import com.impact.preshopping.adapter.ImageAdapter;
import com.impact.preshopping.db.MySqlHelper;

public class CategoryFragment extends BaseFragment implements IUpdateFragment{
	private String id;
	public interface IOnItemClicked_Category {
		public void onItemClicked_Category(String id);
		
	}
	private IOnItemClicked_Category itemClicked;
	public CategoryFragment(IOnItemClicked_Category itemClicked) {
		this.itemClicked = itemClicked;
	}
	
	private GridView gridView;
	public static final String TAG = CategoryFragment.class.getSimpleName();
	private ImageAdapter adapter;
	
	public CategoryFragment () {
		// required...
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		if (savedInstanceState != null) {
//			String tag = savedInstanceState.getString("TAG");
//			String id = savedInstanceState.getString("ID");
//			this.id = id;
//		} 
	}

//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		
//		outState.putString("TAG", getTag());
//		outState.putString("ID", getCurrentId());
//		super.onSaveInstanceState(outState);
//		
//	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.grid_layout, container, false);
		
		gridView = (GridView) v.findViewById(R.id.gridView2);
		
		if (adapter != null) {
			gridView.setAdapter(adapter);
		}
		
		return v;
	}

	@Override
	public void update(String _id) {
		if (TextUtils.isEmpty(_id)) {
			return;
		}
		
		this.id = _id;
		List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		try {
			
			SQLiteDatabase db = MySqlHelper.getInstance(getActivity()).getReadableDatabase();
			db.beginTransaction();
			
			Cursor c = db.rawQuery("SELECT * from prodCategory WHERE _id = " + _id, null);
			
			
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
		
		adapter = new ImageAdapter((Activity)itemClicked, ImageAdapter.ItemType.CATEGORY, list);
		adapter.setiCategory(itemClicked);
		
		if (gridView != null) {
			gridView.setAdapter(adapter);	
		}
	}

	@Override
	public String getPreviousFragmentTag() {
		return BaseFragment.TAG_COMPANY;
	}

	@Override
	public String getCurrentId() {
		
		return this.id;
	}

	@Override
	public String toString() {
		
		return this.id + "," + this.getTag();
	}

	@Override
	public void resumeUi(String id) {
		update(id);
	}
}
