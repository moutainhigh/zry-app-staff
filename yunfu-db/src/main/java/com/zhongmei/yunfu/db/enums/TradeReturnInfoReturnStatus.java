package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**

 */
public enum TradeReturnInfoReturnStatus implements ValueEnum<Integer> {

    /**
     * 申请退单
     */
    APPLY(1),

    /**
     * 商户同意退单
     */
    AGREE(2),

    /**
     * 商户拒绝退单
     */
    REFUSE(3),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private TradeReturnInfoReturnStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private TradeReturnInfoReturnStatus() {
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
