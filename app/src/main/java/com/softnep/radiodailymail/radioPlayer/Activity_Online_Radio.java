package com.softnep.radiodailymail.radioPlayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;


import com.google.common.util.concurrent.ExecutionError;
import com.softnep.radiodailymail.R;
import com.softnep.radiodailymail.activity.Activity_NavigationDrawer;
import com.softnep.radiodailymail.helper.ConnectionDetector;
import com.spoledge.aacdecoder.MultiPlayer;
import com.spoledge.aacdecoder.PlayerCallback;


import es.dmoral.toasty.Toasty;

public class Activity_Online_Radio extends AppCompatActivity implements OnClickListener {

	private static final String LOG = "DMFM log";
	private static final String TAG = "Activity_Online_Radio";

	private static MultiPlayer multiPlayer;
	private static Handler uiHandler;
	private static TextView txtStatus;
	public static boolean playerStarted;
	public static boolean wasPlayingBeforePhoneCall = false;
	public static boolean isStarted = false;
	public static int NOTIFICATION_ID = 98;
	public static ConnectionDetector cd;
	public static int result;
	public static Toolbar toolbar;

	private final static String URL_RADIO = "http://streaming.softnep.net:8143/";

	public static RemoteViews notification_view;

	public static Context context = null;

	public static ImageView btnPlay, btnPause, btnVolumeDown, btnVolumeUp;

	boolean volumeUpTouching = false;
	boolean volumeDownTouching = false;

	private static ProgressBar progressBar;

	private SeekBar volumeSeekbar = null;
	private static AudioManager audioManager = null;

	public static NotificationManager mNotificationManager;
	private static Notification notification;

	private PhoneStateListener phoneStateListener;
	private TelephonyManager mgr;
	private static OnAudioFocusChangeListener audioFocusChangeListener;

	LinearLayout seekbar_layout;

	private Vibrator myVib;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_online_radio);

		super.onCreate(savedInstanceState);
		context = this;
		cd=new ConnectionDetector(this);
		// runTask();

		initializeUIComponents();
		startService(new Intent(this, KillNotificationsService.class));

		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


		audioFocusChangeListener = new OnAudioFocusChangeListener() {

			@Override
			public void onAudioFocusChange(int focusChange) {
				switch (focusChange) {
					case AudioManager.AUDIOFOCUS_GAIN:

						Log.d("FOCUS", "focus gained now");

						if (!isStarted && wasPlayingBeforePhoneCall)
							start();
						// }
						break;

					case AudioManager.AUDIOFOCUS_LOSS:

						stop();
						removeNotification();
						closeActivity();
						break;

					case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:

						stop();
						removeNotification();
						break;

					case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:

						break;
				}

			}
		};

		result = audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

		if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

			if (!isStarted)
				start();
		} else if (result == AudioManager.AUDIOFOCUS_REQUEST_FAILED){
			// audio focus cannot be granted. cannot play audio
			progressBar.setVisibility(View.GONE);
			btnPlay.setVisibility(View.VISIBLE);
			btnPlay.setEnabled(true);
			Toasty.error(getApplicationContext(), "Cannot play radio now. Try again.", Toast.LENGTH_SHORT).show();
		}

		// handle play/pause of radio on phone calls

		phoneStateListener = new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				if (state == TelephonyManager.CALL_STATE_RINGING) {
					// Incoming call: Pause music
					wasPlayingBeforePhoneCall = playerStarted;
					stop();

				} else if (state == TelephonyManager.CALL_STATE_IDLE) {
					// Not in call: Play music
					if (wasPlayingBeforePhoneCall) {

					}
				} else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
					// A call is dialing, active or on hold
					wasPlayingBeforePhoneCall = playerStarted;
					stop();
				}
				super.onCallStateChanged(state, incomingNumber);
			}
		};

		mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		if (mgr != null) {
			mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

		}

	}

	public static void runTask() {
		if (cd.isNetworkAvailable() || cd.isDataAvailable()) {
//			isOnline = true;
		}

		if(!cd.isNetworkAvailable() || !cd.isDataAvailable()){

//			isOnline = false;
			CustomDialog cd = new CustomDialog((Activity) context);
			cd.show();
		}

	}

	/*private void browse(String url) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_BROWSABLE);
		intent.setData(Uri.parse(url));
		startActivity(intent);
	}*/

	public static void closeActivity() {
		((Activity) context).finish();
		System.exit(0);
	}
	public static void exit(){
		System.exit(0);
	}

	private void initializeUIComponents() {

		txtStatus = (TextView) findViewById(R.id.txtStatus);

		uiHandler = new Handler();

		myVib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		toolbar=(Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		seekbar_layout = (LinearLayout) findViewById(R.id.seekbar_layout);

		btnPlay = (ImageView) findViewById(R.id.btnPlay);
		btnPlay.setOnClickListener(this);

		btnPause = (ImageView) findViewById(R.id.btnPause);
		btnPause.setOnClickListener(this);
		btnPause.setVisibility(View.GONE);

		btnVolumeUp = (ImageView) findViewById(R.id.btnVolumeUp);
		btnVolumeUp.setOnClickListener(this);

		btnVolumeUp.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				volumeUpTouching = true;
				Thread volumeUpThread = new Thread() {
					public void run() {
						while (volumeUpTouching) {
							int index = volumeSeekbar.getProgress();
							volumeSeekbar.setProgress(index + 1);
							try {
								sleep(50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				};
				volumeUpThread.start();
				return false;
			}
		});

		btnVolumeDown = (ImageView) findViewById(R.id.btnVolumeDown);
		btnVolumeDown.setOnClickListener(this);

		btnVolumeDown.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				volumeDownTouching = true;
				Thread volumeDownThread = new Thread() {
					public void run() {
						while (volumeDownTouching) {
							int index = volumeSeekbar.getProgress();
							volumeSeekbar.setProgress(index - 1);
							try {
								sleep(50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				};
				volumeDownThread.start();
				return false;
			}
		});

		progressBar = (ProgressBar) findViewById(R.id.progressBarCircular);
		progressBar.setVisibility(View.GONE);

		volumeSeekbar = (SeekBar) findViewById(R.id.volumeSeekbar);
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		volumeSeekbar.setMax(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		volumeSeekbar.setProgress(audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC));

		volumeSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onProgressChanged(SeekBar arg0, int progress,
					boolean arg2) {
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						progress, 0);
			}
		});

		// /converting icy headers to http
		try {
			java.net.URL
					.setURLStreamHandlerFactory(new java.net.URLStreamHandlerFactory() {
						public java.net.URLStreamHandler createURLStreamHandler(
								String protocol) {
							Log.d(LOG,
									"Asking for stream handler for protocol: '"
											+ protocol + "'");
							if ("icy".equals(protocol))
								return new com.spoledge.aacdecoder.IcyURLStreamHandler();
							return null;
						}
					});
		} catch (Throwable t) {
			Log.w(LOG,
					"Cannot set the ICY URLStreamHandler - maybe already set ? - "
							+ t);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			int index = volumeSeekbar.getProgress();
			volumeSeekbar.setProgress(index + 1);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			int index = volumeSeekbar.getProgress();
			volumeSeekbar.setProgress(index - 1);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	@Override
	protected void onResume() {
		Log.d("onResume", "onResume called");
		super.onResume();
	}
	
	@Override
	protected void onPause() {
	Log.d("onPause", "onPaused called");
	super.onPause();
	}
	
	@Override
	protected void onStop() {
	Log.d("onStop", "onStop called");
	super.onStop();
	}

	@Override
	public void onBackPressed() {

		if (playerStarted) {
			Toasty.info(getApplicationContext(),
					getResources().getString(R.string.long_des) + "\nnow playing in background.", Toast.LENGTH_SHORT)
					.show();
			moveTaskToBack(true);

			Intent intent=new Intent(Activity_Online_Radio.this, Activity_NavigationDrawer.class);
			intent.putExtra("radio_back",8);
			startActivity(intent);
			//this.finish();

		} else {

			moveTaskToBack(true);
			Intent intent=new Intent(Activity_Online_Radio.this, Activity_NavigationDrawer.class);
			intent.putExtra("radio_back",8);
			startActivity(intent);
			//this.finish();
		}
	}

	public void onClick(View v) {

		if (v == btnPlay) {
			// startPlaying();
			start();
		} else if (v == btnPause) {
			// pausePlaying();
			stop();
		} else if (v == btnVolumeDown) {
			int index = volumeSeekbar.getProgress();
			volumeSeekbar.setProgress(index - 1);
			volumeDownTouching = false;
		} else if (v == btnVolumeUp) {
			int index = volumeSeekbar.getProgress();
			volumeSeekbar.setProgress(index + 1);
			volumeUpTouching = false;
		}
	}

	@Override
	protected void onDestroy() {

		if (multiPlayer != null && playerStarted) {
			removeNotification();
			stop();
		}

		if (mgr != null) {
			mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
		}

		Log.d("MP", "destroying app");

		if (audioManager != null) {
			audioManager = null;
		}
		super.onDestroy();
	}

	static PlayerCallback mPlayerCallback = new PlayerCallback() {

		public void playerStarted() {
			uiHandler.post(new Runnable() {
				public void run() {

					btnPlay.setVisibility(View.GONE);
					btnPause.setVisibility(View.VISIBLE);
					progressBar.setVisibility(View.GONE);
					playerStarted = true;
					setNotification();
				}
			});
		}

		/**
		 * This method is called periodically by PCMFeed.
		 * 
		 * @param isPlaying
		 *            false means that the PCM data are being buffered, but the
		 *            audio is not playing yet
		 * 
		 * @param audioBufferSizeMs
		 *            the buffered audio data expressed in milliseconds of
		 *            playing
		 * @param audioBufferCapacityMs
		 *            the total capacity of audio buffer expressed in
		 *            milliseconds of playing
		 */
		public void playerPCMFeedBuffer(final boolean isPlaying,
				final int audioBufferSizeMs, final int audioBufferCapacityMs) {

			uiHandler.post(new Runnable() {
				public void run() {

					if (isPlaying) {
						txtStatus.setText(R.string.onair);
					}
				}
			});
		}

		public void playerStopped(final int perf) {
			uiHandler.post(new Runnable() {
				public void run() {

					btnPlay.setVisibility(View.VISIBLE);
					btnPause.setVisibility(View.GONE);

					playerStarted = false;
					txtStatus.setText(R.string.radio_stop);

					removeNotification();
				}
			});
		}

		public void playerException(final Throwable t) {
			uiHandler.post(new Runnable() {
				public void run() {
						Log.d(TAG, "run: Player Exception" + t.toString());
						txtStatus.setText(R.string.radio_off);
						btnPlay.setVisibility(View.VISIBLE);
						progressBar.setVisibility(View.GONE);

						if (playerStarted)
							playerStopped(0);
				}
			});
		}

		public void playerMetadata(final String key, final String value) {
//
		}

		public void playerAudioTrackCreated(AudioTrack atrack) {
		}
	};

	public static void start() {
		audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
		stop();
		runTask();
		isStarted = true;
		txtStatus.setText(R.string.buffering);
		progressBar.setVisibility(View.VISIBLE);
		btnPlay.setVisibility(View.GONE);

		multiPlayer = new MultiPlayer(mPlayerCallback, 1500, 700);

		multiPlayer.playAsync(context.getResources().getString(R.string.radio_url));
	}

	public static void stop() {
		isStarted = false;
		if (multiPlayer != null) {
			multiPlayer.stop();
			multiPlayer = null;
		}
	}

	@SuppressLint("NewApi")
	private static void setNotification() {
		
		Context context = Activity_Online_Radio.context;

		notification_view = new RemoteViews(context.getPackageName(), R.layout.notification_view);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(Activity_Online_Radio.context);

		builder.setTicker("Now Playing\n" + context.getResources().getString(R.string.long_des));
		builder.setSmallIcon(R.mipmap.test_launcher);
		
		// This intent is fired when notification is clicked
		Intent intent = new Intent(context, Activity_Online_Radio.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

		// Set the intent that will fire when the user taps the notification.
		builder.setContentIntent(pendingIntent);
		// Large icon appears on the left of the notification
		 builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.test_launcher));

		Intent buttonIntent = new Intent("com.softnep.radiodailymail.ACTION_CLOSE");
		buttonIntent.putExtra("notificationId", NOTIFICATION_ID);
		PendingIntent cancelNotifIntent = PendingIntent.getBroadcast(context,
				0, buttonIntent, 0);
		notification_view.setOnClickPendingIntent(R.id.iv_close, cancelNotifIntent);
		builder.setContent(notification_view);
		
		mNotificationManager = (NotificationManager) context
				.getSystemService(NOTIFICATION_SERVICE);

		// Will display the notification in the notification barff
		notification = builder.build();
		notification.flags = Notification.FLAG_ONGOING_EVENT;

		mNotificationManager.notify(NOTIFICATION_ID, notification);
	
	
	}

	private static void removeNotification() {
		if (mNotificationManager != null)
			mNotificationManager.cancel(NOTIFICATION_ID);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id=item.getItemId();
		if(id==android.R.id.home)
		{
			if (playerStarted) {
				Toasty.info(getApplicationContext(),
						getResources().getString(R.string.long_des) + "\nnow playing in background.", Toast.LENGTH_SHORT)
						.show();
			    	moveTaskToBack(true);

				Intent intent=new Intent(Activity_Online_Radio.this, Activity_NavigationDrawer.class);
				intent.putExtra("radio_back",8);
				startActivity(intent);
				//this.finish();
			}

			else {
				moveTaskToBack(true);
				Intent intent=new Intent(Activity_Online_Radio.this, Activity_NavigationDrawer.class);
				intent.putExtra("radio_back",8);
				startActivity(intent);
				//this.finish();
			}
		}
		return true;
	}


}
