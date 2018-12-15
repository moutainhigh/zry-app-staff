package com.zhongmei.yunfu.http.processor;

import com.zhongmei.yunfu.orm.DatabaseHelper;

import java.util.concurrent.Callable;

/**
 * Desc
 *
 * @created 2017/6/12
 */
public abstract class CalmDatabaseProcessor<E> extends SaveDatabaseResponseProcessor<E> {


    @Override
    protected Callable<Void> getCallable(final DatabaseHelper helper, final E resp) throws Exception {
        return new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                transactionCallable(helper, resp);
                return null;
            }
        };
    }

    /**
     * 只需关心业务数据处事务已经在上层处理了
     *
     * @param helper
     * @param resp
     */
    protected abstract void transactionCallable(DatabaseHelper helper, E resp) throws Exception;
}