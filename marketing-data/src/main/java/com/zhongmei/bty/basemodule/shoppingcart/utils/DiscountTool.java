package com.zhongmei.bty.basemodule.shoppingcart.utils;

import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.yunfu.db.enums.PrivilegeType;

/**
 * 折扣工具类，处理整单、单品的折扣和让价
 */
public class DiscountTool {

    /**
     * @Title: isSinglePrivilege
     * @Description: 是否有单品优惠
     * @Param @param item
     * @Param @return TODO
     * @Return boolean 返回类型
     */
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
