package com.impact.preshopping.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dudev.util.DownloadImageThread;
import com.dudev.util.DownloadImageThread.ImageType;
import com.dudev.util.SyncDataThread;
import com.dudev.util.Utilities;
import com.dudev.util.webview.WebViewDemoActivity;
import com.impact.preshopping.GcmIntentService;
import com.impact.preshopping.R;
import com.impact.preshopping.SyncDataService;

public class PromotionDlgActivity extends Activity {

    private Button btnNoThanks;
    private Button btnYes;
    private Bundle promotionData;
    private TextView textViewPromotionDesc;
    private TextView textViewPromotionTitle;
    private boolean shouldShowSyncDataDlg;
    private boolean skipCompanyPage;
    private SyncDataTask syncDataTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promotion_dlg_activity);

        skipCompanyPage = Boolean.valueOf(getString(R.string.app_no_company_preset));

        try {
            promotionData = getIntent().getBundleExtra(GcmIntentService.PROMOTION_DATA);
        } catch (Exception e) {
            Log.e("err", "" + e);
        }

        try {
            shouldShowSyncDataDlg = getIntent().getBooleanExtra(SyncDataService.SHOW_SYNC_DATA_DLG, false);
        } catch (Exception e) {
            Log.e("err", "" + e);
        }

        btnNoThanks = (Button) findViewById(R.id.btnNoThanks);
        btnYes = (Button) findViewById(R.id.btnYes);

        btnNoThanks.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        btnYes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent web = new Intent(getApplicationContext(), WebViewDemoActivity.class);
                web.putExtra(GcmIntentService.PROMOTION_URL, promotionData.getString(GcmIntentService.PROMOTION_URL));
                web.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(web);

                finish();
            }
        });

        textViewPromotionDesc = (TextView) findViewById(R.id.textViewPromotionDesc);
        textViewPromotionTitle = (TextView) findViewById(R.id.textViewPromotionTitle);

        if (promotionData != null && promotionData.containsKey(GcmIntentService.PROMOTION_DESC)) {
            String desc = promotionData.getString(GcmIntentService.PROMOTION_DESC);
            textViewPromotionDesc.setText(TextUtils.isEmpty(desc) ? "" : desc);

            String companyName = promotionData.getString(GcmIntentService.PROMOTION_COMPANY_NAME);
            if (!TextUtils.isEmpty(companyName)) {
                textViewPromotionTitle.setText(companyName);
            }
        }

        if (shouldShowSyncDataDlg) {
            textViewPromotionTitle.setText(getString(R.string.lbl_sync_data_dlg_title));
            textViewPromotionDesc.setVisibility(View.GONE);
            btnNoThanks.setVisibility(View.GONE);

            btnYes.setText("Ok");
            btnYes.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    syncDataTask = new SyncDataTask();
                    syncDataTask.execute();
                }
            });
        }
    }

    public static final String TAG = PromotionDlgActivity.class.getSimpleName();

    private class SyncDataTask extends AsyncTask<Void, Void, Boolean> {
        private ExecutorService e;
        private Future<Boolean> f;
        private ProgressDialog dialog;
        private List<WeakReference<Activity>> weakRef = new ArrayList<WeakReference<Activity>>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakRef.add(new WeakReference<Activity>(PromotionDlgActivity.this));
            dialog = new ProgressDialog(PromotionDlgActivity.this);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Syncronizing data...");
            dialog.show();

        }

        @SuppressWarnings("unchecked")
        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean success = Boolean.FALSE;
            
            try {

                SyncDataThread sync = new SyncDataThread(getApplicationContext(), null, Utilities.reformEndpoint(getApplicationContext(),
                        com.dudev.util.Constants.TAG_GET_DATA), getString(R.string.sync_data_method), null);

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
                if (!e.awaitTermination(3, TimeUnit.MINUTES)) {
                	success = false;
                } else {
                	success = true;
                }

                if (success) {
                	for (int i = 0; i < listOfThreads.size(); i++) {
                        Log.i(TAG, "" + listOfThreads.get(i).get().get("BELONG_TO") + ", " + listOfThreads.get(i).get().get("ID") + ", "
                                + listOfThreads.get(i).get().get("URI"));
                        String belongTo = listOfThreads.get(i).get().get("BELONG_TO").toString();
                        String id = listOfThreads.get(i).get().get("ID").toString();
                        String filePath = listOfThreads.get(i).get().get("URI").toString();
                        long total = Utilities.updateIconOrImageFilePath(getApplicationContext(), belongTo, id, filePath);
                        Log.e(TAG, "Updated: " + total);
                    }                	
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

            Intent launch = new Intent(getApplicationContext(), skipCompanyPage ? CategoryActivity.class : CompanyActivity.class);
            launch.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(launch);
            finish();
        }
    }
}
