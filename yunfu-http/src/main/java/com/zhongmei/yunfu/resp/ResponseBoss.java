package com.zhongmei.yunfu.resp;

import com.zhongmei.yunfu.context.util.NoProGuard;


public abstract class ResponseBoss implements NoProGuard {

    private int status;
    private int code;
    private String message;
    private String messageId;
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
