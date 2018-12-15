package com.zhongmei.analysis;

/**
 * Created by demo on 2018/12/15
 */
public interface DataHandleListener<T> {
    /**
     * 数据操作成功
     */
    void onSuccess(T data);

    /**
     * 数据操作失败
     */
    void onFailure();
}
