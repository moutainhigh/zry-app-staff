package com.zhongmei.yunfu.db;

/**
 * 用于检查字段非空异常
 * Created by demo on 2018/12/15
 */
public class DbCheckNullException extends RuntimeException {

    public DbCheckNullException() {
    }

    public DbCheckNullException(String detailMessage) {
        super(detailMessage);
    }

    public DbCheckNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbCheckNullException(Throwable cause) {
        super(cause);
    }
}
