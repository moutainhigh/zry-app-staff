package com.zhongmei.yunfu.orm;

import com.zhongmei.yunfu.context.util.FileUtil;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;

import android.content.Context;
import android.net.Uri;


public abstract class SQLiteDatabaseHelper extends OrmLiteSqliteOpenHelper implements DatabaseHelper {

    private final Context mContext;
    private final ChangeSupportable mChangeSupportable;
        private ConnectionSource mConnectionSource;

    protected SQLiteDatabaseHelper(Context context, String databaseName, int databaseVersion) {
        super(context, FileUtil.getLocalExDatabaseFilePath(context, databaseName), null, databaseVersion);
        mContext = context;
        mChangeSupportable = new AbstractChangeSupport() {

            @Override
            protected void notifyChange(Class<?>... tables) {
                doNotifyChange(tables);
            }

                        @Override
            protected void notifyChange(Set<Uri> tables, Map<Uri, List<Object>> dataMap) {
                                DataDistributionCenter.notifyDataChange(tables, dataMap);
            }
                    };
    }

    @Override
    public synchronized <E> E callInTransaction(Callable<E> callable) throws SQLException {
        return TransactionManager.callInTransaction(getConnectionSource(), callable);
    }

    @Override
    public ChangeSupportable getChangeSupportable() {
        return mChangeSupportable;
    }

    private void doNotifyChange(Class<?>... tables) {
        Uri[] uris = new Uri[tables.length];
        for (int i = 0; i < tables.length; i++) {
            uris[i] = DBHelperManager.getUri(tables[i]);
            mContext.getContentResolver().notifyChange(uris[i], null);
        }
        Registry.notifyChange(Arrays.asList(uris));
    }



    @Override
    public ConnectionSource getConnectionSource() {
        if (mConnectionSource == null) {
            mConnectionSource = super.getConnectionSource();
        }
        return mConnectionSource;
    }

}
