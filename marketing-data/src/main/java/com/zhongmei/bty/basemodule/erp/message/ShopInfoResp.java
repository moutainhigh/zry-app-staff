package com.zhongmei.bty.basemodule.erp.message;

import com.zhongmei.yunfu.context.data.ShopInfo;
import com.zhongmei.yunfu.resp.IResponse;

public class ShopInfoResp extends ShopInfo implements IResponse {

    private int status;
    private int code;
    private String message;
    private String messageId;
    @Override
    public boolean isOk() {
        return status == 0;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getMessageId() {
        return messageId;
    }
}
