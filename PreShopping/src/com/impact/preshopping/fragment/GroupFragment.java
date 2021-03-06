package com.impact.preshopping.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.impact.preshopping.CompanyFragmentActivity.IUpdateFragment;
import com.impact.preshopping.R;
import com.impact.preshopping.adapter.ImageAdapter;
import com.impact.preshopping.db.MySqlHelper;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link CompanyFragment.OnFragmentInteractionListener} interface to handle
 * interaction events.
 * 
 */
public class GroupFragment extends BaseFragment implements IUpdateFragment {

	public interface IOnItemClicked_Group {
		public void onItemClicked_Group(String id);
	}

//	private IOnItemClicked_Group itemClicked;
private String id;
	private GridView gridView;

	public static final String TAG = CategoryFragment.class.getSimpleName();
	private ImageAdapter adapter;
//	public GroupFragment(IOnItemClicked_Group itemClicked) {
//
//		this.itemClicked = itemClicked;
//	}

	public GroupFragment () {
		// required....
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// try {
		// mListener = (OnFragmentInteractionListener) activity;
		// } catch (ClassCastException e) {
		// throw new ClassCastException(activity.toString() +
		// " must implement OnFragmentInteractionListener");
		// }
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		// if (mListener != null) {
		// mListener.onFragmentInteraction(uri);
		// }
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

	@Override
	public void onDetach() {
		super.onDetach();
		// mListener = null;
	}

	@Override
	public void update(String _id) {
		
		if (TextUtils.isEmpty(_id)) {
			return;
		}
		
		this.id = _id;
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {

			SQLiteDatabase db = MySqlHelper.getInstance(getActivity()).getReadableDatabase();
			db.beginTransaction();

			Cursor c = db.rawQuery("SELECT * from prodGroup WHERE _id = " + _id, null);

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

//		adapter = new ImageAdapter((Activity)itemClicked, ImageAdapter.ItemType.GROUP, list);
//		adapter.setiGroup(itemClicked);
		
		if (gridView != null) {
			gridView.setAdapter(adapter);	
		}
		
	}

	@Override
	public String getPreviousFragmentTag() {
		return BaseFragment.TAG_CATEGORY;
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
