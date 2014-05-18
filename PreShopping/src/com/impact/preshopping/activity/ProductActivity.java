package com.impact.preshopping.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import com.dudev.util.Utilities;
import com.impact.preshopping.BaseActivity;
import com.impact.preshopping.PreShoppingApp;
import com.impact.preshopping.R;
import com.impact.preshopping.R.color;
import com.impact.preshopping.adapter.ProductItemAdapter;
import com.impact.preshopping.adapter.ProductItemAdapter.IOnProductIconClicked;
import com.impact.preshopping.adapter.ProductItemAdapter.IOnProductTextClicked;

public class ProductActivity extends BaseActivity implements IOnProductIconClicked, IOnProductTextClicked{

	private ListView listView;
	private List<WeakReference<ProductActivity>> weakRef = new ArrayList<WeakReference<ProductActivity>>();
	private String grpId = "";
	private Bundle map;
//	public static final List<Integer> seenItemList = new ArrayList<Integer>();
	public static int seenItemPos = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prod_list_layout);
		listView = (ListView) findViewById(R.id.listView1);
		
		try {
			map = getIntent().getBundleExtra("EXTRA_INFO");
			grpId = map.getString("GROUP_ID");
		} catch (Exception e) {
			Log.e(TAG, "Group Id can't be null");
		}
		
		initializeListView();		
		
		weakRef.add(new WeakReference<ProductActivity>(this));
	}

	private void initializeListView() {
		if (!TextUtils.isEmpty(grpId)) {
//			new ProductLoaderTask().execute(grpId);	
			List<HashMap<String, String>> products = getProducts(grpId);
			
			ProductItemAdapter adapter = new ProductItemAdapter(ProductActivity.this, ProductActivity.this, ProductActivity.this, products);
			listView.setAdapter(adapter);
			if (adapter.getCount() > 0) {
				listView.smoothScrollToPosition(seenItemPos);
				listView.setSelection(seenItemPos);
			}
			
		}
	}


	private List<HashMap<String, String>> getProducts(String grpId) {
		List<HashMap<String, String>> products = new ArrayList<HashMap<String,String>>();
		
		try {
			products = Utilities.getProdList(getApplicationContext(), grpId);
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}
		
		return products;
	}
	
	
	@Override
	protected Intent getPreviousIntent() {
		Intent i = new Intent(getApplicationContext(), GroupActivity.class);
		i.putExtra("EXTRA_INFO", map);
		
		return i;
	}

	@Override
	protected void addActivityToStack() {
		((PreShoppingApp) getApplication()).getActivityStack().add(new WeakReference<Activity>(this));
	}

	// Params, Progress, Result
	private class ProductLoaderTask extends AsyncTask<String, Void, List<HashMap<String, String>>> {

		private ProgressDialog progressDialog;
		@Override
		protected void onPostExecute(List<HashMap<String, String>> products) {
			super.onPostExecute(products);
			
			if (weakRef.get(0).get() != null && !weakRef.get(0).get().isFinishing()) {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
					progressDialog = null;
				}	
			}
			
			ProductItemAdapter adapter = new ProductItemAdapter(ProductActivity.this, ProductActivity.this, ProductActivity.this, products);
			listView.setAdapter(adapter);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(ProductActivity.this);
			progressDialog.setIndeterminate(true);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected List<HashMap<String, String>> doInBackground(String... params) {
			
			String grpId = params[0];
			List<HashMap<String, String>> products = new ArrayList<HashMap<String,String>>();
			
			try {
				products = Utilities.getProdList(getApplicationContext(), grpId);
			} catch (Exception e) {
				Log.e(TAG, "" + e);
			}
			
			return products;
		}
	}

	@Override
	public void onIconClicked(int position, String id) {
		seenItemPos = position;
		Intent i = new Intent(ProductActivity.this, PhotoGalleryActivity.class);
		map.putString("PRODUCT_ID", id);
		i.putExtra("EXTRA_INFO", map);
		startActivity(i);
	}

	@Override
	public void onTextClicked(int position, String id) {
//		if (!seenItemList.contains(position)) {
//			seenItemList.add(position);
//		}
		
		// Remember last selected position. Automatically
		// scroll the list to this position when user is back
		// to this page.
		seenItemPos = position;
		
//		listView.getChildAt(position).setBackgroundColor(color.abs__bright_foreground_holo_light);
		
		Intent i = new Intent(ProductActivity.this, VideoListActivity.class);
		map.putString("PRODUCT_ID", id);
		i.putExtra("EXTRA_INFO", map);
		startActivity(i);
		overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
	}
	
	@Override
	public void onBackPressed() {
//		seenItemList.clear();
		seenItemPos = 0;
		super.onBackPressed();
	}
}
