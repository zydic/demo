package com.example.foldergallery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.photobook.R;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000;
    RelativeLayout rlLayout;
    private Animation alphaAnim1, alphaAnim2, alphaAnim3, alphaAnim4;
    ;
    private ImageView ivImg1, ivImg2, ivImg3, ivImg4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        bindView();
        init();
    }

    private void init() {
        new Handler().postDelayed(new Progress(), (long) SPLASH_TIME_OUT);
        splAnimation();
    }

    private void bindView() {
        rlLayout = (RelativeLayout) findViewById(R.id.ll_main);
        ivImg1 = (ImageView) findViewById(R.id.ivImg1);
        ivImg2 = (ImageView) findViewById(R.id.ivImg2);
        ivImg3 = (ImageView) findViewById(R.id.ivImg3);
        ivImg4 = (ImageView) findViewById(R.id.ivImg4);
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        System.gc();
        super.onDestroy();
    }

    class Progress implements Runnable {

        public void run() {
            finish();
            startActivity(new Intent(SplashActivity.this,
                    LauncherActivity.class));
        }
    }

    private void splAnimation() {
        alphaAnim1 = AnimationUtils.loadAnimation(SplashActivity.this,
                R.anim.alpha_1);
        alphaAnim2 = AnimationUtils.loadAnimation(SplashActivity.this,
                R.anim.alpha_2);
        alphaAnim3 = AnimationUtils.loadAnimation(SplashActivity.this,
                R.anim.alpha_3);
        alphaAnim4 = AnimationUtils.loadAnimation(SplashActivity.this,
                R.anim.alpha_4);

        ivImg1.setVisibility(View.VISIBLE);
        ivImg1.startAnimation(alphaAnim1);
        alphaAnim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivImg2.setVisibility(View.VISIBLE);
                ivImg2.startAnimation(alphaAnim2);
            }
        });
        alphaAnim2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivImg3.setVisibility(View.VISIBLE);
                ivImg3.startAnimation(alphaAnim3);
            }
        });
        alphaAnim3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivImg4.setVisibility(View.VISIBLE);
                ivImg4.startAnimation(alphaAnim4);
            }
        });
        alphaAnim4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
    }

}
