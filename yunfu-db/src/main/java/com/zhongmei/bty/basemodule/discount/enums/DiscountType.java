package com.zhongmei.bty.basemodule.discount.enums;

import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.yunfu.util.ValueEnum;
import com.zhongmei.yunfu.db.enums.OperateType;


public enum DiscountType implements ValueEnum<Integer> {
        ALLDISCOUNT(1),
        ALLLET(2),
        BATCHDISCOUNT(3),
        BATCHLET(4),
        ALL_FREE(5),
        BATCH_GIVE(6),
        BATCH_PROBLEM(7),

    @Deprecated
    __UNKNOWN__;



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


    public static ReasonType getReasonType(DiscountType currentDiscountType) {
        switch (currentDiscountType) {
            case ALLDISCOUNT:
            case ALLLET:
                return ReasonType.TRADE_DISCOUNT;
            case ALL_FREE:
                return ReasonType.__UNKNOWN__;             case BATCHDISCOUNT:
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
