package com.zhongmei.bty.commonmodule.event;

/**
 * @version: 1.0
 * @date 2015年5月8日
 */
public abstract class EventBase {

    public final Throwable error;

    protected EventBase() {
        this(null);
    }

    protected EventBase(Throwable error) {
        this.error = error;
    }

    /**
     * 返回true表示引发此event的操作失败了
     *
     * @return
     */
    public boolean isFailure() {
        return error != null;
    }

}
