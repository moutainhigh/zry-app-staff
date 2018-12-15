package com.zhongmei.yunfu.resp.data;

/**
 * Created by demo on 2018/12/15
 */
public class SupplyTransferRespBase extends TransferResp {

    //public Integer code;
    public boolean success;
    public String message;

    @Override
    public boolean isOk() {
        return success /*code != null && code == 200*/;
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
