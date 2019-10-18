package com.zhongmei.yunfu.context.util.peony.land;



public abstract class ExtraRunnable<T> implements Runnable {

    private final T extra;

    public ExtraRunnable(T extra) {
        this.extra = extra;
    }

    protected T getExtra() {
        return extra;
    }
}
