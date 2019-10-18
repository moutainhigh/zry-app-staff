package com.zhongmei.yunfu.init.sync.event;


import com.zhongmei.bty.commonmodule.event.EventBase;


public class SyncEndEvent extends EventBase {

    public SyncEndEvent(Throwable error) {
        super(error);
    }

}
