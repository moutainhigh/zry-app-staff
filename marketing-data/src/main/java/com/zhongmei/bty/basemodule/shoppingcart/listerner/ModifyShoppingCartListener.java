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

/**
 * @Date：2015-3-30 上午9:15:13
 * @Description: TODO
 * @Version: 1.0
 */
public interface ModifyShoppingCartListener {
    /**
     * @Title: addToShoppingCart
     * @Description: 添加菜品到购物车
     * @Param listOrderDishshopVo
     * @Param mTradeVo TODO
     * @Return void 返回类型
     */
    void addToShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, ShopcartItem mShopcartItem);


    /**
     * @param mShopcartItems TODO
     * @Title: addToShoppingCart
     * @Description: 添加多个菜品到购物车
     * @Param listOrderDishshopVo
     * @Param mTradeVo TODO
     * @Return void 返回类型
     */
    void addToShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, List<ShopcartItem> mShopcartItems);

    /**
     * @Title: addTempComboData
     * @Description: 添加零时套餐数据
     * @Param OrderDishshopVo TODO
     * @Return void 返回类型
     */
    void addTempSetmealData(IShopcartItem mShopcartItem);

    /**
     * @Title: removeSetmealData
     * @Description: 移除临时套餐
     * @Param OrderDishshopVo TODO
     * @Return void 返回类型
     */
    void removeSetmealData();

    /**
     * @Title: removeSetmealChild
     * @Description: 移除临时套餐子菜
     * @Param OrderDishshopVo TODO
     * @Return void 返回类型
     */
    void removeSetmealChild(IShopcartItem OrderDishshopVo);

    /**
     * @Title: removeShoppingCart
     * @Description: 删除购物车中菜品
     * @Param listOrderDishshopVo
     * @Param mTradeVo
     * @Param mShopcartItemBase TODO
     * @Return void 返回类型
     */
    void removeShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, IShopcartItemBase mShopcartItemBase);

    void removeShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, IShopcartItem mShopcartItem);

    /**
     * 整单优惠
     *
     * @Title: orderDiscount
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    void orderDiscount(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);

    /**
     * @Title: removeDiscount
     * @Description: 移除整单打折
     * @Param listOrderDishshopVo
     * @Param mTradeVo TODO
     * @Return void 返回类型
     */
    void removeDiscount(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, IShopcartItem mShopcartItem);

    /**
     * @Title: updateDish
     * @Description: 更新购物车中菜品
     * @Param listOrderDishshopVo
     * @Param mTradeVo TODO
     * @Return void 返回类型
     */
    void updateDish(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);

    /**
     * 清楚购物车
     *
     * @Title: clearShoppingCart
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    void clearShoppingCart();

    /**
     * 设置整单备注
     *
     * @Title: setRemark
     * @Description: TODO
     * @Param remark TODO
     * @Return void 返回类型
     */
    void setRemark(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);

    /**
     * @Title: removeRemark
     * @Description: 移除整单备注
     * @Param listOrderDishshopVo
     * @Param mTradeVo TODO
     * @Return void 返回类型
     */
    void removeRemark(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, IShopcartItem mShopcartItem);

    /**
     * @Title: removeSetmealRemark
     * @Description: 删除套餐子菜备注
     * @Param listOrderDishshopVo
     * @Param mTradeVo
     * @Param setmeal TODO
     * @Return void 返回类型
     */
    void removeSetmealRemark(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, ISetmealShopcartItem setmeal);

    /**
     * 设置点菜号牌
     *
     * @Title: setCardNo
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    void setCardNo(String cardNo);

    /**
     * 设置外卖地址。自提用户信息
     *
     * @Title: setOrderUserMessage
     * @Description: TODO
     * @Param entity TODO
     * @Return void 返回类型
     */
    void setOrderUserMessage(TakeOutInfo entity);

    /**
     * 设置登录会员
     *
     * @Title: setCustomerV5
     * @Description: TODO
     * @Param customer TODO
     * @Return void 返回类型
     */
    void setCustomer(TradeCustomer customer);

    /**
     * @Title: batchEdit
     * @Description: 批量编辑折扣
     * @Param listOrderDishshopVo TODO
     * @Return void 返回类型
     */
    void batchPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);

    /**
     * 移除会员相关的优惠
     *
     * @param listOrderDishshopVo
     * @param mTradeVo
     */
    void removeCustomerPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);

    /**
     * @Title: resetOrder
     * @Description: 订单回执
     * @Param listOrderDishshopVo
     * @Param mTradeVo TODO
     * @Return void 返回类型
     */
    void resetOrder(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);

    /**
     * @Title: setCouponPrivi1lege
     * @Description: 设置优惠劵
     * @Param mCouponPrivilegeVo TODO
     * @Return void 返回类型
     */
    void setCouponPrivi1lege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);

    /**
     * @Title: removeCouponPrivilege
     * @Description: 移除优惠劵
     * @Param TODO
     * @Return void 返回类型
     */
    void removeCouponPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);

    /**
     * @Title: setIntegralCash
     * @Description: 设置积分抵现
     * @Param listOrderDishshopVo
     * @Param mTradeVo TODO
     * @Return void 返回类型
     */
    void setIntegralCash(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);

    /**
     * @Title: removeIntegralCash
     * @Description: TODO
     * @Param 移除积分抵现
     * @Return void 返回类型
     */
    void removeIntegralCash(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);

    /**
     * @Title: updateData
     * @Description: 购物车数据修改
     * @Param TODO
     * @Return void 返回类型
     */
    void updateShoppingcartData();

    /**
     * @Title: separateOrder
     * @Description: 拆分订单操作
     * @Param listOrderDishshopVo
     * @Param mTradeVo TODO
     * @Return void 返回类型
     */
    void separateOrder(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);

    /**
     * @Title: addExtraCharge
     * @Description: 添加附加费
     * @Param mTradeVo
     * @Param arrayExtraCharge TODO
     * @Return void 返回类型
     */
    void addExtraCharge(TradeVo mTradeVo, Map<Long, ExtraCharge> arrayExtraCharge);

    /**
     * @Title: removeExtraCharge
     * @Description: 移除附加费
     * @Param mTradeVo
     * @Param mExtraCharge TODO
     * @Return void 返回类型
     */
    void removeExtraCharge(TradeVo mTradeVo, Long extraChargeId);


    /**
     * @Title: removeDeposit
     * @Description: 移除押金
     * @Param mTradeVo
     * @Param mExtraCharge TODO
     * @Return void 返回类型
     */
    void removeDeposit(TradeVo mTradeVo);

    /**
     * @Title: addMarketActivity
     * @Description: 添加营销活动
     * @Param @param mTradeVo TODO
     * @Return void 返回类型
     */
    void addMarketActivity(TradeVo mTradeVo);

    /**
     * @Title: removeMarketActivity
     * @Description: 移除订单中的活动
     * @Param @param mTradeVo TODO
     * @Return void 返回类型
     */
    void removeMarketActivity(TradeVo mTradeVo);

    /**
     * @Title: exception
     * @Description: 异常信息
     * @Param @param message TODO
     * @Return void 返回类型
     */
    void exception(String message);

    /**
     * @Title: addWeiXinCouponsPrivilege
     * @Description: 添加微信卡卷
     * @Param listOrderDishshopVo
     * @Param mTradeVo TODO
     * @Return void 返回类型
     */
    void addWeiXinCouponsPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);

    /**
     * @Title: removeWeiXinCouponsPrivilege
     * @Description: 移除微信卡卷
     * @Param listOrderDishshopVo
     * @Param mTradeVo TODO
     * @Return void 返回类型
     */
    void removeWeiXinCouponsPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo);

    /**
     * 宴请回调
     *
     * @param mTradeVo
     */
    void doBanquet(TradeVo mTradeVo);

    /**
     * 移除宴请回调
     *
     * @param mTradeVo
     */
    void removeBanquet(TradeVo mTradeVo);

    /**
     * 设置订单人数
     *
     * @param tradePeopleCount 订单人数
     */
    void setTradePeopleCount(Integer tradePeopleCount);

    /**
     * 票据类型回调
     *
     * @param deliveryType 票据类型
     */
    void setOrderType(DeliveryType deliveryType);

    /**
     * 更新用户相关的信息
     */
    void updateUserInfo();

    /**
     * 添加促销活动回调接口
     *
     * @param succeeded 是否成功，true为成功，false为失败
     * @param tradeVo   订单信息
     */
    void addSalesPromotion(boolean succeeded, TradeVo tradeVo);

    /**
     * 删除促销活动回调接口
     *
     * @param tradeVo 订单信息
     */
    void removeSalesPromotion(TradeVo tradeVo);
}
