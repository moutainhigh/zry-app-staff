package com.zhongmei.yunfu.http;

import java.util.Map;



public class QSResponseObject<T> {
    private int code;    private String globalMsgId;    private String message;
    private Map<String, Object> validErrors;    private T content;
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getGlobalMsgId() {
        return globalMsgId;
    }

    public void setGlobalMsgId(String globalMsgId) {
        this.globalMsgId = globalMsgId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getValidErrors() {
        return validErrors;
    }

    public void setValidErrors(Map<String, Object> validErrors) {
        this.validErrors = validErrors;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
