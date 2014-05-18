package com.impact.preshopping.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.impact.preshopping.R;

public class FileExplore extends Activity {

	// Stores names of traversed directories
	ArrayList<String> str = new ArrayList<String>();

	// Check if the first level of the directory structure is the one showing
	private Boolean firstLvl = true;

	private static final String TAG = "F_PATH";

	private Item[] fileList;
	private File path = new File(Environment.getExternalStorageDirectory() + "");
	private String chosenFile;
	private static final int DIALOG_LOAD_FILE = 1000;

	ListAdapter adapter;

	private String sdcard2;
	private String sdcard1;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		
		sdcard2 = getExternalStorage();
		sdcard1 = Environment.getExternalStorageDirectory().getAbsolutePath();
		
		
		if (sdcard1.equalsIgnoreCase(sdcard2) || !isMediaMounted()) {
			loadFileList();	
		} else {
			loadSdcardList();
		}

		showDialog(DIALOG_LOAD_FILE);

//		Log.d(TAG, path.getAbsolutePath());
//		Log.i(TAG, "Inernal: " + Environment.getRootDirectory() + "");
//		Log.i(TAG, "External1: " + Environment.getExternalStorageDirectory() + "");
//		Log.i(TAG, "External2: " + getExternalStorage());
	}

	private boolean isMediaMounted () {
		if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
			return false;
		}
		return true;
	}
	
	private void loadSdcardList() {
		String[] fList = new String[]{sdcard1, sdcard2};
		fileList = new Item[fList.length];
		for (int i = 0; i < fList.length; i++) {
			fileList[i] = new Item(fList[i], R.drawable.file_icon);

			// Convert into file path
			File sel = new File(path, fList[i]);

			// Set drawables
//			if (sel.isDirectory()) {
//				fileList[i].icon = R.drawable.directory_icon;
//				Log.d("DIRECTORY", fileList[i].file);
//			} else {
//				Log.d("FILE", fileList[i].file);
//			}
			fileList[i].icon = R.drawable.directory_icon;
		}
		
		adapter = new ArrayAdapter<Item>(this, android.R.layout.select_dialog_item, android.R.id.text1, fileList) {
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				// creates view
				View view = super.getView(position, convertView, parent);
				TextView textView = (TextView) view.findViewById(android.R.id.text1);

				// put the image on the text view
				textView.setCompoundDrawablesWithIntrinsicBounds(fileList[position].icon, 0, 0, 0);

				// add margin between image and text (support various screen
				// densities)
				int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
				textView.setCompoundDrawablePadding(dp5);

				view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (position == 0) {
							path = new File(sdcard1);
						} else {
							path = new File(sdcard2);
						}
						removeDialog(DIALOG_LOAD_FILE);
						firstLvl = false;
						loadFileList();
						showDialog(DIALOG_LOAD_FILE);
					}
				});
				
				return view;
			}
		};
	}

	private void loadFileList() {
		try {
			path.mkdirs();
		} catch (SecurityException e) {
			Log.e(TAG, "unable to write on the sd card ");
		}

		// Checks whether path exists
		if (path.exists()) {
			FilenameFilter filter = new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					File sel = new File(dir, filename);
					// Filters based on whether the file is hidden or not
					return ((sel.isFile() || sel.isDirectory()) && !sel.isHidden());

				}
			};

			String[] fList = path.list(filter);
			fileList = new Item[fList.length];
			for (int i = 0; i < fList.length; i++) {
				fileList[i] = new Item(fList[i], R.drawable.file_icon);

				// Convert into file path
				File sel = new File(path, fList[i]);

				// Set drawables
				if (sel.isDirectory()) {
					fileList[i].icon = R.drawable.directory_icon;
					Log.d("DIRECTORY", fileList[i].file);
				} else {
					Log.d("FILE", fileList[i].file);
				}
			}

			if (!firstLvl) {
				Item temp[] = new Item[fileList.length + 1];
				for (int i = 0; i < fileList.length; i++) {
					temp[i + 1] = fileList[i];
				}
				temp[0] = new Item("Up", R.drawable.directory_up);
				fileList = temp;
			}
		} else {
			Log.e(TAG, "path does not exist");
		}

		adapter = new ArrayAdapter<Item>(this, android.R.layout.select_dialog_item, android.R.id.text1, fileList) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// creates view
				View view = super.getView(position, convertView, parent);
				TextView textView = (TextView) view.findViewById(android.R.id.text1);

				// put the image on the text view
				textView.setCompoundDrawablesWithIntrinsicBounds(fileList[position].icon, 0, 0, 0);

				// add margin between image and text (support various screen
				// densities)
				int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
				textView.setCompoundDrawablePadding(dp5);

				firstLvl = false;
				return view;
			}
		};

	}

	private class Item {
		public String file;
		public int icon;

		public Item(String file, Integer icon) {
			this.file = file;
			this.icon = icon;
		}

		@Override
		public String toString() {
			return file;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new Builder(this);

		if (fileList == null) {
			Log.e(TAG, "No files loaded");
			dialog = builder.create();
			return dialog;
		}

		switch (id) {
		case DIALOG_LOAD_FILE:
			builder.setTitle("Choose your file");
			builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
				
				
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					chosenFile = fileList[which].file;
					Log.e(TAG, "chosenFile=" + chosenFile);
					File sel = new File(path + "/" + chosenFile);
					
					if (sel.isDirectory()) {
						firstLvl = false;

						// Adds chosen directory to list
						
						str.add(chosenFile);
						fileList = null;
						path = new File(sel + "");

						loadFileList();

						removeDialog(DIALOG_LOAD_FILE);
						showDialog(DIALOG_LOAD_FILE);
						Log.d(TAG, path.getAbsolutePath());

					}

					// Checks if 'up' was clicked
					else if (chosenFile.equalsIgnoreCase("up") && !sel.exists()) {

						// present directory removed from list
						int index = str.size() - 1;
						if (index < 0) {
							firstLvl = false;
							loadSdcardList();
							removeDialog(DIALOG_LOAD_FILE);
							showDialog(DIALOG_LOAD_FILE);
							
						} else {
							String s = str.remove(index);

							// path modified to exclude present directory
							path = new File(path.toString().substring(0, path.toString().lastIndexOf(s)));
							fileList = null;

							// if there are no more directories in the list, then
							// its the first level
//							if (str.isEmpty()) {
//								firstLvl = true;
//							}
							loadFileList();

							removeDialog(DIALOG_LOAD_FILE);
							showDialog(DIALOG_LOAD_FILE);
							Log.d(TAG, path.getAbsolutePath());
						}
						

					}
					// File picked
					else {
						// Perform action with file picked
						Log.i(TAG, "Selected File="+path.getAbsolutePath()+"/"+chosenFile);
						String filePath = path.getAbsolutePath()+"/"+chosenFile;
						Uri uri = Uri.fromFile(new File(filePath));
						Intent data = new Intent();
						data.putExtra("SELECTED_FILE_PATH", uri);
						setResult(99, data);
						finish();
					}	
				}
			});
			break;
		}
		dialog = builder.create();
		dialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				onBackPressed();
			}
		});
		dialog.show();
		return dialog;
	}
	public static String getExternalStorage() {
		File file = new File("/system/etc/vold.fstab");
		FileReader fr = null;
		BufferedReader br = null;
		String path = "/mnt/sdcard";
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			if (fr != null) {
				br = new BufferedReader(fr);
				String s = br.readLine();
				while (s != null) {
					if (s.startsWith("dev_mount")) {
						String[] tokens = s.split("\\s");
						path = tokens[2]; // mount_point
						Log.w(TAG, "path: " + path);
						if (!Environment.getExternalStorageDirectory().getAbsolutePath().equals(path)) {
							break;
						}
					}
					s = br.readLine();
					Log.w(TAG, "storage: " + s);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fr != null) {
					fr.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return path;
	}

	@Override
	public void onBackPressed() {
		setResult(Activity.RESULT_CANCELED);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBackPressed();
		}
		return true;
	}
	
	
	
}