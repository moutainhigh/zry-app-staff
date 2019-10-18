package com.zhongmei.bty.dinner.util;



public class BookingTradeItemPropertyUtil extends TradeItemPropertyUtil {

    @Override
    protected boolean isCanModify() {
        return true;
    }

    @Override
    protected String getModifyDishPermissionCode() {
        return null;
    }

    @Override
    protected boolean isNeedInventory() {
        return false;
    }
}
