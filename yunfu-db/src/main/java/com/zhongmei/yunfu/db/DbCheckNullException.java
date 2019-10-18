package com.zhongmei.yunfu.db;


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
