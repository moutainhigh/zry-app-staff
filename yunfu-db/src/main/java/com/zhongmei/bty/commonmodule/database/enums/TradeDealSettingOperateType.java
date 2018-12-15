package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 *

 *
 */
public enum TradeDealSettingOperateType implements ValueEnum<Integer> {

    /**
     * 自动接单（当business_type=3时，表示'开启已上餐设置'）
     */
    ACCEPT(1),

    /**
     * 自动拒绝接单（当business_type=3时，表示'自动清台'）
     */
    REFUSE(2),

    /**
     * 自动接单（当business_type=5时，表示'开启正餐自动接单'）
     */
    DINNER_ACCEPT(5),

    /**
     * 服务器自动接单
     */
    SERVER_ACCEPT(6),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    /**
     * @version: 1.0
     * @date 2015年11月23日
     */
    public static interface $ {

        /**
         * 开启已上餐设置
         */
        TradeDealSettingOperateType ENABLE_SERVING = TradeDealSettingOperateType.ACCEPT;

        /**
         * 自动清台
         */
        TradeDealSettingOperateType AUTO_CLEAR_TABLE = TradeDealSettingOperateType.REFUSE;
    }

    private final Helper<Integer> helper;

    private TradeDealSettingOperateType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private TradeDealSettingOperateType() {
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
