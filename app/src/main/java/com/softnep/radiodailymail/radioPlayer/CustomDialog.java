package com.softnep.radiodailymail.radioPlayer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.softnep.radiodailymail.R;


public class CustomDialog extends Dialog implements View.OnClickListener {

	public Activity c;
	public Dialog d;
	public TextView txtSettings, txtRetry;

	public CustomDialog(Activity a) {
		super(a);
		this.c = a;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog);

		txtSettings = (TextView) findViewById(R.id.txtSettings);
		txtRetry = (TextView) findViewById(R.id.txtRetry);

		txtSettings.setOnClickListener(this);
		txtRetry.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtSettings:
			c.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

			break;
		case R.id.txtRetry:
			Activity_Online_Radio.runTask();
			break;
		default:
			break;
		}
		dismiss();
	}
}
