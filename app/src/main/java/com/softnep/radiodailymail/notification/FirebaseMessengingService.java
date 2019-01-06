package com.softnep.radiodailymail.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.orhanobut.hawk.Hawk;
import com.softnep.radiodailymail.R;
import com.softnep.radiodailymail.activity.Activity_NationalNews_Description;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADMIN on 2017-11-14.
 */

public class FirebaseMessengingService extends FirebaseMessagingService {

    private int notifyID=1;
    private String notify_summary,title;
    private int news_id,category_id,breaking;
    private static final String TAG = "FirebaseMessengingServi";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if(remoteMessage.getData().size()>0)
        {
            news_id=Integer.parseInt(remoteMessage.getData().get("news_id"));
            category_id=Integer.parseInt(remoteMessage.getData().get("category"));
            title=remoteMessage.getData().get("title");
            breaking=Integer.parseInt(remoteMessage.getData().get("breaking"));
            notify_summary=remoteMessage.getData().get("summary");

            Log.d(TAG, "onMessageReceived: data msg  ");

            Log.d(TAG, "onMessageReceived: data msg  "+ remoteMessage.getData().toString());
        }

        if(remoteMessage.getNotification() !=null)
        {
            Log.d(TAG, "onMessageReceived: notification  msg  "+ remoteMessage.getNotification().getBody());
        }

        Log.d(TAG, "onMessageReceived: ");
        if (breaking == 1) {

            if (Hawk.contains("news_key")) {
                showNotification(news_id, category_id, title, breaking, notify_summary);
            }
        }else{
            showNotification(news_id, category_id, title, breaking, notify_summary);
        }
    }

    public void showNotification(int news_id, int category_id, String title, int breaking, String notify_summary)
    {

        Intent in=new Intent(this,Activity_NationalNews_Description.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        in.putExtra("news_id",news_id);
        in.putExtra("category_id",category_id);
        in.putExtra("isNotification",true);

        Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,in,PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)

                          .setContentTitle(title)
                          .setContentText(notify_summary)
                          .setAutoCancel(true)
                          .setSmallIcon(R.mipmap.test_launcher)
                          .setContentIntent(pendingIntent)
                          .setSound(sound);


            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notifyID);
           // manager.cancelAll();
            manager.notify(notifyID, builder.build());

    }

}
