package com.zhongmei.bty.basemodule.trade.enums;

/**
 * 桌台状态
 *
 * @version: 1.0
 * @date 2015年9月20日
 */
public enum DinnertableStatus {

    /**
     * 未出单
     */
    UNISSUED(0),

    /**
     * 已出单
     */
    ISSUED(1),

    /**
     * 已上菜
     */
    SERVING(2),

    /**
     * 未使用
     */
    EMPTY(3),

    /**
     * 未清台
     */
    DONE(4);

    public final int value;

    private DinnertableStatus(int value) {
        this.value = value;
    }

}
