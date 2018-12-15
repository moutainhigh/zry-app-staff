package com.zhongmei.bty.dinner.util;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.dinner.manager.DinnerTradeItemManager;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
public class BuffetTradeItemOperateUtil extends TradeItemOperateUtil {
    @Override
    protected void doGive(final IShopcartItemBase shopcartItem, Reason reason) {
        DinnerTradeItemManager.getInstance().give(shopcartItemBase, reason);

        DinnerTradeItemManager.getInstance().showBuffetTradeAmountNegativeDialog(mActivity, new Runnable() {
            @Override
            public void run() {
                DinnerShoppingCart.getInstance().removeShopcarItemPrivilege(shopcartItemBase);
            }
        });
    }

    @Override
    public void doDelete() {
        if (shopcartItemBase.getCouponPrivilegeVo() != null && shopcartItemBase.getCouponPrivilegeVo().isUsed()) {
            ToastUtil.showShortToast(mActivity.getString(R.string.buffet_privilege_used));
            return;
        }

        DinnerTradeItemManager.getInstance().deleteItem(shopcartItemBase,
                dishDataItem.getItem().getUuid(), mChangePageListener, mActivity);

        DinnerTradeItemManager.getInstance().showBuffetTradeAmountNegativeDialog(mActivity, new Runnable() {
            @Override
            public void run() {
                DinnerTradeItemManager.getInstance().cancelDeleteItem(shopcartItemBase, dishDataItem.getItem().getUuid());
            }
        });
    }

    @Override
    public void doDelete(final int type, final IShopcartItemBase shopcartItemBase, final List<InventoryItem> inventoryItemList) {
        if (shopcartItemBase.getCouponPrivilegeVo() != null && shopcartItemBase.getCouponPrivilegeVo().isUsed()) {
            ToastUtil.showShortToast(mActivity.getString(R.string.buffet_privilege_used));
            return;
        }

        DinnerTradeItemManager.getInstance().deleteItem(shopcartItemBase,
                dishDataItem.getItem().getUuid(), mChangePageListener, mActivity);

        DinnerTradeItemManager.getInstance().showBuffetTradeAmountNegativeDialog(mActivity, new Runnable() {
            @Override
            public void run() {
                DinnerTradeItemManager.getInstance().cancelDeleteItem(shopcartItemBase, dishDataItem.getItem().getUuid());
            }
        }, new Runnable() {
            @Override
            public void run() {
                DinnerTradeItemManager.getInstance().doChangeInventory(type, shopcartItemBase, inventoryItemList);
            }
        });
    }
}
