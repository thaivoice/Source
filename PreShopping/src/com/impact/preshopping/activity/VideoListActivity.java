package com.impact.preshopping.activity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.dudev.util.Utilities;
import com.dudev.util.Utilities.MediaType;
import com.impact.preshopping.BaseActivity;
import com.impact.preshopping.PreShoppingApp;
import com.impact.preshopping.R;
import com.impact.preshopping.VideoDownloadCompletedEvent;
import com.impact.preshopping.adapter.VideoListAdapter;
import com.impact.preshopping.adapter.VideoListAdapter.IOnChecked;
import com.impact.preshopping.adapter.VideoListAdapter.IOnItemClicked;
import com.squareup.otto.Subscribe;
import com.stickmanventures.android.example.immersive_videoplayer.ImmersiveVideoplayer;
import com.stickmanventures.android.example.immersive_videoplayer.entities.Video;

public class VideoListActivity extends BaseActivity implements IOnItemClicked, IOnChecked {

	public interface IOnExit {
		public void onExitActivity();
	}

	private IOnExit iOnExit;
	private ListView listView;
	private String prodId;
	private Bundle map;
	private List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
	private List<Integer> checkedBoxs = new ArrayList<Integer>();
	public static int seenItemPos = 0;

	private int mProgress = 100;
	Handler mHandler = new Handler();
	Runnable mProgressRunner = new Runnable() {
		@Override
		public void run() {
			mProgress += 2;

			// Normalize our progress along the progress bar's scale
			int progress = (Window.PROGRESS_END - Window.PROGRESS_START) / 100 * mProgress;
			setSupportProgress(progress);

			if (mProgress < 100) {
				mHandler.postDelayed(mProgressRunner, 50);
			}
		}

	};

	private boolean backFromQrJob;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);

		// if (mProgress == 100) {
		// mProgress = 0;
		// mProgressRunner.run();
		// }

		try {
			map = getIntent().getBundleExtra("EXTRA_INFO");
			prodId = map.getString("PRODUCT_ID");
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}
		
		if (TextUtils.isEmpty(prodId)) {
			prodId = getIntent().getExtras().getString("PRODUCT_ID");
			backFromQrJob = true;
		}
		
		listView = (ListView) findViewById(R.id.listView1);
		
	}

	@Override
	protected void onResume() {
		
		initialize();
		super.onResume();
		
	}

	private void initialize() {

		if (!list.isEmpty()) {
			list.clear();
		}
		
		// Get product detail part for list header.
		List<String> result = Utilities.getProdInfo(getApplicationContext(), prodId);
		HashMap<String, Object> productDetail = new HashMap<String, Object>();
		productDetail.put("PROD_DETAIL", result);
		// also add header of the list.
		list.add(productDetail);

		// Get video list item.
		List<HashMap<String, Object>> videoList = Utilities.getVideoInfo(getApplicationContext(), prodId);
		list.addAll(videoList);

		VideoListAdapter adapter = new VideoListAdapter(VideoListActivity.this, R.layout.video_list_item, android.R.id.text1, this, this, list);
		listView.setAdapter(adapter);
		iOnExit = adapter;
		
		// Automatically scroll to last seen item on the list.
		if (adapter.getCount() > 0) {
			listView.setSelection(seenItemPos);
		}
	}

	@Override
	protected Intent getPreviousIntent() {
//		VideoListActivity.seenItemList.clear();
		
		Intent i = new Intent(getApplicationContext(), ProductActivity.class);
		i.putExtra("EXTRA_INFO", map);

		return i;
	}

	@Override
	protected void addActivityToStack() {
		((PreShoppingApp) getApplication()).getActivityStack().add(new WeakReference<Activity>(this));
	}

	@Override
	public void onBackPressed() {

		seenItemPos = 0;
		
		if (iOnExit != null) {
			iOnExit.onExitActivity();
		}

		startDownload(null);

		// super.onBackPressed();
	}

	@SuppressWarnings("unchecked")
	private void startDownload(MenuItem item) {

		List<HashMap<String, Object>> downloadList = new ArrayList<HashMap<String, Object>>();

		for (int i = 0; i < checkedBoxs.size(); i++) {
			HashMap<String, Object> data = list.get(checkedBoxs.get(i));
			downloadList.add(data);
		}

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < downloadList.size(); i++) {
			HashMap<String, Object> data = downloadList.get(i);

			Uri uri = (Uri) data.get("URI");
			String id = data.get("ID").toString();
			String type = data.get("TYPE").toString();
			MediaType t;
			if (Integer.parseInt(type) == MediaType.MP3.getCode()) {
				t = MediaType.MP3;
			} else if (Integer.parseInt(type) == MediaType.IMAGE.getCode()) {
				t = MediaType.IMAGE;
			} else { // mp4.
				t = MediaType.MP4;
			}

			HashMap<String, String> toDownloadData = new HashMap<String, String>();
			toDownloadData.put(Utilities._ID, id);
			toDownloadData.put(Utilities.URL, uri.toString());
			toDownloadData.put(Utilities.TYPE, t.name());
			toDownloadData.put(Utilities.PROD_ID, prodId);

			list.add(toDownloadData);
		}
		
		checkedBoxs.clear();
		if (list.size() > 0) {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			prefs.edit().putBoolean("APP_STATUS", true).commit();

			boolean shouldAskForPermision = prefs.getBoolean("download_permission", true);
			if (shouldAskForPermision) {
				// Show dialog first.
				showPermisionDialog(list);
			} else {
				downloadTask = new DownloadVideoTask();
				downloadTask.execute(list);
				VideoListActivity.super.onBackPressed();
			}
		} else {
			
			if (item == null) { // called by onBackpressed method.
				VideoListActivity.super.onBackPressed();
			} else {
				VideoListActivity.super.onOptionsItemSelected(item);
			}
		}

	}

	private void showPermisionDialog(final List<HashMap<String, String>> list) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Dialog));
		alertDialog.setTitle("Download Permision"); // your dialog title
		alertDialog.setMessage(getString(R.string.lbl_download_permision, String.valueOf(list.size())));

		alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			@SuppressWarnings("unchecked")
            public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				downloadTask = new DownloadVideoTask();
				downloadTask.execute(list);
			}
		});
		alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				prefs.edit().putBoolean("APP_STATUS", false).commit();
				VideoListActivity.super.onBackPressed();
			}
		});
		alertDialog.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (iOnExit != null) {
				iOnExit.onExitActivity();
			}
		}

		startDownload(item);
		return true;
	}

	@Override
	public void onItemClicked(int position) {
		Log.i(TAG, "" + position);
		
		// Remember last seen item position on the list.
		seenItemPos = position;
		
		try {
			Intent intent = new Intent(VideoListActivity.this, Class.forName("com.stickmanventures.android.example.immersive_videoplayer.ui.activities.VideoPlayerActivity"));

			HashMap<String, Object> data = list.get(position);
			Uri local = TextUtils.isEmpty(data.get("FILE_PATH").toString()) ? null : (Uri) data.get("FILE_PATH");
			Uri remote = data.get("URI") == null ? null : (Uri) data.get("URI");

			Uri uri = null;
			if (local == null) {
				uri = remote;
			} else {

				if ((new File(local.getPath()).exists())) {
					uri = local;
				} else {
					uri = remote;
				}
			}

			// Create a video object to be passed to the activity
			Video video = new Video(uri.toString());

			// video.setTitle("Big Buck Bunny");
			// video.setAuthor("the Blender Institute");
			//
			video.setDescription("A short computer animated film by the Blender Institute, part of the Blender Foundation. Like the foundation's previous film Elephants Dream, the film was made using Blender, a free software application for animation made by the same foundation. It was released as an Open Source film under Creative Commons License Attribution 3.0.");

			// Launch the activity with some extras
			intent.putExtra(ImmersiveVideoplayer.EXTRA_LAYOUT, "0");
			intent.putExtra(Video.class.getName(), video);
			startActivity(intent);
	
		} catch (ClassNotFoundException e) {
			Log.e(TAG, "" + e);
		}
	}

	@Override
	public void onChecked(int position) {

		if (position >= 0) {
			checkedBoxs.add(Integer.valueOf(position));
			Toast.makeText(getApplicationContext(), getString(R.string.lbl_download_instruction), Toast.LENGTH_SHORT).show();
		} else {
			checkedBoxs.remove(Integer.valueOf(Math.abs(position)));
		}
	}

	@Subscribe
	public void onDownloadComplete(VideoDownloadCompletedEvent e) {

		checkedBoxs.clear();
		Intent group = getPreviousIntent();
		group.putExtra("EXTRA_INFO", map);
		startActivity(group);
//		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		finish();
	}

}
