package com.zhongmei.bty.commonmodule.database.db.base;

import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.orm.SQLiteDatabaseHelper;



public abstract class SuperDBManager {
    private static final String TAG = SuperDBManager.class.getSimpleName();

    protected static void closeHelper(SQLiteDatabaseHelper sqLiteDatabaseHelper) {
        if (sqLiteDatabaseHelper != null) {
            sqLiteDatabaseHelper.close();
        }
    }

    protected static void notifyChange(DatabaseHelper.ChangeSupportable changeSupportable) {
        if (changeSupportable != null && changeSupportable.isChange()) {
            changeSupportable.notifyChange();
        }
    }

}
