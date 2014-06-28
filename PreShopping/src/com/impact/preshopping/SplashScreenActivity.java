
package com.impact.preshopping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tr.com.turkcellteknoloji.turkcellupdater.Message;
import tr.com.turkcellteknoloji.turkcellupdater.UpdaterDialogManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.dudev.util.Constants;
import com.dudev.util.Utilities;
import com.impact.preshopping.activity.CategoryActivity;
import com.impact.preshopping.activity.CompanyActivity;
import com.stickmanventures.android.example.immersive_videoplayer.ImmersiveVideoplayer;
import com.stickmanventures.android.example.immersive_videoplayer.entities.Video;
import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

public class SplashScreenActivity extends Activity implements

        UpdaterDialogManager.UpdaterUiListener {

    protected int splashTime = 4000;
    private SharedPreferences prefs;
    private View splashLogo;
    private boolean skipCompanyPage;
    private static String APP_ID;
    private Message message;    
    public static final String TAG = SplashScreenActivity.class.getSimpleName();
    
    @Override
    @SuppressWarnings("static-access")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        APP_ID = getString(R.string.app_id_hockey);
        checkAppUpdate();
        checkForUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForCrashes();
    }

    private void checkForCrashes() {
        CrashManager.register(this, APP_ID);
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this, APP_ID);
    }

    private void initializeUi() {
        skipCompanyPage = Boolean.valueOf(getString(R.string.app_no_company_preset));

        // Check tasks list
        final ActivityManager am = (ActivityManager) getSystemService(this.ACTIVITY_SERVICE);
        final List<RunningTaskInfo> tasksInfo = am.getRunningTasks(1024);
        boolean skipSplash = true;

        if (!tasksInfo.isEmpty()) {
            final String ourAppPackageName = getPackageName();

            for (RunningTaskInfo taskInfo : tasksInfo) {

                if (ourAppPackageName.equals(taskInfo.baseActivity.getPackageName())
                        && taskInfo.numActivities == 1) {
                    skipSplash = false;
                    break;
                }
            }
        }

        if (skipSplash) {

            try {
                Bundle b = getIntent().getBundleExtra("VIDEO_FILE_INFO");
                final String strUri = b.getString("VIDEO_URI_STR");
                Video video = new Video(strUri);
                video.setDescription("");

                Intent intent = new Intent(
                        getApplicationContext(),
                        Class.forName("com.stickmanventures.android.example.immersive_videoplayer.ui.activities.VideoPlayerActivity"));
                intent.putExtra(ImmersiveVideoplayer.EXTRA_LAYOUT, "0");
                intent.putExtra(Video.class.getName(), video);
                startActivity(intent);

            } catch (Exception e) {

            }
            finish();
        } else {
            setContentView(R.layout.splash_screen);
            setTitle(getString(R.string.app_name));

            splashLogo = findViewById(R.id.splash_logo);
            splashLogo.setAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_screen));

            new Handler().postDelayed(new Runnable() {

                public void run() {
                    splashLogo.setVisibility(View.INVISIBLE);

                    prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    if (prefs.getBoolean(Constants.TAG_NONE_REGISTERED, true)) {
                        Intent reg = new Intent(getApplicationContext(), PreLoginActivity.class);
                        // Intent reg = new Intent(getApplicationContext(),
                        // LoginActivity.class);
                        reg.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(reg);
                    } else {
                        if (prefs.getBoolean(Constants.TAG_REGISTERED_SUCCESS, false)) {

                            if (prefs.getBoolean(Constants.TAG_HAS_LOGGED_IN, false)) {
                                Intent login = new Intent(getApplicationContext(),
                                        skipCompanyPage ? CategoryActivity.class
                                                : CompanyActivity.class);
                                login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(login);
                            } else {
                                Intent company = new Intent(getApplicationContext(),
                                        LoginActivity.class);
            
                                company.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(company);
                            }

                        } else {
                            Intent reg = new Intent(getApplicationContext(), PreLoginActivity.class);
                            reg.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(reg);
                        }
                    }

                    finish();
                }
            }, splashTime);
        }

        // By Company
        HashMap<String, String> allCompanies = Utilities.getAllCompanies(getApplicationContext());
        Set<String> companies = new HashSet<String>();

        String[] arrCompEntries = new String[allCompanies.size()];
        String[] arrCompValues = new String[allCompanies.size()];
        int i = 0;
        for (String id : allCompanies.keySet()) {
            arrCompValues[i] = id;
            arrCompEntries[i] = allCompanies.get(id);
            i++;

            companies.add("0");
        }
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        Log.i("set", "" + prefs.getStringSet("by_company", companies));

        Set<String> compSet = prefs.getStringSet("by_company", companies);
        // Set<String> test = new HashSet<String>();
        // test.add(arrCompValues[0]);
        // test.add(arrCompValues[1]);
        // test.add(arrCompValues[2]);
        // prefs.edit().putStringSet("by_company", test).commit();

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
    }

    private void checkAppUpdate() {
        UpdaterDialogManager updaterUI = new UpdaterDialogManager(
                getString(R.string.application_endpoint) + Constants.TAG_APP_UPDATER_URL);
        updaterUI.setPostProperties(true);
        updaterUI.startUpdateCheck(SplashScreenActivity.this, SplashScreenActivity.this);
    }

    @Override
    public void onExitApplication() {
        finish();
    }

    @Override
    public void onUpdateCheckCompleted() {
        // final Intent intent = new Intent(this, LoginActivity.class);
        // intent.putExtra("message", (Serializable) message);
        // startActivity(intent);
        // finish();
        initializeUi();
    }

    @Override
    public boolean onDisplayMessage(Message message) {
        // To automatically display message:
        // return false;
        this.message = message;
        return true;
    }
}
