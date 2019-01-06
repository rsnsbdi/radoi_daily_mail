package com.softnep.radiodailymail.radioPlayer;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

public class NotificationActionButtonReceiver extends BroadcastReceiver {

	NotificationManager mNotificationManager;

	@Override
	public void onReceive(Context context, Intent intent) {

		mNotificationManager = Activity_Online_Radio.mNotificationManager;

		String action = intent.getAction();
		int notificationId = intent.getIntExtra("notificationId", 0);

		if (action.equalsIgnoreCase("com.softnep.radiodailymail.ACTION_CLOSE")) {
			Activity_Online_Radio.stop();
				mNotificationManager.cancel(notificationId);
				context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
			    Activity_Online_Radio.closeActivity();

		}

	}

}
