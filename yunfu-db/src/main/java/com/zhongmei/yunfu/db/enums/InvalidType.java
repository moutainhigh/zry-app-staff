package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * @version: 1.0
 * @date 2015年10月30日
 */
public enum InvalidType implements ValueEnum<Integer> {

    /**
     * 退菜
     */
    RETURN_QTY(1),

    /**
     * 拆单支付
     */
    SPLIT(2),

    /**
     * 被删除
     */
    DELETE(3),

    /**
     * 改菜
     */
    MODIFY_DISH(4),

    /**
     * 退菜被撤回
     */
    DELETE_RETURN_QTY(5),

    /**
     * 改菜被撤回
     */
    DELETE_MODIY_DISH(6),
    /**
     * 联台主单点菜时，子单虚拟的菜，用于服务器端占位
     */
    SUB_BATCH_VITURAL(8),


    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private InvalidType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private InvalidType() {
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
