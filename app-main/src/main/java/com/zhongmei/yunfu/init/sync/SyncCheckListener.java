package com.zhongmei.yunfu.init.sync;

/**
 * Created by demo on 2018/12/15
 */
public interface SyncCheckListener {

    void onSyncCheck(boolean success, String errorMsg, Throwable err);
}
