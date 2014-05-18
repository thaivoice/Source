package com.impact.preshopping;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.dudev.util.Utilities;
import com.dudev.util.Utilities.MediaType;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DownloadIntentService extends IntentService {
	// TODO: Rename actions, choose action names that describe tasks that this
	// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
	private static final String ACTION_FOO = "com.impact.preshopping.action.FOO";
	private static final String ACTION_DOWNLOAD = "com.impact.preshopping.action.BAZ";

	// TODO: Rename parameters
	private static final String EXTRA_ID = "com.impact.preshopping.extra.PARAM1";
	private static final String EXTRA_URI = "com.impact.preshopping.extra.PARAM2";
	private static final String EXTRA_MEDIA_TYPE = "com.impact.preshopping.extra.PARAM3";

	public static final String TAG = DownloadIntentService.class.getSimpleName();
	
	/**
	 * Starts this service to perform action Foo with the given parameters. If
	 * the service is already performing a task this action will be queued.
	 * 
	 * @see IntentService
	 */
	// TODO: Customize helper method
	public static void startActionFoo(Context context, String param1, String param2) {
		Intent intent = new Intent(context, DownloadIntentService.class);
		intent.setAction(ACTION_FOO);
		intent.putExtra(EXTRA_ID, param1);
		intent.putExtra(EXTRA_URI, param2);
		context.startService(intent);
	}

	/**
	 * Starts this service to perform action Baz with the given parameters. If
	 * the service is already performing a task this action will be queued.
	 * 
	 * @see IntentService
	 */
	// TODO: Customize helper method
	public static void startActionDownload(Context context, String id, String uri, MediaType type) {
		Intent intent = new Intent(context, DownloadIntentService.class);
		intent.setAction(ACTION_DOWNLOAD);
		intent.putExtra(EXTRA_ID, id);
		intent.putExtra(EXTRA_URI, uri);
		intent.putExtra(EXTRA_MEDIA_TYPE, type.name());
		context.startService(intent);
	}

	public DownloadIntentService() {
		super("DownloadIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent != null) {
			final String action = intent.getAction();
			if (ACTION_FOO.equals(action)) {
				final String param1 = intent.getStringExtra(EXTRA_ID);
				final String param2 = intent.getStringExtra(EXTRA_URI);
				handleActionFoo(param1, param2);
			} else if (ACTION_DOWNLOAD.equals(action)) {
				final String id = intent.getStringExtra(EXTRA_ID);
				final String uri = intent.getStringExtra(EXTRA_URI);
				final String type = intent.getStringExtra(EXTRA_MEDIA_TYPE);
				handleActionDownload(id, uri, type);
			}
		}
	}

	/**
	 * Handle action Foo in the provided background thread with the provided
	 * parameters.
	 */
	private void handleActionFoo(String param1, String param2) {
		// TODO: Handle action Foo
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Handle action Baz in the provided background thread with the provided
	 * parameters.
	 */
	private void handleActionDownload(String id, String uri, String type) {
		Log.i(TAG, "Downloading: " + uri);
		Uri downloadedUri = Utilities.download(getApplicationContext(), id, uri, MediaType.valueOf(type));

		if (downloadedUri != null) {
			long row = Utilities.updateMediaTbl(getApplicationContext(), id, downloadedUri.toString());
			System.out.println("downloaded to..." + downloadedUri);
			System.out.println("Updated number of rows: " + row);
		}
	}
}
