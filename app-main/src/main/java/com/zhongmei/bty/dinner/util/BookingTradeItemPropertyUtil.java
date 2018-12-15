package com.zhongmei.bty.dinner.util;

/**
 * 预点菜菜品属性操作条
 * Created by demo on 2018/12/15
 */

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
