package com.zhongmei.bty.cashier.shoppingcart;

import com.zhongmei.bty.basemodule.shoppingcart.listerner.ModifyShoppingCartListener;
import com.zhongmei.bty.basemodule.trade.bean.TakeOutInfo;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import java.util.List;
import java.util.Map;

public class ShoppingCartListener implements ModifyShoppingCartListener {

    public enum OperationStatus {
        None, addToShoppingCart, removeShoppingCart, addTempSetmealData, removeSetmealData, removeSetmealChild, updateDish
    }

    @Override
    public void addToShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, ShopcartItem mShopcartItem) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addToShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, List<ShopcartItem> mShopcartItems) {

    }

    @Override
    public void addTempSetmealData(IShopcartItem mShopcartItem) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeSetmealData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeSetmealChild(IShopcartItem OrderDishshopVo) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo,
                                   IShopcartItemBase mShopcartItemBase) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, IShopcartItem mShopcartItem) {

    }

    @Override
    public void orderDiscount(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeDiscount(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, IShopcartItem mShopcartItem) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateDish(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
        // TODO Auto-generated method stub

    }

    @Override
    public void clearShoppingCart() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setRemark(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeRemark(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, IShopcartItem mShopcartItem) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeSetmealRemark(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo,
                                    ISetmealShopcartItem setmeal) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setCardNo(String cardNo) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setOrderUserMessage(TakeOutInfo entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setCustomer(TradeCustomer customer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void batchPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeCustomerPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {

    }

    @Override
    public void resetOrder(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setCouponPrivi1lege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeCouponPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {

    }


    @Override
    public void setIntegralCash(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeIntegralCash(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateShoppingcartData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void separateOrder(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addExtraCharge(TradeVo mTradeVo, Map<Long, ExtraCharge> arrayExtraCharge) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeExtraCharge(TradeVo mTradeVo, Long extraChargeId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeDeposit(TradeVo mTradeVo) {

    }

    @Override
    public void addMarketActivity(TradeVo mTradeVo) {
        // TODO Auto-generated method stub


    }

    @Override
    public void removeMarketActivity(TradeVo mTradeVo) {
        // TODO Auto-generated method stub


    }

    @Override
    public void exception(String message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addWeiXinCouponsPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeWeiXinCouponsPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
        // TODO Auto-generated method stub

    }

    @Override
    public void doBanquet(TradeVo mTradeVo) {

    }

    @Override
    public void removeBanquet(TradeVo mTradeVo) {

    }

    @Override
    public void setTradePeopleCount(Integer tradePeopleCount) {

    }

    @Override
    public void setOrderType(DeliveryType deliveryType) {

    }

    @Override
    public void updateUserInfo() {

    }

    @Override
    public void addSalesPromotion(boolean succeeded, TradeVo tradeVo) {

    }

    @Override
    public void removeSalesPromotion(TradeVo tradeVo) {

    }
}
