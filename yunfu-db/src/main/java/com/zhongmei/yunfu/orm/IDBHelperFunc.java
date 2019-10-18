package com.zhongmei.yunfu.orm;



public interface IDBHelperFunc {
    boolean contains(Class cls);

    DatabaseHelper getHelper();

    void releaseHelper(DatabaseHelper helper);
}
