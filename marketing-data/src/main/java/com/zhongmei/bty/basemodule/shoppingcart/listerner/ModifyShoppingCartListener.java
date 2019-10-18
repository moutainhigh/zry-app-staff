package com.zhongmei.bty.basemodule.shoppingcart.listerner;

import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.trade.bean.TakeOutInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.enums.DeliveryType;

import java.util.List;
import java.util.Map;


public interface ModifyShoppingCartListener {

    void addToShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, ShopcartItem mShopcartItem);



    void addToShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, List<ShopcartItem> mShopcartItems);


    void addTempSetmealData(IShopcartItem mShopcartItem);


    void removeSetmealData();


    void removeSetmealChild(IShopcartItem OrderDishshopVo);


    void removeShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, IShopcartItemBase mShopcartItemBase);

    void removeShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, IShopcartItem mShopcartItem);


    void orderDiscount(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);


    void removeDiscount(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, IShopcartItem mShopcartItem);


    void updateDish(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);


    void clearShoppingCart();


    void setRemark(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);


    void removeRemark(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, IShopcartItem mShopcartItem);


    void removeSetmealRemark(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, ISetmealShopcartItem setmeal);


    void setCardNo(String cardNo);


    void setOrderUserMessage(TakeOutInfo entity);


    void setCustomer(TradeCustomer customer);


    void batchPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);


    void removeCustomerPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);


    void resetOrder(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);


    void setCouponPrivi1lege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);


    void removeCouponPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);


    void setIntegralCash(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);


    void removeIntegralCash(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);


    void updateShoppingcartData();


    void separateOrder(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);


    void addExtraCharge(TradeVo mTradeVo, Map<Long, ExtraCharge> arrayExtraCharge);


    void removeExtraCharge(TradeVo mTradeVo, Long extraChargeId);



    void removeDeposit(TradeVo mTradeVo);


    void addMarketActivity(TradeVo mTradeVo);


    void removeMarketActivity(TradeVo mTradeVo);


    void exception(String message);


    void addWeiXinCouponsPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);


    void removeWeiXinCouponsPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);


    void doBanquet(TradeVo mTradeVo);


    void removeBanquet(TradeVo mTradeVo);


    void setTradePeopleCount(Integer tradePeopleCount);


    void setOrderType(DeliveryType deliveryType);


    void updateUserInfo();


    void addSalesPromotion(boolean succeeded, TradeVo tradeVo);


    void removeSalesPromotion(TradeVo tradeVo);
}
