package com.impact.preshopping;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import tr.com.turkcellteknoloji.turkcellupdater.Message;
import tr.com.turkcellteknoloji.turkcellupdater.UpdaterDialogManager;
import net.appositedesigns.fileexplorer.FileExplorerApp;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

public class PreShoppingApp extends FileExplorerApp implements UpdaterDialogManager.UpdaterUiListener{
    
    private boolean isAdminMode;

	public boolean isAdminMode() {
        return isAdminMode;
    }

    public void setAdminMode(boolean isAdminMode) {
        this.isAdminMode = isAdminMode;
    }

    private List<WeakReference<Activity>> activityStack = new ArrayList<WeakReference<Activity>>();

	public List<WeakReference<Activity>> getActivityStack() {
		return activityStack;
	}

	private List<Class<?>> activityClazz = new ArrayList<Class<?>>();
	private SharedPreferences prefs;

	public List<Class<?>> getActivityClazz() {
		return activityClazz;
	}

	private static Bundle map;
	public static Bundle getMap() {
		if (map == null) {
			map = new Bundle();
		}
		
		return map;
	}
	
	public void setMap(Bundle map) {
		this.map = map;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		cancelAlarm();
	}

	public void cancelAlarm() {
	    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(getPendingIntent(getApplicationContext(), 1234));
	}
	
    private PendingIntent getPendingIntent(Context context, int id) {
        Intent intent =  new Intent(context, SyncDataService.class);
        return PendingIntent.getService(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onExitApplication() {
        Log.w("updater", "onExitApplication");
    }

    @Override
    public void onUpdateCheckCompleted() {
        Log.w("updater", "onUpdateCheckCompleted");
    }

    @Override
    public boolean onDisplayMessage(Message message) {
        Log.w("updater", "onDisplayMessage");
        return false;
    }
}
