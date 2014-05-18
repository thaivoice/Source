package com.impact.preshopping;

import java.io.File;

import net.appositedesigns.fileexplorer.FileExplorerApp;
import net.appositedesigns.fileexplorer.activity.FileListActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dudev.util.DiskUtils;
import com.dudev.util.Utilities;
import com.dudev.util.Utilities.MediaType;

public class ManageDataPanelActivity extends Activity {

	private String appRootPath;
	private TextView textViewTotalAppSpace;
	private TextView textViewMp4Total;
	private TextView textViewMp3Total;
	private TextView textViewImageTotal;
	private TextView textViewMp4;
	private TextView textViewMp3;
	private TextView textViewImage;
	private TextView textViewTotalSpace;
	private Button btnManageData;
	private TextView textViewFreeSpace;
	private ImageView imageViewMp4;
	private ImageView imageViewImage;

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_manage_data_panel);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(true);
//		getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_action_back));
		getActionBar().setTitle("Back");
//		getActionBar().setBackgroundDrawable(getResources().getDrawable(android.R.color.black));
		setTitle(Html.fromHtml("<font color='#ffffff'><b>Manage Data Panel</b></font>"));
		
		appRootPath = Utilities.getAppFolder_ExtSd(getApplicationContext());
		// Log.i("space", dirSize(appRootPath));
		textViewTotalAppSpace = (TextView) findViewById(R.id.textViewTotalAppSpace);
		textViewTotalAppSpace.setText(dirSize(appRootPath, MediaType.OTHER));

		textViewMp4Total = (TextView) findViewById(R.id.textViewMp4Total);
		textViewMp4Total.setText(dirSize(appRootPath + "/MP4", MediaType.MP4));

		textViewMp3Total = (TextView) findViewById(R.id.textViewMp3Total);
		textViewMp3Total.setText(dirSize(appRootPath + "/MP3", MediaType.MP3));

		textViewImageTotal = (TextView) findViewById(R.id.textViewImageTotal);
		textViewImageTotal.setText(dirSize(appRootPath + "/IMAGE", MediaType.IMAGE));

		textViewMp4 = (TextView) findViewById(R.id.textViewMp4);
		textViewMp4.setText(String.valueOf(totalMp4File) + " files");

		textViewMp3 = (TextView) findViewById(R.id.textViewMp3);
		textViewMp3.setText(String.valueOf(totalMp3File) + " files");

		textViewImage = (TextView) findViewById(R.id.textViewImage);
		textViewImage.setText(String.valueOf(totalImageFile) + " files");
		
		textViewFreeSpace = (TextView) findViewById(R.id.textViewFreeSpace);
		textViewFreeSpace.setText("" + DiskUtils.freeSpace(true) + " MB");
		
		textViewTotalSpace = (TextView) findViewById(R.id.textViewTotalSpace);
		textViewTotalSpace.setText("" + DiskUtils.totalSpace(true) + " MB");
		
		btnManageData = (Button) findViewById(R.id.btnMangeData);
		btnManageData.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (new File(appRootPath).exists()) {
					Intent fileExplorer = new Intent(getApplicationContext(), FileListActivity.class);
					fileExplorer.putExtra(FileExplorerApp.EXTRA_FOLDER, appRootPath);
					startActivity(fileExplorer);					
				}
			}
		});
		
		imageViewMp4 = (ImageView) findViewById(R.id.imageViewMp4);
		imageViewMp4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (new File(appRootPath + "/MP4").exists()) {
					Intent fileExplorer = new Intent(getApplicationContext(), FileListActivity.class);
					fileExplorer.putExtra(FileExplorerApp.EXTRA_FOLDER, appRootPath + "/MP4");
					startActivity(fileExplorer);
				}
			}
		});
		
		
		imageViewImage = (ImageView) findViewById(R.id.imageViewImage);
		imageViewImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (new File(appRootPath + "/IMAGE").exists()) {
					Intent fileExplorer = new Intent(getApplicationContext(), FileListActivity.class);
					fileExplorer.putExtra(FileExplorerApp.EXTRA_FOLDER, appRootPath + "/IMAGE");
					startActivity(fileExplorer);					
				}
			}
		});
	}

	private static String dirSize(String path, MediaType type) {

		File dir = new File(path);

		if (dir.exists()) {
			long bytes = getFolderSize(dir, type);
			if (bytes < 1024)
				return bytes + " B";
			int exp = (int) (Math.log(bytes) / Math.log(1024));
			String pre = ("KMGTPE").charAt(exp - 1) + "";

			return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
		}

		return "0 B";
	}

	private static int totalMp3File;
	private static int totalMp4File;
	private static int totalImageFile;

	public static long getFolderSize(File dir, MediaType type) {
		if (dir.exists()) {
			long result = 0;
			File[] fileList = dir.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// Recursive call if it's a directory
				if (fileList[i].isDirectory()) {
					result += getFolderSize(fileList[i], type);
				} else {
					// Sum the file size in bytes
					result += fileList[i].length();

					// Take the opportunity to calculate total files
					// in the folder.
					if (type == MediaType.IMAGE) {
						totalImageFile++;
					} else if (type == MediaType.MP3) {
						totalMp3File++;
					} else if (type == MediaType.MP4) { // MP4
						totalMp4File++;
					} else {
						// ignore!
					}
				}
			}
			return result; // return the file size
		}
		return 0;
	}
}
