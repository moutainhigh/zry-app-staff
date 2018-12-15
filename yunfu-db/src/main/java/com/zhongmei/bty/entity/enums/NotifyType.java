package com.zhongmei.bty.entity.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 通知类型
 */

public enum NotifyType implements ValueEnum<Integer> {

    /**
     * 手机
     */
    MOBILE(1),

    /**
     * 座机
     */
    PHONE(2),

    /**
     * 叫号
     */
    CALL(3),
    /**
     * 短信
     */
    MESSAGE(4),

    /**
     * IVR语音提醒
     */
    IVR_VOICE(5),

    /**
     * 微信叫号
     */
    WECHAT(6),
    /**
     * IVR叫号
     */
    IVR(7),
    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated __UNKNOWN__;

    private final Helper<Integer> helper;

    private NotifyType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private NotifyType() {
        helper = Helper.unknownHelper();
    }

    @Override
    public Integer value() {
        return helper.value();
    }

    @Override
    public boolean equalsValue(Integer value) {
        return helper.equalsValue(this, value);
    }

    @Override
    public boolean isUnknownEnum() {
        return helper.isUnknownEnum();
    }

    @Override
    public void setUnknownValue(Integer value) {
        helper.setUnknownValue(value);
    }

    @Override
    public String toString() {
        return "" + value();
    }

}
