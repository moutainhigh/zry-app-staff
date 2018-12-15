package com.zhongmei.yunfu.resp;

import com.zhongmei.yunfu.context.util.NoProGuard;

/**
 * 实时请求返回数据的封装
 *
 * @param <T>
 */
public abstract class ResponseBoss implements NoProGuard {

    private int status;
    private int code;
    private String message;
    private String messageId;//适配erp接口

    public boolean isOk() {
        return status == 0;
    }

    public int getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageId() {
        return messageId;
    }
}
