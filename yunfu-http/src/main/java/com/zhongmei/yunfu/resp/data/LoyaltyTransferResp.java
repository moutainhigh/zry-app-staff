package com.zhongmei.yunfu.resp.data;


public class LoyaltyTransferResp<T> extends TransferResp {

    private Integer code;
    public String errorMessage;
    public T result;

    @Override
    public boolean isOk() {
        return hasCode(1);
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
