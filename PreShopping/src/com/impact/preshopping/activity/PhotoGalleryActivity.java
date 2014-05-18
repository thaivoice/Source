package com.impact.preshopping.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.TextView;

import com.dudev.util.Utilities;
import com.dudev.util.Utilities.MediaType;
import com.impact.preshopping.BaseActivity;
import com.impact.preshopping.PreShoppingApp;
import com.impact.preshopping.R;
import com.impact.preshopping.adapter.GalleryImageAdapter;
import com.impact.preshopping.adapter.TouchImageView;

public class PhotoGalleryActivity extends BaseActivity {

	private Gallery gallery;
	private String prodId;
	private List<WeakReference<PhotoGalleryActivity>> weakRef = new ArrayList<WeakReference<PhotoGalleryActivity>>();
	private TouchImageView prodImageView;
	public static final String TAG = PhotoGalleryActivity.class.getSimpleName();
	private List<Uri> list;
	private Bundle map;
	private TextView textViewPaging;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_gallery);
		
		try {
			map = getIntent().getBundleExtra("EXTRA_INFO");
			prodId = map.getString("PRODUCT_ID");
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}
		
		gallery = (Gallery) findViewById(R.id.prodGallery);
		prodImageView = (TouchImageView) findViewById(R.id.prodImageView);
		weakRef.add(new WeakReference<PhotoGalleryActivity>(this));
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Uri uri = list.get(position);
				prodImageView.setImageURI(uri);
				textViewPaging.setText("" + (position+1) + "/"+list.size());
			}
			
		});
		
		textViewPaging = (TextView) findViewById(R.id.textViewPaging);
		
		if (!TextUtils.isEmpty(prodId)) {
			new LoadImageTask().execute(prodId);
		}
	}

	// Params, Progress, Result
	private class LoadImageTask extends AsyncTask<String, Void, List<Uri>> {

		private ProgressDialog progressDialog;

		@Override
		protected List<Uri> doInBackground(String... params) {
			
			String id = params[0];
			boolean shouldDownload = false;
			List<Uri> list = new ArrayList<Uri>();
			List<String> filePathList = Utilities.getMediaFilePath(getApplicationContext(), prodId);
			for (String path : filePathList) {
				if (TextUtils.isEmpty(path) || path.equals("0")) {
					shouldDownload = true;
					break;
				}
			}
			
			if (shouldDownload) {
				List<String> urls = Utilities.getMediaUrl(getApplicationContext(), id);	
				
				for (String url : urls) {
					Uri uri = Utilities.download(getApplicationContext(), id, url, MediaType.IMAGE);
					
					if (uri != null) {
						Utilities.updateMediaTblByProdId(getApplicationContext(), prodId, url, uri.toString());
						list.add(uri);	
					}
				}
			} else {
				for (String path : filePathList) {
					try {
						list.add(Uri.parse(path));
					} catch (Exception e) {
						Log.e(TAG, "" + e);
					}
				}
			}

			return list;
		}

		@Override
		protected void onPostExecute(List<Uri> list) {
			super.onPostExecute(list);
			if (weakRef.get(0).get() != null && !weakRef.get(0).get().isFinishing()) {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
					progressDialog = null;
				}	
			}
			PhotoGalleryActivity.this.list = list;
			GalleryImageAdapter adapter = new GalleryImageAdapter(PhotoGalleryActivity.this, list);
			gallery.setAdapter(adapter);
			
			if (list.size() > 0) {
				gallery.setSelection(0);
				prodImageView.setImageURI(list.get(0));
				textViewPaging.setText("1" + "/"+list.size());
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(PhotoGalleryActivity.this);
			progressDialog.setMessage("Loading images, please wait...");
			progressDialog.setIndeterminate(true);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}
	}
	
	
	
	@Override
	protected Intent getPreviousIntent() {
		Intent i = new Intent(getApplicationContext(), ProductActivity.class);
		i.putExtra("EXTRA_INFO", map);
		
		return i;
	}

	@Override
	protected void addActivityToStack() {
		((PreShoppingApp) getApplication()).getActivityStack().add(new WeakReference<Activity>(this));
	}

	
}
