package com.zhongmei.bty.entity.event;

/**
 * @since 2018.06.13.
 */
public class AliPayLoginEvent {

    public String msg;
    public String msgId;
    public int result;
    public AliPayLogin data;

    public class AliPayLogin {
        public Long accountId;
    }
}
