package com.zhongmei.yunfu.http.processor;

import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;

import java.util.concurrent.Callable;


public abstract class SaveDatabaseResponseProcessor<R> extends CalmResponseProcessor<R> {
    private static final String TAG = SaveDatabaseResponseProcessor.class.getName();


    @Override
    protected void saveToDatabase(R resp) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Callable<Void> callable = getCallable(helper, resp);
            if (callable != null) {
                helper.callInTransaction(callable);
            }
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }


    protected abstract Callable<Void> getCallable(final DatabaseHelper helper, final R resp) throws Exception;
}
