package com.zhongmei.yunfu.resp.data;

/**
 * Created by demo on 2018/12/15
 */
public class Mind1000TransferResp<T> extends MindTransferResp<T> {

    @Override
    public boolean isOk() {
        return hasCode(1000);
    }
}
