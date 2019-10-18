package com.zhongmei.yunfu.http.processor;

import com.zhongmei.yunfu.orm.DatabaseHelper;

import java.util.concurrent.Callable;


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


    protected abstract void transactionCallable(DatabaseHelper helper, E resp) throws Exception;
}