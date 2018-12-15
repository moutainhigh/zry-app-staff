package com.zhongmei.bty.commonmodule.database.db.base;

import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.orm.SQLiteDatabaseHelper;

/**
 * Created by demo on 2018/12/15
 */

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
