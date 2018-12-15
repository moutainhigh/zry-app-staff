package com.zhongmei.yunfu.resp.data;

/**
 * Gateway通用返回结果
 */

public class GatewayTransferRespBase extends TransferResp {

    //    code	Integer	是	错误码；0：成功；其他 表示异常码
//    message	string	是	错误说明
//    message_uuid	string	是	消息uuid
//    result	cardInfo	是	消费卡信息

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
