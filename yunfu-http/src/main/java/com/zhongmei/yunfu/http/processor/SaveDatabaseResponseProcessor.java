package com.zhongmei.yunfu.http.processor;

import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;

import java.util.concurrent.Callable;

/**
 * Created by demo on 2018/12/15
 */
public abstract class SaveDatabaseResponseProcessor<R> extends CalmResponseProcessor<R> {
    private static final String TAG = SaveDatabaseResponseProcessor.class.getName();

    /**
     * 回复操作成功后将调用此方法将回复数据保存到数据库中
     *
     * @param resp
     */
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

    /**
     * 返回要在同一个数据库事务中执行的操作
     *
     * @param helper
     * @param resp
     * @return
     */
    protected abstract Callable<Void> getCallable(final DatabaseHelper helper, final R resp) throws Exception;
}
