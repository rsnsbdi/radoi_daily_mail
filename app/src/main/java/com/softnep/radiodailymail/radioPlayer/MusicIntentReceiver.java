package com.softnep.radiodailymail.radioPlayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class MusicIntentReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(
				android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
			// signal your service to stop playback
				if (Activity_Online_Radio.playerStarted) {
					Activity_Online_Radio.stop();
					Toasty.info(context, "Headphones unplugged", Toast.LENGTH_SHORT).show();
				}
		}
	}

}
