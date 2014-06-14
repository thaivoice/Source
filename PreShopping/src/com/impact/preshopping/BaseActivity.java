package com.impact.preshopping;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import me.dm7.barcodescanner.zbar.BarcodeFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.SyncStateContract.Constants;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.dudev.util.DownloadImageThread;
import com.dudev.util.DownloadImageThread.ImageType;
import com.dudev.util.RequestType;
import com.dudev.util.RestClient;
import com.dudev.util.SyncDataThread;
import com.dudev.util.Utilities;
import com.dudev.util.Utilities.MediaType;
import com.impact.preshopping.activity.CategoryActivity;
import com.impact.preshopping.activity.CompanyActivity;
import com.impact.preshopping.activity.RegistrationActivity2;
import com.impact.preshopping.activity.VideoListActivity;

public abstract class BaseActivity extends SherlockActivity {

    public static final String TAG = BaseActivity.class.getSimpleName();
    private List<WeakReference<Activity>> stack;
    private List<Class<?>> clazz;
    // protected DownloadVideoTask downloadVideoTask;
    private boolean refresh;
    private static com.actionbarsherlock.view.Menu menu;
    private static MenuItem menuItem;
    private ProgressDialog progressDialog;
    protected DownloadVideoTask downloadTask;
    private List<WeakReference<Activity>> weakRef = new ArrayList<WeakReference<Activity>>();

    private Handler playVideoHandle = new Handler(new Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            } catch (Exception e) {
            }

            String desc = msg.getData().getString("VIDEO_SHORT_DESC");
            final Bundle b = msg.getData();
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getApplicationContext(), android.R.style.Theme_Dialog));
            builder.setTitle("Information");
            // builder.setIcon(R.drawable.ic_action_);
            builder.setMessage(TextUtils.isEmpty(desc) ? "Video has just been downloaded. Would you like to watch it now?" : desc
                    + " video has just been downloaded. Would you like to watch it now?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    PackageManager managerclock = getPackageManager();
                    Intent i = managerclock.getLaunchIntentForPackage(getApplication().getPackageName());
                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                    i.putExtra("VIDEO_FILE_INFO", b);
                    getApplicationContext().startActivity(i);

                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alert.show();
            return true;
        }
    });
    private Boolean skipCompanyPage;

    // protected abstract void onDownloadCompleted();

    // @Override
    // protected void onResume() {
    // Log.e(TAG, "onResume...");
    // super.onResume();
    // SharedPreferences prefs =
    // PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    // boolean isDownloading = prefs.getBoolean("APP_STATUS", false);
    // if (isDownloading) {
    // if (menuItem != null)
    // menuItem.setActionView(R.layout.download_progressbar);
    // else {
    // Log.e(TAG, "menuItem is null...");
    // }
    // }
    // }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.text);
        // ((TextView)
        // findViewById(R.id.text)).setText(R.string.submenus_content);
        skipCompanyPage = Boolean.valueOf(getString(R.string.app_no_company_preset));
        String clazzName = "";
        if (skipCompanyPage) {
            clazzName = CategoryActivity.class.getSimpleName();
        } else {
            clazzName = CompanyActivity.class.getSimpleName();
        }

        if (this.getClass().getSimpleName().equals(clazzName)) {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(false);
            getActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setIcon(getResources().getDrawable(R.drawable.p_tu_96));
            getActionBar().setTitle("Exit");

        } else {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(false);
            getActionBar().setDisplayShowHomeEnabled(true);
//            getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_action_back));
            getActionBar().setTitle("Back");
        }
//        getActionBar().setBackgroundDrawable(getResources().getDrawable(android.R.color.black));

        stack = ((PreShoppingApp) getApplication()).getActivityStack();
        clazz = ((PreShoppingApp) getApplication()).getActivityClazz();

        int size = stack.size();
        boolean alreadyInStack = false;
        for (int i = 0; i < size; i++) {
            Activity a = stack.get(i).get();
            if (a != null) {
                if (a.getClass().getName().equals(this.getClass().getName())) {
                    alreadyInStack = true;
                }
            }
        }

        if (!alreadyInStack) {
            Log.w(TAG, "Not exist in stack - request to add new ref.");
            addActivityToStack();
        }

        weakRef.clear();
        if (this.getClass().getSimpleName().equals(VideoListActivity.class.getSimpleName())) {
            weakRef.add(new WeakReference<Activity>(this));
        }

    }

    // @Override
    // protected void onPause() {
    // super.onPause();
    //
    // int size = stack.size();
    // if (size == 0) {
    // stack.add(new WeakReference<Activity>(this));
    // } else {
    //
    //
    // }
    // }

    protected abstract Intent getPreviousIntent();

    protected abstract void addActivityToStack();

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isInDownloadMode = prefs.getBoolean("APP_STATUS", false);

        if (isInDownloadMode) {
            outState.putBoolean("DOWNLOAD_MODE", true);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("DOWNLOAD_MODE")) {
                // progressDialog = new ProgressDialog(getContext());

                if (progressDialog != null) {
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Downloading file...");
                    progressDialog.setTitle("Please press Home button to switch to other apps");
                    progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            prefs.edit().putBoolean("APP_STATUS", false).commit();

                            dialog.dismiss();

                            if (downloadTask != null) {
                                downloadTask.cancel(true);
                            }
                        }
                    });

                    progressDialog.setIndeterminate(false);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.show();

                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {

//        SpannableString sReg = new SpannableString("Registration");
//        sReg.setSpan(new ForegroundColorSpan(Color.WHITE), 0, sReg.length(), 0);
//
//        SpannableString sFav = new SpannableString("Favorites");
//        sFav.setSpan(new ForegroundColorSpan(Color.WHITE), 0, sFav.length(), 0);

        SubMenu subMenu1 = menu.addSubMenu("Menu");
        subMenu1.add("Registration").setIntent(new Intent(this, RegistrationActivity2.class).putExtra("data", "0"));
        subMenu1.add("Favorites").setIntent(new Intent(this, FavoriteSettingActivity.class).putExtra("data", "2"));
        String folderPath = Utilities.getAppFolder_ExtSd(getApplicationContext());

        // Intent intent = new Intent();
        // intent.setAction(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.fromFile(new File(folderPath));
        // intent.setDataAndType(uri, "file/*");

        // Intent intent = new Intent(Intent.ACTION_VIEW);
        // Uri uri = Uri.fromFile(new File(folderPath));
        // intent.setData(uri);
        // Intent i = new Intent(Intent.ACTION_PICK,
        // android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);

//        SpannableString sMngData = new SpannableString("Manage Data");
//        sMngData.setSpan(new ForegroundColorSpan(Color.WHITE), 0, sMngData.length(), 0);
        subMenu1.add("Manage Data").setIntent(intent.putExtra("MANAGE_DATA", true));

        Intent qr = new Intent();
        qr.putExtra("QR", true);

//        SpannableString sQrReader = new SpannableString("QR Reader");
//        sQrReader.setSpan(new ForegroundColorSpan(Color.WHITE), 0, sQrReader.length(), 0);
        subMenu1.add("QR/Barcode Reader").setIntent(qr);
        
//        SpannableString sPromotion = new SpannableString("Promotion History");
//        sPromotion.setSpan(new ForegroundColorSpan(Color.WHITE), 0, sPromotion.length(), 0);
        subMenu1.add("Promotion History").setIntent(new Intent(this, PromotionHistoryListActivity.class).putExtra("data", "0"));
        subMenu1.add("Sync Data").setIntent(new Intent().putExtra("SHOULD_SYNC", true));
        
        MenuItem subMenu1Item = subMenu1.getItem();
        // subMenu1Item.setIcon(R.drawable.ic_menu_moreoverflow_normal_holo_dark);
//        subMenu1Item.setIcon(R.drawable.ic_action_overflow);
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        // SubMenu subMenu2 = menu.addSubMenu("Overflow Item");
        // subMenu2.add("These");
        // subMenu2.add("Are");
        // subMenu2.add("Sample");
        // subMenu2.add("Items");
        //
        // MenuItem subMenu2Item = subMenu2.getItem();
        // subMenu2Item.setIcon(R.drawable.ic_compose);
        /*
        MenuItem refreshItem = menu.add(Menu.NONE, 99, 1, "Refresh");
        refreshItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        refreshItem.setIcon(R.drawable.ic_action_refresh);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isDownloading = prefs.getBoolean("APP_STATUS", false);
        if (isDownloading) {
            refreshItem.setActionView(R.layout.download_progressbar);
        }

        menuItem = refreshItem;*/

        return true;
    }

    

    // Params, Progress, Result
    private class GetDbVersionTask extends AsyncTask<Void, Void, Boolean> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(BaseActivity.this);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Check data version...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean shouldSync) {
            super.onPostExecute(shouldSync);

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }

            if (shouldSync) {
                new SyncDataTask().execute();
            } else {
                if (refresh) {
                    Intent launch = new Intent(getApplicationContext(), CompanyActivity.class);
                    launch.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(launch);
                    finish();
                } else {
                    finish();
                }
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean shouldUpdate = Boolean.FALSE;

            RestClient client = new RestClient(Utilities.reformEndpoint(getApplicationContext(), com.dudev.util.Constants.TAG_GET_DBVERSION_METHOD));
            try {
                client.Execute(RequestType.POST);
            } catch (Exception e) {
                Log.e(TAG, "" + e);
            }
            String response = client.GetResponse();
            if (!TextUtils.isEmpty(response)) {
                Log.i(TAG, "" + response);

                try {
                    //
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

            } else {
                Log.e(TAG, "Error occurred while getting dbversion.");
            }

            return shouldUpdate;
        }

    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isInDownloadMode = prefs.getBoolean("APP_STATUS", false);
        if (isInDownloadMode) {

            Toast.makeText(getApplicationContext(), "Please press Home button to switch to other apps", Toast.LENGTH_LONG).show();

            return true;
        }

        if (item.getItemId() == 99) {
            menuItem = item;
            item.setActionView(R.layout.download_progressbar);
            refresh = true;
            // new GetDbVersionTask().execute();
            // ForceSync...
            new SyncDataTask().execute();
        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (getPreviousIntent() == null) {
                // startActivity(getPreviousIntent());
                new GetDbVersionTask().execute();
            } else {
                // Intent launch = getPreviousIntent();
                // launch.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                // launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // startActivity(launch);
                // finish();

                Intent launch = getPreviousIntent();
                boolean shouldFinish = false;

                try {
                    shouldFinish = launch.getBooleanExtra("SHOULD_FINISH", false);
                } catch (Exception e) {
                    shouldFinish = false;
                }

                if (shouldFinish) {
                    finish();
                } else {
                    launch.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(launch);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    finish();
                }

            }
        } else {
            Intent launch = item.getIntent();

            boolean manageData = false;
            try {
                manageData = launch.getBooleanExtra("MANAGE_DATA", false);

            } catch (Exception e) {
                manageData = false;
            }
            
            boolean shouldSync = false;
            try {
                shouldSync = launch.getBooleanExtra("SHOULD_SYNC", false);
            } catch (Exception e) {
                
            }

            boolean qrRequest = false;
            try {
                qrRequest = launch.getBooleanExtra("QR", false);

            } catch (Exception e) {
                qrRequest = false;
            }

            if (shouldSync) {
                ((PreShoppingApp)getApplication()).cancelAlarm();
                refresh = true;
                new SyncDataTask().execute();
            } else if (manageData) {

                try {
                    // Intent i = null;
                    // PackageManager managerclock = getPackageManager();
                    // i =
                    // managerclock.getLaunchIntentForPackage("org.openintents.filemanager");
                    // i.addCategory(Intent.CATEGORY_LAUNCHER);
                    // i.putExtra("data", Uri.fromFile(new
                    // File(Utilities.getAppFolder_ExtSd(getApplicationContext()))));
                    // startActivity(i);

                    Intent mng = new Intent(getApplicationContext(), ManageDataPanelActivity.class);
                    startActivity(mng);

                } catch (Exception e) {

                    showDialogToDownloadIoMgr();
                }

            } else if (qrRequest) {
                Log.i(TAG, "Start QR processing...");

                startBarcodeScanner();

            } else {
                int size = stack.size();
                Log.w(TAG, "activity stack size = " + size);
                for (int i = 0; i < size; i++) {
                    Activity a = stack.get(i).get();
                    if (a != null) {

                        if (launch != null) {
                            if (a.getClass().getName().equals(launch.getComponent().getClassName())) {
                                if (launch.getComponent().getClassName().equals(CompanyActivity.class.getName())) {
                                    Log.w(TAG, "MainActivity is about to launch.");
                                    stack.clear();
                                    launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                }
                                launch.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                break;
                            }
                        }

                    }
                }

                if (launch != null) {
                    if (launch.getComponent().getClassName().equals(CompanyActivity.class.getName())) {
                        Log.w(TAG, "MainActivity is about to launch.");
                        stack.clear();
                        launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    }
                    startActivity(launch);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                }
            }
        }
        return true;
        // return super.onOptionsItemSelected(item);
    }

    private void showDialogToDownloadIoMgr() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(getApplicationContext(), android.R.style.Theme_Dialog));
        alertDialog.setTitle("Information"); // your dialog title
        alertDialog.setMessage("PreShopping App required IO Manager App to function. Press Ok to start download...");

        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    protected static final int REQUEST_CODE_PICK_FILE_OR_DIRECTORY = 1;
    protected static final int REQUEST_CODE_GET_CONTENT = 2;
    private static final String MY_EXTRA = "org.openintents.filemanager.demo.EXTRA_MY_EXTRA";

    /**
     * Use GET_CONTENT to open a file.
     */
    public void getContent() {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_DEFAULT);

        try {
            // startActivityForResult(intent, REQUEST_CODE_GET_CONTENT);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // No compatible file manager was found.
            Toast.makeText(this, R.string.no_filemanager_installed, Toast.LENGTH_SHORT).show();
        }
    }

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
                        getContent();

                    }
                }
            }
            break;
        case REQUEST_CODE_GET_CONTENT:
            if (resultCode == RESULT_OK && data != null) {
                String filePath = null;
                long fileSize = 0;
                String displayName = null;
                Uri uri = data.getData();
                Cursor c = getContentResolver().query(
                        uri,
                        new String[] { MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.MIME_TYPE, MediaStore.MediaColumns.DISPLAY_NAME,
                                MediaStore.MediaColumns.SIZE }, null, null, null);
                if (c != null && c.moveToFirst()) {
                    int id = c.getColumnIndex(Images.Media.DATA);
                    if (id != -1) {
                        filePath = c.getString(id);
                        Log.i(TAG, "" + filePath);
                    }
                    displayName = c.getString(2);
                    fileSize = c.getLong(3);
                }
                if (filePath != null) {

                    String strFileSize = getString(R.string.get_content_info, displayName, "" + fileSize);
                    Log.i(TAG, "" + strFileSize);
                }
            }

        case ScannerActivity.FLAG_SCAN_REQUEST:

            // http://mobile.bgnsolutions.com/index.php?r=products/view&id=1

            if (resultCode == Activity.RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                String formatName = data.getStringExtra("SCAN_RESULT_FORMAT");
                Log.e(TAG, "contents=" + contents + ", format=" + formatName);

                if (formatName.equals(BarcodeFormat.QRCODE.getName())) {
                    try {
                        int lastIndex = contents.lastIndexOf("id=");
                        String prodId = contents.substring(lastIndex + "id=".length());
                        attemptToStartProductDetailPage(prodId);
                    } catch (Exception e) {
                        Toast.makeText(this, "Unknown QR code, please try again!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    attemptToStartProductDetailPage_Barcode(contents);
                }
            } else {
                ; // invalid result, ignore.
            }
        }
    }

    private void attemptToStartProductDetailPage_Barcode(String barcode) {
        // verify if this product id does exist.
        // if not, display informative dialog.
        boolean exists = false;
        try {
            exists = Utilities.doesProdExist_Barcode(getApplicationContext(), barcode);
        } catch (Exception e) {
            Log.e(TAG, "" + e);
            exists = false;
        }

        if (exists) {

            String prodId = Utilities.getProdIdByBarcode(getApplicationContext(), barcode);

            String groupId = Utilities.getGroupIdByProdId(getApplicationContext(), prodId);
            String categoryId = Utilities.getCategoryIdByGroupId(getApplicationContext(), groupId);
            String companyId = Utilities.getCompanyIdByCategoryId(getApplicationContext(), categoryId);

            Bundle b = new Bundle();
            b.putString("GROUP_ID", groupId);
            b.putString("CATEGORY_ID", categoryId);
            b.putString("COMPANY_ID", companyId);

            Intent i = new Intent(getApplicationContext(), VideoListActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("PRODUCT_ID", prodId);
            i.putExtra("EXTRA_INFO", b);
            startActivity(i);
            finish();
        } else {
            showProductNotExistDialog();
        }

    }

    private void attemptToStartProductDetailPage(String prodId) {

        // verify if this product id does exist.
        // if not, display informative dialog.
        boolean exists = false;
        try {
            int id = Integer.parseInt(prodId);
            exists = Utilities.doesProdExist(getApplicationContext(), prodId);
        } catch (Exception e) {
            Log.e(TAG, "" + e);
            exists = false;
        }

        if (exists) {
            String groupId = Utilities.getGroupIdByProdId(getApplicationContext(), prodId);
            String categoryId = Utilities.getCategoryIdByGroupId(getApplicationContext(), groupId);
            String companyId = Utilities.getCompanyIdByCategoryId(getApplicationContext(), categoryId);

            Bundle b = new Bundle();
            b.putString("GROUP_ID", groupId);
            b.putString("CATEGORY_ID", categoryId);
            b.putString("COMPANY_ID", companyId);

            Intent i = new Intent(getApplicationContext(), VideoListActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("PRODUCT_ID", prodId);
            i.putExtra("EXTRA_INFO", b);
            startActivity(i);
            finish();
        } else {
            showProductNotExistDialog();
        }
    }

    private void showProductNotExistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getApplicationContext(), android.R.style.Theme_Dialog));
        builder.setTitle("Information");
        // builder.setIcon(R.drawable.ic_action_sd_storage);
        builder.setMessage("Product does not exist. Would you like to re-scan?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();

                startBarcodeScanner();

            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alert.show();
    }

    protected void startBarcodeScanner() {
        Intent scannerActivity = new Intent(getApplicationContext(), ScannerActivity.class);
        startActivityForResult(scannerActivity, ScannerActivity.FLAG_SCAN_REQUEST);
    }

    @Override
    public void onBackPressed() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isInDownloadMode = prefs.getBoolean("APP_STATUS", false);
        if (isInDownloadMode) {

            Toast.makeText(getApplicationContext(), "Please press Home button to switch to other apps", Toast.LENGTH_LONG).show();

            return;
        }

        if (getPreviousIntent() == null) {
            new GetDbVersionTask().execute();
        } else {
            Intent launch = getPreviousIntent();
            launch.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(launch);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        }
    }

    // protected boolean hasActivityStored() {
    //
    // if (clazz.size() == 0) {
    // return false;
    // }
    //
    // boolean hasStored = false;
    //
    // for (int i = 0; i < clazz.size(); i++) {
    // if (clazz.get(i).getName()
    // .equals(this.getClass().getName())) {
    // hasStored = true;
    // break;
    // }
    // }
    //
    // return hasStored;
    // }

    public class SyncDataTask extends AsyncTask<Void, Void, Boolean> {
        private ExecutorService e;
        private Future<Boolean> f;
        private ProgressDialog dialog;
        private List<WeakReference<Activity>> weakRef = new ArrayList<WeakReference<Activity>>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakRef.add(new WeakReference<Activity>(BaseActivity.this));
            dialog = new ProgressDialog(BaseActivity.this);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Syncronizing data...");
            dialog.show();

        }

        @SuppressWarnings("unchecked")
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Boolean success;
            try {
                // Simulate login process...
                // Thread.sleep(1000);

                // Sync data....
                //    <string name="get_data_url">http://mobile.bgnsolutions.com/getdata.php</string>
                SyncDataThread sync = new SyncDataThread(getApplicationContext(), null, Utilities.reformEndpoint(getApplicationContext(), com.dudev.util.Constants.TAG_GET_DATA), getString(R.string.sync_data_method),
                        null);

                e = Executors.newFixedThreadPool(1);
                f = (Future<Boolean>) e.submit(sync);
                e.shutdown();
                success = f.get();

                // publishProgress();

                HashMap<String, String> company = Utilities.getCompanyIconUrl(getApplicationContext());
                HashMap<String, String> category = Utilities.getCategoryIconUrl(getApplicationContext());
                HashMap<String, String> group = Utilities.getGroupIconUrl(getApplicationContext());
                HashMap<String, String> product = Utilities.getProductIconUrl(getApplicationContext());

                List<Future<HashMap<String, Object>>> listOfThreads = new ArrayList<Future<HashMap<String, Object>>>();
                e = Executors.newFixedThreadPool(10);

                for (String key : company.keySet()) {
                    String url = company.get(key);
                    DownloadImageThread t = new DownloadImageThread(getApplicationContext(), Integer.valueOf(key), url, ImageType.COMPANY);
                    Future<HashMap<String, Object>> d = (Future<HashMap<String, Object>>) e.submit(t);
                    listOfThreads.add(d);
                }

                for (String key : category.keySet()) {
                    String url = category.get(key);
                    DownloadImageThread t = new DownloadImageThread(getApplicationContext(), Integer.valueOf(key), url, ImageType.CATEGORY);
                    Future<HashMap<String, Object>> d = (Future<HashMap<String, Object>>) e.submit(t);
                    listOfThreads.add(d);
                }

                for (String key : group.keySet()) {
                    String url = group.get(key);
                    DownloadImageThread t = new DownloadImageThread(getApplicationContext(), Integer.valueOf(key), url, ImageType.GROUP);
                    Future<HashMap<String, Object>> d = (Future<HashMap<String, Object>>) e.submit(t);
                    listOfThreads.add(d);
                }

                for (String key : product.keySet()) {
                    String url = product.get(key);
                    DownloadImageThread t = new DownloadImageThread(getApplicationContext(), Integer.valueOf(key), url, ImageType.PRODUCT);
                    Future<HashMap<String, Object>> d = (Future<HashMap<String, Object>>) e.submit(t);
                    listOfThreads.add(d);
                }
                e.shutdown();
                // while (!e.isTerminated()) {
                // Thread.sleep(500);
                // }

                // publishProgress();

                for (int i = 0; i < listOfThreads.size(); i++) {
                    Log.i(TAG, "" + listOfThreads.get(i).get().get("BELONG_TO") + ", " + listOfThreads.get(i).get().get("ID") + ", "
                            + listOfThreads.get(i).get().get("URI"));
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
            }

            return success;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (weakRef.get(0) != null && !weakRef.get(0).get().isFinishing()) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
            }

            if (refresh) {
                Intent launch = new Intent(getApplicationContext(), skipCompanyPage ? CategoryActivity.class : CompanyActivity.class);
                launch.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(launch);
                finish();
            } else {
                finish();
            }
        }

    }

    protected class DownloadVideoTask extends AsyncTask<List<HashMap<String, String>>, DownloadProgressInfo, Integer> {

        public DownloadVideoTask() {
            // TODO Auto-generated constructor stub
        }

        private List<HashMap<String, String>> list;
        private List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

        @Override
        protected void onCancelled() {
            super.onCancelled();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            prefs.edit().putBoolean("APP_STATUS", false).commit();
            downloadTask = null;
            // Intent intent = new Intent("VIDEO_DOWNLOAD_TASK_COMPLETE");
            // getApplicationContext().sendBroadcast(intent);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            prefs.edit().putBoolean("APP_STATUS", true).commit();

            progressDialog = new ProgressDialog(new ContextThemeWrapper(getContext(), android.R.style.Theme_Dialog));
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Downloading file...");
            progressDialog.setTitle("Please press Home button to switch to other apps");
            progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    prefs.edit().putBoolean("APP_STATUS", false).commit();

                    dialog.dismiss();
                    downloadTask.cancel(true);
                }
            });

            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Integer result) {// -1 ==> no space left
                                                      // error case.
            super.onPostExecute(result);

            if (weakRef.get(0).get() != null && !weakRef.get(0).get().isFinishing()) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }

            downloadTask = null;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            prefs.edit().putBoolean("APP_STATUS", false).commit();

            if (result == -1) {
                showNoSpaceLeftDialog();
            } else {
                if (isCancelled()) {
                    ; // most likely ignore for now!
                } else {
                    Intent intent = new Intent("VIDEO_DOWNLOAD_TASK_COMPLETE");
                    getApplicationContext().sendBroadcast(intent);
                }
            }
        }

        @Override
        protected void onProgressUpdate(DownloadProgressInfo... values) {
            super.onProgressUpdate(values);
            DownloadProgressInfo p = values[0];
            if (weakRef.get(0).get() != null && !weakRef.get(0).get().isFinishing()) {
                progressDialog.setMax(p.totalBytes);
                progressDialog.setProgress(p.downloadedBytes);
                progressDialog.setMessage("Downloading file " + p.fileNumber + ", " + Utilities.getReadableSize(p.totalBytes - p.downloadedBytes) + " left");
            }
        }

        @Override
        protected Integer doInBackground(List<HashMap<String, String>>... params) {

            try {
                list = params[0];

                for (int i = 0; i < list.size(); i++) {

                    if (!isCancelled()) {
                        HashMap<String, String> data = list.get(i);
                        String prodId = data.get(Utilities.PROD_ID);
                        String mediaId = data.get(Utilities._ID);
                        String url = data.get(Utilities.URL);
                        MediaType type = MediaType.valueOf(data.get(Utilities.TYPE));
                        Uri downloadedUri = download((i + 1), prodId, mediaId, url, type);

                        if (!Utilities.isAppInForeground(getApplicationContext())) {
                            // alert a dialog suggesting if user want ot watch
                            // the video being just downloaded.

                            showVideoDownloadedDailog(mediaId, downloadedUri);
                        }

                    } else {
                        break;
                    }
                }

                return list.size();

            } catch (IOException ioe) {
                // most likely no space left for download to complete.
                return -1;
            } catch (Exception e) {
                Log.e(TAG, "" + e);
            }

            return 0;
        }

        private Uri download(int fileNumber, String id, String mediaId, String downloadUrl, MediaType type) throws IOException {
            Uri uri = null;

            OutputStream out = null;
            InputStream in = null;
            int downloaded = 0;
            IOException ioe = null;
            File destFile = null;
            try {
                URL url = new URL(downloadUrl);

                char c = downloadUrl.charAt(downloadUrl.length() - 1);

                String filename = "";
                if (c == '/') {
                    int index = downloadUrl.lastIndexOf(c);
                    filename = downloadUrl.substring(index);
                } else {
                    int index = downloadUrl.lastIndexOf("/");
                    filename = downloadUrl.substring(index + 1);
                }

                // File destFile = new File(filePath + "/" + filename); //
                // /storage/extSdCard/com.impact.preshopping/media/forbes.gif
                String filePath = Utilities.getAppFolder_ExtSd(getApplicationContext());
                // filePath = filePath + "/" + type.name() + "/" + id + "/";
                filePath = filePath + "/" + type.name() + "/";
                File dir = new File(filePath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                destFile = new File(filePath + filename);
                if (!destFile.exists()) {
                    destFile.createNewFile();
                } else {
                    downloaded = (int) destFile.length();
                }

                out = downloaded == 0 ? new FileOutputStream(destFile) : new FileOutputStream(destFile, true);

                // ...
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int total = conn.getContentLength();
                conn.disconnect();

                if (total > downloaded) {
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Range", "bytes=" + downloaded + "-");
                    conn.setConnectTimeout(120 * 1000);
                    conn.setReadTimeout(120 * 1000);
                    conn.connect();
                    in = conn.getInputStream();

                    byte[] buffer = new byte[1024];
                    int length = 0;

                    DownloadProgressInfo progress = new DownloadProgressInfo();
                    progress.totalBytes = conn.getContentLength();
                    progress.downloadedBytes = downloaded;
                    progress.fileNumber = fileNumber;

                    while ((length = in.read(buffer)) > 0 && !isCancelled()) {
                        out.write(buffer, 0, length);

                        progress.downloadedBytes += length;
                        publishProgress(progress);
                    }

                    conn.disconnect();
                }

                if (isCancelled()) {
                    uri = null;
                } else {
                    uri = Uri.fromFile(destFile);
                }

            } catch (IOException e) {
                Log.e(TAG, "" + e);
                // Most likely there is no space left.
                ioe = new IOException("No sapce left for download");
                throw ioe;

            } catch (ClassCastException e) {
                Log.e(TAG, "" + e);
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Cannot close outputstream");
                    }
                }

                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    in = null;
                }

                if (uri != null && ioe == null) {
                    long row = Utilities.updateMediaTbl(getApplicationContext(), mediaId, uri.toString());
                    System.out.println("downloaded to..." + uri.toString());
                    System.out.println("Updated number of rows: " + row);
                } else {
                    // error happened during download...
                    // remove this unfinished file.
                    if (destFile != null) {
                        if (destFile.exists()) {
                            destFile.delete();
                        }
                    }
                }
            }

            return uri;
        }
    }

    protected class DownloadProgressInfo {
        int totalBytes;
        int downloadedBytes;
        int fileNumber;
    }

    public Context getContext() {
        return this;
    }

    public void showVideoDownloadedDailog(String mediaId, final Uri downloadedUri) {

        // get media short desc
        String shortDesc = Utilities.getMediaShortDesc(getApplicationContext(), mediaId);
        Message msg = Message.obtain(playVideoHandle);
        Bundle data = new Bundle();
        data.putString("VIDEO_SHORT_DESC", shortDesc);
        data.putString("VIDEO_URI_STR", downloadedUri.toString());
        msg.setData(data);

        playVideoHandle.sendMessage(msg);
    }

    public void showNoSpaceLeftDialog() {

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getApplicationContext(), android.R.style.Theme_Dialog));
        builder.setTitle("Information");
        builder.setIcon(R.drawable.ic_action_sd_storage);
        builder.setMessage("Couldn't write to disk. Possibly no more space left on device to complete the download. Please clean up old data and try again");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();

                Intent intent = new Intent("VIDEO_DOWNLOAD_TASK_COMPLETE");
                getApplicationContext().sendBroadcast(intent);

            }
        });
        // builder.setNegativeButton("Close", new
        // DialogInterface.OnClickListener() {
        // public void onClick(DialogInterface dialog, int whichButton) {
        // dialog.dismiss();
        //
        // Intent intent = new Intent("VIDEO_DOWNLOAD_TASK_COMPLETE");
        // getApplicationContext().sendBroadcast(intent);
        // }
        // });
        AlertDialog alert = builder.create();
        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alert.show();
    }
}
