package com.impact.preshopping;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.dudev.util.BusProvider;
import com.dudev.util.Constants;
import com.dudev.util.RequestType;
import com.dudev.util.RestClient;
import com.dudev.util.Utilities;
import com.impact.preshopping.activity.CategoryActivity;
import com.squareup.otto.Subscribe;

public class DeviceManagerIntentService extends IntentService {

	public static final String ACTION_VALIDATE_DEVICE = "com.impact.preshopping.action.CHECK_DEV_VALIDITY";
	public static final String EXTRA_DEV_ID = "com.impact.preshopping.extra.PARAM_DEV_ID";
	public static final int ID = 5005;
	public static final String TAG = DeviceManagerIntentService.class
			.getSimpleName();

	public DeviceManagerIntentService() {
		super(DeviceManagerIntentService.class.getSimpleName());
	}

	@Override
	public void onHandleIntent(Intent intent) {
		if (intent != null) {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			String devId = prefs.getString(Utilities.REG_DEVICE_ID, "");
			validateDeviceById(devId);
		}
	}

	private void validateDeviceById(String devId) {

		boolean isValid = false;
		String endpoint = Utilities.reformEndpoint(getApplicationContext(),
				Constants.TAG_VALIDATE_DEVICE_METHOD);
		RestClient validateDevClient = new RestClient(endpoint);

		Map<String, String> data = new HashMap<String, String>();
		data.put(Utilities.REG_DEVICE_ID, devId);
		JSONObject name = new JSONObject(data);
		validateDevClient.AddParam("data", name.toString());

		try {
			validateDevClient.Execute(RequestType.POST);
			JSONArray response = new JSONArray(validateDevClient.GetResponse());
			String responseCode = response.getJSONObject(0).getString("status");
			Log.i(TAG, "" + responseCode);
			JSONArray arr = response.getJSONObject(0).getJSONArray("data");
			Log.i(TAG, "nExists=" + arr.getJSONObject(0).getString("nExists"));
			int count = Integer.parseInt(arr.getJSONObject(0).getString(
					"nExists"));
			if (count > 0) {
				isValid = true;
			} else {
				isValid = false;
			}
		} catch (Exception e) {
			isValid = true;
			Log.e(TAG, "" + e);
		}

		System.out.println((isValid) ? "VALID DEVICE" : "INVALID DEVICE");
		if (!isValid) {
			BusProvider.getInstance().post(new InvalidDeviceDetectedEvent());
		}
	}

	@Subscribe
	public void onInvalidDeviceDetected(InvalidDeviceDetectedEvent e) {
    	Intent category = new Intent(getApplicationContext(), CategoryActivity.class);
    	category.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    	category.putExtra("FORCE_EXIT", true);
    	startActivity(category);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		BusProvider.getInstance().register(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		BusProvider.getInstance().unregister(this);
	}
}
