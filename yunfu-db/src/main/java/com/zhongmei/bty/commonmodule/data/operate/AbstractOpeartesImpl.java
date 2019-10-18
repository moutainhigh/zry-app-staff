package com.zhongmei.bty.commonmodule.data.operate;

import android.content.Context;
import android.util.Log;

import com.zhongmei.yunfu.util.Beans;
import com.zhongmei.yunfu.context.util.NoProGuard;
import com.zhongmei.bty.commonmodule.data.operate.IOperates.ImplContext;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;


public class AbstractOpeartesImpl implements NoProGuard {

    private static final String TAG = AbstractOpeartesImpl.class.getSimpleName();

    private final ImplContext implContext;

    protected AbstractOpeartesImpl(ImplContext context) {
        this.implContext = context;
    }

    public ImplContext getImplContext() {
        return implContext;
    }

    protected Context getContext() {
        return implContext.getContext();
    }

    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        try {
            Beans.copyProperties(source, target, ignoreProperties);
        } catch (Exception e) {
            Log.i(TAG, "Copy properties error!", e);
        }
    }

    public interface DatabaseHelperCallback {
        void onDatabaseHelper(DatabaseHelper helper) throws Exception;
    }

    public void db(DatabaseHelperCallback callback) {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            if (callback != null) {
                callback.onDatabaseHelper(helper);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }
}
