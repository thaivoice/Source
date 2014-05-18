package com.dudev.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.Callable;

import javax.net.ssl.HttpsURLConnection;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DownloadImageThread implements Callable {

	public enum ImageType {
		COMPANY,
		CATEGORY,
		GROUP,
		PRODUCT,
		NONE
	}
	
	public final String TAG;
	private String url;
	private Context context;
	private Handler handler;
	private ImageType belongTo;
	private int id;

	public HashMap<String, Object> run() {
		
		HashMap<String, Object> result = startDownload(context, url);
		
		if (handler != null) {
			Message msg = Message.obtain(handler, SyncDataThread.FLAG_SHOW_IMAGE, result);
			handler.sendMessage(msg);	
		}
		
		return result;
	}

	public DownloadImageThread(Context context, int id, String url, Handler handler) {
		TAG = this.getClass().getSimpleName();
		this.url = url;
		this.context = context;
		this.handler = handler;
		this.id = id;
	}

	public DownloadImageThread(Context context, int id, String url, ImageType belongTo) {
		TAG = this.getClass().getSimpleName();
		this.url = url;
		this.context = context;
		this.belongTo = belongTo;
		this.id = id;
	}
	
	
	public HashMap<String, Object> startDownload(Context context, String downloadUrl) {
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		OutputStream out = null;
		InputStream in = null;
		int downloaded = 0;

		String filePath = Utilities.getAppFolder(context);
		try {
			URL url = new URL(downloadUrl);
			
			char c = downloadUrl.charAt(downloadUrl.length() - 1);
			
			String filename = "";
			if (c == '/') {
				int index = downloadUrl.lastIndexOf(c);
				filename = downloadUrl.substring(index);
			} else {
				int index = downloadUrl.lastIndexOf("/");
				filename = downloadUrl.substring(index + 1);
			}

			File destFile = new File(filePath + "/" + filename);
			
			if (!destFile.exists()) {
				destFile.createNewFile();
			} else {
				downloaded = (int) destFile.length();
			}

			out = downloaded == 0 ? new FileOutputStream(destFile) : new FileOutputStream(destFile, true);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			int total = conn.getContentLength();
			conn.disconnect();
			
			if (total > downloaded) {
				conn = (HttpURLConnection) url.openConnection(); 
				conn.setRequestMethod("GET");
				conn.setDoOutput(true);
				conn.setRequestProperty("Range", "bytes=" + downloaded + "-");
				conn.setConnectTimeout(120 * 1000);
				conn.setReadTimeout(120 * 1000);
				conn.connect();
				in = conn.getInputStream();
				
				byte[] buffer = new byte[1024];
				int length = 0;
				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}

				conn.disconnect();
			}
			
			result.put("URI", Uri.fromFile(destFile).toString());
			result.put("ID", String.valueOf(this.id));
			result.put("BELONG_TO", this.belongTo.name());
			
		} catch (IOException e) {
			result.put("URI", "");
			result.put("ID", this.id);
			result.put("BELONG_TO", this.belongTo);
		} catch (ClassCastException e) {
			Log.e(TAG, "Download error " + e.getMessage());
			result.put("URI", "");
			result.put("ID", this.id);
			result.put("BELONG_TO", this.belongTo);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					Log.e(TAG, "Cannot close outputstream");
				}
			}

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				in = null;
			}
		}
		
		return result;
	}

	@Override
	public Object call() throws Exception {
		return run();
	}
}
