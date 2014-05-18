package com.impact.preshopping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import com.actionbarsherlock.view.MenuItem;
import com.dudev.util.Constants;
import com.dudev.util.RequestType;
import com.dudev.util.RestClient;
import com.dudev.util.Utilities;

public class FavoriteSettingActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{

	private HashMap<String, String> allCompanies = new HashMap<String, String>();
//	private HashMap<String, String> allCategories = new HashMap<String, String>();
//	private HashMap<String, String> allGroups = new HashMap<String, String>();
//	private HashMap<String, String> allProducts = new HashMap<String, String>();

	public static final String TAG = FavoriteSettingActivity.class.getSimpleName();
	private boolean changeDetected;
	private MultiSelectListPreference byCompany;
//	private MultiSelectListPreference byCategory;
//	private MultiSelectListPreference byGroup;
//	private MultiSelectListPreference byProduct;
	private CheckBoxPreference permision;
//	private ListPreference home_screen;
	private CheckBoxPreference notification_on_off;
	private ListPreference alert_type;
	private RingtonePreference notification_sounds;
	private Uri alertSound;
	
	
	@Override
    protected void onPause() {
        super.onPause();
        ((PreShoppingApp)getApplication()).setAdminMode(false);
    }


    @Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

		
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == event.KEYCODE_BACK) {
			if (!changeDetected) {
				return super.onKeyDown(keyCode, event);
			}
			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			String email = prefs.getString(Utilities.EMAIL, "");
			
			HashMap<String, String> data = new HashMap<String, String>();
			Set<String> compSet = byCompany.getValues();
			String byCompany = "";
			boolean first = true;
			for (String k : compSet) {
				if (first) {
					byCompany = k;
					first = false;
				} else {
					byCompany += "," + k;
				}
			}
			data.put("BY_COMPANY", byCompany);

//			Set<String> categorySet = byCategory.getValues();
//			String byCategory = "";
//			first = true;
//			for (String k : categorySet) {
//				if (first) {
//					byCategory = k;
//					first = false;
//				} else {
//					byCategory += "," + k;
//				}
//			}
			data.put("BY_CATEGORY", "0");
//
//			Set<String> groupSet = byGroup.getValues();
//			String byGroup = "";
//			first = true;
//			for (String k : groupSet) {
//				if (first) {
//					byGroup = k;
//					first = false;
//				} else {
//					byGroup += "," + k;
//				}
//			}
			data.put("BY_GROUP", "0");
//
//			Set<String> productSet = byProduct.getValues();
//			String byProduct = "";
//			first = true;
//			for (String k : productSet) {
//				if (first) {
//					byProduct = k;
//					first = false;
//				} else {
//					byProduct += "," + k;
//				}
//			}
			data.put("BY_PRODUCT", "0");
			data.put("DOWNLOAD_PERMISION", permision.isChecked() ? "1" : "0");
			data.put("HOME_SCREEN", "0");
			data.put("notificationOnOff", notification_on_off.isChecked() ? "1" : "0");
			data.put("alertType", alert_type.getValue());
			data.put("alertType", alert_type.getValue());
			data.put("soundFile", alertSound == null ? "" : alertSound.toString());
			data.put("email", email);

			HashMap<String, String> copy = new HashMap<String, String>();
			copy.remove("email");
			copy.put("userId", email);
			copy.put(Utilities.SETTING_ALERT_TYPE, data.get("alertType"));
			copy.put(Utilities.SETTING_COMPANY, data.get("BY_COMPANY"));
			copy.put(Utilities.SETTING_DOWNLOAD_PERMISION, data.get("DOWNLOAD_PERMISION"));
			copy.put(Utilities.SETTING_HOME_SCREEN_TYPE, "0");
			copy.put(Utilities.SETTING_NOTIFICATION_ONOFF, data.get("notificationOnOff"));
			copy.put(Utilities.SETTING_PROD, "0");
			copy.put(Utilities.SETTING_PROD_CATEGORY, "0");
			copy.put(Utilities.SETTING_PROD_GROUP, "0");
			copy.put(Utilities.SETTING_SOUND_FILE, data.get("soundFile"));
			
			// store settings in db.
			Utilities.insertOrUpdate(getApplicationContext(), copy);

			JSONObject obj = new JSONObject(data);
			
			new StoreSettingOnServerTask().execute(obj);
		}
		
		return true;
	}

	// AsyncTask<Params, Progress, Result>
	private class StoreSettingOnServerTask extends AsyncTask<JSONObject, Void, Boolean> {

		private ProgressDialog dialog;

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			
			finish();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new ProgressDialog(FavoriteSettingActivity.this);
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage(getString(R.string.lbl_msg_update_setting_data));
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(JSONObject... params) {
			JSONObject obj = params[0];

			try {
				// Validate screen name.
				String endpoint = Utilities.reformEndpoint(getApplicationContext(), Constants.TAG_SETTING_METHOD);
				RestClient settingClient = new RestClient(endpoint);
				
				settingClient.AddParam("data", obj.toString());
				settingClient.Execute(RequestType.POST);

				Log.i(TAG, "" + settingClient.GetResponse());

				JSONArray response = new JSONArray(settingClient.GetResponse());
				String responseCode = response.getJSONObject(0).getString("status");
				Log.i(TAG, "responseCode=" + responseCode);

				if (responseCode.equals(Constants.TAG_RESPONSE_CODE_SUCCESS)) {
					JSONArray arr = response.getJSONObject(0).getJSONArray("data");
					Log.i(TAG, "" + arr);
				}
			} catch (Exception e) {
				Log.e(TAG, "" + e);
			}

			return Boolean.TRUE;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(true);
//		getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_action_back));
		getActionBar().setTitle("Back");
//		getActionBar().setBackgroundDrawable(getResources().getDrawable(android.R.color.black));
		setTitle(Html.fromHtml("<font color='#ffffff'><b>Favorite Setting</b></font>"));

		// Change endpoint:
		Preference header = (Preference) getPreferenceScreen().findPreference("pref_header");
		
		if (!((PreShoppingApp)getApplication()).isAdminMode()) {
		    PreferenceCategory endpoint = (PreferenceCategory) getPreferenceScreen().findPreference("pref_category_endpoint");
		    getPreferenceScreen().removePreference(endpoint);
		}
		
		// By Company
		allCompanies = Utilities.getAllCompanies(getApplicationContext());
		PreferenceCategory favorite = (PreferenceCategory) getPreferenceScreen().findPreference("pref_category_favorite_product");
		byCompany = (MultiSelectListPreference) favorite.findPreference("by_company");

		String[] arrCompEntries = new String[allCompanies.size()];
		String[] arrCompValues = new String[allCompanies.size()];
		int i = 0;
		for (String id : allCompanies.keySet()) {
			arrCompValues[i] = id;
			arrCompEntries[i] = allCompanies.get(id);
			i++;

		}
		byCompany.setEntries(arrCompEntries);
		byCompany.setEntryValues(arrCompValues);

//		// By Category
//		allCategories = Utilities.getAllCategories(getApplicationContext());
//		byCategory = (MultiSelectListPreference) favorite.findPreference("by_category");
//		String[] arrCatEntries = new String[allCategories.size()];
//		String[] arrCatValues = new String[allCategories.size()];
//		i = 0;
//		for (String id : allCategories.keySet()) {
//			arrCatValues[i] = id;
//			arrCatEntries[i] = allCategories.get(id);
//			i++;
//
//		}
//		byCategory.setEntries(arrCatEntries);
//		byCategory.setEntryValues(arrCatValues);
//
//		// By Group
//		allGroups = Utilities.getAllGroups(getApplicationContext());
//		byGroup = (MultiSelectListPreference) favorite.findPreference("by_group");
//		String[] arrGrpEntries = new String[allGroups.size()];
//		String[] arrGrpValues = new String[allGroups.size()];
//		i = 0;
//		for (String id : allGroups.keySet()) {
//			arrGrpValues[i] = id;
//			arrGrpEntries[i] = allGroups.get(id);
//			i++;
//
//		}
//		byGroup.setEntries(arrGrpEntries);
//		byGroup.setEntryValues(arrGrpValues);
//
//		// By Product
//		allProducts = Utilities.getAllProducts(getApplicationContext());
//		byProduct = (MultiSelectListPreference) favorite.findPreference("by_product");
//		String[] arrProdEntries = new String[allProducts.size()];
//		String[] arrProdValues = new String[allProducts.size()];
//		i = 0;
//		for (String id : allProducts.keySet()) {
//			arrProdValues[i] = id;
//			arrProdEntries[i] = allProducts.get(id);
//			i++;
//		}
//		byProduct.setEntries(arrProdEntries);
//		byProduct.setEntryValues(arrProdValues);

		// Download permision
		PreferenceCategory pref_category_download_permission = (PreferenceCategory) getPreferenceScreen().findPreference("pref_category_download_permission");
		permision = (CheckBoxPreference) pref_category_download_permission.findPreference("download_permission");

//		PreferenceCategory pref_category_home_screen_selection = (PreferenceCategory) getPreferenceScreen().findPreference("pref_category_home_screen_selection");
//		home_screen = (ListPreference) pref_category_home_screen_selection.findPreference("home_screen");

		PreferenceCategory pref_category_notification_on_off = (PreferenceCategory) getPreferenceScreen().findPreference("pref_category_notification_on_off");
		notification_on_off = (CheckBoxPreference) pref_category_notification_on_off.findPreference("notification_on_off");

		alert_type = (ListPreference) pref_category_notification_on_off.findPreference("alert_type");

		notification_sounds = (RingtonePreference) pref_category_notification_on_off.findPreference("notification_sounds");
		notification_sounds.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {

				alertSound = Uri.parse((String) newValue);

				return false;
			}
		});
		
		getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		
		restorePreviousSettings();
	}

	private void restorePreviousSettings() {
		
		HashMap<String, String> settings = new HashMap<String, String>();
		settings = Utilities.getSettings(getApplicationContext());
		
		for (String field : settings.keySet()) {
			Log.i(TAG, field + "=" + settings.get(field));
		}
		String onOff = settings.get(Utilities.SETTING_NOTIFICATION_ONOFF);
		if (!TextUtils.isEmpty(onOff)) {
			notification_on_off.setChecked(onOff.equals("1") ? true : false);	
		}
		
		String downloadPermision = settings.get(Utilities.SETTING_DOWNLOAD_PERMISION);
		if (!TextUtils.isEmpty(downloadPermision)) {
			permision.setChecked(downloadPermision.equals("1") ? true : false);
		}
		
//		String homeScreen = settings.get(Utilities.SETTING_HOME_SCREEN_TYPE);
//		if (!TextUtils.isEmpty(homeScreen)) {
//			home_screen.setValue(homeScreen);
//		}
		
		String alertType = settings.get(Utilities.SETTING_ALERT_TYPE);
		if (!TextUtils.isEmpty(alertType)) {
			alert_type.setValue(alertType);
		}
		
//		String category = settings.get(Utilities.SETTING_PROD_CATEGORY);
//		if (!TextUtils.isEmpty(category)) {
//			Set<String> c = new HashSet<String>();
//			String[] split = category.split(",");
//			for (String s : split) {
//				c.add(s);
//			}
//			
//			byCategory.setValues(c);
//		}
//		
//		String group = settings.get(Utilities.SETTING_PROD_GROUP);
//		if (!TextUtils.isEmpty(group)) {
//			Set<String> c = new HashSet<String>();
//			String[] split = group.split(",");
//			for (String s : split) {
//				c.add(s);
//			}
//			
//			byGroup.setValues(c);
//		}
//		
//		String prod = settings.get(Utilities.SETTING_PROD);
//		if (!TextUtils.isEmpty(prod)) {
//			Set<String> c = new HashSet<String>();
//			String[] split = prod.split(",");
//			for (String s : split) {
//				c.add(s);
//			}
//			
//			byProduct.setValues(c);
//		}
		
		String company = settings.get(Utilities.SETTING_COMPANY);
		if (!TextUtils.isEmpty(company)) {
			Set<String> c = new HashSet<String>();
			String[] split = company.split(",");
			for (String s : split) {
				c.add(s);
			}
			
			byCompany.setValues(c);
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		changeDetected = true;
	}
}
