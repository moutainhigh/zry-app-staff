package com.zhongmei.yunfu.monitor;

import com.zhongmei.yunfu.resp.AbsEventListener;
import com.zhongmei.yunfu.resp.UserActionEvent;


public abstract class AbsEventListenerProxy extends AbsEventListener {

    public AbsEventListenerProxy(UserActionEvent event) {
        super(event);
    }

    public AbsEventListenerProxy(String eventName) {
        super(eventName);
    }
}
