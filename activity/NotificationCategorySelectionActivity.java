package com.example.foldergallery.activity;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
//import com.example.mycamapppp.R;
//import com.example.notifymypic.Adapter.NotificationCategorySelectionAdapter;
//import com.example.notifymypic.Application.Api;
//import com.example.notifymypic.Bean.CatDataListModel;
//import com.example.notifymypic.Bean.CatDataListVariableModel;
//import com.example.notifymypic.Utils.DBManager;
//import com.example.notifymypic.Utils.EPreferences;
//import com.example.notifymypic.Utils.Utils;

import com.example.foldergallery.adapters.NotificationCategorySelectionAdapter;
import com.example.foldergallery.data.Api;
import com.example.foldergallery.data.CatDataListModel;
import com.example.foldergallery.data.CatDataListVariableModel;
import com.example.foldergallery.data.DBManager;
import com.example.foldergallery.util.EPreferences;
import com.example.foldergallery.util.Utils;
import com.example.photobook.R;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationCategorySelectionActivity extends AppCompatActivity {
    public ArrayList<CatDataListVariableModel> arrGetData;
    ArrayList<CatDataListVariableModel> arrDataBaseData;
    ArrayList<String> arrDataBaseCatName;
    Toolbar toolbar;
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    public NotificationCategorySelectionAdapter adapter;
    File[] downloadedFileName;
    public DBManager dbHelpler;
    AlertDialog alert;
    private EPreferences pref;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        bindview();
        init();
    }

    private void init() {
        initDB();
        downloadedFileName = null;
        setSupportActionBar(toolbar);
        arrGetData = new ArrayList<>();
        arrDataBaseData = new ArrayList<>();
        arrDataBaseCatName = new ArrayList<>();

        pref = EPreferences.getInstance(getApplicationContext());
        if (pref.getBoolean(EPreferences.IS_FIRST_TIME, true) == true) {
            addStaticCategoryName();
            arrGetData.clear();
            arrGetData.addAll(dbHelpler.getAllCategoryData());
            setRecyclerView();
            pref.putBoolean(EPreferences.IS_FIRST_TIME, false);
        } else {
            arrGetData.clear();
            arrGetData.addAll(dbHelpler.getAllCategoryData());
            setRecyclerView();
        }
        if (Utils.checkNetworkConnectivity(NotificationCategorySelectionActivity.this)) {
            loadData();
        } else {
//            if (Utils.toast == null || Utils.toast.getView().getWindowVisibility() != View.VISIBLE) {
                Toast.makeText(getApplicationContext(), "Please connect with internet", Toast.LENGTH_SHORT).show();
//                Utils.toast.show();
//            }
        }

        arrDataBaseData = dbHelpler.getAllCategoryData();


        for (int i = 0; i < arrDataBaseData.size(); i++) {
            arrDataBaseCatName.add(arrDataBaseData.get(i).getName());
        }

        setRecyclerView();


    }

    public void initDB() {
        DBManager.initializeDB(NotificationCategorySelectionActivity.this);
        dbHelpler = DBManager.getInstance();
    }


    private void addStaticCategoryName() {

        CatDataListVariableModel category = new CatDataListVariableModel();
        category.setCategory_Id(0);
        category.setName("Birthday Cake");
        category.setThumbImg("");
        category.setIsStatic(1);
        category.setIsdownloaded(0);
        dbHelpler.insertCategoryDetail(category);
        CatDataListVariableModel category1 = new CatDataListVariableModel();
        category1.setCategory_Id(1);
        category1.setName("Anniversary Romantic");
        category1.setThumbImg("");
        category1.setIsStatic(1);
        category1.setIsdownloaded(0);
        dbHelpler.insertCategoryDetail(category1);


    }

    private void loadData() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        Api api = retrofit.create(Api.class);
        Call<CatDataListModel> call = api.getCategoryList();
        adapter = new NotificationCategorySelectionAdapter(NotificationCategorySelectionActivity.this, arrGetData);
        call.enqueue(new Callback<CatDataListModel>() {

            @Override
            public void onFailure(Call<CatDataListModel> arg0, Throwable t) {
                Log.d("calldemo","fail ");
            }

            @Override
            public void onResponse(Call<CatDataListModel> arg0,
                                   Response<CatDataListModel> response) {
                ArrayList<CatDataListVariableModel> arrayList = (ArrayList<CatDataListVariableModel>) response
                        .body().getData();

                Log.d("calldemo","success "+arrayList.size());
                for (int i = 0; i < arrayList.size(); i++) {
                    CatDataListVariableModel category = new CatDataListVariableModel();


                    if (!arrDataBaseCatName.contains(arrayList.get(i).getName())) {
                        category.setCategory_Id(arrayList.get(i).getCategory_Id());
                        category.setName(arrayList.get(i).getName());
                        category.setThumbImg(arrayList.get(i).getThumbImg());
                        category.setIsStatic(0);
                        category.setIsdownloaded(0);
                        dbHelpler.insertCategoryDetail(category);

                    }


                }
                arrGetData.clear();
                arrGetData.addAll(dbHelpler.getAllCategoryData());
                adapter.notifyDataSetChanged();

            }
        });
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        adapter = new NotificationCategorySelectionAdapter(NotificationCategorySelectionActivity.this, arrGetData);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(NotificationCategorySelectionActivity.this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void bindview() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(NotificationCategorySelectionActivity.this, LauncherActivity.class);
        startActivity(i);
        finish();
        adapter.detail();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }




}
