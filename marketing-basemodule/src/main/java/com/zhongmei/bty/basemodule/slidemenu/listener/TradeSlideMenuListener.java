package com.zhongmei.bty.basemodule.slidemenu.listener;

/**
 * Created by demo on 2018/12/15
 */

public interface TradeSlideMenuListener extends SlideMenuListener {

    /**
     * 跳转到桌台／点菜
     */
    boolean switchToTrade();

    /**
     * 跳转到订单中心
     */
    boolean switchToOrderCenter();

    /**
     * 跳转到门店管理
     */
    boolean switchToShopManagement();

    /**
     * 跳转到报表中心
     */
    boolean switchToFormCenter();

}
