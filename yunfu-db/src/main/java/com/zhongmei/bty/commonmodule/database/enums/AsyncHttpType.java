package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 异步网络请求业务
 */
public enum AsyncHttpType implements ValueEnum<Integer> {
    //	改单操作
    MODIFYTRADE(1),
    //	收银操作
    CASHER(2),
    //开台操作
    OPENTABLE(3),
    //联台主单改单
    UNION_MAIN_MODIFYTRADE(4),
    //联台子单改单
    UNION_SUB_MODIFYTRADE(5),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private AsyncHttpType(Integer value) {
        // TODO Auto-generated constructor stub
        helper = Helper.valueHelper(value);
    }

    private AsyncHttpType() {
        // TODO Auto-generated constructor stub
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
