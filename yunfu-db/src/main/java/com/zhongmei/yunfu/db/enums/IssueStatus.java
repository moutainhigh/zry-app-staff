package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * @version: 1.0
 * @date 2015年7月10日
 */
public enum IssueStatus implements ValueEnum<Integer> {

    /**
     * 暂停出单
     */
    PAUSE(1),
    /**
     * 立即出单
     */
    DIRECTLY(2),
    /**
     * 出单中
     */
    ISSUING(3),
    /**
     * 已出单
     */
    FINISHED(4),
    /**
     * 出单失败
     */
    FAILED(5),

    /**
     * 立即出单(来源于云打印)
     */
    DIRECTLY_FROM_CLOUD(6),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private IssueStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private IssueStatus() {
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
