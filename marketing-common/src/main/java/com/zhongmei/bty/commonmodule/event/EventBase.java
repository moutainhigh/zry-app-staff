package com.zhongmei.bty.commonmodule.event;


public abstract class EventBase {

    public final Throwable error;

    protected EventBase() {
        this(null);
    }

    protected EventBase(Throwable error) {
        this.error = error;
    }


    public boolean isFailure() {
        return error != null;
    }

}
