package com.zhongmei.bty.data.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.bty.router.PackageURI;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 通过电话号码判断是否本地手机
 */
public class MobileDBManager {
    private static final String TAG = MobileDBManager.class.getSimpleName();

    private static SQLiteDatabase database;

    public static final String DATABASE_FILENAME = "dm_mobile.db"; // 这个是DB文件名字

    public static final String PACKAGE_NAME = PackageURI.BTY; // 这个是自己项目包路径

    public static final String DATABASE_PATH =
            "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME; // 获取存储位置地址

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
                InputStream is = context.getResources().openRawResource(R.raw.dm_mobile);
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
     * 判断是否本地手机号码
     *
     * @param mobile 手机号码
     * @return 本地true 外地 false
     */
    public static boolean checkLocalMobile(String mobile) {
        String area = ShopInfoCfg.getInstance().areaCode;
        String headMobile = "";
        if (mobile.length() > 7) {
            headMobile = mobile.substring(0, 7);
        }
        if (!TextUtils.isEmpty(headMobile) && !TextUtils.isEmpty(area)) {
            SQLiteDatabase db = openDatabase(MainApplication.getInstance());
            String areaCode = null;
            try {
                String sql = "select AreaCode from Dm_Mobile where MobileNumber = '" + headMobile + "'";
                Cursor c = db.rawQuery(sql, null);
                if (c.moveToFirst()) {
                    areaCode = c.getString(c.getColumnIndex("AreaCode"));
                }
                if (areaCode != null && areaCode.equals(area)) {
                    Log.i("TAG", "dbArea = " + areaCode);
                    Log.i("TAG", "localArea = " + area);
                    return true;
                }
            } catch (Exception e) {
                Log.e("TAG", "", e);
            } finally {
                if (db != null && db.isOpen()) {
                    db.close();
                }
            }
        }
        return false;
    }
}