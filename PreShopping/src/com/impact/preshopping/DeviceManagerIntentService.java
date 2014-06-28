package com.impact.preshopping;

import java.util.HashMap;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.github.kevinsawicki.http.HttpRequest;

public class DeviceManagerIntentService extends IntentService {

	private static final String ACTION_VALIDATE_DEVICE = "com.impact.preshopping.action.CHECK_DEV_VALIDITY";
	private static final String ACTION_OTHER = "com.impact.preshopping.action.OTHER";

	private static final String EXTRA_DEV_ID = "com.impact.preshopping.extra.PARAM_DEV_ID";
	private static final String EXTRA_OTHER_PARAM = "com.impact.preshopping.extra.PARAM_OTHER_PARAM";
	public static final int ID = 5005;

	public static void validateDevice(Context context, String devId,
			String other) {
		Intent intent = new Intent(context, DeviceManagerIntentService.class);
		intent.setAction(ACTION_VALIDATE_DEVICE);
		intent.putExtra(EXTRA_DEV_ID, devId);
		intent.putExtra(EXTRA_OTHER_PARAM, other);
		context.startService(intent);
	}

	public static void startActionBaz(Context context, String param1,
			String param2) {
		Intent intent = new Intent(context, DeviceManagerIntentService.class);
		intent.setAction(ACTION_OTHER);
		intent.putExtra(EXTRA_DEV_ID, param1);
		intent.putExtra(EXTRA_OTHER_PARAM, param2);
		context.startService(intent);
	}

	public DeviceManagerIntentService() {
		super("DeviceManagerIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent != null) {
			final String action = intent.getAction();
			if (ACTION_VALIDATE_DEVICE.equals(action)) {
				final String devId = intent.getStringExtra(EXTRA_DEV_ID);
				final String other = intent.getStringExtra(EXTRA_OTHER_PARAM);
				validateDeviceById(devId, other);
			} else if (ACTION_OTHER.equals(action)) {
				final String param1 = intent.getStringExtra(EXTRA_DEV_ID);
				final String param2 = intent.getStringExtra(EXTRA_OTHER_PARAM);
				handleAction_OTHER(param1, param2);
			}
		}
	}

	private void validateDeviceById(String devId, String notUse) {
		
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("deviceID", devId);
		String response = HttpRequest.post("http://www.preshopping.net/checkdevice.php", data, false).acceptJson().body();
		System.out.println("response="+response);
	}

	private void handleAction_OTHER(String param1, String param2) {
		// TODO: Handle action Baz
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
