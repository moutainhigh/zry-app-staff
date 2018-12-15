package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.yunfu.resp.data.MindTransferRespBase;

/**
 * @Date： 2016/7/21
 * @Description:收支录入返回对象
 * @Version: 1.0
 */
public class PaymentsInputResp extends MindTransferRespBase {

    private String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
