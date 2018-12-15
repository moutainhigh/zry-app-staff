package com.zhongmei.yunfu.monitor;

import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.resp.AbsEventListener;
import com.zhongmei.yunfu.resp.UserActionEvent;

/**
 * Created by demo on 2018/12/15
 */
public abstract class EventListener<T> extends AbsEventListener implements Response.Listener<T> {

    public EventListener(UserActionEvent event) {
        super(event);
    }

    public EventListener(String eventName) {
        super(eventName);
    }
}
