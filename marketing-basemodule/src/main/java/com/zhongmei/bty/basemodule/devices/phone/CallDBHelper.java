package com.zhongmei.bty.basemodule.devices.phone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zhongmei.bty.basemodule.devices.phone.entity.CallHistory;
import com.zhongmei.bty.basemodule.devices.phone.operates.CallServiceImpl;
import com.zhongmei.yunfu.context.base.BaseApplication;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 负责所有表的初始化及升级更改维护, </br>
 *
 * @date 2014-10-21
 */
public final class CallDBHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = CallDBHelper.class.getSimpleName();

    private static final String DB_NAME = "calm_phone.db";
    private static final int DB_VERSION = 2;

    private static CallDBHelper instance;
    private static AtomicInteger count = new AtomicInteger();

    private static final Object LOCK = new Object();

    private CallDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, CallHistory.class);
        } catch (SQLException e) {
            Log.e(TAG, "", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {

        if (newVersion > oldVersion) {
            try {
                TableUtils.dropTable(connectionSource, CallHistory.class, true);
                onCreate(db, connectionSource);
            } catch (SQLException e) {
                Log.e(TAG, "", e);
            }
        }
    }

    private static CallDBHelper getHelper() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new CallDBHelper(BaseApplication.sInstance);
            }
            count.incrementAndGet();

            return instance;
        }
    }

    private static void release() {
        synchronized (LOCK) {
            if (count.decrementAndGet() <= 0) {
                if (instance != null) {
                    instance.close();
                    instance = null;
                }
            }
        }
    }


    public static <T extends Service> T getService(Class<T> interfaceClazz) {
        Service service = null;
        if (interfaceClazz != null) {
            if (interfaceClazz == CallService.class) {
                service = new CallServiceImpl();
            }
        }
        return (T) service;
    }

    public static abstract class AbsService implements Service {

        @Override
        public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws SQLException {
            return CallDBHelper.getHelper().getDao(clazz);
        }

        @Override
        public void release() {
            CallDBHelper.release();
        }

        @Override
        public void exeSQL(String sql) throws SQLException {
            CallDBHelper.getHelper().getWritableDatabase().execSQL(sql);
        }
    }

    public interface Service {
        <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws SQLException;

        void exeSQL(String sql) throws SQLException;

        void release();
    }
}
