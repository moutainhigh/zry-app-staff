package com.zhongmei.bty.basemodule.shoppingcart.utils;

import com.zhongmei.bty.basemodule.beauty.BeautyCardManager;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.discount.bean.CardServicePrivilegeVo;
import com.zhongmei.bty.commonmodule.database.enums.ServerPrivilegeType;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.discount.utils.BuildPrivilegeTool;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.entity.trade.TradePrivilegeLimitNumCard;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.SystemUtils;



public class CardServiceTool {

    public static void removeService(IShopcartItemBase mShopcartItem) {
        if (mShopcartItem.getCardServicePrivilgeVo() == null) {
            return;
        }

        ServerPrivilegeType type = null;
        CardServicePrivilegeVo cardServicePrivilegeVo = mShopcartItem.getCardServicePrivilgeVo();
        if (cardServicePrivilegeVo.getTradePrivilege() != null) {
            cardServicePrivilegeVo.getTradePrivilege().setInValid();
            type = cardServicePrivilegeVo.getType();
        }

        BeautyCardManager.getInstance().removeDishshopFromShopCart(type, mShopcartItem);
    }



    public static ShopcartItem createCardService(Trade trade, DishShop dishShop, Long serverRecordId) {
        ShopcartItem shopcartItem = CreateItemTool.createShopcartItem(dishShop.getId());
        CardServicePrivilegeVo cardServicePrivilegeVo = new CardServicePrivilegeVo();
        shopcartItem.setCardServicePrivilegeVo(cardServicePrivilegeVo);
        TradePrivilege tradePrivilege = BuildPrivilegeTool.buildCardServicePrivige(serverRecordId, trade.getUuid(), shopcartItem);
        cardServicePrivilegeVo.setTradePrivilege(tradePrivilege);
        return shopcartItem;
    }


    private static TradePrivilegeLimitNumCard buildLimitNumCard(Trade trade, String cardNum, TradePrivilege tradePrivilege) {
        TradePrivilegeLimitNumCard numCard = new TradePrivilegeLimitNumCard();
        numCard.setTradeId(trade.getId());
        numCard.setTradeUuid(trade.getUuid());
        numCard.setStatusFlag(StatusFlag.VALID);
        numCard.setCardNo(cardNum);
        numCard.setTradePrivilegeId(tradePrivilege.getId());
        numCard.setTradePrivilegeUuid(tradePrivilege.getUuid());
        numCard.setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        numCard.setDeviceIdenty(BaseApplication.sInstance.getDeviceIdenty());
        if (CustomerManager.getInstance().getDinnerLoginCustomer() != null)
            numCard.setCustomerId(CustomerManager.getInstance().getDinnerLoginCustomer().customerId);
        return numCard;
    }

}
