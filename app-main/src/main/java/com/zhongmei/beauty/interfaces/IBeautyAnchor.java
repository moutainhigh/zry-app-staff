package com.zhongmei.beauty.interfaces;

import android.view.View;

/**
 * Created by demo on 2018/12/15
 */

public interface IBeautyAnchor {
    /**
     * 收银台
     */
    void toCashier();

    /**
     * 预约
     */
    void toReserverManager();

    /**
     * 订单中心
     */
    void toTradeCenter();

    /**
     * 会员中心
     */
    void toMemberCenter();


    /**
     * 报表中心
     */
    void toReportCenter();


    /**
     * 技师管理
     */
    void toTechniciaManage();


    /**
     * 通知中心
     */
    void toNotifycationCenter();

    /**
     * 门店管理
     */
    void toShopManagerCenter();

    /**
     * 门店数据刷新同步
     *
     * @param v
     */
    void toShopSyncRefresh(View v);

    /**
     * 去任务管理中心
     */
    void toTaskCenter();
}
