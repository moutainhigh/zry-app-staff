package com.zhongmei.yunfu.resp.data;



public class GatewayTransferResp<T> extends GatewayTransferRespBase {

    protected T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
