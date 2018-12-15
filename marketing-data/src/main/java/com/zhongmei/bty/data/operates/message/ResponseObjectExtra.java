package com.zhongmei.bty.data.operates.message;

import com.zhongmei.yunfu.resp.data.SupplyTransferRespBase;

/**
 * 工程中，目前各端口定义的接口Req与Rep格式不统一，
 * 暂时增加此类。
 * <p>
 * Created by demo on 2018/12/15
 */
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
