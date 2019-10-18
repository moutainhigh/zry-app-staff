package com.zhongmei.bty.data.db.local;

import static com.zhongmei.bty.commonmodule.database.db.local.LocalDatabaseHelper.TABLES;

import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.orm.IDBHelperFunc;
import com.zhongmei.bty.commonmodule.database.db.local.LocalDBManager;



public class LocalDBHelperFunc implements IDBHelperFunc {
    @Override
    public boolean contains(Class cls) {
        return TABLES.contains(cls);
    }

    @Override
    public DatabaseHelper getHelper() {
        return LocalDBManager.getHelper();
    }

    @Override
    public void releaseHelper(DatabaseHelper helper) {
        LocalDBManager.releaseHelper();
    }
}
