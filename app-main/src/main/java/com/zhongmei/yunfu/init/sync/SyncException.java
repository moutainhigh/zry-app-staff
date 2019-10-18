package com.zhongmei.yunfu.init.sync;


public class SyncException extends Exception {

    String messageId;

    public SyncException(String messageId) {
        this(messageId, (String) null);
    }

    public SyncException(String messageId, String detailMessage) {
        this(messageId, detailMessage, null);
    }

    public SyncException(String messageId, String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.messageId = messageId;
    }

    public SyncException(String messageId, Throwable throwable) {
        super(throwable);
        this.messageId = messageId;
    }

    @Override
    public String getMessage() {
        return String.format("[msgId=%s]%s", messageId, super.getMessage());
    }
}
