package com.example.foldergallery.activity;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.photobook.R;
import com.example.foldergallery.util.EPreferences;

public class CustomPermissionActivity extends AppCompatActivity {

	SwitchCompat switchTut;
	ImageView imageHand;
	Button btnGotIt;
	private AnimatorSet g;
	private TextView tvName;
	EPreferences ePref;
	ComponentName SecurityComponentName = null;
	String AppName = "";

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			final Window window = this.getWindow();
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		setContentView(R.layout.tutorial_activity);
		ePref = EPreferences.getInstance(CustomPermissionActivity.this);
		switchTut = (SwitchCompat) findViewById(R.id.scSwitchTut);
		imageHand = (ImageView) findViewById(R.id.ivImageHand);
		btnGotIt = (Button) findViewById(R.id.btnGotIt);
		tvName = (TextView) findViewById(R.id.tvName);
		btnGotIt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent MainMenuIntent = getIntent();
				SecurityComponentName = (ComponentName) MainMenuIntent
						.getParcelableExtra("PACKAGE");
				AppName = MainMenuIntent.getStringExtra("APPNAME");
				if (SecurityComponentName != null) {
					Intent intent = new Intent();
					intent.setComponent(SecurityComponentName);
					startActivity(intent);
				} else {
					// ((MainMenuActivity)MainMenuContext).runApp(Utils.autostart_app_name,1);
				}
				ePref.putBoolean("HasAutoStartPermission", true);
				finish();
			}
		});
		staranimation();
	}

	private void staranimation() {

		imageHand.setAlpha(0.0F);
		switchTut.setAlpha(0.0F);
		tvName.setAlpha(0.0F);
		int i = b(this, 125.0F);
		imageHand.setTranslationY(i);
		imageHand.setTranslationX(-i / 2);
		ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(imageHand,
				"alpha", new float[] { 1.0F });
		ObjectAnimator tvNameAlpha = ObjectAnimator.ofFloat(tvName, "alpha",
				new float[] { 1.0F });
		ObjectAnimator switchAlpha = ObjectAnimator.ofFloat(switchTut, "alpha",
				new float[] { 1.0F });

		localObjectAnimator1.setDuration(250L);
		tvNameAlpha.setDuration(250L);
		switchAlpha.setDuration(250L);

		ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(imageHand,
				"translationY", new float[] { 0.0F });
		ObjectAnimator localObjectAnimator3 = ObjectAnimator.ofFloat(imageHand,
				"translationX", new float[] { 0.0F });
		ObjectAnimator mRotateAnimation = ObjectAnimator.ofFloat(this,
				"rotationX", -60, 0);
		localObjectAnimator2.setDuration(600L);
		localObjectAnimator3.setDuration(600L);
		mRotateAnimation.setDuration(600L);
		mRotateAnimation.setInterpolator(new AccelerateInterpolator());

		g = new AnimatorSet();
		g.playTogether(new Animator[] { switchAlpha, tvNameAlpha,
				localObjectAnimator1, localObjectAnimator2,
				localObjectAnimator3 });
		g.setStartDelay(700L);
		g.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator arg0) {
				switchTut.setChecked(false);
			}

			@Override
			public void onAnimationRepeat(Animator arg0) {
				switchTut.setChecked(false);
			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				switchTut.setChecked(true);
			}

			@Override
			public void onAnimationCancel(Animator arg0) {
			}

		});
		g.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(0, 0);
	}

	public static int b(Context paramContext, float paramFloat) {
		return (int) (paramContext.getResources().getDisplayMetrics().density
				* paramFloat + 0.5F);
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
	}
}
