package com.zhongmei.bty.basemodule.database.queue;

import com.zhongmei.yunfu.util.ValueEnum;


public enum QueueNotifyType implements ValueEnum<String> {

    UNNOTIFIED("unnotified"),


    NOTIFIED("notified"),


    NOTIFYNOROUTE("notifyNoRoute"),


    NOTIFYMISSED("notifyMissed"),


    NOTIFIEDIVR("notifiedIvr"),


    NOTIFIEDSMS("notifiedSms"),


    NOTIFIEDVOICE("notifiedVoice"),


    NOTIFIEDNORES("notifiedNoRes"),


    NOTIFIEDNOTHR("notifiedNoThr"),


    @Deprecated __UNKNOWN__;

    private final Helper<String> helper;

    private QueueNotifyType(String value) {
        helper = Helper.valueHelper(value);
    }

    private QueueNotifyType() {
        helper = Helper.unknownHelper();
    }

    @Override
    public String value() {
        return helper.value();
    }

    @Override
    public boolean equalsValue(String value) {
        return helper.equalsValue(this, value);
    }

    @Override
    public boolean isUnknownEnum() {
        return helper.isUnknownEnum();
    }

    @Override
    public void setUnknownValue(String value) {
        helper.setUnknownValue(value);
    }

    @Override
    public String toString() {
        return value();
    }

}
