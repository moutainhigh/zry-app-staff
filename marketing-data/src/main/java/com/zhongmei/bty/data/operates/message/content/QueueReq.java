package com.zhongmei.bty.data.operates.message.content;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 封装排队请求
 */
public class QueueReq {

    @IntDef({Type.CANCEL,
            Type.RESET_ALL,
            Type.PASS,
            Type.IN,
            Type.RESET})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
        //1-排队入场，2-排队过号，3-排队号清零，4-全部排队号清零,5-取消排队
        //type=1、2、5时必须
        //type=3时必须
        int IN = 1;
        int PASS = 2;
        int RESET = 3;
        int RESET_ALL = 4;
        int CANCEL = 5;
    }

    /**
     * 1-排队入场，2-排队过号，3-排队号清零，4-全部排队号清零,5-取消排队
     */
    private int type;

    /**
     * 唯一标志
     */
    private String serverId;

    /**
     * 队列id
     */
    private Long queueLineId;

    /**
     * 修改时间
     */
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
