package com.zhongmei.bty.dinner.util;

import com.zhongmei.bty.basemodule.orderdish.bean.OrderDish;
import com.zhongmei.bty.entity.event.orderdishevent.EventDishPropertiesNotice;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.util.ToastUtil;

/**
 * Created by demo on 2018/12/15
 */
public class BuffetTradeItemPropertyUtil extends TradeItemPropertyUtil {

    @Override
    protected boolean setStandard(EventDishPropertiesNotice eventDishPropertiesNotice) {
        OrderDish orderDish = eventDishPropertiesNotice.dishPropertiesVo.getOrderDish();
        DishShop curDishShop = mRealItemBase.getDishShop();//当前菜品
        if (orderDish != null && orderDish.getDishShop() != null) {
            DishShop newDishShop = orderDish.getDishShop();//新切换的规格商品
            //过滤掉不同类型（单点和自助）规格的菜品
            //List<BuffetComboVo> buffetComboVoList = BuffetManager.getInstance().getBuffetComboList();
            //BuffetComboVo buffetComboVo = BuffetManager.getInstance().getCurComboVo(buffetComboVoList, mShoppingCart.getOrder());
            //DishCarte dishCarte = buffetComboVo.getDishCarte();
            //是否是自助餐标下的菜品
            boolean isCurBuffetDish = false;//当前菜品
            boolean isNewBuffetDish = false;//新切换的规格商品
            /*if (dishCarte != null && dishCarte.getId() != null) {
                List<DishShop> buffetDishShopList = DishCache.getDishCarteDetailHolder().getDishShopsByCarte(dishCarte.getUuid());
                if (buffetDishShopList != null) {
                    isCurBuffetDish = buffetDishShopList.contains(curDishShop);
                    if (curDishShop != null) {
                        isNewBuffetDish = buffetDishShopList.contains(newDishShop);
                    }
                }
            }*/

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
        //return BuffetApplication.PERMISSION_BUFFET_MODIFY_DISH;
        return null;
    }

    /*
    @Override
    protected boolean isCanModify() {
        if(mShoppingCart.getOrder().getTrade().getTradePayStatus() == TradePayStatus.PREPAID)
            return false;
        else
            return true;
    }*/
}
