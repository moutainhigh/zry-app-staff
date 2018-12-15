package com.zhongmei.bty.thirdplatform.openplatform;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by demo on 2018/12/15
 * 保存商户用户信息
 */

public class ShopUserUtil {
    public static void saveShopUserInfo(Context context, ShopUserInfo shopUserInfo) {
        if (context == null || shopUserInfo == null) return;
        try {
            DbOpenHelper dbOpenHelper = new DbOpenHelper(context);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            if (IsExists(db, shopUserInfo)) {
                updateShopUserInfo(context, shopUserInfo);
            } else {
                insertShopUserInfo(context, shopUserInfo);
            }
            db.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    private static boolean IsExists(SQLiteDatabase db, ShopUserInfo shopUserInfo) {
        boolean isExists = false;
        Cursor cursor = db.query(ShopUserInfo.TABLE_NAME, null, ShopUserInfo.SHOPID + " =?",
                new String[]{shopUserInfo.getShopId()}, null, null, null);

        if (cursor != null) {
            if (cursor.getCount() > 0)
                isExists = true;
            cursor.close();
        }
        return isExists;
    }

    private static void updateShopUserInfo(Context context, ShopUserInfo shopUserInfo) {
        Uri shopUserUri = ShopUserProvider.SHOP_USER_CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put(ShopUserInfo.BRANDID, shopUserInfo.getBrandId());
        values.put(ShopUserInfo.BRANDNAME, shopUserInfo.getBrandName());
        values.put(ShopUserInfo.SHOPID, shopUserInfo.getShopId());
        values.put(ShopUserInfo.SHOPNAME, shopUserInfo.getShopName());
        values.put(ShopUserInfo.USERID, shopUserInfo.getUserId());
        values.put(ShopUserInfo.USERNAME, shopUserInfo.getUserName());
        context.getContentResolver().update(shopUserUri, values, "_id = ? ", new String[]{"1"});
    }

    private static void insertShopUserInfo(Context context, ShopUserInfo shopUserInfo) {
        Uri shopUserUri = ShopUserProvider.SHOP_USER_CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put("_id", 1);
        values.put(ShopUserInfo.BRANDID, shopUserInfo.getBrandId());
        values.put(ShopUserInfo.BRANDNAME, shopUserInfo.getBrandName());
        values.put(ShopUserInfo.SHOPID, shopUserInfo.getShopId());
        values.put(ShopUserInfo.SHOPNAME, shopUserInfo.getShopName());
        values.put(ShopUserInfo.USERID, shopUserInfo.getUserId());
        values.put(ShopUserInfo.USERNAME, shopUserInfo.getUserName());
        context.getContentResolver().insert(shopUserUri, values);
    }

    public static void deleteShopUserInfo(Context context) {
        if (context == null) return;
        DbOpenHelper dbOpenHelper = new DbOpenHelper(context);
        SQLiteDatabase _db = dbOpenHelper.getWritableDatabase();
        String sql = "delete from " + ShopUserInfo.TABLE_NAME;
        _db.execSQL(sql);
        _db.close();
    }
}
