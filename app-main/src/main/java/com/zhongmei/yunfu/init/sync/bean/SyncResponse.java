package com.zhongmei.yunfu.init.sync.bean;

import com.zhongmei.yunfu.bean.YFResponse;


/**
 * 封装同步用的Response
 *
 * @version: 1.0
 * @date 2015年4月15日
 */
public class SyncResponse extends YFResponse<SyncContent> {

    private int lastSyncStatus;

    public int getLastSyncStatus() {
        return lastSyncStatus;
    }

    public boolean isSyncSuccess() {
        return lastSyncStatus == 0;
    }
}
