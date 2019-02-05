package com.example.foldergallery.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.foldergallery.adapters.ImageEditAdapter;
import com.example.foldergallery.data.ImageData;
import com.example.foldergallery.util.ActivityAnimUtil;
import com.example.foldergallery.util.CustomTFSpan;
import com.example.foldergallery.util.Utils;
import com.example.foldergallery.view.EmptyRecyclerView;
import com.example.notifymypic.Photobook.CurlActivity;
import com.example.photobook.R;
import com.exampleqwe.foldergallery.MyApplication;

import java.util.Collections;


public class ImageEditActivity extends AppCompatActivity   {

    private MyApplication application;
    private ImageEditAdapter imageEditAdapter;
    private EmptyRecyclerView rvSelectedImages;
    private Toolbar toolbar;
    public static String albumkey = "";
    RelativeLayout windowManagerAlertDialog;
    Button positivebutton, negativebutton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_album);
        application = MyApplication.getInstance();
        bindView();
        init();
        addListener();
    }

    private void bindView() {
        rvSelectedImages = (EmptyRecyclerView) findViewById(R.id.rvVideoAlbum);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Swap Images");
        windowManagerAlertDialog = (RelativeLayout) this.findViewById(R.id.window);
        positivebutton = (Button) findViewById(R.id.positiveBtn);
        negativebutton = (Button) findViewById(R.id.negativeBtn);

    }

    private void init() {

        setupAdapter();
        ItemTouchHelper ith = new ItemTouchHelper(_ithCallback);
        ith.attachToRecyclerView(rvSelectedImages);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Utils.applyFontForToolbarTitle(ImageEditActivity.this, toolbar);
    }

    private void addListener() {

    }

    private void setupAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2,
                GridLayoutManager.VERTICAL, false);
        imageEditAdapter = new ImageEditAdapter(this);
        rvSelectedImages.setLayoutManager(gridLayoutManager);
        rvSelectedImages.setItemAnimator(new DefaultItemAnimator());
        rvSelectedImages.setEmptyView(findViewById(R.id.list_empty));
        rvSelectedImages.setAdapter(imageEditAdapter);
    }



    @Override
    protected void onResume() {
        super.onRestart();
        if (imageEditAdapter != null) {
            imageEditAdapter.notifyDataSetChanged();
        }
        super.onResume();
    }

    ItemTouchHelper.Callback _ithCallback = new ItemTouchHelper.Callback() {
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                      int actionState) {
            if (actionState == 0) {
                imageEditAdapter.notifyDataSetChanged();
            }
        }

        ;

        @Override
        public void onMoved(RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, int fromPos,
                            RecyclerView.ViewHolder target, int toPos, int x, int y) {
            imageEditAdapter.swap(viewHolder.getAdapterPosition(),
                    target.getAdapterPosition());
            application.min_pos = Math.min(application.min_pos,
                    Math.min(fromPos, toPos));
            MyApplication.isBreak = true;
        }

        ;

        // and in your imlpementaion of
        @Override
        public boolean onMove(RecyclerView recyclerView,
                              RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder) {
            return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                    ItemTouchHelper.DOWN | ItemTouchHelper.UP
                            | ItemTouchHelper.START | ItemTouchHelper.END);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_selection, menu);

        menu.removeItem(R.id.menu_clear);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_done:
                done();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void done() {
        Utils.arrPhotoBook.clear();

        for (int i = 0; i < application.getSelectedImages().size(); i++) {
            ImageData data = application.getSelectedImages().get(i);
            Log.d("tag", data.imagePath);
            Log.d("tag", "arrrr" + Utils.arrPhotoBook.size());

            Utils.arrPhotoBook.add(data.imagePath);


            Log.i("patharr", data.imagePath);
        }
        application.getSelectedImages().clear();
//		if (isFromPreview) {
//			setResult(RESULT_OK);
//			finish();
//			return;
//		} else {

        if (getIntent().getBooleanExtra("isFromCamera", false)) {
            DefaultThemeAlertDialogue();
        } else {
            Intent intent = new Intent(ImageEditActivity.this, CategorySelectionActivity.class);
            ActivityAnimUtil.startActivitySafely(toolbar, intent);
            finish();
        }

        // super.onBackPressed();
    }

    public void onBackPressed() {

//		if (isFromPreview && !isFromCameraNotification) {
//			done();
//		}
//		if (isFromCameraNotification) {


        Intent intent = new Intent(ImageEditActivity.this,
                ImageSelectionActivity.class);
        Utils.arrPhotoBook.clear();
        startActivity(intent);
        finish();
//		} else {
//			super.onBackPressed();
//		}
    }

    ;



    private void DefaultThemeAlertDialogue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(
                ImageEditActivity.this, R.style.AppAlertDialog);

        Typeface face = Typeface.createFromAsset(getAssets(), "Comfortaa-Regular.ttf");
        CustomTFSpan tfSpan = new CustomTFSpan(face);
        SpannableString spannableString = new SpannableString("Continue With Default Album Theme?");
        spannableString.setSpan(tfSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setTitle(spannableString);
        SpannableString ss = new SpannableString("Are you sure you want to continue with our default album theme ?");
        ss.setSpan(tfSpan, 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setMessage(ss);
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        Intent intent = new Intent(ImageEditActivity.this, CurlActivity.class);
                        startActivity(intent);
                        finish();
                    }

                });

        builder.setNegativeButton(getResources().getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Utils.bt2.clear();
                        MyApplication application = new MyApplication();
                        application.getSelectedImages().clear();
                        ImageSelectionActivity.tempImage.clear();
                        Collections.reverse(Utils.arrPhotoBook);
                        Intent intent2 = new Intent(ImageEditActivity.this, NotificationCategorySelectionActivity.class);
                        startActivity(intent2);
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
        TextView textView = (TextView) alert.getWindow().findViewById(android.R.id.message);
        Button btnYes = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        btnYes.setAllCaps(false);
        btnYes.setTextColor(getResources().getColor(R.color.white));
        Button btnNo = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        btnNo.setAllCaps(false);
        btnNo.setTextColor(getResources().getColor(R.color.white));
        textView.setTextColor(getResources().getColor(R.color.white));
    }


}