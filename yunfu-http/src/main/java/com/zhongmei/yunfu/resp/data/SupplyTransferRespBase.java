package com.zhongmei.yunfu.resp.data;


public class SupplyTransferRespBase extends TransferResp {

        public boolean success;
    public String message;

    @Override
    public boolean isOk() {
        return success ;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return -1;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
