package com.zhongmei.yunfu.monitor;

import com.zhongmei.yunfu.resp.AbsEventListener;
import com.zhongmei.yunfu.resp.UserActionEvent;

/**
 * Desc
 *
 * @created 2017/7/25
 */
public abstract class AbsEventListenerProxy extends AbsEventListener {

    public AbsEventListenerProxy(UserActionEvent event) {
        super(event);
    }

    public AbsEventListenerProxy(String eventName) {
        super(eventName);
    }
}
