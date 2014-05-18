package com.impact.preshopping;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class VideoDownloadTaskReceiver extends BroadcastReceiver {
	
	public interface IOnDownloadComplete {
		public void onDownloadComplete();
	}
	private IOnDownloadComplete listener;
	public VideoDownloadTaskReceiver(IOnDownloadComplete listener) {
		this.listener = listener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (listener != null) {
			listener.onDownloadComplete();
		}
	}
}
