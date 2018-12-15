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

/**
 * 次卡服务工具
 * Created by demo on 2018/12/15
 */

public class CardServiceTool {
    /**
     * 移除ShopcartItem中的次卡服务
     *
     * @param mShopcartItem
     */
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


    /**
     * 创建次卡服务对应的购物车对象
     *
     * @return
     */
    public static ShopcartItem createCardService(Trade trade, DishShop dishShop, Long serverRecordId) {
        ShopcartItem shopcartItem = CreateItemTool.createShopcartItem(dishShop.getId());
        CardServicePrivilegeVo cardServicePrivilegeVo = new CardServicePrivilegeVo();
        shopcartItem.setCardServicePrivilegeVo(cardServicePrivilegeVo);
        TradePrivilege tradePrivilege = BuildPrivilegeTool.buildCardServicePrivige(serverRecordId, trade.getUuid(), shopcartItem);
        cardServicePrivilegeVo.setTradePrivilege(tradePrivilege);
        return shopcartItem;
    }

    /**
     * 创建优惠和次卡的关联
     *
     * @param trade
     * @param cardNum
     * @param tradePrivilege
     * @return
     */
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

//    private static TradePrivilegeLimitNumCardSku buildLimitNumCardSku(Trade trade, TradePrivilege tradePrivilege, IShopcartItem shopcartItem,Long serverRecordId){
//        TradePrivilegeLimitNumCardSku numCardSku=new TradePrivilegeLimitNumCardSku();
//        numCardSku.setUuid(SystemUtils.genOnlyIdentifier());
//        numCardSku.setTradeId(trade.getId());
//        numCardSku.setTradeUuid(trade.getUuid());
//        numCardSku.setStatusFlag(StatusFlag.VALID);
//        numCardSku.setTradePrivilegeId(tradePrivilege.getId());
//        numCardSku.setTradePrivilegeUuid(tradePrivilege.getUuid());
//        numCardSku.setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
//        numCardSku.setTradeItemId(shopcartItem.getId());
//        numCardSku.setTradeItemUuid(shopcartItem.getUuid());
//        numCardSku.setBrandDishId(shopcartItem.getSkuId());
//        numCardSku.setBrandDishNum(shopcartItem.getTotalQty());
//        numCardSku.setBrandDishName(shopcartItem.getSkuName());
//        numCardSku.setCardInstanceId(serverRecordId);
//        return  numCardSku;
//    }
}
