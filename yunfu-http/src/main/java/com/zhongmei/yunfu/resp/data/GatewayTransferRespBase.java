package com.zhongmei.yunfu.resp.data;



public class GatewayTransferRespBase extends TransferResp {


    private Integer code;
    private String message;
    private String message_uuid;

    @Override
    public boolean isOk() {
        return hasCode(0);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_uuid() {
        return message_uuid;
    }

    public void setMessage_uuid(String message_uuid) {
        this.message_uuid = message_uuid;
    }
}
