package com.zhongmei.bty.data.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.zhongmei.bty.router.PackageURI;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;

/**
 * 通过区号获取城市
 */
public class AreaDBManager {
    private static final String TAG = AreaDBManager.class.getSimpleName();

    private static SQLiteDatabase database;

    public static final String DATABASE_FILENAME = "erea.db"; // 这个是DB文件名字

    public static final String PACKAGE_NAME = PackageURI.BTY; // 这个是自己项目包路径

    public static final String DATABASE_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME; // 获取存储位置地址

    /**
     * 导入数据
     *
     * @param context
     * @return
     */
    public static SQLiteDatabase openDatabase(Context context) {
        try {
            String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
            File dir = new File(DATABASE_PATH);
            if (!dir.exists() || !dir.isDirectory()) {
                dir.mkdir();
            }
            if (!(new File(databaseFilename)).exists()) {
                InputStream is = context.getResources().openRawResource(R.raw.erea);
                FileOutputStream fos = new FileOutputStream(databaseFilename);
                byte[] buffer = new byte[8192];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            database = SQLiteDatabase.openDatabase(databaseFilename, null, SQLiteDatabase.OPEN_READONLY);
            return database;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        return null;
    }

    /**
     * 城市名称
     *
     * @param context
     * @param area
     * @return
     */
    public static String getCity(String area) {
        SQLiteDatabase db = openDatabase(MainApplication.getInstance());
        String cityName = null;
        Cursor c = null;
        try {
            String sql = "select * from erea where area = '" + area + "'";
            c = db.rawQuery(sql, null);
            if (c.moveToFirst()) {
                cityName = c.getString(c.getColumnIndex("city"));
            }
            if (cityName != null) {
                Log.i("baidusearch", cityName);
            } else {
                Log.w("baidusearch", "Not Found!");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return cityName;
    }
}