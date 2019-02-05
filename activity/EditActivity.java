package com.example.foldergallery.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.photobook.R;
import com.exampleqwe.foldergallery.MyApplication;

public class EditActivity extends AppCompatActivity {

    ImageView imggetPos;
    MyApplication application;
    int tempImgePosition;
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        bindview();
        init();
        addlistener();

    }
    private void bindview()
    {

        imggetPos = (ImageView) findViewById(R.id.ivgetpos);
    }
    private void init()
    {
        application = MyApplication.getInstance();
        tempImgePosition = getIntent().getExtras().getInt("position");
        Glide.with(this.context)
                .load(application.selectedImages.get(tempImgePosition).imagePath)
                .into(imggetPos);
        System.gc();
    }
    private void addlistener()
    {

    }
}
