package com.zhongmei.bty.basemodule.trade.event;

/**
 * @Date：2015年11月20日
 * @Description:开台信息页底部状态event
 * @Version: 1.0
 */
public class ActionTableInfoBottomBarStatus {
    private BottomBarStatus status;
    private boolean isUpdateShop; //是否刷新购物车

    public ActionTableInfoBottomBarStatus(BottomBarStatus status) {
        this.status = status;
        this.isUpdateShop = true;
    }

    public ActionTableInfoBottomBarStatus(BottomBarStatus status, boolean isUpdateShop) {
        this.status = status;
        this.isUpdateShop = isUpdateShop;
    }

    public BottomBarStatus getStatus() {
        return status;
    }

    public boolean isUpdateShop() {
        return isUpdateShop;
    }

    public void setStatus(BottomBarStatus status) {
        this.status = status;
    }

    public enum BottomBarStatus {
        TRADE_OPERATE,
        DISH_SERVING_MODIFY,
        OPENTABLE,
        DELETE_TRADE,
        NEW_TRADE_OPERATE,
        NO_BOTTOMBAR,
        TRADE_FINISH,//大众点评完成订单
        MOVE_DISH_CHOOSE_MODE,//移菜选择模式
        MOVE_DISH_DRAG_MODE,//移菜拖动模式
        CHANGE_TABLE,//改单保存
        CHOOSE_ADD_DISH_ITEM;//加菜单选中

    }


}
