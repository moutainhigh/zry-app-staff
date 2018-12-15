package com.zhongmei.beauty.interfaces;

/**
 * Created by demo on 2018/12/15
 */

public interface IBeautyOperator {
    /**
     * 订单管理
     */
    void toTrades();

    /**
     * 工作太管理
     */
    void toTableManager();

    /**
     * 开单
     */
    void toCreateTrade();

    /**
     * 开卡
     */
    void toCreateCrad();

    /**
     * 充值
     */
    void toCharge();

    /**
     * 添加会员
     */
    void toCreateMember();

    /**
     * 添加预约
     */
    void toCreateReserver();
}
