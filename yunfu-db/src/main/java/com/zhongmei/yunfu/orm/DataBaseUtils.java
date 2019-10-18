package com.zhongmei.yunfu.orm;

import android.net.Uri;
import android.text.TextUtils;

import com.j256.ormlite.table.DatabaseTable;

public class DataBaseUtils {
    private static String sAuthority;
    private static String sUriHeader;

    public static void init(String authority) {
        sAuthority = authority;
        sUriHeader = "content://" + sAuthority + "/";
    }

    public static String getAuthority() {
        if (TextUtils.isEmpty(sAuthority)) {
            throw new RuntimeException("DataBaseUtils should be initialized");
        }
        return sAuthority;
    }

    public static String getUriHeader() {
        if (TextUtils.isEmpty(sUriHeader)) {
            throw new RuntimeException("DataBaseUtils should be initialized");
        }
        return sUriHeader;
    }

    public static Uri getUri(Class<?> cls) {
        if (TextUtils.isEmpty(sUriHeader)) {
            throw new RuntimeException("DataBaseUtils should be initialize");
        }
        return Uri.parse(sUriHeader + getTableName(cls));
    }

    public static String getTableName(Class<?> cls) {
        DatabaseTable tableName = cls.getAnnotation(DatabaseTable.class);
        if (tableName == null) {
            throw new IllegalArgumentException("donot mark @TableName ");
        }
        return tableName.tableName();
    }




}
