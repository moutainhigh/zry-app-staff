package com.zhongmei.bty.basemodule.shopmanager.handover.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * @Date：2016年2月17日 下午5:22:58
 * @Description: cash_handover_config 表的key种类
 * @Version: 1.0
 */
public enum CashHandoverConfigKey implements ValueEnum<Integer> {

    /**
     * 表示交接班的交接方式
     */
    HANDOVERSTYLE(1),
    /**
     * 打印出单设置
     */
    PRINTISSUESETTING(2),
    /**
     * 打印定时时间
     */
    PRINTTIME(3),
    /**
     * 收银小数点保留位数
     */
    CARRYLIMIT(4),
    /**
     * 收银进位规则
     */
    CARRYRULE(5),
    /**
     * 正餐一台多单设置
     */
    DINNERBILLORISTYLE(6),
    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private CashHandoverConfigKey(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private CashHandoverConfigKey() {
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
