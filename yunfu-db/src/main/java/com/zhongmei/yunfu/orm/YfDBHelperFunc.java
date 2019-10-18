package com.zhongmei.yunfu.orm;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.zhongmei.yunfu.context.base.BaseApplication;



public class YfDBHelperFunc implements IDBHelperFunc {

    @Override
    public boolean contains(Class cls) {
        return YfDatabaseHelper.TABLES.contains(cls);
    }

    @Override
    public DatabaseHelper getHelper() {
        return OpenHelperManager.getHelper(BaseApplication.getInstance(), YfDatabaseHelper.class);
    }

    @Override
    public void releaseHelper(DatabaseHelper helper) {
        DatabaseHelper.ChangeSupportable changeSupportable = helper.getChangeSupportable();
        OpenHelperManager.releaseHelper();
        if (changeSupportable != null && changeSupportable.isChange()) {
            changeSupportable.notifyChange();
        }
    }
}
