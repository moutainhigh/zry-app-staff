package com.zhongmei.yunfu.sync;


public interface InitCheckCallback {

    void onCheckProgress(InitCheck initCheck, int progress, String message);

    void onCheckComplete(InitCheck initCheck, boolean success, String error, Throwable err);
}
