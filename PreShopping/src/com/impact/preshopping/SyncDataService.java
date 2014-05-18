package com.impact.preshopping;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dudev.util.RequestType;
import com.dudev.util.RestClient;
import com.dudev.util.Utilities;
import com.impact.preshopping.activity.PromotionDlgActivity;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class SyncDataService extends IntentService{

    public static final String TAG = SyncDataService.class.getSimpleName();
    public static final String SHOW_SYNC_DATA_DLG = "SHOW_SYNC_DATA_DLG";
    
    public SyncDataService() {
        super("SyncDataService");
    }
    
    public SyncDataService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
       
        Log.w(TAG, "Checking data base version on backend...");
        boolean shouldSyncData = shouldSync();
        
        if (shouldSyncData) {
            Intent i = new Intent(getApplicationContext(), PromotionDlgActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra(SHOW_SYNC_DATA_DLG, true);
            startActivity(i);
        }
    }

    private boolean shouldSync() {
        Boolean shouldUpdate = Boolean.FALSE;

        RestClient client = new RestClient(Utilities.reformEndpoint(getApplicationContext(), com.dudev.util.Constants.TAG_GET_DBVERSION_METHOD));
        try {
            client.Execute(RequestType.POST);
        } catch (Exception e) {
            Log.e(TAG, "" + e);
            client = null;
        }
        
        if (client == null) {
            return shouldUpdate;
        }
            
            
        String response = client.GetResponse();
        if (TextUtils.isEmpty(response)) {
            return shouldUpdate;
        }
        Log.i(TAG, "" + response);

        try {
            StringBuilder builder = new StringBuilder(response);
            builder.deleteCharAt(0);
            builder.deleteCharAt(builder.length() - 1);

            JSONObject json = new JSONObject(builder.toString());
            JSONArray arr = json.getJSONArray("data");
            if (arr.length() > 0) {
                JSONObject data = arr.getJSONObject(0);
                String dbVersion = data.getString("Version");
                String lastUpdateTime = data.getString("LastUpdate");

                String oldVersion = Utilities.getDbVersion(getApplicationContext());
                if (!oldVersion.equals(dbVersion)) {
                    long num = Utilities.updateDbVersion(getApplicationContext(), dbVersion, lastUpdateTime);
                    Log.e(TAG, "inserted rows: " + num);

                    shouldUpdate = Boolean.TRUE;
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "" + e);
        }
        
        return shouldUpdate;
    }
}
