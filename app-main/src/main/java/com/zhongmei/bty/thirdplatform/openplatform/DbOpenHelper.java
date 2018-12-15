package com.zhongmei.bty.thirdplatform.openplatform;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zhongmei.yunfu.context.util.FileUtil;

/**
 * Created by demo on 2018/12/15
 * 供开放平台获取商户及用户信息
 */

public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "shop_user.db";
    private static final int DB_VERSION = 1;

    public DbOpenHelper(Context context) {
        super(context, FileUtil.getLocalExDatabaseFilePath(context, DB_NAME), null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ShopUserInfo.creatTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ShopUserInfo.dropTable());
        db.execSQL(ShopUserInfo.creatTable());
    }
}
