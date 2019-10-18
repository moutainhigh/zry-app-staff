package com.zhongmei.yunfu.resp.data;


public class MindTransferRespBase extends TransferResp {

    protected boolean success;
    protected Integer code;
    protected String message;

    @Override
    public boolean isOk() {
        return success || hasCode(200);
    }

    public boolean isSuccess() {
        return success;
    }

    public Integer getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
