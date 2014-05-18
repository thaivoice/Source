package net.appositedesigns.fileexplorer.activity;


import net.appositedesigns.fileexplorer.R;
import net.appositedesigns.fileexplorer.util.PreferenceHelper;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;

public abstract class BaseFileListActivity extends ListActivity {

	protected PreferenceHelper prefs;
	
	private OnSharedPreferenceChangeListener listener;
	protected boolean shouldRestartApp = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		prefs = new PreferenceHelper(this);
		
		setTheme(prefs.getTheme());
		super.onCreate(savedInstanceState);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(true);
//		getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_action_back));
		getActionBar().setTitle(Html.fromHtml("<b>Back</b>"));
//		getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_action_back));
//        getActionBar().setBackgroundDrawable(getResources().getDrawable(android.R.color.black));
		listenToThemeChange();
	}
	
	private void listenToThemeChange() {

		listener = new OnSharedPreferenceChangeListener() {

			@Override
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				if (PreferenceHelper.PREF_THEME.equals(key)) {

					shouldRestartApp = true;

				}
				if (PreferenceHelper.PREF_USE_QUICKACTIONS.equals(key)) {

					shouldRestartApp = true;

				}
			}
		};

		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(listener);
	}
	public synchronized PreferenceHelper getPreferenceHelper()
	{
		return prefs;
	}
	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		PreferenceManager.getDefaultSharedPreferences(this)
				.unregisterOnSharedPreferenceChangeListener(listener);
	}
}
