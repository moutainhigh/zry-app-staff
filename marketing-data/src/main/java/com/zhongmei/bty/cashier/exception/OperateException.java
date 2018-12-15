package com.zhongmei.bty.cashier.exception;

/**
 * Created by demo on 2018/12/15
 */
public class OperateException extends RuntimeException {
    public OperateException() {
    }

    public OperateException(String detailMessage) {
        super(detailMessage);
    }

    public OperateException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public OperateException(Throwable throwable) {
        super(throwable);
    }
}
