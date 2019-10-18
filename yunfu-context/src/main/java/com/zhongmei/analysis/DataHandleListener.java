package com.zhongmei.analysis;


public interface DataHandleListener<T> {

    void onSuccess(T data);


    void onFailure();
}
