package com.zhongmei.bty.data.operates.message.content;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 封装排队请求
 */
public class QueueRecoverInvalidReq {

    String synFlag;

    Long modifyDateTime;

    public void setSynFlag(String synFlag) {
        this.synFlag = synFlag;
    }

    public void setModifyDateTime(Long modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }
}
