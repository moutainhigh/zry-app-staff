package com.zhongmei.yunfu.init.sync;


public interface SyncCheckListener {

    void onSyncCheck(boolean success, String errorMsg, Throwable err);
}
