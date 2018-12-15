package com.zhongmei.yunfu.orm;

/**
 * Created by demo on 2018/12/15
 */

public interface IDBHelperFunc {
    boolean contains(Class cls);

    DatabaseHelper getHelper();

    void releaseHelper(DatabaseHelper helper);
}
