package com.impact.preshopping.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.impact.preshopping.R;
import com.impact.preshopping.CompanyFragmentActivity.IUpdateFragment;
import com.impact.preshopping.R.layout;
import com.impact.preshopping.adapter.ImageAdapter;
import com.impact.preshopping.db.MySqlHelper;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link CompanyFragment.OnFragmentInteractionListener} interface to handle
 * interaction events.
 * 
 */
public class CompanyFragment extends BaseFragment implements IUpdateFragment{
	
	public interface IOnItemClicked_Company {
		public void onItemClicked_Company(String id);

	}

	private IOnItemClicked_Company itemClicked;
	
	public CompanyFragment(IOnItemClicked_Company itemClicked) {

		this.itemClicked = itemClicked;
	}

	private String id;
	private GridView gridView;
	public static final String TAG = CategoryFragment.class.getSimpleName();
	private List<HashMap<String, String>> list;
	private ImageAdapter adapter;
	
	public CompanyFragment() {
		// required...
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.grid_layout, container, false);
		gridView = (GridView) v.findViewById(R.id.gridView2);
		if (adapter != null) {
			gridView.setAdapter(adapter);	
		}
		
		return v;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
//		if (mListener != null) {
//			mListener.onFragmentInteraction(uri);
//		}
	}



	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
//		try {
//			mListener = (OnFragmentInteractionListener) activity;
//		} catch (ClassCastException e) {
//			throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
//		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
//		mListener = null;
	}
	@Override
	public String getPreviousFragmentTag() {
		return "";
	}

	@Override
	public String getCurrentId() {
		// TODO Auto-generated method stub
		return "0";
	}
	
	@Override
	public String toString() {
		
		return "*," + this.getTag();
	}


	@Override
	public void resumeUi(String id) {
		update(id);
	}


	@Override
	public void update(String _id) {
		
		if (TextUtils.isEmpty(_id)) {
			return;
		}
		
		this.id = _id;
		try {
			list = new ArrayList<HashMap<String,String>>();
			SQLiteDatabase db = MySqlHelper.getInstance((Activity)itemClicked).getReadableDatabase();
			db.beginTransaction();
			
			Cursor c = db.rawQuery(String.format("SELECT %s from company WHERE iconFilePath is not null;", _id), null);
			
			
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
		
		adapter = new ImageAdapter((Activity)itemClicked, ImageAdapter.ItemType.COMPANY, list);
		adapter.setiCompany(itemClicked);
		if (gridView != null) {
			gridView.setAdapter(adapter);	
		}
	}
}
