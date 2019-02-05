package com.example.foldergallery.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.foldergallery.receiver.AlarmManagerBroadcastReceiver;
import com.example.foldergallery.receiver.AlarmManagerBroadcastReceiverEve;
import com.example.foldergallery.receiver.CameraStateReceiver;
import com.example.foldergallery.util.EPreferences;
import com.example.foldergallery.util.Log;
import com.example.foldergallery.util.TypeFaceUtil;
import com.example.foldergallery.util.Utils;
import com.example.foldergallery.view.CheckableRelativeLayout;
import com.example.photobook.R;
import com.exampleqwe.foldergallery.MyApplication;

import java.util.regex.Pattern;

public class SettingsActivity extends AppCompatActivity implements
		OnClickListener {

	private Toolbar toolbar;
	CheckableRelativeLayout rlNotificationVibrate, rlNotificationFlash,
			rlNotificationRing, rlAlert, rlVideoQauality, rlAlertForPhoto;
	SwitchCompat swSettingVibrate, swSettingFlash, swSettingRing,
			sw_setting_daily_alert, scSettingCapturedAlert;
	OnCheckedChangeListener notificationCCL, dailyAlertCCL, capturedAlertCCL;
	OnClickListener rlClickListener;
	EPreferences ePref;
	private TextView tvTypeHeader,tvringtone,tvvibrate,tvFlash,tvalertTitle,tvShowDailyAlert,tvShowCapturedAlert;
	private AlarmManagerBroadcastReceiver alarm;
	private AlarmManagerBroadcastReceiverEve alarmEve;
	private CameraStateReceiver cameraReceiver;
	ImageButton imgbtnNotification, imgbtnAlert;
	TextView tvResolution;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		Utils.setStatusBarColor(SettingsActivity.this);
		bindView();
		init();
		addListener();
	}

	private void bindView() {

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		swSettingVibrate = (SwitchCompat) findViewById(R.id.scSettingVibrate);
		swSettingFlash = (SwitchCompat) findViewById(R.id.scSettingFlesh);
		swSettingRing = (SwitchCompat) findViewById(R.id.scSettingRing);
		sw_setting_daily_alert = (SwitchCompat) findViewById(R.id.scSettingDailyAlert);
		scSettingCapturedAlert = (SwitchCompat) findViewById(R.id.scSettingCapturedAlert);
		rlNotificationVibrate = (CheckableRelativeLayout) findViewById(R.id.rlNotificationVibrate);
		rlNotificationFlash = (CheckableRelativeLayout) findViewById(R.id.rlNotificationFlash);
		rlNotificationRing = (CheckableRelativeLayout) findViewById(R.id.rlNotificationRing);
		rlAlert = (CheckableRelativeLayout) findViewById(R.id.rlAlert);
		rlAlertForPhoto = (CheckableRelativeLayout) findViewById(R.id.rlAlertForPhoto);
		rlVideoQauality = (CheckableRelativeLayout) findViewById(R.id.rlVideoQauality);
		imgbtnNotification = (ImageButton) findViewById(R.id.ibNotification);
		imgbtnAlert = (ImageButton) findViewById(R.id.ibAlert);
		tvResolution = (TextView) findViewById(R.id.tvResolution);
		tvalertTitle = (TextView)findViewById(R.id.tvAlerttitle);
		tvFlash = (TextView)findViewById(R.id.tvflash);
		tvringtone = (TextView)findViewById(R.id.tvringtone);
		tvTypeHeader = (TextView)findViewById(R.id.tvTypeHeader);
		tvShowCapturedAlert = (TextView)findViewById(R.id.tvShowcapturedAlert);
		tvvibrate = (TextView)findViewById(R.id.tvvibrate);
		tvShowDailyAlert = (TextView)findViewById(R.id.tvShowDailyalert);

	}

	private void init() {
		ePref = EPreferences.getInstance(SettingsActivity.this);
		alarm = new AlarmManagerBroadcastReceiver();
		alarmEve = new AlarmManagerBroadcastReceiverEve();
		cameraReceiver = new CameraStateReceiver();
		setNotificationUserSetting();
		setDailyAlertSetting();
		setCapturedAlertSetting();
		setDefaultVideoQuality();
		Utils.applyFontForToolbarTitle(SettingsActivity.this,toolbar);
		tvalertTitle.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
		tvFlash.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
		tvringtone.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
		tvTypeHeader.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
		tvShowCapturedAlert.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
		tvvibrate.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
		tvShowDailyAlert.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());

	}

	private void setDefaultVideoQuality() {
		String strVideoResolution[] = getResources().getStringArray(
				R.array.video_resolution);
		int videoPos = EPreferences.getInstance(getApplicationContext())
				.getInt(EPreferences.PREF_KEY_VIDEO_QUALITY, 2);
		tvResolution.setText(strVideoResolution[videoPos]);
	}

	private void addListener() {
		notificationCCL = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				changeNotificationUserSetting(buttonView, isChecked);
			}
		};
		dailyAlertCCL = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				changeDailyAlertUserSetting(buttonView, isChecked);
			}
		};
		capturedAlertCCL = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				changeCapturedAlertUserSetting(buttonView, isChecked);
			}
		};
		rlClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeSettingsWithId(v);
			}
		};
		swSettingVibrate.setOnCheckedChangeListener(notificationCCL);
		swSettingFlash.setOnCheckedChangeListener(notificationCCL);
		swSettingRing.setOnCheckedChangeListener(notificationCCL);
		sw_setting_daily_alert.setOnCheckedChangeListener(dailyAlertCCL);
		scSettingCapturedAlert.setOnCheckedChangeListener(capturedAlertCCL);
		rlNotificationVibrate.setOnClickListener(rlClickListener);
		rlNotificationFlash.setOnClickListener(rlClickListener);
		rlNotificationRing.setOnClickListener(rlClickListener);
		rlAlert.setOnClickListener(rlClickListener);
		rlAlertForPhoto.setOnClickListener(rlClickListener);
		rlVideoQauality.setOnClickListener(rlClickListener);
		imgbtnNotification.setOnClickListener(this);
		imgbtnAlert.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ibNotification:
//			imgbtnNotification.setImageResource(R.drawable.btn_help_black);
//			Intent intentNoty = new Intent(SettingsActivity.this,
//					BlankScreenActivity.class);
//			intentNoty.putExtra("dialogType", "NOTIFICATION");
//			startActivityForResult(intentNoty, 0);
			Notification_type();
			break;

		case R.id.ibAlert:
//			imgbtnAlert.setImageResource(R.drawable.btn_help_black);
//			Intent intentAlert = new Intent(SettingsActivity.this,
//					BlankScreenActivity.class);
//			intentAlert.putExtra("dialogType", "ALERT");
//			startActivityForResult(intentAlert, 1);
			Alert_type();

			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		this.getWindow().clearFlags(128);
		super.onBackPressed();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	public void setNotificationUserSetting() {
		// VIBRATE
		if (ePref.getBoolean(EPreferences.PREF_KEY_NOTIFICATION_VIBRATE, false)) {
			swSettingVibrate.setChecked(true);
		} else {
			swSettingVibrate.setChecked(false);
		}
		// FLESH
		if (ePref.getBoolean(EPreferences.PREF_KEY_NOTIFICATION_FLESH, false)) {
			swSettingFlash.setChecked(true);
		} else {
			swSettingFlash.setChecked(false);
		}
		// RING
		if (ePref.getBoolean(EPreferences.PREF_KEY_NOTIFICATION_RING, true)) {
			swSettingRing.setChecked(true);
		} else {
			swSettingRing.setChecked(false);
		}
	}

	public void changeNotificationUserSetting(CompoundButton buttonView,
			boolean isChecked) {
		if (buttonView.getId() == R.id.scSettingVibrate) {
			if (isChecked == true) {
				ePref.putBoolean(EPreferences.PREF_KEY_NOTIFICATION_VIBRATE,
						true);
			} else {
				ePref.putBoolean(EPreferences.PREF_KEY_NOTIFICATION_VIBRATE,
						false);
			}
		} else if (buttonView.getId() == R.id.scSettingFlesh) {
			if (isChecked == true) {
				ePref.putBoolean(EPreferences.PREF_KEY_NOTIFICATION_FLESH, true);
			} else {
				ePref.putBoolean(EPreferences.PREF_KEY_NOTIFICATION_FLESH,
						false);
			}
		} else if (buttonView.getId() == R.id.scSettingRing) {
			if (isChecked == true) {
				ePref.putBoolean(EPreferences.PREF_KEY_NOTIFICATION_RING, true);
			} else {
				ePref.putBoolean(EPreferences.PREF_KEY_NOTIFICATION_RING, false);
			}
		}
	}

	public void setDailyAlertSetting() {
		// ALERT SETTINGS
		if (ePref.getBoolean(EPreferences.PREF_KEY_WANT_DAILY_ALERT, true)) {
			sw_setting_daily_alert.setChecked(true);
		} else {
			sw_setting_daily_alert.setChecked(false);
		}
	}

	public void changeDailyAlertUserSetting(CompoundButton buttonView,
			boolean isChecked) {
		if (buttonView.getId() == R.id.scSettingDailyAlert) {
			if (isChecked == true) {
				ePref.putBoolean(EPreferences.PREF_KEY_WANT_DAILY_ALERT, true);
				alarm.setOnetimeTimer(getApplicationContext());
				alarmEve.setOnetimeTimer(getApplicationContext());
			} else {
				ePref.putBoolean(EPreferences.PREF_KEY_WANT_DAILY_ALERT, false);
				// Utils.setDailyAlertOff(getApplicationContext());
				// alarm.CancelAlarm(getApplicationContext());
			}
		}
	}

	public void setCapturedAlertSetting() {
		// CAPTURED ALERT SETTINGS
		if (ePref.getBoolean(EPreferences.PREF_KEY_WANT_CAPTURED_ALERT, true)) {
			scSettingCapturedAlert.setChecked(true);
		} else {
			scSettingCapturedAlert.setChecked(false);
		}
	}

	public void changeCapturedAlertUserSetting(CompoundButton buttonView,
			boolean isChecked) {
		if (buttonView.getId() == R.id.scSettingCapturedAlert) {
			if (isChecked == true) {
				ePref.putBoolean(EPreferences.PREF_KEY_WANT_CAPTURED_ALERT,
						true);
			} else {
				ePref.putBoolean(EPreferences.PREF_KEY_WANT_CAPTURED_ALERT,
						false);
			}
		}
	}

	public void changeSettingsWithId(View v) {
		if (v.getId() == R.id.rlNotificationVibrate) {
			// VIBRATE
			if (ePref.getBoolean(EPreferences.PREF_KEY_NOTIFICATION_VIBRATE,
					false)) {
				swSettingVibrate.setChecked(false);
			} else {
				swSettingVibrate.setChecked(true);
			}
		} else if (v.getId() == R.id.rlNotificationFlash) {
			// FLESH
			if (ePref.getBoolean(EPreferences.PREF_KEY_NOTIFICATION_FLESH,
					false)) {
				swSettingFlash.setChecked(false);
			} else {
				swSettingFlash.setChecked(true);
			}
		} else if (v.getId() == R.id.rlNotificationRing) {
			// RING
			if (ePref.getBoolean(EPreferences.PREF_KEY_NOTIFICATION_RING, true)) {
				swSettingRing.setChecked(false);
			} else {
				swSettingRing.setChecked(true);
			}
		} else if (v.getId() == R.id.rlAlert) {
			// DAILY ALERT
			if (ePref.getBoolean(EPreferences.PREF_KEY_WANT_DAILY_ALERT, true)) {
				sw_setting_daily_alert.setChecked(false);
			} else {
				sw_setting_daily_alert.setChecked(true);
			}
		} else if (v.getId() == R.id.rlAlertForPhoto) {
			// DAILY ALERT
			if (ePref.getBoolean(EPreferences.PREF_KEY_WANT_CAPTURED_ALERT,
					true)) {
				scSettingCapturedAlert.setChecked(false);
			} else {
				scSettingCapturedAlert.setChecked(true);
			}
		} else if (v.getId() == R.id.rlVideoQauality) {
			String strVideoResolution[] = getResources().getStringArray(
					R.array.video_resolution);
			EPreferences.getInstance(getApplicationContext()).getInt(
					EPreferences.PREF_KEY_VIDEO_QUALITY, 2);
			videoSettings(
					strVideoResolution,
					"Video Quality",
					EPreferences.PREF_KEY_VIDEO_QUALITY,
					EPreferences.getInstance(getApplicationContext()).getInt(
							EPreferences.PREF_KEY_VIDEO_QUALITY, 2));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int arg1, Intent arg2) {
		super.onActivityResult(requestCode, arg1, arg2);
		if (requestCode == 0)
			imgbtnNotification.setImageResource(R.drawable.btn_help);
		else
			imgbtnAlert.setImageResource(R.drawable.btn_help);
	}

	private void videoSettings(final String items[], final String title,
			final String keyPref, final int selectItem) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SettingsActivity.this, R.style.Theme_MovieMaker_AlertDialog);
		builder.setTitle(title);
		builder.setCancelable(false);

		builder.setSingleChoiceItems(items, selectItem,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						if (item == 0 || item == 1) {
							warnnigAlertDialog(items, title, keyPref,
									selectItem, item);
							dialog.dismiss();

						} else {
							EPreferences.getInstance(SettingsActivity.this)
									.putInt(keyPref, item);
							setDefaultVideoQuality();
							setVideoHeightWidth();
							dialog.dismiss();
						}
					}
				});
		builder.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});

		AlertDialog alert = builder.create();
		alert.show();
		// AppCompatButton nbutton = (AppCompatButton) alert
		// .getButton(DialogInterface.BUTTON_NEGATIVE);
		// nbutton.setTextColor(getColor(R.color.white));
	}

	private void warnnigAlertDialog(final String items[], String title,
			final String keyPref, int selectItem, final int clickedItem) {
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this, R.style.AppAlertDialog);
		alertDialogBuilder.setTitle("Warnning!");
		alertDialogBuilder
				.setMessage(
						"You selected video quality " + items[clickedItem]
								+ ",It may take more time to create.")
				.setCancelable(false)
				.setPositiveButton(
						"Proceed",
						(DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
							public void onClick(
									final DialogInterface dialogInterface,
									final int n) {
								EPreferences.getInstance(SettingsActivity.this)
										.putInt(keyPref, clickedItem);
								setDefaultVideoQuality();
								setVideoHeightWidth();
								dialogInterface.dismiss();
							}
						})
				.setNegativeButton(
						(CharSequence) "Select Another",
						(DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
							public void onClick(
									final DialogInterface dialogInterface,
									final int n) {
								dialogInterface.dismiss();
								String strVideoResolution[] = getResources()
										.getStringArray(
												R.array.video_resolution);
								EPreferences.getInstance(
										getApplicationContext()).getInt(
										EPreferences.PREF_KEY_VIDEO_QUALITY, 2);
								videoSettings(
										strVideoResolution,
										"Video Quality",
										EPreferences.PREF_KEY_VIDEO_QUALITY,
										EPreferences
												.getInstance(
														getApplicationContext())
												.getInt(EPreferences.PREF_KEY_VIDEO_QUALITY,
														2));
							}
						});
		alertDialogBuilder.create().show();
	}

	private void setVideoHeightWidth() {
		String strVideoWidthHeight[] = getResources().getStringArray(
				R.array.video_height_width);
		int pos = EPreferences.getInstance(getApplicationContext()).getInt(
				EPreferences.PREF_KEY_VIDEO_QUALITY, 2);
		String strTemp = strVideoWidthHeight[pos];
		Log.d("TAG", "Setting VideoQuality value is:- " + strTemp);
		MyApplication.VIDEO_WIDTH = Integer.parseInt(strTemp.split(Pattern
				.quote("*"))[0]);
		MyApplication.VIDEO_HEIGHT = Integer.parseInt(strTemp.split(Pattern
				.quote("*"))[1]);
	}



	AlertDialog b;
	public void Notification_type() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AppAlertDialogSetting);
		LayoutInflater inflater = this.getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.notification_type_dialogue, null);
		dialogBuilder.setView(dialogView);
		dialogBuilder.setCancelable(false);

		Button gotit = (Button)dialogView.findViewById(R.id.btnGotitnoti);
		 b = dialogBuilder.create();
		gotit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				b.dismiss();
			}
		});
		b.show();
	}



	public void Alert_type() {
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AppAlertDialogSetting);
		LayoutInflater inflater = this.getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.alert_type_dialogue, null);
		dialogBuilder.setView(dialogView);
		dialogBuilder.setCancelable(false);
		Button gotit = (Button)dialogView.findViewById(R.id.btnGotitalert);
		b = dialogBuilder.create();
		gotit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				b.dismiss();
			}
		});

//		gotit.setOnCheckedChangeListener(new CheckableRelativeLayout.OnCheckedChangeListener() {
//

		b = dialogBuilder.create();
		b.show();
	}


}
