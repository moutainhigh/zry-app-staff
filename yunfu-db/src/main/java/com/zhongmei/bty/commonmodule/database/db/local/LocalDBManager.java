package com.zhongmei.bty.commonmodule.database.db.local;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.orm.SQLiteDatabaseHelper;
import com.zhongmei.bty.commonmodule.database.db.base.SuperDBManager;



public class LocalDBManager extends SuperDBManager {

    protected static volatile SQLiteDatabaseHelper mSqLiteDatabaseHelper = null;
    protected static int instanceCount = 0;


    public static DatabaseHelper getHelper() {
        synchronized (LocalDBManager.class) {
            if (mSqLiteDatabaseHelper == null) {
                mSqLiteDatabaseHelper = new LocalDatabaseHelper(BaseApplication.sInstance);
                instanceCount = 0;
            }
            ++instanceCount;
        }
        return mSqLiteDatabaseHelper;
    }

    public static void releaseHelper() {
        if (mSqLiteDatabaseHelper == null) {
            return;
        }
        DatabaseHelper.ChangeSupportable changeSupportable = mSqLiteDatabaseHelper.getChangeSupportable();
        synchronized (LocalDBManager.class) {
            --instanceCount;
            if (instanceCount <= 0) {
                SuperDBManager.closeHelper(mSqLiteDatabaseHelper);
                mSqLiteDatabaseHelper = null;
            }
        }
        SuperDBManager.notifyChange(changeSupportable);
    }
}
