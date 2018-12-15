package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;


/**
 * Created by demo on 2018/12/15
 * 备注:有些地方需要根据但前状态的value做优先级排序,优先级高的,value就越小。
 */

public enum AsyncHttpState implements ValueEnum<Integer> {

    //正在执行中
    EXCUTING(2),

    //成功
    SUCCESS(1),

    //重试中
    RETRING(0),


    //失败
    FAILED(-1),


    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private AsyncHttpState(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private AsyncHttpState() {
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
