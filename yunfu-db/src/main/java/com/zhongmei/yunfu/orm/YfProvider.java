package com.zhongmei.yunfu.orm;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.zhongmei.OSLog;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.IEntity;

import java.util.ArrayList;

public class YfProvider extends ContentProvider {

    public static final String DB_AUTHORITY = "com.zhongmei.calm.provider.Calm";
    private static final UriMatcher sUriMatcher;
    private SQLiteDatabaseHelper mOpenHelper;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        int i = 0;
        for (Class<? extends IEntity<?>> classType : YfDatabaseHelper.TABLES) {
            sUriMatcher.addURI(DB_AUTHORITY, DBHelperManager.getTableName(classType), i);
            i++;
        }
    }

    @Override
    public boolean onCreate() {
        //mOpenHelper = OpenHelperManager.getHelper(getContext(), CalmDatabaseHelper.class);
        return true;
    }

    private SQLiteDatabaseHelper getDbHelper() {
        if (mOpenHelper == null) {
            mOpenHelper = OpenHelperManager.getHelper(BaseApplication.getInstance(), YfDatabaseHelper.class);
        }
        return mOpenHelper;
    }

    @Override
    public ContentProviderResult[] applyBatch(
            ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        SQLiteDatabase db = getDbHelper().getWritableDatabase();
        db.beginTransaction();
        try {
            ContentProviderResult[] results = super.applyBatch(operations);
            boolean success = true;
            // 增返回uri，删改返回count
            for (ContentProviderResult result : results) {
                if (result.uri == null && result.count == -1) {
                    success = false;
                    break;
                }
            }
            if (success) {
                db.setTransactionSuccessful();
            }
            return results;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(getType(uri));
        SQLiteDatabase db = getDbHelper().getReadableDatabase();
        Cursor cursor = qb.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        //int oldTableCount = DataBaseTableList.getAllTableList().size();
        int newTableCount = YfDatabaseHelper.TABLES.size();
        int i = sUriMatcher.match(uri);
		/*if (i < oldTableCount) {
			return DataBaseUtils.getTableName(DataBaseTableList.getAllTableList().get(i));
		} else if (i < (oldTableCount+newTableCount)) {
			return DBHelperManager.getTableName(CalmDatabaseHelper.TABLES.get(i-oldTableCount));
		}*/
        if (i < newTableCount) {
            return DBHelperManager.getTableName(YfDatabaseHelper.TABLES.get(i));
        }
        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }
        Uri noteUri = null;
        String table = getType(uri);

		/*if (isCommonDataTable(table)) {
			long dateTime1 = System.currentTimeMillis();
			if (values
					.containsKey(CommonDataBaseInfo.COMMON_CREATE_DATETIME_KEY) == false
					|| TextUtils
							.isEmpty(values
									.getAsString(CommonDataBaseInfo.COMMON_CREATE_DATETIME_KEY))) {
				values.put(CommonDataBaseInfo.COMMON_CREATE_DATETIME_KEY,
						dateTime1);
			}
			if (values
					.containsKey(CommonDataBaseInfo.COMMON_MODIFY_DATETIME_KEY) == false
					|| TextUtils
							.isEmpty(values
									.getAsString(CommonDataBaseInfo.COMMON_MODIFY_DATETIME_KEY))) {
				values.put(CommonDataBaseInfo.COMMON_MODIFY_DATETIME_KEY,
						dateTime1);
			}
		}*/
        SQLiteDatabase db = getDbHelper().getWritableDatabase();
        long rowId = db.replace(table, "", values);
        if (rowId > 0) {
            noteUri = ContentUris.withAppendedId(uri, rowId);
        }

        if (noteUri != null) {
            getContext().getContentResolver()
                    .notifyChange(noteUri, null, false);
        } else {
            throw new SQLException("Failed to insert row into " + uri);
        }

        return noteUri;
    }

	/*private boolean isCommonDataTable(String table) {
		for (Class<?> cls : DataBaseTableList.getSyncTableList()) {
			if (DataBaseUtils.getTableName(cls).equals(table)) {
				return true;
			}
		}
		return false;
	}*/

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        int count = 0;
        SQLiteDatabase db = getDbHelper().getWritableDatabase();
        try {
            db.delete(getType(uri), where, whereArgs);
            getContext().getContentResolver().notifyChange(uri, null, false);
        } catch (Exception e) {
            count = -1;
            Log.e("CalmProvider", "delete exception" + e.toString());
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        ContentValues valuesTmp;
        if (values != null) {
            valuesTmp = new ContentValues(values);
        } else {
            valuesTmp = new ContentValues();
        }
        String table = getType(uri);
		/*if (isCommonDataTable(table)) {
			long dateTime1 = System.currentTimeMillis();
			if (values
					.containsKey(CommonDataBaseInfo.COMMON_MODIFY_DATETIME_KEY) == false
					|| TextUtils
							.isEmpty(values
									.getAsString(CommonDataBaseInfo.COMMON_MODIFY_DATETIME_KEY))) {
				values.put(CommonDataBaseInfo.COMMON_MODIFY_DATETIME_KEY,
						dateTime1);
			}
		}*/
        SQLiteDatabase db = getDbHelper().getWritableDatabase();
        try {
            db.update(table, valuesTmp, selection, selectionArgs);
            getContext().getContentResolver().notifyChange(uri, null, false);
        } catch (Exception e) {
            count = -1;
            OSLog.error("update exception" + e.toString());
        }
        return count;
    }

}
