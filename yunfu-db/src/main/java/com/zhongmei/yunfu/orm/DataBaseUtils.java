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

    /*public static <T extends DataBaseInfo> List<T> cursor2List(
            Class<? extends DataBaseInfo> cls, Cursor cursor) {
        ArrayList<T> list = new ArrayList<T>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            T dataBaseInfo = (T) ReflectHelper.newInstance(cls.getName());
            dataBaseInfo.initFromCursor(cursor);
            list.add(dataBaseInfo);
        }
        return list;
    }*/

    /*public static <T> T queryOneObject(Context context, Class<T> cls, String serverId) {
        if (TextUtils.isEmpty(serverId) || context == null) {
            return null;
        }
        DataBaseInfo dataBaseInfo = (DataBaseInfo) ReflectHelper
                .newInstance(cls.getName());
        Cursor cursor = context.getApplicationContext()
                .getContentResolver()
                .query(getUri(cls), null,
                        dataBaseInfo.pkKey() + "=?",
                        new String[]{serverId}, null);
        try {
            if (cursor != null && !cursor.isAfterLast() && cursor.getCount() > 0) {
                cursor.moveToFirst();
                dataBaseInfo.initFromCursor(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return (T) dataBaseInfo;
    }*/
}
