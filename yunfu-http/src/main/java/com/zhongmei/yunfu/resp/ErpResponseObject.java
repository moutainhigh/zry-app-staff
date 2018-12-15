package com.zhongmei.yunfu.resp;

import java.util.Map;

/**
 * Created by demo on 2018/12/15
 */
public class ErpResponseObject<T> {

    private int status;
    private String message;
    private String messageId;
    private Map<String, Object> errors;
    private T content;
    private Map<String, Object> returnValues;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Map<String, Object> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, Object> errors) {
        this.errors = errors;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public Map<String, Object> getReturnValues() {
        return returnValues;
    }

    public void setReturnValues(Map<String, Object> returnValues) {
        this.returnValues = returnValues;
    }

    /**
     * 操作成功
     */
    public static final int OK = 1000;

    /**
     * 判断指定的回复信息是否是成功状态，是成功状态返回true
     *
     * @param obj
     * @return
     */
    public static boolean isOk(ErpResponseObject<?> obj) {
        if (obj == null) {
            return false;
        }
        return OK == obj.getStatus();
    }

}
