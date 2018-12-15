package com.zhongmei.yunfu.resp.data;

/**
 * Created by demo on 2018/12/15
 */
public class LoyaltyMindTransferResp<T> extends MindTransferResp<T> {

    @Override
    public boolean isOk() {
        return code != null && code == 1;
    }
}
