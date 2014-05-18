package com.impact.preshopping;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dudev.util.Constants;
import com.dudev.util.Utilities;
import com.dudev.util.webview.WebViewDemoActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.impact.preshopping.activity.ProductActivity;
import com.impact.preshopping.activity.PromotionDlgActivity;

public class GcmIntentService extends IntentService {
    Context context;
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public static final String TAG = "GCM Demo";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                // ignore...
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                // ignore...

            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // If it's a regular GCM message, do some work.

                Log.i(TAG, "Received: " + extras.toString());
                String startDate = extras.getString(PROMOTION_START_DATE);
                DateTime sDate = new DateTime(startDate);
                DateTime tDate = new DateTime();

                // Make sure the it is not an old promotion pushed in.
                if (DateTimeComparator.getDateOnlyInstance().compare(sDate, tDate) >= 0) {

                    // extract data and store it in database.
                    String endDate = extras.getString(PROMOTION_END_DATE);
                    String promotionDesc = extras.getString(PROMOTION_DESC);
                    String promotionUrl = extras.getString(PROMOTION_URL);
                    String promotionAlertSound = extras.getString(PROMOTION_ALERT_SOUND);
                    String companyId = extras.getString(PROMOTION_COMPANY_ID);

                    if (Utilities.isValidPromotion(getApplicationContext(), companyId)) {
                        Bundle data = new Bundle();
                        data.putString(PROMOTION_END_DATE, endDate);
                        data.putString(PROMOTION_START_DATE, startDate);
                        data.putString(PROMOTION_URL, promotionUrl);
                        data.putString(PROMOTION_DESC, promotionDesc);
                        data.putString(PROMOTION_ALERT_SOUND, promotionAlertSound);
                        data.putString(PROMOTION_COMPANY_ID, companyId);

                        // store records...
                        long row = Utilities.insertNewPromotion(getApplicationContext(), data);

                        try {
                            // Uri notification =
                            // RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            // Ringtone ringTone =
                            // RingtoneManager.getRingtone(getApplicationContext(),
                            // Uri.parse(promotionAlertSound));
                            // Ringtone r =
                            // RingtoneManager.getRingtone(getApplicationContext(),
                            // notification);
                            // ringTone.play();
                            
                            if (Utilities.isNotificationOn(getApplicationContext())) {
                                String alertType = Utilities.getAlertType(getApplicationContext(), companyId);
                                String companyName = Utilities.getCompanyName(getApplicationContext(), companyId);
                                boolean isSilientPush = Constants.FLAG_ALERT_TYPE_MSG_ONLY.equals(alertType) ? true : false;
                                sendNotification(companyName, promotionDesc, promotionUrl, Uri.parse(promotionAlertSound), isSilientPush);

                                if (Constants.FLAG_ALERT_TYPE_MSG_ONLY.equals(alertType) || Constants.FLAG_ALERT_TYPE_MSG_AND_SOUND.equals(alertType)) {
                                    data.putString(PROMOTION_COMPANY_NAME, companyName);
                                    showPushMsgDialog(data);
                                }                                
                            }

                        } catch (Exception e) {
                            
                        }
                    }
                }
            }
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    public static final String DATE_PATTERN = "yyyy-mm-dd"; // 2014-05-02
    public static final String PROMOTION_DESC = "alert";
    public static final String PROMOTION_URL = "URL";
    public static final String PROMOTION_START_DATE = "startDate";
    public static final String PROMOTION_END_DATE = "endDate";
    public static final String PROMOTION_ALERT_SOUND = "sound";
    public static final String PROMOTION_DATA = "promotionData";
    public static final String PROMOTION_COMPANY_ID = "companyID";
    public static final String PROMOTION_COMPANY_NAME = "companyName";

    private void showPushMsgDialog(Bundle msg) {
        // AlertDialog.Builder builder = new AlertDialog.Builder(new
        // ContextThemeWrapper(getApplicationContext(),
        // android.R.style.Theme_Dialog));
        // builder.setTitle("Information");
        // // builder.setIcon(R.drawable.ic_action_sd_storage);
        // builder.setMessage(msg);
        // builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        // {
        // public void onClick(DialogInterface dialog, int whichButton) {
        // dialog.dismiss();
        // }
        // });
        // builder.setNegativeButton("Close", new
        // DialogInterface.OnClickListener() {
        // public void onClick(DialogInterface dialog, int whichButton) {
        // dialog.dismiss();
        // }
        // });
        // AlertDialog alert = builder.create();
        // alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        // alert.show();

        Intent promotion = new Intent(this, PromotionDlgActivity.class);
        promotion.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        promotion.putExtra(PROMOTION_DATA, msg);
        
        startActivity(promotion);
    }

    private void sendNotification(String companyName, String msg, String url, Uri soundUri, boolean isSilentPush) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent myintent = new Intent(this, WebViewDemoActivity.class);
        myintent.putExtra(PROMOTION_URL, url);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, myintent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_500px).setContentTitle(companyName)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).setContentText(msg);

        // Uri alarmSound =
        // RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (!isSilentPush) {
            mBuilder.setSound(soundUri);
        }

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}