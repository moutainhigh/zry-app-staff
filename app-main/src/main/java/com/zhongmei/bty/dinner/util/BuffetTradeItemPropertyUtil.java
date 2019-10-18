package com.zhongmei.bty.dinner.util;

import com.zhongmei.bty.basemodule.orderdish.bean.OrderDish;
import com.zhongmei.bty.entity.event.orderdishevent.EventDishPropertiesNotice;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.util.ToastUtil;


public class BuffetTradeItemPropertyUtil extends TradeItemPropertyUtil {

    @Override
    protected boolean setStandard(EventDishPropertiesNotice eventDishPropertiesNotice) {
        OrderDish orderDish = eventDishPropertiesNotice.dishPropertiesVo.getOrderDish();
        DishShop curDishShop = mRealItemBase.getDishShop();        if (orderDish != null && orderDish.getDishShop() != null) {
            DishShop newDishShop = orderDish.getDishShop();                                                                        boolean isCurBuffetDish = false;            boolean isNewBuffetDish = false;

            if ((isCurBuffetDish && !isNewBuffetDish) || (!isCurBuffetDish && isNewBuffetDish)) {
                ToastUtil.showShortToast(R.string.buffet_and_single_standard_cannot_switch);
                if (mListener != null) {
                    mListener.closePage(null);
                }
                return false;
            }

        }

        return super.setStandard(eventDishPropertiesNotice);
    }

    @Override
    protected String getModifyDishPermissionCode() {
                return null;
    }


}
