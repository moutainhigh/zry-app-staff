package com.zhongmei.bty.data.operates.message;

import com.zhongmei.yunfu.resp.data.SupplyTransferRespBase;


public class ResponseObjectExtra extends SupplyTransferRespBase {

    private Integer errorCode;

    @Override
    public Integer getCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

}
