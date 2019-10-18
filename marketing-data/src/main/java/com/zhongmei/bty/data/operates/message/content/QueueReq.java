package com.zhongmei.bty.data.operates.message.content;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class QueueReq {

    @IntDef({Type.CANCEL,
            Type.RESET_ALL,
            Type.PASS,
            Type.IN,
            Type.RESET})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
                                int IN = 1;
        int PASS = 2;
        int RESET = 3;
        int RESET_ALL = 4;
        int CANCEL = 5;
    }


    private int type;


    private String serverId;


    private Long queueLineId;


    private Long lastSyncMarker;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Long getQueueLineId() {
        return queueLineId;
    }

    public void setQueueLineId(Long queueLineId) {
        this.queueLineId = queueLineId;
    }

    public Long getLastSyncMarker() {
        return lastSyncMarker;
    }

    public void setLastSyncMarker(Long lastSyncMarker) {
        this.lastSyncMarker = lastSyncMarker;
    }


}
