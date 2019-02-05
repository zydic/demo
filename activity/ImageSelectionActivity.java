package com.example.foldergallery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foldergallery.adapters.AlbumAdapterById;
import com.example.foldergallery.adapters.ImageByAlbumAdapter;
import com.example.foldergallery.adapters.OnItemClickListner;
import com.example.foldergallery.adapters.SelectedImageAdapter;
import com.example.foldergallery.data.ImageData;
import com.example.foldergallery.util.TypeFaceUtil;
import com.example.foldergallery.util.Utils;
import com.example.foldergallery.view.EmptyRecyclerView;
import com.example.foldergallery.view.ExpandIconView;
import com.example.foldergallery.view.VerticalSlidingPanel;
import com.example.foldergallery.view.VerticalSlidingPanel.PanelSlideListener;
import com.example.photobook.R;
import com.exampleqwe.foldergallery.MyApplication;

import java.util.ArrayList;

public class ImageSelectionActivity extends AppCompatActivity implements
        PanelSlideListener {
    private RecyclerView rvAlbum, rvAlbumImages;
    public static ArrayList<ImageData> tempImage = new ArrayList<>();
    private AlbumAdapterById albumAdapter;
    private ImageByAlbumAdapter albumImagesAdapter;
    private SelectedImageAdapter selectedImageAdapter;
    private VerticalSlidingPanel panel;
    private View parent;
    private Toolbar toolbar;
    private ExpandIconView expandIcon;
    private Button btnClear;
    private TextView tvImageCount;
    private MyApplication application;
    public boolean isFromPreview = false, isFromCameraNotification = false;
    public static boolean isForFirst = false;
    private EmptyRecyclerView rvSelectedImage;
    public static final String EXTRA_FROM_PREVIEW = "extra_from_preview";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_select_activity);
        application = MyApplication.getInstance();

        bindView();
        init();
        addListner();
    }

    public void scrollToPostion(final int pos) {
        rvAlbum.postDelayed(new Runnable() {
            @Override
            public void run() {
                rvAlbum.scrollToPosition(pos);
            }
        }, 300);
    }

    private void init() {
        setSupportActionBar(toolbar);

            application.getFolderList();

        albumAdapter = new AlbumAdapterById(this);
        albumImagesAdapter = new ImageByAlbumAdapter(this);
        selectedImageAdapter = new SelectedImageAdapter(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvAlbum.setLayoutManager(mLayoutManager);
        rvAlbum.setItemAnimator(new DefaultItemAnimator());
        rvAlbum.setAdapter(albumAdapter);

        RecyclerView.LayoutManager gridLayputManager = new GridLayoutManager(
                getApplicationContext(), 3);
        rvAlbumImages.setLayoutManager(gridLayputManager);
        rvAlbumImages.setItemAnimator(new DefaultItemAnimator());
        rvAlbumImages.setAdapter(albumImagesAdapter);

        RecyclerView.LayoutManager gridLayputManager1 = new GridLayoutManager(
                getApplicationContext(), 4);
        rvSelectedImage.setLayoutManager(gridLayputManager1);
        rvSelectedImage.setItemAnimator(new DefaultItemAnimator());
        rvSelectedImage.setAdapter(selectedImageAdapter);
        rvSelectedImage.setEmptyView(findViewById(R.id.list_empty));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.icon_back));
        tvImageCount.setText(String.valueOf(application.getSelectedImages()
                .size()));
        clearData();
    }

    private void bindView() {
        tvImageCount = (TextView) findViewById(R.id.tvImageCount);
        expandIcon = (ExpandIconView) findViewById(R.id.settings_drag_arrow);
        rvAlbum = (RecyclerView) findViewById(R.id.rvAlbum);
        rvAlbumImages = (RecyclerView) findViewById(R.id.rvImageAlbum);
        rvSelectedImage = (EmptyRecyclerView) findViewById(R.id.rvSelectedImagesList);
        panel = (VerticalSlidingPanel) findViewById(R.id.overview_panel);
        panel.setEnableDragViewTouchEvents(true);
        panel.setDragView(findViewById(R.id.settings_pane_header));
        panel.setPanelSlideListener(this);
        parent = findViewById(R.id.default_home_screen_panel);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnClear = (Button) findViewById(R.id.btnClear);
        tvImageCount.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
        Utils.applyFontForToolbarTitle(ImageSelectionActivity.this,toolbar);
        btnClear.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
    }

    private void addListner() {
        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });
        albumAdapter.setOnItemClickListner(new OnItemClickListner<Object>() {
            @Override
            public void onItemClick(View view, Object item) {
                albumImagesAdapter.notifyDataSetChanged();
            }
        });
        albumImagesAdapter
                .setOnItemClickListner(new OnItemClickListner<Object>() {
                    @Override
                    public void onItemClick(View view, Object item) {
                        tvImageCount.setText(String.valueOf(application
                                .getSelectedImages().size()));
                        selectedImageAdapter.notifyDataSetChanged();
                    }
                });
        selectedImageAdapter
                .setOnItemClickListner(new OnItemClickListner<Object>() {
                    @Override
                    public void onItemClick(View view, Object item) {
                        tvImageCount.setText(String.valueOf(application
                                .getSelectedImages().size()));
                        albumImagesAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == RESULT_OK) {
            application.selectedImages.remove(MyApplication.TEMP_POSITION);
            ImageData imageData = new ImageData();
            imageData.setImagePath(data.getExtras().getString("ImgPath"));
            application.selectedImages.add(MyApplication.TEMP_POSITION,
                    imageData);
            setupAdapter();
            // MyApplication.TEMP_POSITION = -1;
        }
    }

    private void setupAdapter() {
        selectedImageAdapter = new SelectedImageAdapter(this);
        RecyclerView.LayoutManager gridLayputManager1 = new GridLayoutManager(
                getApplicationContext(), 4);
        rvSelectedImage.setLayoutManager(gridLayputManager1);
        rvSelectedImage.setItemAnimator(new DefaultItemAnimator());
        rvSelectedImage.setAdapter(selectedImageAdapter);
        rvSelectedImage.setEmptyView(findViewById(R.id.list_empty));
    }

    boolean isPause = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (isPause) {
            isPause = false;
            tvImageCount.setText(String.valueOf(application.getSelectedImages()
                    .size()));
            albumImagesAdapter.notifyDataSetChanged();
            selectedImageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_selection, menu);

//            menu.removeItem(R.id.menu_clear);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done:
                if (application.getSelectedImages().size() > 3) {


                        Intent intent = new Intent(this, ImageEditActivity.class);
                        intent.putExtra("isFromCameraNotification", false);
                        intent.putExtra("KEY", "FromImageSelection");
                        startActivity(intent);
                        finish();

                } else {
                    Toast.makeText(this,
                            R.string.select_more_than_3_images_for_create,
                            Toast.LENGTH_LONG).show();
                }
                break;

            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.menu_clear:
                clearData();
                break;

            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (panel.isExpanded()) {
            panel.collapsePane();
            return;
        }
        if (isFromCameraNotification) {
            startActivity(new Intent(ImageSelectionActivity.this,
                    LauncherActivity.class));
            application.clearAllSelection();
            finish();
            return;
        }
//        if (!isFromPreview) {
//            application.videoImages.clear();
//            application.clearAllSelection();
//        } else {
//            setResult(RESULT_OK);
//            finish();
//            return;
//        }
        super.onBackPressed();
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        if (expandIcon != null)
            expandIcon.setFraction(slideOffset, false);
        if (slideOffset >= 0.005f) {
            if (parent != null && parent.getVisibility() != View.VISIBLE) {
                parent.setVisibility(View.VISIBLE);
            }
        } else {
            if (parent != null && parent.getVisibility() == View.VISIBLE) {
                parent.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onPanelCollapsed(View panel) {
        if (parent != null) {
            parent.setVisibility(View.VISIBLE);
        }
        selectedImageAdapter.isExpanded = false;
        selectedImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPanelExpanded(View panel) {
        if (parent != null) {
            parent.setVisibility(View.GONE);
        }
        selectedImageAdapter.isExpanded = true;
        selectedImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPanelAnchored(View panel) {
    }

    @Override
    public void onPanelShown(View panel) {
    }

    private void clearData() {
        ArrayList<ImageData> selectedImages = application.getSelectedImages();
        for (int i = selectedImages.size() - 1; i >= 0; i--) {
            application.removeSelectedImage(i);
        }
        tvImageCount.setText("0");
        selectedImageAdapter.notifyDataSetChanged();
        albumImagesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
