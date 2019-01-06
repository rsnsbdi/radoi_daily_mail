package com.softnep.radiodailymail.radioPlayer;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class KillNotificationsService extends Service {

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(Activity_Online_Radio.NOTIFICATION_ID);
		
		stopSelf();
	}

	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}