package com.zhongmei.yunfu.resp.data;


public class Mind1000TransferResp<T> extends MindTransferResp<T> {

    @Override
    public boolean isOk() {
        return hasCode(1000);
    }
}
