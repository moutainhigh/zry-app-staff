package com.zhongmei.yunfu.monitor;

import com.zhongmei.yunfu.net.builder.NetworkRequest;
import com.zhongmei.yunfu.resp.AbsEventListener;
import com.zhongmei.yunfu.resp.UserActionEvent;


public abstract class CalmResponseListener<T> extends AbsEventListener implements NetworkRequest.OnErrorListener, NetworkRequest.OnSuccessListener<T> {

    public CalmResponseListener() {
        super((String) null);
    }

    public CalmResponseListener(UserActionEvent event) {
        super(event.eventName);
    }
}
