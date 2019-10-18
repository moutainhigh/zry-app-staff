package com.zhongmei.bty.basemodule.shoppingcart.utils;

import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.yunfu.db.enums.PrivilegeType;


public class DiscountTool {


    public static boolean isSinglePrivilege(IShopcartItemBase item) {
        if (item == null) {
            return false;
        }
        TradePrivilege tradePrivilege = item.getPrivilege();
        if (tradePrivilege == null) {
            return false;
        }
        PrivilegeType type = tradePrivilege.getPrivilegeType();
        if (type == PrivilegeType.DISCOUNT
                || type == PrivilegeType.REBATE
                || type == PrivilegeType.FREE
                || type == PrivilegeType.GIVE
                || type == PrivilegeType.AUTO_DISCOUNT
                || type == PrivilegeType.MEMBER_PRICE
                || type == PrivilegeType.MEMBER_REBATE
                || item.getCouponPrivilegeVo() != null) {
            return true;
        }

        return false;
    }

}
