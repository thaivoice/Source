package com.impact.preshopping;

import static com.dudev.util.gcm.CommonUtilities.EXTRA_MESSAGE;
import static com.dudev.util.gcm.CommonUtilities.SENDER_ID;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.dudev.util.Constants;
import com.dudev.util.RequestType;
import com.dudev.util.RestClient;
import com.dudev.util.Utilities;
import com.dudev.util.gcm.ConnectionDetector;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.impact.preshopping.activity.RegistrationActivity2.PreferredLanguage;

public class RegistrationActivity extends Activity implements OnEditorActionListener, OnFocusChangeListener {

	private int invalidFieldNum;
	private int totalRequiredFieldNum;
	private Button btnRegister;
	private List<EditText> requiredFieldList = new ArrayList<EditText>();
	private GoogleCloudMessaging gcm;
	private String regid;
	private RegisterTask registerTask;
	private EditText editTextEmail;
	private EditText editTextConfirmEmail;
	private EditText editTextScreenName;

	public enum Field {
		EMAIL, SCREEN_NAME, NONE
	}

	private RadioButton radioBtnEn;
	private RadioButton radioBtnThai;
	public static final String TAG = RegistrationActivity.class.getSimpleName();
	private List<View> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dummy_activity2);
		
	    getActionBar().setTitle("Registration");
//	    getActionBar().setBackgroundDrawable(getResources().getDrawable(android.R.color.black));
		
		list = getAllChildren(findViewById(R.id.layoutRegister));
		for (View v : list) {
			if (v instanceof EditText) {
				if (((EditText) v).getHint().equals(getString(R.string.lbl_field_required))) {
					((EditText) v).setOnEditorActionListener(this);
					((EditText) v).setOnFocusChangeListener(this);
					invalidFieldNum++;
					totalRequiredFieldNum++;
					requiredFieldList.add(((EditText) v));
				} else {
					((EditText) v).setVisibility(View.GONE);
				}
			}
		}

		editTextEmail = (EditText) findViewById(R.id.editTextEmail);
		editTextConfirmEmail = (EditText) findViewById(R.id.editTextConfirmEmail);
		editTextScreenName = (EditText) findViewById(R.id.editTextScreenName);

		// radioBtnEn = (RadioButton) findViewById(R.id.radioBtnEn);
		// radioBtnThai = (RadioButton) findViewById(R.id.radioBtnThai);
		// radioBtnThai.setChecked(true);

		btnRegister = (Button) findViewById(R.id.btnSave);
		btnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (registerTask != null) {
					return;
				}

				ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

				// Check if Internet present
				if (!cd.isConnectingToInternet()) {
					// Internet Connection is not present
					Toast.makeText(getApplicationContext(), getString(R.string.lbl_err_plz_connect_internet), Toast.LENGTH_LONG).show();
					return;
				}

				for (EditText e : requiredFieldList) {
					if (!TextUtils.isEmpty(e.getError()) || TextUtils.isEmpty(e.getText().toString())) {
						Toast.makeText(getApplicationContext(), getString(R.string.lbl_err_plz_enter_valid_data), Toast.LENGTH_LONG).show();
						return;
					} /*else {
						onValidate(e);
						if (e.getError() != null) {
							return;
						}
					}*/
				}

				String email = editTextEmail.getText().toString();
				String confirmedEmail = editTextConfirmEmail.getText().toString();
				if (!Utilities.isValidEmail(email)) {
					editTextEmail.setError(getString(R.string.lbl_err_plz_enter_valid_email));
					Toast.makeText(getApplicationContext(), getString(R.string.lbl_err_plz_enter_valid_email), Toast.LENGTH_LONG).show();
					return;
				} else {
					editTextEmail.setError(null);
				}
//
				if (!email.equals(confirmedEmail)) {
					editTextConfirmEmail.setError(getString(R.string.lbl_err_email_not_equal));
					Toast.makeText(getApplicationContext(), getString(R.string.lbl_err_email_not_equal), Toast.LENGTH_LONG).show();
					return;
				} else {
					editTextConfirmEmail.setError(null);
				}

				// good to go...
				HashMap<String, String> data = new HashMap<String, String>();
				data.put(Utilities.REG_DEVICE_ID, "dummyDevId");
				data.put(Utilities.REG_EMAIL, ((EditText) findViewById(R.id.editTextEmail)).getText().toString());

				// Requirements: Exclude all optional fields for first time
				// registration.
				// data.put(Utilities.REG_FACEBOOK, ((EditText)
				// findViewById(R.id.editTextFacebook)).getText().toString());
				// data.put(Utilities.REG_INSTRAGRAM, ((EditText)
				// findViewById(R.id.editTextInstragram)).getText().toString());
				// data.put(Utilities.REG_LINE, ((EditText)
				// findViewById(R.id.editTextLine)).getText().toString());
				// data.put(Utilities.REG_OFFICE_PHONE, ((EditText)
				// findViewById(R.id.editTextOfficePhone)).getText().toString());
				// data.put(Utilities.REG_PREGERRED_LANG,
				// radioBtnThai.isChecked() ? PreferredLanguage.TH.name() :
				// PreferredLanguage.EN.name());
				// data.put(Utilities.REG_SKYPE, ((EditText)
				// findViewById(R.id.editTextSkype)).getText().toString());
				// data.put(Utilities.REG_TWEETER, ((EditText)
				// findViewById(R.id.editTextTweeter)).getText().toString());
				data.put(Utilities.REG_SKYPE, "");
				data.put(Utilities.REG_TWEETER, "");
				data.put(Utilities.REG_PREGERRED_LANG, PreferredLanguage.TH.name());
				data.put(Utilities.REG_OFFICE_PHONE, "");
				data.put(Utilities.REG_LINE, "");
				data.put(Utilities.REG_INSTRAGRAM, "");
				data.put(Utilities.REG_FACEBOOK, "");

				data.put(Utilities.REG_FIRST_NAME, ((EditText) findViewById(R.id.editTextFirstName)).getText().toString());
				data.put(Utilities.REG_IMEI, Utilities.getImei(getApplicationContext()));

				data.put(Utilities.REG_IS_ACTIVE, "1"); // hard coded for now.
														// Backend used only
														// flag.
				data.put(Utilities.REG_LAST_NAME, ((EditText) findViewById(R.id.editTextLastName)).getText().toString());

				data.put(Utilities.REG_MOBILE_PHONE, ((EditText) findViewById(R.id.editTextMobilePhone)).getText().toString());

				data.put(Utilities.REG_PASSWORD, ((EditText) findViewById(R.id.editTextPassword)).getText().toString());

				data.put(Utilities.REG_SCREEN_NAME, ((EditText) findViewById(R.id.editTextScreenName)).getText().toString());

				data.put(Utilities.REG_DEVICE_TYPE, Constants.TAG_ANDROID_DEV_TYPE); // 1
																						// =
																						// iOS,
																						// 2
																						// =
																						// Android.

				registerTask = new RegisterTask();
				registerTask.execute(data);

			}
		});

		// Check if GoogleplayService is installed...
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getGcmDevId();
		}
	}

	private void storeGcmDevId(String regId) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		int appVersion = getAppVersion();
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Constants.TAG_GCM_DEV_ID, regId);
		editor.putInt(Constants.TAG_APP_VERSION, appVersion);

		editor.commit();
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, Constants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	private String getGcmDevId() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String registrationId = prefs.getString(Constants.TAG_GCM_DEV_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}

		int registeredVersion = prefs.getInt(Constants.TAG_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion();
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}

		return registrationId;
	}

	private int getAppVersion() {
		try {
			PackageInfo packageInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private ArrayList<View> getAllChildren(View v) {

		if (!(v instanceof ViewGroup)) {
			ArrayList<View> viewArrayList = new ArrayList<View>();
			viewArrayList.add(v);
			return viewArrayList;
		}

		ArrayList<View> result = new ArrayList<View>();

		ViewGroup vg = (ViewGroup) v;
		for (int i = 0; i < vg.getChildCount(); i++) {

			View child = vg.getChildAt(i);

			ArrayList<View> viewArrayList = new ArrayList<View>();
			viewArrayList.add(v);
			viewArrayList.addAll(getAllChildren(child));

			result.addAll(viewArrayList);
		}
		return result;
	}

	// @Override
	// protected Intent getPreviousIntent() {
	// Intent i = new Intent(this, DummyActivity.class);
	// return i;
	// }

	// @Override
	// protected void addActivityToStack() {
	// ((PreShoppingApp) getApplication()).getActivityStack().add(new
	// WeakReference<Activity>(this));
	//
	// }

	@Override
	public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
		try {
			boolean isValid = onValidate(((EditText) textView));
			if (isValid) {
				if (invalidFieldNum > 0) {
					invalidFieldNum--;
				} else {
					if (invalidFieldNum < totalRequiredFieldNum) {
						invalidFieldNum++;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	private boolean onValidate(EditText view) {

		if (view != null) {
			view.setError(null);
			if (TextUtils.isEmpty(view.getText().toString())) {
				if (view.getHint().equals(getString(R.string.lbl_field_required))) {
					view.setError(getString(R.string.error_field_required));
					if (view.getId() == R.id.editTextMobilePhone) {
						((ImageView) findViewById(R.id.imageViewMobilePhone)).setVisibility(View.GONE);
					} else if (view.getId() == R.id.editTextFirstName) {
						((ImageView) findViewById(R.id.imageViewFirstName)).setVisibility(View.GONE);
					} else if (view.getId() == R.id.editTextLastName) {
						((ImageView) findViewById(R.id.imageViewLastName)).setVisibility(View.GONE);
					}else if (view.getId() == R.id.editTextPassword) {
						((ImageView) findViewById(R.id.imageViewPassword)).setVisibility(View.GONE);
					} else if (view.getId() == R.id.editTextConfirmEmail) {
						((ImageView) findViewById(R.id.imageViewConfirmEmail)).setVisibility(View.GONE);
					}else if (view.getId() == R.id.editTextEmail) {
						((ImageView) findViewById(R.id.imageViewEmail)).setVisibility(View.GONE);
					}else if (view.getId() == R.id.editTextConfirmPw) {
						((ImageView) findViewById(R.id.imageViewConfirmPw)).setVisibility(View.GONE);
					}else if (view.getId() == R.id.editTextScreenName) {
						((ImageView) findViewById(R.id.imageViewScreenName)).setVisibility(View.GONE);
					}
					return false;
				}
			}
			
			if (view.getId() == R.id.editTextMobilePhone) {
				
				new ValidateMobileNumberTask().execute(view.getText().toString());
			}
			else if (view.getId() == R.id.editTextEmail) {
				// Check with backend if this email has already been registered
				// by someone else.
				new ValidateEmailTask().execute(view.getText().toString());
			} else if (view.getId() == R.id.editTextConfirmEmail) {
				String email = editTextEmail.getText().toString();
				String confirm = editTextConfirmEmail.getText().toString();
				if (!email.equals(confirm)) {
					editTextConfirmEmail.setError(getString(R.string.error_emails_not_equal));
					((ImageView) findViewById(R.id.imageViewConfirmEmail)).setVisibility(View.GONE);
					return false;
				} else {
					((ImageView) findViewById(R.id.imageViewConfirmEmail)).setVisibility(View.VISIBLE);
				}
			} else if (view.getId() == R.id.editTextConfirmPw) {
				String confirm = view.getText().toString();
				String pw = ((EditText) findViewById(R.id.editTextPassword)).getText().toString();
				if (!pw.equals(confirm)) {
					view.setError(getString(R.string.error_pw_not_equal));
					((ImageView) findViewById(R.id.imageViewConfirmPw)).setVisibility(View.GONE);
					return false;
				} else {
					((ImageView) findViewById(R.id.imageViewConfirmPw)).setVisibility(View.VISIBLE);
				}
			} else if (view.getId() == R.id.editTextPassword) {
				((ImageView) findViewById(R.id.imageViewPassword)).setVisibility(View.VISIBLE);
			} else if (view.getId() == R.id.editTextScreenName) {
				((ImageView) findViewById(R.id.imageViewScreenName)).setVisibility(View.VISIBLE);
			}
			
			else if (view.getId() == R.id.editTextMobilePhone) {
				((ImageView) findViewById(R.id.imageViewMobilePhone)).setVisibility(View.VISIBLE);
			} else if (view.getId() == R.id.editTextFirstName) {
				((ImageView) findViewById(R.id.imageViewFirstName)).setVisibility(View.VISIBLE);
			} else if (view.getId() == R.id.editTextLastName) {
				((ImageView) findViewById(R.id.imageViewLastName)).setVisibility(View.VISIBLE);
			}
		}

		return true;
	}

	private boolean validateMobileNumber(String number) {
		try {

			String endpoint = Utilities.reformEndpoint(getApplicationContext(), Constants.TAG_VALIDATE_MOBILE_NUMBER_METHOD);
            RestClient emailClient = new RestClient(endpoint);

			Map<String, String> data = new HashMap<String, String>();
			data.put(Utilities.REG_MOBILE_PHONE, number);
			JSONObject name = new JSONObject(data);
			emailClient.AddParam("data", name.toString());
			emailClient.Execute(RequestType.POST);

			Log.i(TAG, "" + emailClient.GetResponse());
			JSONArray response = new JSONArray(emailClient.GetResponse());
			String responseCode = response.getJSONObject(0).getString("status");
			Log.i(TAG, "" + responseCode);
			JSONArray arr = response.getJSONObject(0).getJSONArray("data");
			Log.i(TAG, "nExists=" + arr.getJSONObject(0).getString("nExists"));
			int count = Integer.parseInt(arr.getJSONObject(0).getString("nExists"));
			if (count > 0) {
				
				return false;
			}
			
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}
		return true;
	}
	
	private boolean validateEmail(String email) {
		// Validate email.
		try {
			
			// Validate screen name.
            String endpoint = Utilities.reformEndpoint(getApplicationContext(), Constants.TAG_CHECK_EMAIL_METHOD);
            RestClient emailClient = new RestClient(endpoint);
			
			// Parameter: 'data={"email":"kitti@test.com"}'
			Map<String, String> data = new HashMap<String, String>();
			data.put(Utilities.REG_EMAIL, email);
			JSONObject name = new JSONObject(data);
			emailClient.AddParam("data", name.toString());
			emailClient.Execute(RequestType.POST);

			Log.i(TAG, "" + emailClient.GetResponse());
			JSONArray response = new JSONArray(emailClient.GetResponse());
			String responseCode = response.getJSONObject(0).getString("status");
			Log.i(TAG, "" + responseCode);
			JSONArray arr = response.getJSONObject(0).getJSONArray("data");
			Log.i(TAG, "nExists=" + arr.getJSONObject(0).getString("nExists"));
			int count = Integer.parseInt(arr.getJSONObject(0).getString("nExists"));
			if (count > 0) {
				
				return false;
			}
			
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}
		return true;
	}
	
	private class ValidateEmailTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(RegistrationActivity.this);
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage(getString(R.string.lbl_msg_validate_email));
			dialog.show();
		}

		@Override
		protected void onPostExecute(Boolean valid) {
			super.onPostExecute(valid);

			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			
			ImageView green = (ImageView)findViewById(R.id.imageViewEmail);
			if (valid) {
				green.setVisibility(View.VISIBLE);
				((EditText) findViewById(R.id.editTextEmail)).setError(null);
			} else {
				green.setVisibility(View.GONE);
				((EditText) findViewById(R.id.editTextEmail)).setError(getString(R.string.error_email_already_registered));
			}
		}

		@Override
		protected Boolean doInBackground(String... params) {
			boolean isValid = validateEmail(params[0]);
			return Boolean.valueOf(isValid);
		}
	}

	private class ValidateMobileNumberTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(RegistrationActivity.this);
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage(getString(R.string.lbl_msg_mobile_number));
			dialog.show();
		}

		@Override
		protected void onPostExecute(Boolean valid) {
			super.onPostExecute(valid);

			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			
			ImageView green = (ImageView)findViewById(R.id.imageViewMobilePhone);
			if (valid) {
				green.setVisibility(View.VISIBLE);
				((EditText) findViewById(R.id.editTextMobilePhone)).setError(null);
			} else {
				green.setVisibility(View.GONE);
				((EditText) findViewById(R.id.editTextMobilePhone)).setError(getString(R.string.error_mobile_number_already_registered));
			}
		}

		@Override
		protected Boolean doInBackground(String... params) {
			boolean isValid = validateMobileNumber(params[0]);
			return Boolean.valueOf(isValid);
		}
	}
	
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus) {
			onValidate((EditText) v);
		}
	}

	// AsyncTask<Params, Progress, Result>
	private class RegisterTask extends AsyncTask<HashMap<String, String>, String, List<Object>> {

		private GoogleCloudMessaging gcm;
		private ProgressDialog dialog;
		private HashMap<String, String> regInfoMap;

		@Override
		protected void onPostExecute(List<Object> result) {
			super.onPostExecute(result);
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}

			boolean success = Boolean.valueOf(result.get(0).toString());
			if (success) {

				storeRegInfoInDb();

				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				prefs.edit().putBoolean(Constants.TAG_REGISTERED_SUCCESS, true).commit();
				prefs.edit().putBoolean(Constants.TAG_NONE_REGISTERED, false).commit();
				prefs.edit().putString(Utilities.EMAIL, regInfoMap.get(Utilities.EMAIL)).commit();

				Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(loginActivity);
				finish();
			} else {
				String msg = result.get(1).toString();
				String field = result.get(2).toString();
				if (Field.valueOf(field) == Field.EMAIL) {
					editTextEmail.setError(msg);
				} else if (Field.valueOf(field) == Field.SCREEN_NAME) {
					editTextScreenName.setError(msg);
				}

				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
			}

			registerTask = null;
		}

		private void storeRegInfoInDb() {
			Utilities.insertNewRegInfo(getApplicationContext(), regInfoMap);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new ProgressDialog(RegistrationActivity.this);
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage(getString(R.string.lbl_msg_registering));
			dialog.show();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			String msg = values[0];
			if (msg.equals("0")) {
				dialog.setMessage(getString(R.string.lbl_msg_registering_to_gcm));
			}
		}

		@Override
		protected List<Object> doInBackground(HashMap<String, String>... params) {
			regInfoMap = params[0];
			List<Object> result = new ArrayList<Object>();
//			try {
//				// Validate screen name.
//				RestClient screenNameClient = new RestClient(Constants.TAG_CHECK_SCREEN_NAME_URL);
//				Map<String, String> data = new HashMap<String, String>();
//				data.put(Utilities.REG_SCREEN_NAME, regInfoMap.get(Utilities.REG_SCREEN_NAME).toString());
//				JSONObject name = new JSONObject(data);
//				screenNameClient.AddParam("data", name.toString());
//				screenNameClient.Execute(RequestType.POST);
//
//				Log.i(TAG, "" + screenNameClient.GetResponse());
//
//				JSONArray response = new JSONArray(screenNameClient.GetResponse());
//				String responseCode = response.getJSONObject(0).getString("status");
//				Log.i(TAG, "responseCode=" + responseCode);
//
//				if (responseCode.equals(Constants.TAG_RESPONSE_CODE_SUCCESS)) {
//					JSONArray arr = response.getJSONObject(0).getJSONArray("data");
//					Log.i(TAG, "nExists=" + arr.getJSONObject(0).getString("nExists"));
//					int count = Integer.parseInt(arr.getJSONObject(0).getString("nExists"));
//					if (count > 0) {
//						result.add(Boolean.FALSE);
//						result.add("The screen name is not available, Please try a new one");
//						result.add(Field.SCREEN_NAME.name());
//						return result;
//					}
//				}
//			} catch (Exception e) {
//				Log.e(TAG, "" + e);
//			}

			// Validate email.
			try {
				// Validate screen name.
				
				String endpoint = Utilities.reformEndpoint(getApplicationContext(), Constants.TAG_CHECK_EMAIL_METHOD);
				RestClient emailClient = new RestClient(endpoint);
				
				Map<String, String> data = new HashMap<String, String>();
				data.put(Utilities.REG_EMAIL, regInfoMap.get(Utilities.REG_EMAIL).toString());
				JSONObject name = new JSONObject(data);
				emailClient.AddParam("data", name.toString());
				emailClient.Execute(RequestType.POST);

				Log.i(TAG, "" + emailClient.GetResponse());
				JSONArray response = new JSONArray(emailClient.GetResponse());
				String responseCode = response.getJSONObject(0).getString("status");
				Log.i(TAG, "" + responseCode);
				if (responseCode.equals(Constants.TAG_RESPONSE_CODE_SUCCESS)) {
					JSONArray arr = response.getJSONObject(0).getJSONArray("data");
					Log.i(TAG, "nExists=" + arr.getJSONObject(0).getString("nExists"));
					int count = Integer.parseInt(arr.getJSONObject(0).getString("nExists"));
					if (count > 0) {
						result.add(Boolean.FALSE);
						result.add("The email is not available, Please try a new one");
						result.add(Field.EMAIL.name());
						return result;
					}
				}
			} catch (Exception e) {
				Log.e(TAG, "" + e);
			}

			
			String endpoint = Utilities.reformEndpoint(getApplicationContext(), Constants.TAG_REGISTER_METHOD);
			RestClient client = new RestClient(endpoint);
			
			int max = 5;
			int count = 0;
			publishProgress("0"); // 0 = registering to google cloud message.
			while (count++ < max) {

				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
					}

					if (TextUtils.isEmpty(regid)) {
						regid = gcm.register(SENDER_ID);
						storeGcmDevId(regid);
						break;
					}

				} catch (IOException e) {
					Log.e(TAG, "" + e);
				}

				try {
					SystemClock.sleep(100);
				} catch (Exception e) {
					Log.e(TAG, "" + e);
				}
			}

			if (TextUtils.isEmpty(regid)) {
				result.add(Boolean.FALSE);
				result.add("Failed registering to Google Cloud Messaging server");
				result.add(Field.NONE.name());
				return result;
			} else {
				String msg = "Dvice registered, registration ID=" + regid;
				Log.i(TAG, msg);

				regInfoMap.put(Utilities.REG_DEVICE_ID, regid);
				JSONObject newReg = new JSONObject(regInfoMap);
				JSONArray arr = new JSONArray();

				try {
					arr.put(0, newReg);
					client.AddParam("data", arr.getString(0));
					client.Execute(RequestType.POST);

					Log.i(TAG, "" + client.GetResponse());
				} catch (Exception e) {

					Log.e(TAG, "" + e);
					result.add(Boolean.FALSE);
					result.add(Field.NONE.name());
					result.add("" + e);

					return result;
				}

			}

			result.add(Boolean.TRUE);
			result.add("SUCCESS");

			return result;
		}
	}

	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */

			// Showing received message

			Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

			// Releasing wake lock
			// WakeLocker.release();
		}
	};
}
