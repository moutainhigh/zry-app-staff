package com.zhongmei.yunfu.init.sync.event;


import com.zhongmei.bty.commonmodule.event.EventBase;

/**
 * 同步执行完成时发送的通知
 *
 * @version: 1.0
 * @date 2015年6月18日
 */
public class SyncEndEvent extends EventBase {

    public SyncEndEvent(Throwable error) {
        super(error);
    }

}
