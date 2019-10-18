package com.zhongmei.bty.entity.event;


public class AliPayLoginEvent {

    public String msg;
    public String msgId;
    public int result;
    public AliPayLogin data;

    public class AliPayLogin {
        public Long accountId;
    }
}
