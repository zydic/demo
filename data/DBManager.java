package com.example.foldergallery.data;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class DBManager {
	private static final String LOG_TAG = "Database";
	private static DBManager dbManager = null;
	private static DBContext databaseContext = null;
	private static SQLiteDatabase database = null;
	private AtomicInteger openCounter = new AtomicInteger();

	public synchronized static DBManager getInstance() {
		if (dbManager == null) {
			throw new IllegalStateException(
					String.format(
							"%s is not initialized, App initializeDB(..) method first.",
							DBManager.class.getSimpleName()));
		}
		return dbManager;
	}

	public static synchronized boolean initializeDB(Context context) {
		try {
			if (dbManager == null) {
				dbManager = new DBManager();
				databaseContext = new DBContext(context);
			}
			return true;
		} catch (Exception ex) {
			Log.e(LOG_TAG, "Error initializing DB", ex);
			return false;
		}
	}

	public synchronized SQLiteDatabase getWritableDB() {
		try {
			if (openCounter.incrementAndGet() == 1) {
				database = databaseContext.getWritableDatabase();
			}
			return database;
		} catch (Exception ex) {
			Log.e(LOG_TAG, "Error opening DB in W mode ", ex);
			return null;
		}
	}

	public synchronized boolean closeDatabase() {
		if (openCounter.decrementAndGet() == 0) {
			database.close();
		}
		return false;
	}


	public synchronized void insertCategoryDetail(CatDataListVariableModel categoryModel) {
		ContentValues values = new ContentValues();
		values.put(DBContext.PHOTOBOOK_CATEGOTY_ID,
				categoryModel.getCategory_Id());
		values.put(DBContext.PHOTOBOOK_CATEGOTY_NAME,
				categoryModel.getName());
		values.put(DBContext.PHOTOBOOK_CATEGOTY_THUMBNAIL,
				categoryModel.getThumbImg());
		values.put(DBContext.PHOTOBOOK_CATEGOTY_ISSTATIC,
				categoryModel.getIsStatic());
		values.put(DBContext.PHOTOBOOK_CATEGOTY_ISDOWNLOADED,
				categoryModel.getIsdownloaded());

		getWritableDB().insertWithOnConflict(
				DBContext.TABLE_NAME, null, values,
				SQLiteDatabase.CONFLICT_IGNORE);
		closeDatabase();
	}

	public synchronized void updateCategoryDetail(CatDataListVariableModel categoryModel,int id) {
		ContentValues values = new ContentValues();

		values.put(DBContext.PHOTOBOOK_CATEGOTY_ISSTATIC,
				categoryModel.getIsStatic());
		values.put(DBContext.PHOTOBOOK_CATEGOTY_ISDOWNLOADED,
				categoryModel.getIsdownloaded());

		getWritableDB().updateWithOnConflict(
				DBContext.TABLE_NAME,values,DBContext.PHOTOBOOK_CATEGOTY_ID + "=" + id,null,
				SQLiteDatabase.CONFLICT_IGNORE);
		closeDatabase();
	}

//	public synchronized ArrayList<CatDataListVariableModel> getCategoryListForCheckDuplicateData() {
//		ArrayList<CatDataListVariableModel> allData = new ArrayList<CatDataListVariableModel>();
//		String select_query = "SELECT * FROM "
//				+ DBContext.TABLE_NAME + " WHERE "
//				+ DBContext.COLUMN_PHOTO_FRAME_CATEGORY_TABLE_STATUS
//				+ " ='1' AND "
//				+ DBContext.COLUMN_PHOTO_FRAME_CATEGORY_IS_STATIC + " = '0'"
//				+ " ORDER BY "
//				+ DBContext.COLUMN_PHOTO_FRAME_CATEGORY_IS_STATIC + " DESC";
//		Cursor mCursor = getWritableDB().rawQuery(select_query, null);
//		if (mCursor.getCount() != 0) {
//			if (mCursor != null) {
//				mCursor.moveToFirst();
//				for (int i = 0; i < mCursor.getCount(); i++) {
//					CategoryModel categoryModel = new CategoryModel();
//					categoryModel
//							.setCategoryName(mCursor.getString(mCursor
//									.getColumnIndex(DBContext.COLUMN_PHOTO_FRAME_CATEGORY_NAME)));
//					categoryModel
//							.setFrameCategoryId(mCursor.getInt(mCursor
//									.getColumnIndex(DBContext.COLUMN_PHOTO_FRAME_CATEGORY_ID_NO)));
//					categoryModel
//							.setStaticCategory(mCursor.getInt(mCursor
//									.getColumnIndex(DBContext.COLUMN_PHOTO_FRAME_CATEGORY_IS_STATIC)));
//					allData.add(categoryModel);
//					mCursor.moveToNext();
//				}
//			}
//		}
//		mCursor.close();
//		closeDatabase();
//		return allData;
//	}

	public synchronized ArrayList<CatDataListVariableModel> getAllCategoryData() {
		ArrayList<CatDataListVariableModel> allData = new ArrayList<CatDataListVariableModel>();
		String select_query = "SELECT * FROM "
				+ DBContext.TABLE_NAME;
		Cursor mCursor = getWritableDB().rawQuery(select_query, null);
		if (mCursor.getCount() != 0) {
			if (mCursor != null) {
				mCursor.moveToFirst();
				for (int i = 0; i < mCursor.getCount(); i++) {
					CatDataListVariableModel categoryModel = new CatDataListVariableModel();
					categoryModel
							.setCategory_Id(mCursor.getInt(mCursor
									.getColumnIndex(DBContext.PHOTOBOOK_CATEGOTY_ID)));
					categoryModel
							.setName(mCursor.getString(mCursor
									.getColumnIndex(DBContext.PHOTOBOOK_CATEGOTY_NAME)));
					categoryModel
							.setThumbImg(mCursor.getString(mCursor
									.getColumnIndex(DBContext.PHOTOBOOK_CATEGOTY_THUMBNAIL)));

					categoryModel
							.setIsStatic(mCursor.getInt(mCursor
									.getColumnIndex(DBContext.PHOTOBOOK_CATEGOTY_ISSTATIC)));
					categoryModel
							.setIsdownloaded(mCursor.getInt(mCursor
									.getColumnIndex(DBContext.PHOTOBOOK_CATEGOTY_ISDOWNLOADED)));
					allData.add(categoryModel);
					mCursor.moveToNext();
				}
			}
		}
		mCursor.close();
		closeDatabase();
		return allData;
	}
//
//	public synchronized ArrayList<ModelPhotoCount> getFrames(int frameType,
//			int photoCount) {
//		ArrayList<ModelPhotoCount> allData = new ArrayList<ModelPhotoCount>();
//		String select_query = "SELECT * FROM " + DBContext.TABLE_PHOTO_COUNT
//				+ " WHERE " + DBContext.COLUMN_PHOTO_COUNT_TABLE_STATUS
//				+ " ='1' AND " + DBContext.COLUMN_PHOTO_COUNT_FRAME_TYPE
//				+ " = " + frameType + " AND "
//				+ DBContext.COLUMN_PHOTO_COUNT_VALUE + " = '" + photoCount
//				+ "'" + " ORDER BY " + DBContext.COLUMN_PHOTO_COUNT_ID
//				+ " DESC";
//
//		Cursor mCursor = getWritableDB().rawQuery(select_query, null);
//		if (mCursor.getCount() != 0) {
//			if (mCursor != null) {
//				mCursor.moveToFirst();
//				for (int i = 0; i < mCursor.getCount(); i++) {
//					ModelPhotoCount modelPhotoCount = new ModelPhotoCount();
//					modelPhotoCount
//							.setFrameName(mCursor.getString(mCursor
//									.getColumnIndex(DBContext.COLUMN_PHOTO_COUNT_FRAME_NAME)));
//					modelPhotoCount
//							.setFrameType(mCursor.getInt(mCursor
//									.getColumnIndex(DBContext.COLUMN_PHOTO_COUNT_FRAME_TYPE)));
//					modelPhotoCount
//							.setPhotoCount(mCursor.getInt(mCursor
//									.getColumnIndex(DBContext.COLUMN_PHOTO_COUNT_VALUE)));
//					modelPhotoCount
//							.setFrameCategory(mCursor.getString(mCursor
//									.getColumnIndex(DBContext.COLUMN_PHOTO_COUNT_FRAME_CATEGORY)));
//					allData.add(modelPhotoCount);
//					mCursor.moveToNext();
//				}
//			}
//		}
//		mCursor.close();
//		closeDatabase();
//		return allData;
//	}

}