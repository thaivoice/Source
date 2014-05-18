package com.impact.preshopping.activity;

import static com.dudev.util.gcm.CommonUtilities.SENDER_ID;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.appositedesigns.fileexplorer.FileExplorerApp;
import net.appositedesigns.fileexplorer.activity.FileListActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.dudev.util.Constants;
import com.dudev.util.RequestType;
import com.dudev.util.RestClient;
import com.dudev.util.Utilities;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.impact.preshopping.LoginActivity;
import com.impact.preshopping.PreLoginActivity;
import com.impact.preshopping.R;
import com.impact.preshopping.adapter.SquareImageView;

public class RegistrationActivity2 extends Activity {

	public enum PreferredLanguage {
		EN, TH, NONE
	}

	private EditText editTextEmail;
	private EditText editTextFacebook;
	private EditText editTextFirstName;
	private EditText editTextInstragram;
	private EditText editTextLastName;
	private EditText editTextLine;
	private EditText editTextMobilePhone;
	private EditText editTextOfficePhone;
	private EditText editTextPassword;
	private EditText editTextPrefer;
	private EditText editTextScreenName;
	private EditText editTextSkype;
	private EditText editTextTweeter;
	private RadioButton radioBtnEn;
	private RadioButton radioBtnThai;

	private HashMap<String, Object> regInfo;
	private SquareImageView imgView;
	private Button btnSave;
	private boolean swappingDevice;
	public static final String TAG = RegistrationActivity2.class.getSimpleName();
	private String existingMobileNumber;

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
		setContentView(R.layout.activity_registration_activity2);
		
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(true);
//		getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_action_back));
		getActionBar().setTitle("Back");
//		getActionBar().setBackgroundDrawable(getResources().getDrawable(android.R.color.black));
		setTitle(Html.fromHtml("<font color='#ffffff'><b>Manage Registration Data</b></font>"));
		
		try {
			swappingDevice = getIntent().getExtras().getBoolean("REQUEST_SWAP_DEVICE");
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}

		List<View> list = getAllChildren(findViewById(R.id.main));
		for (View v : list) {
			if (v instanceof EditText) {
				((EditText) v).setHint("");
			}
		}
		editTextEmail = (EditText) findViewById(R.id.editTextEmail);
		editTextEmail.setEnabled(false);
		editTextFacebook = (EditText) findViewById(R.id.editTextFacebook);
		editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
		editTextInstragram = (EditText) findViewById(R.id.editTextInstragram);
		editTextLastName = (EditText) findViewById(R.id.editTextLastName);
		editTextLine = (EditText) findViewById(R.id.editTextLine);
		editTextMobilePhone = (EditText) findViewById(R.id.editTextMobilePhone);
		editTextMobilePhone.setEnabled(false);
		editTextOfficePhone = (EditText) findViewById(R.id.editTextOfficePhone);
		editTextScreenName = (EditText) findViewById(R.id.editTextScreenName);
		editTextSkype = (EditText) findViewById(R.id.editTextSkype);
		editTextTweeter = (EditText) findViewById(R.id.editTextTweeter);
		radioBtnEn = (RadioButton) findViewById(R.id.radioBtnEn);
		radioBtnThai = (RadioButton) findViewById(R.id.radioBtnThai);

		regInfo = Utilities.getRegInfo(getApplicationContext());
		Object avatar = null;
		if (regInfo != null && regInfo.size() > 0) {
			String email = regInfo.get(Utilities.REG_EMAIL) == null ? "" : regInfo.get(Utilities.REG_EMAIL).toString();
			editTextEmail.setText(email);
			String facebook = regInfo.get(Utilities.REG_FACEBOOK) == null ? "" : regInfo.get(Utilities.REG_FACEBOOK).toString();
			editTextFacebook.setText(facebook);
			String lastName = regInfo.get(Utilities.REG_LAST_NAME) == null ? "" : regInfo.get(Utilities.REG_LAST_NAME).toString();
			editTextLastName.setText(lastName);
			String firstName = regInfo.get(Utilities.REG_FIRST_NAME) == null ? "" : regInfo.get(Utilities.REG_FIRST_NAME).toString();
			editTextFirstName.setText(firstName);
			String line = regInfo.get(Utilities.REG_LINE) == null ? "" : regInfo.get(Utilities.REG_LINE).toString();
			editTextLine.setText(line);
			
			String mobile = regInfo.get(Utilities.REG_MOBILE_PHONE) == null ? "" : regInfo.get(Utilities.REG_MOBILE_PHONE).toString();
			editTextMobilePhone.setText(mobile);
			editTextMobilePhone.setEnabled(swappingDevice ? true : false);
			existingMobileNumber = mobile;
			
			String officePhone = regInfo.get(Utilities.REG_OFFICE_PHONE) == null ? "" : regInfo.get(Utilities.REG_OFFICE_PHONE).toString();
			editTextOfficePhone.setText(officePhone);
			String screenName = regInfo.get(Utilities.REG_SCREEN_NAME) == null ? "" : regInfo.get(Utilities.REG_SCREEN_NAME).toString();
			editTextScreenName.setText(screenName);
			
			try {
				PreferredLanguage prefLang = regInfo.get(Utilities.REG_PREGERRED_LANG) == null ? PreferredLanguage.TH : PreferredLanguage.valueOf(regInfo.get(
						Utilities.REG_PREGERRED_LANG).toString());

				radioBtnThai.setChecked(prefLang == PreferredLanguage.TH ? true : false);
				radioBtnEn.setChecked(prefLang == PreferredLanguage.EN ? true : false);
				radioBtnThai.setVisibility(View.GONE);
				radioBtnEn.setVisibility(View.GONE);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String skype = regInfo.get(Utilities.REG_SKYPE) == null ? "" : regInfo.get(Utilities.REG_SKYPE).toString();
			editTextSkype.setText(skype);
			String tweeter = regInfo.get(Utilities.REG_TWEETER) == null ? "" : regInfo.get(Utilities.REG_TWEETER).toString();
			editTextTweeter.setText(tweeter);
			String instragram = regInfo.get(Utilities.REG_INSTRAGRAM) == null ? "" : regInfo.get(Utilities.REG_INSTRAGRAM).toString();
			editTextInstragram.setText(instragram);
			avatar = regInfo.get(Utilities.REG_USER_AVATAR);
		}

		try {
			imgView = (SquareImageView) findViewById(R.id.imageViewAvatar);
			Bitmap b = (Bitmap) avatar;
			if (b != null) {
				imgView.setImageBitmap(b);
			}

		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}

		imgView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), FileListActivity.class);
				intent.putExtra(FileExplorerApp.EXTRA_IS_PICKER, true);
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, 1);
			}
		});

		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (editTextMobilePhone.getError() != null) {
					Toast.makeText(getApplicationContext(), getString(R.string.lbl_err_plz_enter_valid_data), Toast.LENGTH_LONG).show();
					return;
				}
				
				new UpdateAvatarTask().execute(new HashMap<String, String>() );
			}
		});
		
		
		if (swappingDevice) {
			editTextMobilePhone.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					editTextMobilePhone.setError(null);
					if (!hasFocus) {
						if (TextUtils.isEmpty(editTextMobilePhone.getText().toString())) {
							editTextMobilePhone.setError(getString(R.string.error_field_required));
						} else {
							if (!editTextMobilePhone.getText().toString().equals(existingMobileNumber)) {
								// Validate number...
								new ValidateMobileNumberTask().execute(editTextMobilePhone.getText().toString());								
							}
						}
					}
				}
			});
		}
	}

	
	private class ValidateMobileNumberTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(RegistrationActivity2.this);
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
	
	private Uri avatarUri;
	private GoogleCloudMessaging gcm;
	
	// AsyncTask<Params, Progress, Result>
	private class UpdateAvatarTask extends AsyncTask<HashMap<String, String>, Void, Boolean> {

		private ProgressDialog dialog;
		private HashMap<String, String> data = new HashMap<String, String>();

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}

			if (swappingDevice) {
				Intent i = new Intent(getApplicationContext(), LoginActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.putExtra(LoginActivity.EXTRA_FORCE_LOGIN, true);
				startActivity(i);
			}
			
			finish();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new ProgressDialog(RegistrationActivity2.this);
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage(getString(R.string.lbl_msg_update_register_data));
			dialog.show();
			
			HashMap<String, Object> info = new HashMap<String, Object>();
            if (avatarUri != null) {
                info.put("AVATAR_URI", avatarUri);
            }

            info.put(Utilities.IMEI, Utilities.getImei(getApplicationContext()));
            info.put(Utilities.EMAIL, editTextEmail.getText().toString());
            info.put(Utilities.FACEBOOK, editTextFacebook.getText().toString());
            info.put(Utilities.FIRST_NAME, editTextFirstName.getText().toString());
            info.put(Utilities.INSTRAGRAM, editTextInstragram.getText().toString());
            info.put(Utilities.LAST_NAME, editTextLastName.getText().toString());
            info.put(Utilities.LINE, editTextLine.getText().toString());
            info.put(Utilities.MOBILE_PHONE, editTextMobilePhone.getText().toString());
            info.put(Utilities.OFFICE_PHONE, editTextOfficePhone.getText().toString());
            info.put(Utilities.SCREEN_NAME, editTextScreenName.getText().toString());
            info.put(Utilities.SKYPE, editTextSkype.getText().toString());
            info.put(Utilities.TWEETER, editTextTweeter.getText().toString());
            info.put(Utilities.PREFERRED_LANG, radioBtnEn.isChecked() ? PreferredLanguage.EN.name() : PreferredLanguage.TH.name());
            info.put(Utilities.REG_DEVICE_ID, "DUMMY");
            info.put(Utilities.REG_DEVICE_TYPE, Constants.TAG_ANDROID_DEV_TYPE);

            Utilities.updateRegInfo(getApplicationContext(), info);

            // Remove none updatable fields.
            if (info.containsKey("AVATAR_URI")) {
                info.remove("AVATAR_URI");
            }

            for (String field : info.keySet()) {
                data.put(field, info.get(field) == null ? "" : info.get(field).toString());
            }

		}

		@Override
		protected Boolean doInBackground(HashMap<String, String>... params) {
			
			String regid = "";
			
			int max = 5;
            int count = 0;
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
            
            data.put(Utilities.REG_DEVICE_ID, TextUtils.isEmpty(regid) ? "" : regid);

            String endpoint = Utilities.reformEndpoint(getApplicationContext(), Constants.TAG_UPDATE_REG_DATA_METHOD);
			RestClient settingClient = new RestClient(endpoint);
			
			try {
//				Uri uri = Uri.parse(path);
//				String fileName = uri.getPath();
				String str = "";
				HashMap<String, byte[]> avatar = Utilities.getAvatar(getApplicationContext());
				if (avatar.get(Utilities.REG_USER_AVATAR) == null) {
					// use default icon instead.
					Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.contacts_96px);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					byte[] bitMapData = stream.toByteArray();
					str = Base64.encodeToString(bitMapData, 0);
				} else {
					str = Base64.encodeToString(avatar.get(Utilities.REG_USER_AVATAR), 0);
				}
//				FileUtils.writeStringToFile(new File(Utilities.getAppFolder_ExtSd(getApplicationContext()) + "/helloworld.jpg"), str, false);
//				FileUtils.writeByteArrayToFile(new File(Utilities.getAppFolder_ExtSd(getApplicationContext()) + "/beforeSend.jpg"), Base64.decode(getString(R.string.temp), 0));
				
				data.put("avatar", str);
//				data.put("avatar", getString(R.string.temp));

				JSONObject obj = new JSONObject(data);

				settingClient.AddParam("data", obj.toString());
//				settingClient.AddParam("getfile", getString(R.string.temp));
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
				return Boolean.FALSE;
			}

			return Boolean.TRUE;
		}
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// if (resultCode != Activity.RESULT_CANCELED) {
	// if (requestCode == 99) {
	// Uri uri = (Uri) data.getExtras().get("SELECTED_FILE_PATH");
	// avatarUri = uri;
	// Log.i(TAG, "SELECTED_FILE_PATH=" + uri.toString());
	// float width = Utilities.convertDpToPixel(150, getApplicationContext());
	//
	// try {
	// Options options = Utilities.getOptions(uri.getPath(), (int)width,
	// (int)width);
	// Bitmap b = BitmapFactory.decodeFile(uri.getPath(), options);
	//
	// if (b != null) {
	// imgView.setImageBitmap(b);
	// } else {
	// Toast.makeText(getApplicationContext(),
	// getString(R.string.lbl_err_invalid_photo), Toast.LENGTH_LONG).show();
	// }
	// } catch (Exception e) {
	// Log.e(TAG, "" + e);
	// }
	// }
	// }
	// }
	private int getAppVersion() {
        try {
            PackageInfo packageInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
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
	
	
	protected static final int REQUEST_CODE_PICK_FILE_OR_DIRECTORY = 1;
	protected static final int REQUEST_CODE_GET_CONTENT = 2;
	private static final String MY_EXTRA = "org.openintents.filemanager.demo.EXTRA_MY_EXTRA";



	/**
	 * This is called after the file manager finished.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQUEST_CODE_PICK_FILE_OR_DIRECTORY:
			if (resultCode == RESULT_OK && data != null) {
				// obtain the filename
				Uri fileUri = data.getData();
				if (fileUri != null) {
					String filePath = fileUri.getPath();
					if (filePath != null) {
						// mEditText.setText(filePath);
						Uri uri = Uri.fromFile(new File(filePath));
						avatarUri = uri;
						Log.i(TAG, "SELECTED_FILE_PATH=" + uri.toString());
						float width = Utilities.convertDpToPixel(150, getApplicationContext());

						try {
							Options options = Utilities.getOptions(uri.getPath(), (int) width, (int) width);
							Bitmap b = BitmapFactory.decodeFile(uri.getPath(), options);

							if (b != null) {
								imgView.setImageBitmap(b);
							} else {
								Toast.makeText(getApplicationContext(), getString(R.string.lbl_err_invalid_photo), Toast.LENGTH_LONG).show();
							}
						} catch (Exception e) {
							Log.e(TAG, "" + e);
						}
					}
					// mTextView.setText("additional extra: " +
					// data.getStringExtra(MY_EXTRA));
				}
			}
			break;
		case REQUEST_CODE_GET_CONTENT:
			if (resultCode == RESULT_OK && data != null) {
				String filePath = null;
				long fileSize = 0;
				String displayName = null;
				Uri uri = data.getData();
				Cursor c = getContentResolver().query(uri,
						new String[] { MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.MIME_TYPE, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.SIZE }, null,
						null, null);
				if (c != null && c.moveToFirst()) {
					int id = c.getColumnIndex(Images.Media.DATA);
					if (id != -1) {
						filePath = c.getString(id);
					}
					displayName = c.getString(2);
					fileSize = c.getLong(3);
				}
				if (filePath != null) {
					// mEditText.setText(filePath);
					// String strFileSize = getString(R.string.get_content_info,
					// displayName, "" + fileSize);
					// mTextView.setText(strFileSize);
				}
			}
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
	protected Intent getPreviousIntent() {
		Intent dummy = new Intent(getApplicationContext(), PreLoginActivity.class);
		dummy.putExtra("SHOULD_FINISH", true);
		return dummy;
	}

	// @Override
	protected void addActivityToStack() {
		// TODO Auto-generated method stub

	}
}
