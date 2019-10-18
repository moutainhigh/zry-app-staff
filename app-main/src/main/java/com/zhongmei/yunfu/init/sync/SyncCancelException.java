package com.zhongmei.yunfu.init.sync;


public class SyncCancelException extends InterruptedException {

    public SyncCancelException() {
    }

    public SyncCancelException(String detailMessage) {
        super(detailMessage);
    }

}
