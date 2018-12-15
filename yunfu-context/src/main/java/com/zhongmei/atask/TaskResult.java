package com.zhongmei.atask;

/**
 * Created by demo on 2018/12/15
 */
public interface TaskResult<T> {

    void onResult(T result);

}
