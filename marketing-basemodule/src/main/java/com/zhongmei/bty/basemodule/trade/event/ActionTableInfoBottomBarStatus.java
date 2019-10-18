package com.zhongmei.bty.basemodule.trade.event;


public class ActionTableInfoBottomBarStatus {
    private BottomBarStatus status;
    private boolean isUpdateShop;
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
        TRADE_FINISH,        MOVE_DISH_CHOOSE_MODE,        MOVE_DISH_DRAG_MODE,        CHANGE_TABLE,        CHOOSE_ADD_DISH_ITEM;
    }


}
