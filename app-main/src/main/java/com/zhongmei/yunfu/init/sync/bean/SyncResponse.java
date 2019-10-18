package com.zhongmei.yunfu.init.sync.bean;

import com.zhongmei.yunfu.bean.YFResponse;



public class SyncResponse extends YFResponse<SyncContent> {

    private int lastSyncStatus;

    public int getLastSyncStatus() {
        return lastSyncStatus;
    }

    public boolean isSyncSuccess() {
        return lastSyncStatus == 0;
    }
}
