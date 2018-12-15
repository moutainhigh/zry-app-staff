package com.zhongmei.bty.basemodule.discount.enums;

import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.yunfu.util.ValueEnum;
import com.zhongmei.yunfu.db.enums.OperateType;

/**
 * 折扣表
 *
 * @date:2016年2月15日下午5:59:23
 */
public enum DiscountType implements ValueEnum<Integer> {
    //整单折扣
    ALLDISCOUNT(1),
    //	整单让价
    ALLLET(2),
    //	批量折扣
    BATCHDISCOUNT(3),
    //	批量让价
    BATCHLET(4),
    //整单赠送
    ALL_FREE(5),
    //批量赠送
    BATCH_GIVE(6),
    //批量问题菜品
    BATCH_PROBLEM(7),
    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;


    /**
     * 获取当前交易理由类型
     *
     * @return
     */
    public static OperateType getOperateType(DiscountType currentDiscountType) {
        switch (currentDiscountType) {
            case ALLDISCOUNT:
                return OperateType.TRADE_DISCOUNT;
            case ALLLET:
                return OperateType.TRADE_REBATE;
            case BATCHDISCOUNT:
                return OperateType.TRADE_SINGLE_DISCOUNT;
            case BATCHLET:
                return OperateType.TRADE_SINGLE_REBATE;
            case ALL_FREE:
                return OperateType.TRADE_BANQUET;
            case BATCH_GIVE:
                return OperateType.ITEM_GIVE;
            case BATCH_PROBLEM:
                return OperateType.TRADE_SINGLE_PROBLEM;
            default:
                return OperateType.__UNKNOWN__;
        }
    }

    /**
     * 获取理由类型
     *
     * @param currentDiscountType
     * @return
     */
    public static ReasonType getReasonType(DiscountType currentDiscountType) {
        switch (currentDiscountType) {
            case ALLDISCOUNT:
            case ALLLET:
                return ReasonType.TRADE_DISCOUNT;
            case ALL_FREE:
                return ReasonType.__UNKNOWN__; //TRADE_BANQUET|TRADE_FREE这里有可能是名单，这里的类型不确定
            case BATCHDISCOUNT:
            case BATCHLET:
            case BATCH_PROBLEM:
                return ReasonType.TRADE_SINGLE_DISCOUNT;
            case BATCH_GIVE:
                return ReasonType.ITEM_GIVE;
            default:
                return ReasonType.__UNKNOWN__;
        }
    }

    private final Helper<Integer> helper;

    private DiscountType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private DiscountType() {
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
