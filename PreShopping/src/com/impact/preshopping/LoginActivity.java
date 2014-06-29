package com.impact.preshopping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.dudev.util.Constants;
import com.dudev.util.DownloadImageThread;
import com.dudev.util.DownloadImageThread.ImageType;
import com.dudev.util.RequestType;
import com.dudev.util.RestClient;
import com.dudev.util.SyncDataThread;
import com.dudev.util.Utilities;
import com.impact.preshopping.activity.CategoryActivity;
import com.impact.preshopping.activity.CompanyActivity;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] { "foo@example.com:hello", "bar@example.com:world" };

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";
	public static final String EXTRA_PASSWORD = "com.example.android.authenticatordemo.extra.PASSWORD";
	public static final String EXTRA_FORCE_LOGIN = "EXTRA_FORCE_LOGIN";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private static final String TAG = LoginActivity.class.getSimpleName();
	private boolean skipCompanyPage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		getActionBar().setTitle("PreShopping");
//		getActionBar().setBackgroundDrawable(getResources().getDrawable(android.R.color.black));
		
		skipCompanyPage = Boolean.valueOf(getString(R.string.app_no_company_preset));
		boolean forceLogin = false;
		try {
		    forceLogin = getIntent().getBooleanExtra(EXTRA_FORCE_LOGIN, false);
		} catch (Exception e) {
		    
		}
		
		mEmailView = (EditText) findViewById(R.id.email);
		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});
		
		if (forceLogin) {
		    
	        mEmail = getIntent().getExtras().getString(Utilities.REG_EMAIL);
	        mPassword = getIntent().getExtras().getString(Utilities.REG_PASSWORD);

		    login();
		}
	}

	

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		
	    // check if admin logs in.
		if (!TextUtils.isEmpty(mEmail) && !TextUtils.isEmpty(mPassword)) {
		    if (mEmail.equals(getString(R.string.addmin_acc_name)) && mPassword.equals(getString(R.string.addmin_password))) {
		        
		        ((PreShoppingApp)getApplication()).setAdminMode(true);
		        
		        Intent pref = new Intent(getApplicationContext(), FavoriteSettingActivity.class);
		        startActivity(pref);
		        finish();
		        return;
		    }
		}
		
		((PreShoppingApp)getApplication()).setAdminMode(false);
		
		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		} else {
			boolean isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches();
			if (!isValid) {
				mEmailView.setError(getString(R.string.error_invalid_email));
				focusView = mEmailView;
				cancel = true;
			}
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			login();
		}
	}

	private void login() {
	    // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
        showProgress(true);
        mAuthTask = new UserLoginTask();
        
        mAuthTask.execute(mEmail, mPassword);
    }



    /**
	 * Shows the progress UI and hides the login form.
	 */
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
				}
			});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
				}
			});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<String, Void, Integer> {
		private ExecutorService e;
		private Future<Boolean> f;
		private int counter = 0;
		
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			if (counter % 2 == 0) {
				mLoginStatusMessageView.setText(getString(R.string.lbl_downloading_icons));	
			} else {
				mLoginStatusMessageView.setText(getString(R.string.lbl_updating_db));
			}
			
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Integer doInBackground(String... params) {
			
			Boolean success;
			int status = 0;
			try {
				String endpoint = Utilities.reformEndpoint(getApplicationContext(), Constants.TAG_LOGIN_METHOD);
				RestClient loginClient = new RestClient(endpoint);
				
				Map<String, String> data = new HashMap<String, String>();
				data.put(Utilities.REG_EMAIL, params[0]);
				data.put(Utilities.REG_PASSWORD, params[1]);
				data.put(Utilities.IMEI, Utilities.getImei(getApplicationContext()));
				
				JSONObject name = new JSONObject(data);
				loginClient.AddParam("data", name.toString());
				loginClient.Execute(RequestType.POST);
				JSONArray arr = new JSONArray(loginClient.GetResponse());
				JSONObject response = arr.getJSONObject(0);
				
				try {
					status = Integer.parseInt(response.getString("status"));
					
					if (status < 0) {
						if (status == Constants.SWAP_DEVICE_CODE) {
							storeRegDataInDb(true, loginClient.GetResponse());
						}
						return status;
					}
				} catch (NumberFormatException f) {
					Log.e(TAG, "" + e);
				}

				Log.i(TAG, "" + loginClient.GetResponse());
				storeRegDataInDb(false, loginClient.GetResponse());
				storeSettingsInDb(loginClient.GetResponse());
				
				// Sync data....
				SyncDataThread sync = new SyncDataThread(getApplicationContext(), null, Utilities.reformEndpoint(getApplicationContext(), Constants.TAG_GET_DATA), getString(R.string.sync_data_method), null);
				
				e = Executors.newFixedThreadPool(1);
				f = (Future<Boolean>)e.submit(sync);
				e.shutdown();
				success = f.get();
				
				publishProgress();
				
				HashMap<String, String> company = Utilities.getCompanyIconUrl(getApplicationContext());
				HashMap<String, String> category = Utilities.getCategoryIconUrl(getApplicationContext());
				HashMap<String, String> group = Utilities.getGroupIconUrl(getApplicationContext());
				HashMap<String, String> product = Utilities.getProductIconUrl(getApplicationContext());
				
				List<Future<HashMap<String, Object>>> listOfThreads = new ArrayList<Future<HashMap<String, Object>>>();
				e = Executors.newFixedThreadPool(10);
				
				for (String key : company.keySet()) {
					String url = company.get(key);
					DownloadImageThread t = new DownloadImageThread(getApplicationContext(), Integer.valueOf(key), url, ImageType.COMPANY);
					Future<HashMap<String, Object>> d = (Future<HashMap<String, Object>>)e.submit(t);
					listOfThreads.add(d);
				}
				
				for (String key : category.keySet()) {
					String url = category.get(key);
					DownloadImageThread t = new DownloadImageThread(getApplicationContext(), Integer.valueOf(key), url, ImageType.CATEGORY);
					Future<HashMap<String, Object>> d = (Future<HashMap<String, Object>>)e.submit(t);
					listOfThreads.add(d);
				}
				
				for (String key : group.keySet()) {
					String url = group.get(key);
					DownloadImageThread t = new DownloadImageThread(getApplicationContext(), Integer.valueOf(key), url, ImageType.GROUP);
					Future<HashMap<String, Object>> d = (Future<HashMap<String, Object>>)e.submit(t);
					listOfThreads.add(d);
				}
				
				for (String key : product.keySet()) {
					String url = product.get(key);
					DownloadImageThread t = new DownloadImageThread(getApplicationContext(), Integer.valueOf(key), url, ImageType.PRODUCT);
					Future<HashMap<String, Object>> d = (Future<HashMap<String, Object>>)e.submit(t);
					listOfThreads.add(d);
				}
				e.shutdown();
//				while (!e.isTerminated()) {
//					Thread.sleep(500);
//				}
				
				publishProgress();
				
				for (int i = 0; i < listOfThreads.size(); i++) {
					Log.i(TAG, "" + listOfThreads.get(i).get().get("BELONG_TO") + ", " + listOfThreads.get(i).get().get("ID") + ", " + listOfThreads.get(i).get().get("URI"));
					String belongTo = listOfThreads.get(i).get().get("BELONG_TO").toString();
					String id = listOfThreads.get(i).get().get("ID").toString();
					String filePath = listOfThreads.get(i).get().get("URI").toString();
					long total = Utilities.updateIconOrImageFilePath(getApplicationContext(), belongTo, id, filePath);
					Log.e(TAG, "Updated: " + total);
				}
				
				
			} catch (InterruptedException e) {
				Log.e(TAG, "" + e);
				success = false;
			} catch (ExecutionException e) {
				Log.e(TAG, "" + e);
				success = false;
			} catch (Exception e) {
				Log.e(TAG, "" + e);
				success = false;
			}

			return success ? 0 : status;
		}

		private void storeSettingsInDb(String getResponse) {

			HashMap<String, String> setting = new HashMap<String, String>();
			try {
				JSONArray arr = new JSONArray(getResponse);
				JSONObject obj = arr.getJSONObject(0).getJSONArray("data").getJSONObject(0).getJSONArray("setting").getJSONObject(0);
				setting.put(Utilities.SETTING_ALERT_TYPE, obj.getString(Utilities.SETTING_ALERT_TYPE));
				setting.put(Utilities.SETTING_COMPANY, obj.getString(Utilities.SETTING_COMPANY));
				setting.put(Utilities.SETTING_DOWNLOAD_PERMISION, obj.getString(Utilities.SETTING_DOWNLOAD_PERMISION));
				setting.put(Utilities.SETTING_HOME_SCREEN_TYPE, obj.getString(Utilities.SETTING_HOME_SCREEN_TYPE));
				setting.put(Utilities.SETTING_NOTIFICATION_ONOFF, obj.getString(Utilities.SETTING_NOTIFICATION_ONOFF));
				setting.put(Utilities.SETTING_PROD, obj.getString(Utilities.SETTING_PROD));
				setting.put(Utilities.SETTING_PROD_CATEGORY, obj.getString(Utilities.SETTING_PROD_CATEGORY));
				setting.put(Utilities.SETTING_PROD_GROUP, obj.getString(Utilities.SETTING_PROD_GROUP));
				setting.put(Utilities.SETTING_SOUND_FILE, obj.getString(Utilities.SETTING_SOUND_FILE));
				
				Utilities.insertNewSettings(getApplicationContext(), setting);
				
			} catch (JSONException e) {
				Log.e(TAG, "" + e);
			}
		}

		private void storeRegDataInDb(boolean isOldRegData, String getResponse) {

			HashMap<String, String> data = new HashMap<String, String>();
			try {
				JSONArray arr = new JSONArray(getResponse);
				JSONObject obj = null;
				//oldregistration
				if (isOldRegData) {
					obj = arr.getJSONObject(0).getJSONArray("data").getJSONObject(0).getJSONArray("oldregistration").getJSONObject(0);
				} else {
					obj = arr.getJSONObject(0).getJSONArray("data").getJSONObject(0);
				}
				
				data.put(Utilities._ID, obj.getString("cusID"));
				data.put(Utilities.REG_EMAIL, obj.getString(Utilities.REG_EMAIL));
				data.put(Utilities.REG_SCREEN_NAME, obj.getString(Utilities.REG_SCREEN_NAME));
				data.put(Utilities.REG_PASSWORD, obj.getString(Utilities.REG_PASSWORD));
				data.put(Utilities.REG_FIRST_NAME, obj.getString(Utilities.REG_FIRST_NAME));
				data.put(Utilities.REG_LAST_NAME, obj.getString(Utilities.REG_LAST_NAME));
				data.put(Utilities.REG_IS_ACTIVE, obj.getString(Utilities.REG_IS_ACTIVE));
				data.put(Utilities.REG_OFFICE_PHONE, obj.getString(Utilities.REG_OFFICE_PHONE));
				data.put(Utilities.REG_MOBILE_PHONE, obj.getString(Utilities.REG_MOBILE_PHONE));
				data.put(Utilities.REG_FACEBOOK, obj.getString(Utilities.REG_FACEBOOK));
				data.put(Utilities.REG_INSTRAGRAM, obj.getString(Utilities.REG_INSTRAGRAM));
				data.put(Utilities.REG_TWEETER, obj.getString(Utilities.REG_TWEETER));
				data.put(Utilities.REG_SKYPE, obj.getString(Utilities.REG_SKYPE));
				data.put(Utilities.REG_LINE, obj.getString(Utilities.REG_LINE));
				data.put(Utilities.REG_PREGERRED_LANG, obj.getString(Utilities.REG_PREGERRED_LANG));
				data.put(Utilities.REG_IMEI, obj.getString(Utilities.REG_IMEI));
				data.put(Utilities.REG_DEVICE_ID, obj.getString(Utilities.REG_DEVICE_ID));
				data.put(Utilities.REG_DEVICE_TYPE, obj.getString(Utilities.REG_DEVICE_TYPE));
				data.put(Utilities.REG_USER_AVATAR, obj.getString(Utilities.REG_USER_AVATAR).trim());
				
//				try {
//                    byte[] img = Base64.decode(obj.getString(Utilities.REG_USER_AVATAR).trim(), 0);
//                    FileUtils.writeByteArrayToFile(new File("/storage/sdcard0/avatar.jpg"), img);
//
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
				
				Utilities.insertNewRegInfo(getApplicationContext(), data);
				
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				prefs.edit().putString(Utilities.REG_DEVICE_ID, obj.getString(Utilities.REG_DEVICE_ID)).commit();
				
			} catch (JSONException e) {
				Log.e(TAG, "" + e);
			}
		}

		@Override
		protected void onPostExecute(Integer status) {
			mAuthTask = null;
			showProgress(false);
			
			if (status == 0) {
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				prefs.edit().putBoolean(Constants.TAG_HAS_LOGGED_IN, true).commit();
				prefs.edit().putBoolean(Constants.TAG_NONE_REGISTERED, false).commit();
				prefs.edit().putBoolean(Constants.TAG_REGISTERED_SUCCESS, true).commit();
				prefs.edit().putString(Utilities.EMAIL, mEmail).commit();
				
				Intent i = new Intent(getApplicationContext(), skipCompanyPage ? CategoryActivity.class : CompanyActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
			} else {
				mEmailView.setError(getString(R.string.error_incorrect_email_or_password));
				mEmailView.requestFocus();
//				mPasswordView.setError(getString(R.string.error_incorrect_email_or_password));
//				mPasswordView.requestFocus();
				
				if (status == Constants.SWAP_DEVICE_CODE) {
					Intent i = new Intent(getApplicationContext(), SwapDeviceActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					finish();
				}
			}
		}

		@Override
		protected void onCancelled() {
			f.cancel(true);
			mAuthTask = null;
			showProgress(false);
		}
	}
}
