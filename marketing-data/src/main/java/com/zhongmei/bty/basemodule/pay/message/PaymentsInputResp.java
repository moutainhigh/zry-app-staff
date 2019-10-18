package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.yunfu.resp.data.MindTransferRespBase;


public class PaymentsInputResp extends MindTransferRespBase {

    private String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
