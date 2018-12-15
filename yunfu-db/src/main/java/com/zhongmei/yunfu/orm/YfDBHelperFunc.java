package com.zhongmei.yunfu.orm;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.zhongmei.yunfu.context.base.BaseApplication;

/**
 * Created by demo on 2018/12/15
 */

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
//		// 通知数据更改
//		if (helper.getChangeSupportable().isChange()) {
//			helper.getChangeSupportable().notifyChange();
//		}
        OpenHelperManager.releaseHelper();
        if (changeSupportable != null && changeSupportable.isChange()) {
            changeSupportable.notifyChange();
        }
    }
}
