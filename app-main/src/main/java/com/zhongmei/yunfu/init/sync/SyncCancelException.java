package com.zhongmei.yunfu.init.sync;

/**
 * 用户任务取消异常
 * Created by demo on 2018/12/15
 */
public class SyncCancelException extends InterruptedException {

    public SyncCancelException() {
    }

    public SyncCancelException(String detailMessage) {
        super(detailMessage);
    }

}
