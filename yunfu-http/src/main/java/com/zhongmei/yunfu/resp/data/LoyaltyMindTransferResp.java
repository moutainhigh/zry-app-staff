package com.zhongmei.yunfu.resp.data;


public class LoyaltyMindTransferResp<T> extends MindTransferResp<T> {

    @Override
    public boolean isOk() {
        return code != null && code == 1;
    }
}
