package com.zhongmei.yunfu.sync;

/**
 * 初始化回调
 * Created by demo on 2018/12/15
 */
public interface InitCheckCallback {

    void onCheckProgress(InitCheck initCheck, int progress, String message);

    void onCheckComplete(InitCheck initCheck, boolean success, String error, Throwable err);
}
