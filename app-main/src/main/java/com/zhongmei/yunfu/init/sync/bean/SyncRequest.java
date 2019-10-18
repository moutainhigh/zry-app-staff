package com.zhongmei.yunfu.init.sync.bean;

import android.content.Context;

import com.zhongmei.yunfu.init.sync.SyncConstant;
import com.zhongmei.yunfu.init.sync.SyncServiceUtil;
import com.zhongmei.yunfu.context.util.NoProGuard;


public class SyncRequest implements NoProGuard {

    private SyncContent content;
    private int syncCount;
    private int isInit;

    public void setContent(SyncContent content) {
        this.content = content;
    }

    public void setSyncCount(int syncCount) {
        this.syncCount = syncCount;
    }

    public void setIsInit(int isInit) {
        this.isInit = isInit;
    }

    public static SyncRequest create(Context context, SyncContent content, boolean notify) {
        return create(context, content, notify ? 0 : 1);
    }


    public static SyncRequest create(Context context, SyncContent content, int syncType) {
        SyncRequest request = new SyncRequest();
        request.setContent(content);
        request.setSyncCount(SyncConstant.DEFAULT_SYNC_COUNT);
        boolean isInit = SyncServiceUtil.isNeedInit();
        if (isInit) {
            request.setIsInit(0);
        } else {
            request.setIsInit(1);
        }
        return request;
    }
}
