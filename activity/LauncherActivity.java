package com.example.foldergallery.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.foldergallery.receiver.CameraStateService;
import com.example.foldergallery.receiver.DemoService;
import com.example.foldergallery.util.ActivityAnimUtil;
import com.example.foldergallery.util.EPreferences;
import com.example.foldergallery.util.PermissionModelUtil;
import com.example.foldergallery.util.TypeFaceUtil;
import com.example.foldergallery.util.Utils;
import com.example.photobook.MyCreation.MyCreationPhotobookActivity;
import com.example.photobook.R;
import com.exampleqwe.foldergallery.MyApplication;

public class LauncherActivity extends AppCompatActivity implements
        OnClickListener {
    PermissionModelUtil modelUtil;
    EPreferences ePref;
    ComponentName SecurityComponentName = null;
    private static final int PERMISSIONS_REQUEST = 922;
    private boolean isOpenFisrtTime = false;
    Animation zoomin, zoomout;
    ImageView ivcreatepb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launcher);
        ePref = EPreferences.getInstance(LauncherActivity.this);
        bindView();
        init();
        addListener();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            checkDrawOverlayPermission();

        }
        boolean need_permission = MyApplication.getInstance().runApp(
                Utils.autostart_app_name, 0);

        if (need_permission == true) {
            if (!check_permission()) {
                permissionDialog();

            }
        }

            startService(new Intent(LauncherActivity.this, CameraStateService.class));
        startService(new Intent(LauncherActivity.this, DemoService.class));

    }

    public boolean check_permission() {
        boolean has_permission = ePref.getBoolean("HasAutoStartPermission",
                false);
        if (!has_permission) {
            return false;
        } else {
            return true;
        }
    }

    private void permissionDialog() {
        final String manufacturer = Build.MANUFACTURER;
        if (manufacturer.equals("Xiaomi")) {
            SecurityComponentName = new ComponentName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.autostart.AutoStartManagementActivity");
            Utils.autostart_app_name = "Security";
        } else if (manufacturer.equals("asus")) {
            SecurityComponentName = new ComponentName("com.asus.mobilemanager",
                    "com.asus.mobilemanager.autostart.AutoStartActivity");
            Utils.autostart_app_name = "Auto-start Manager";
        }
        Intent DialogIntent = new Intent(LauncherActivity.this,
                CustomPermissionActivity.class);
        DialogIntent.putExtra("PACKAGE", SecurityComponentName);
        DialogIntent.putExtra("APPNAME", Utils.autostart_app_name);
        startActivity(DialogIntent);
    }

    private void bindView() {
        ivcreatepb= (ImageView) findViewById(R.id.ivcreatepb);
        zoomin = AnimationUtils.loadAnimation(this, R.anim.zoomin);
        zoomout = AnimationUtils.loadAnimation(this, R.anim.zoomout);
        ivcreatepb.setAnimation(zoomin);
        ivcreatepb.setAnimation(zoomout);
        zoomin.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                ivcreatepb.startAnimation(zoomout);

            }
        });
        zoomout.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                ivcreatepb.startAnimation(zoomin);

            }
        });
    }

    private void init() {
        Utils.isVideoCreationRunning = true;
        modelUtil = new PermissionModelUtil(this);
        if (modelUtil.needPermissionCheck()) {
            permissionDialogSecond();
        } else {
//            MyApplication.getInstance().getFolderList();
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(307);
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (!modelUtil.needPermissionCheck()) {
            MyApplication.getInstance().getFolderList();
        }
        if (requestCode != PERMISSIONS_REQUEST) {
            return;
        }
        if (grantResults[0] == 0) {
            if (Build.VERSION.SDK_INT < 23) {
                return;
            }
            if (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0
                    || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                permissionDialogSecond();
            }
        }


        else if (Build.VERSION.SDK_INT < 23) {

        }


        else {
            if (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0
                    || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                permissionDialogSecond();
            }
        }
    }

    private void addListener() {
        findViewById(R.id.ivmycreatedpb).setOnClickListener(this);
        ivcreatepb.setOnClickListener(this);
        findViewById(R.id.tvSetting).setOnClickListener(this);
//		findViewById(R.id.tvInvite).setOnClickListener(this);
//		findViewById(R.id.tvLikeUs).setOnClickListener(this);
//		findViewById(R.id.tvRateUs).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivcreatepb:
                if (!modelUtil.needPermissionCheck()) {
                    Log.e("SIZE", ""
                            + MyApplication.getInstance().getAllFolder().size());
                    if (MyApplication.getInstance().getAllFolder().size() > 0) {
                        MyApplication.isBreak = false;
                        ActivityAnimUtil.startActivitySafely(v,
                                new Intent(LauncherActivity.this,
                                        ImageSelectionActivity.class));
                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                "No images found in device\nPlease add images in sdCard",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    permissionDialogSecond();
                }
                break;

            case R.id.ivmycreatedpb:
                if (!modelUtil.needPermissionCheck()) {
                    ActivityAnimUtil.startActivitySafely(v, new Intent(
                            LauncherActivity.this,
                            MyCreationPhotobookActivity.class));
                } else {
                    permissionDialogSecond();
                }
                break;

            case R.id.tvSetting:
                if (!modelUtil.needPermissionCheck()) {
                    ActivityAnimUtil.startActivitySafely(v, new Intent(
                            LauncherActivity.this, SettingsActivity.class));
                } else {
                    permissionDialogSecond();
                }
                break;

//		case R.id.tvInvite:
//			Toast.makeText(LauncherActivity.this, "Invite You friends",
//					Toast.LENGTH_SHORT).show();
//			Intent sharingIntent = new Intent(
//					android.content.Intent.ACTION_SEND);
//			sharingIntent.setType("text/plain");
//			String shareBody = "Hey I am using this awesome app MOVIE MAKER !\n Create your own movie with this app./n MAKE YOUR MEMORIES MORE MEMORABLE./nWelcome Visit : https://http://play.google.com/";
//			sharingIntent
//					.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//			startActivity(Intent.createChooser(sharingIntent, "Share via"));
//			break;
//
//		case R.id.tvLikeUs:
//			Toast.makeText(LauncherActivity.this, "Like Social Page",
//					Toast.LENGTH_SHORT).show();
//			break;
//
//		case R.id.tvRateUs:
//			Toast.makeText(LauncherActivity.this, "Rate App",
//					Toast.LENGTH_SHORT).show();
//			break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        rateUS_alert_Dialog();
    }

    private void rateUS_alert_Dialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.AppAlertDialogSetting)
                .setTitle("Rate 5 Stars before exit")
                .setMessage(("Support us to serve you better. Rate 5 stars before exit"))
                .setNegativeButton(getResources().getString(R.string.exit),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        })
                .setPositiveButton(("Give us 5 star"),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                finish();
                                startActivity(new Intent(
                                        "android.intent.action.VIEW", Uri
                                        .parse("market://details?id="
                                                + getPackageName())));

                            }
                        })
                .setNeutralButton(
                        R.string.no,
                        (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(
                                    final DialogInterface dialogInterface,
                                    final int n) {
                                dialogInterface.cancel();

                            }
                        })


                .create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_;

        alertDialog.show();
        TextView textView = (TextView) alertDialog.getWindow().findViewById(android.R.id.message);
        TextView alertTitle = (TextView) alertDialog.getWindow().findViewById(R.id.alertTitle);
        Button button1 = (Button) alertDialog.getWindow().findViewById(android.R.id.button1);
        Button button2 = (Button) alertDialog.getWindow().findViewById(android.R.id.button2);
        Button button3 = (Button) alertDialog.getWindow().findViewById(android.R.id.button3);
        textView.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
        alertTitle.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
        button1.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
        button2.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
        button3.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
        button1.setTextColor(getResources().getColor(R.color.white));
        button2.setTextColor(getResources().getColor(R.color.white));
        button3.setTextColor(getResources().getColor(R.color.white));
        textView.setTextColor(getResources().getColor(R.color.white));
    }


    public void checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(LauncherActivity.this)) {
                AlertDialog.Builder alertDialogDeleteAllSMS = new AlertDialog.Builder(
                        LauncherActivity.this,R.style.AppAlertDialogSetting);
                alertDialogDeleteAllSMS.setTitle("Information");
                alertDialogDeleteAllSMS
                        .setMessage("Please enable system overlay permission to use photobook features.");
                alertDialogDeleteAllSMS.setCancelable(false);
                alertDialogDeleteAllSMS.setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    final DialogInterface dialogInterface,
                                    final int n) {
                                dialogInterface.dismiss();

                                Intent intent = new Intent(
                                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:" + getPackageName()));
                                startActivityForResult(intent, 6);


                            }
                        });

                final AlertDialog create = alertDialogDeleteAllSMS.create();

                create.show();

                TextView textView = (TextView) create.getWindow().findViewById(android.R.id.message);
                TextView alertTitle = (TextView) create.getWindow().findViewById(R.id.alertTitle);
                Button button1 = (Button) create.getWindow().findViewById(android.R.id.button1);
                textView.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
                alertTitle.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
                button1.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
                button1.setTextColor(getResources().getColor(R.color.white));
                textView.setTextColor(getResources().getColor(R.color.white));
//                alertTitle.setTextColor(getResources().getColor(R.color.colorAccent));
//                button1.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        }
    }

    private void permissionDialogSecond() {
        TextView tvpermission,tvstorage,tvpermissiontips,tvgrantpermission,tvPhone;
        final Dialog dialog = new Dialog(LauncherActivity.this, R.style.AppAlertDialogSetting);
        dialog.setContentView(R.layout.permissionsdialog);
        tvPhone= (TextView)dialog.findViewById(R.id.tvPhone);
        tvgrantpermission = (TextView)dialog.findViewById(R.id.tvgrantpermission);
        tvstorage = (TextView)dialog.findViewById(R.id.tvstorage);
        tvpermissiontips = (TextView)dialog.findViewById(R.id.tvpermissiontips);
        tvpermission = (TextView)dialog.findViewById(R.id.tvpermission);
        tvgrantpermission.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
        tvstorage.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
        tvpermissiontips.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
        tvpermission.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
        tvPhone.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());

        dialog.setTitle(getResources().getString(R.string.permission)
                .toString());



        dialog.setCancelable(false);
        Button ok = (Button) dialog.findViewById(R.id.ok);
        ok.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
                ok.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            LauncherActivity.this
                                    .requestPermissions(
                                            new String[]{

                                                    "android.permission.READ_EXTERNAL_STORAGE",
                                                    "android.permission.WRITE_EXTERNAL_STORAGE",
                                                    "android.hardware.camera","android.permission.READ_PHONE_STATE"},
                                            LauncherActivity.PERMISSIONS_REQUEST);
                        }
                        dialog.dismiss();
                    }
                });
        if (this.isOpenFisrtTime) {
            Button setting = (Button) dialog.findViewById(R.id.settings);
            setting.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
            setting.setVisibility(0);
            setting.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(
                            "android.settings.APPLICATION_DETAILS_SETTINGS",
                            Uri.fromParts("package",
                                    LauncherActivity.this.getPackageName(),
                                    null));
                    intent.addFlags(268435456);
                    LauncherActivity.this.startActivityForResult(intent, 101);
                    dialog.dismiss();
                }
            });
        } else {
            this.isOpenFisrtTime = true;
        }
        dialog.show();


    }

}