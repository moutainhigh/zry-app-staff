package com.zhongmei.bty.data.db;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.orm.IDBHelperFunc;

import static com.zhongmei.bty.data.db.CalmDatabaseHelper.TABLES;

/**
 * Created by demo on 2018/12/15
 */

public class CalmDBHelperFunc implements IDBHelperFunc {

    @Override
    public boolean contains(Class cls) {
        return TABLES.contains(cls);
    }

    @Override
    public DatabaseHelper getHelper() {
        return OpenHelperManager.getHelper(BaseApplication.getInstance(), CalmDatabaseHelper.class);
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
