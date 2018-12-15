package com.zhongmei.yunfu.resp.data;

/**
 * Created by demo on 2018/12/15
 */

public abstract class TransferResp {

    public abstract boolean isOk();

    public abstract Integer getCode();

    public abstract String getMessage();

    public boolean hasCode(int status) {
        return getCode() != null && getCode() == status;
    }

    public static boolean isOk(TransferResp resp) {
        return resp != null && resp.isOk();
    }
}
