package com.zhongmei.yunfu;

import com.zhongmei.yunfu.context.util.manager.SwitchServerManager;

/**
 * Created by demo on 2018/12/15
 */

public abstract class ServerKey {

    /**
     * 自助餐改单接口地址
     *
     * @return
     */
    public static String tradeModifyBuffet() {
//        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/buffet/modify";
        return SwitchServerManager.getInstance().getServerKey() + "/CalmRouter/v2/trade/buffet/modify";
    }

    /**
     * 正餐接受并设置桌台
     *
     * @Title: dinnerSetTableAndAccept
     * @Return String 返回类型
     */
    public static String dinnerSetTableAndAccept() {
        return SwitchServerManager.getInstance().getServerKey() + "/CalmRouter/v1/trade/dinnerReceiveAndSetTable";
    }

    /**
     * 改单接口地址
     *
     * @return
     */
    public static String newTradeModifyDinner() {
        return SwitchServerManager.getInstance().getServerKey() + "/CalmRouter/v2/trade/modify";
    }

    /**
     * 团餐改单
     *
     * @return
     */
    public static String getGroupModify() {
//        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/dinner/group/modify";
        return SwitchServerManager.getInstance().getServerKey() + "/CalmRouter/v2/trade/dinner/group/modify";
    }

    /**
     * 修改普通订单菜品打印状态
     *
     * @Title: modifyPrintStatus
     * @Return String 返回类型
     */
    public static String modifyPrintStatus() {
        return SwitchServerManager.getInstance().getServerKey() + "/CalmRouter/v1/trade/modifyPrintStatus";
    }
}
