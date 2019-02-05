package com.example.foldergallery.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBContext extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "photobook_category_detail";
    public static final String PHOTOBOOK_ID = "id";
    public static final String PHOTOBOOK_CATEGOTY_ID = "catid";
    public static final String PHOTOBOOK_CATEGOTY_NAME = "catname";
    public static final String PHOTOBOOK_CATEGOTY_THUMBNAIL = "catthumbnail";
    public static final String PHOTOBOOK_CATEGOTY_ISSTATIC = "catisstatic";
    public static final String PHOTOBOOK_CATEGOTY_ISDOWNLOADED = "catisdownloaded";
    private static final String LOG_TAG = "Database";
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "notifymypic.db";
    private static final String QUERY_CREATE_PHOTO_FRAME_CATEGORY_TABLE = String
            .format("CREATE TABLE %s ("
                            + "%s INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "%s INTEGER NOT NULL, "
                            + "%s TEXT NOT NULL, "
                            + "%s TEXT NOT NULL, "
                            + "%s INTEGER  NOT NULL DEFAULT  1,"
                            + "%s INTEGER  NOT NULL DEFAULT  1 )"
                    ,
                    TABLE_NAME,
                    PHOTOBOOK_ID, PHOTOBOOK_CATEGOTY_ID,
                    PHOTOBOOK_CATEGOTY_NAME,
                    PHOTOBOOK_CATEGOTY_THUMBNAIL,
                    PHOTOBOOK_CATEGOTY_ISSTATIC,
                    PHOTOBOOK_CATEGOTY_ISDOWNLOADED);
    private static final String[] DATABASE_TABLES_QUERY = {

            QUERY_CREATE_PHOTO_FRAME_CATEGORY_TABLE};

    public DBContext(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String table : DATABASE_TABLES_QUERY) {
            try {
                db.execSQL(table);
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Error creating table", ex);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}