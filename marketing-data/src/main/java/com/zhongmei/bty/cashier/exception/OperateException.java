package com.zhongmei.bty.cashier.exception;


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
