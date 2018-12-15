package com.zhongmei.bty.basemodule.database.queue;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 排队提醒状态
 */
public enum QueueNotifyType implements ValueEnum<String> {
    /**
     * 尚未提醒
     */
    UNNOTIFIED("unnotified"),

    /**
     * 已通知
     */
    NOTIFIED("notified"),

    /**
     * 无法接通
     */
    NOTIFYNOROUTE("notifyNoRoute"),

    /**
     * 无人接听
     */
    NOTIFYMISSED("notifyMissed"),

    /**
     * 自动语音发出/已发语音
     */
    NOTIFIEDIVR("notifiedIvr"),

    /**
     * 已发短信
     */
    NOTIFIEDSMS("notifiedSms"),

    /**
     * 已叫号
     */
    NOTIFIEDVOICE("notifiedVoice"),

    /**
     * 无人接听
     */
    NOTIFIEDNORES("notifiedNoRes"),

    /**
     * 无法拨通
     */
    NOTIFIEDNOTHR("notifiedNoThr"),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
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
