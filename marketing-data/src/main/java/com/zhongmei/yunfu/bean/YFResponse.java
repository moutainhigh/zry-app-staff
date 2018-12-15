package com.zhongmei.yunfu.bean;


import com.zhongmei.yunfu.resp.IResponse;

public class YFResponse<T> implements IResponse {

    private int status;
    private String message;
    private String messageId;
    private T content;

    public static boolean isOk(YFResponse response) {
        return response != null && response.isOk();
    }

    public boolean isOk() {
        return status == YFHttpStatus.SC_OK;
    }

    @Override
    public Integer getCode() {
        return status;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageId() {
        return messageId;
    }

    public T getContent() {
        return content;
    }
}
