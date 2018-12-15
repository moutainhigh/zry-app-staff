package com.zhongmei.yunfu.http;

import java.util.Map;

/**
 * QS Server实时接口返回结果格式
 */

public class QSResponseObject<T> {
    private int code;//状态码
    private String globalMsgId;//全局消息ID
    private String message;
    private Map<String, Object> validErrors;//校验错误信息
    private T content;//内容信息

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
