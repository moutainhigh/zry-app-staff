package com.zhongmei.bty.cashier.shoppingcart;

import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.entity.TaxRateInfo;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.shoppingcart.BaseShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShoppingCartTool;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager;
import com.zhongmei.bty.basemodule.orderdish.bean.DishQuantityBean;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CopyMoveDishTool {

    public static int SOURCE = 1;

    public static int TARGET = 2;


    public Map<Integer, TradeVo> moveDish(DinnertableTradeInfo fromTradeVo, DinnertableTradeInfo toTradeVo, TradeTable mTradeTable, List<DishQuantityBean> qutityBeanList) {
        return rellayCopy(fromTradeVo, toTradeVo, mTradeTable, null, qutityBeanList, true, true);
    }



    public Map<Integer, TradeVo> copyDish(DinnertableTradeInfo fromTradeVo, DinnertableTradeInfo toTradeVo, TradeTable mTradeTable,
                                          List<IShopcartItem> listShopcartItem, List<DishQuantityBean> qutityBeanList, boolean isCopyDishProperty) {
        return rellayCopy(fromTradeVo, toTradeVo, mTradeTable, listShopcartItem, qutityBeanList, false, isCopyDishProperty);
    }



    public Map<Integer, TradeVo> rellayCopy(DinnertableTradeInfo fromTradeVo, DinnertableTradeInfo toTradeVo, TradeTable mTradeTable,
                                            List<IShopcartItem> listShopcartItem, List<DishQuantityBean> qutityBeanList, boolean isMove, boolean isCopyDishProperty) {
        Map<Integer, TradeVo> tempMap = new HashMap<Integer, TradeVo>();

                DinnerShoppingCart oDinnerShoppingCart = DinnerShoppingCart.getInstance();
        oDinnerShoppingCart.resetOrderFromTable(fromTradeVo, true);
        if (isMove) {
            if (qutityBeanList != null) {
                for (DishQuantityBean dishQuantityBean : qutityBeanList) {
                    IShopcartItem mIShopcartItem = dishQuantityBean.shopcartItem;
                    oDinnerShoppingCart.removeDinnerShoppingcartItem(mIShopcartItem);
                    if (!dishQuantityBean.isMoveAll()) {
                        IShopcartItem tempShopcartItem = buildNewShopcarItem(dishQuantityBean, false);
                        oDinnerShoppingCart.addReadOnlyShippingToCart(oDinnerShoppingCart.getShoppingCartVo(), tempShopcartItem, true);
                    }
                }
            }

            TradeVo oTradeVo = oDinnerShoppingCart.createDinnerTradeVo();
                        BaseShoppingCart.removeAllPrivilige(oTradeVo, true, true, false);
                        List<IShopcartItem> shopcartItemList = oDinnerShoppingCart.mergeShopcartItem(oDinnerShoppingCart.getShoppingCartVo());
            MathShoppingCartTool.mathTotalPrice(shopcartItemList, oTradeVo);
            tempMap.put(SOURCE, oTradeVo);
            oDinnerShoppingCart.clearShoppingCart();
        } else {
            tempMap.put(SOURCE, fromTradeVo.getTradeVo());
        }
        DinnerShoppingCart nDinnerShoppingCart = DinnerShoppingCart.getInstance();
        if (toTradeVo == null) {
            nDinnerShoppingCart.openTable(mTradeTable);
            nDinnerShoppingCart.getShoppingCartVo().getmTradeVo().getTrade().setBusinessType(BusinessType.DINNER);
            nDinnerShoppingCart.setDinnerOrderType(DeliveryType.HERE);
        } else {
            nDinnerShoppingCart.resetOrderFromTable(toTradeVo, true);
        }
        CustomerResp customer = DinnerCashManager.getTradeVoCustomer(nDinnerShoppingCart.getShoppingCartVo().getmTradeVo());
        if (!isMove) {
            if (qutityBeanList != null) {
                for (DishQuantityBean dishQuantityBean : qutityBeanList) {
                    IShopcartItem mIShopcartItem = dishQuantityBean.shopcartItem;
                    mIShopcartItem.setPrivilege(null);
                    if (mIShopcartItem.getCouponPrivilegeVo() != null) {
                        mIShopcartItem.getCouponPrivilegeVo().setTradePrivilege(null);
                    }
                    if (!isCopyDishProperty) {
                        mIShopcartItem = buildNoPropertyShopcartItem(mIShopcartItem, toTradeVo);
                    }

                    nDinnerShoppingCart.addReadOnlyShippingToCart(nDinnerShoppingCart.getShoppingCartVo(), mIShopcartItem, false);
                }
            }
        } else {
            if (qutityBeanList != null) {
                for (DishQuantityBean dishQuantityBean : qutityBeanList) {
                    IShopcartItem mIShopcartItem = dishQuantityBean.shopcartItem;
                    if (dishQuantityBean.isMoveAll()) {
                        mIShopcartItem.setPrivilege(null);
                        if (mIShopcartItem.getCouponPrivilegeVo() != null) {
                            mIShopcartItem.getCouponPrivilegeVo().setTradePrivilege(null);
                        }
                        nDinnerShoppingCart.addReadOnlyShippingToCart(nDinnerShoppingCart.getShoppingCartVo(), mIShopcartItem, false);
                    } else {
                        IShopcartItem tempShopcartItem = buildNewShopcarItem(dishQuantityBean, true);
                        nDinnerShoppingCart.addReadOnlyShippingToCart(nDinnerShoppingCart.getShoppingCartVo(), tempShopcartItem, true);
                    }


                }
            }
        }

        TradeVo nTradeVo = nDinnerShoppingCart.createDinnerTradeVo();
        List<IShopcartItem> targetIshopcartList = nDinnerShoppingCart.mergeShopcartItem(nDinnerShoppingCart.getShoppingCartVo());
        boolean isHasBanquet = BaseShoppingCart.isHasValidBanquet(nTradeVo);
        Map<String, TradeItemPlanActivity> tradeItemPlanActivityMap = BaseShoppingCart.covertItemPlanListToMap(nTradeVo.getTradeItemPlanActivityList());
        if (targetIshopcartList != null) {
            for (IShopcartItem mIShopcartItem : targetIshopcartList) {
                                boolean isHasTradePlan = BaseShoppingCart.isHasMemberPlanActivity(mIShopcartItem, tradeItemPlanActivityMap);
                if (customer != null && !isHasBanquet && !isHasTradePlan) {
                    nDinnerShoppingCart.setDishMemberPrivilege(nDinnerShoppingCart.getShoppingCartVo(), mIShopcartItem, customer, false);
                }
            }
        }

                if (nTradeVo.getTrade().getId() == null) {
                        TaxRateInfo taxRateInfo = ServerSettingCache.getInstance().getmTaxRateInfo();
            if (taxRateInfo != null && taxRateInfo.isTaxSupplyOpen()) {
                TradeTax tradeTax = taxRateInfo.toTradeTax(null);
                nTradeVo.setTradeTaxs(Arrays.asList(tradeTax));
            }

                        ExtraCharge serviceExtraCharge = ServerSettingCache.getInstance().getmServiceExtraCharge();
            if (serviceExtraCharge != null && serviceExtraCharge.isAutoJoinTrade()) {
                nTradeVo.setTradeInitConfigs(Arrays.asList(serviceExtraCharge.toTradeInitConfig()));
            }
        }

        MathShoppingCartTool.mathTotalPrice(targetIshopcartList, nTradeVo);
        tempMap.put(TARGET, nTradeVo);
        nDinnerShoppingCart.clearShoppingCart();
        return tempMap;
    }

    public static TradeItemExtraDinner buildTradeItemExtraDinner(IShopcartItem shopcartItem, TableSeat tableSeat) {
        if (tableSeat == null) {
            return null;
        }
        TradeItemExtraDinner tradeItemExtraDinner = new TradeItemExtraDinner();
        tradeItemExtraDinner.setStatusFlag(StatusFlag.VALID);
        tradeItemExtraDinner.setChanged(true);
        tradeItemExtraDinner.setTradeItemId(shopcartItem.getId());
        tradeItemExtraDinner.setTradeItemUuid(shopcartItem.getUuid());
        tradeItemExtraDinner.setSeatId(tableSeat.getId());
        tradeItemExtraDinner.setSeatNumber(tableSeat.getSeatName());
        tradeItemExtraDinner.setClientCreateTime(System.currentTimeMillis());
        tradeItemExtraDinner.setClientUpdateTime(System.currentTimeMillis());
        tradeItemExtraDinner.setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        tradeItemExtraDinner.setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        tradeItemExtraDinner.setCreatorId(Session.getAuthUser().getId());
        tradeItemExtraDinner.setCreatorName(Session.getAuthUser().getName());
        tradeItemExtraDinner.setUpdatorId(Session.getAuthUser().getId());
        tradeItemExtraDinner.setUpdatorName(Session.getAuthUser().getName());
        return tradeItemExtraDinner;
    }


    public IShopcartItem buildNewShopcarItem(DishQuantityBean dishQuantityBean, boolean isNew) {
        IShopcartItem oShopcartItem = dishQuantityBean.shopcartItem;
        BigDecimal nItemAmount = BigDecimal.ZERO;
        BigDecimal quntity = dishQuantityBean.getQuantity();
        BigDecimal totalAmount = oShopcartItem.getAmount().add(oShopcartItem.getPropertyAmount()).add(oShopcartItem.getFeedsAmount());
                if (oShopcartItem.getSetmealItems() != null) {
            for (ISetmealShopcartItem item : oShopcartItem.getSetmealItems()) {
                totalAmount = totalAmount.add(item.getActualAmount());
            }
        }

        nItemAmount = MathDecimal.divDown(totalAmount.multiply(quntity), oShopcartItem.getSingleQty(), 2);
        if (!isNew) {
            nItemAmount = totalAmount.subtract(nItemAmount);
        }
        TradeItem tradeItem = new TradeItem();
        tradeItem.setUuid(SystemUtils.genOnlyIdentifier());
        tradeItem.setActualAmount(nItemAmount);
        tradeItem.setAmount(nItemAmount);
        tradeItem.setStatusFlag(StatusFlag.VALID);
        tradeItem.setSkuUuid(oShopcartItem.getSkuUuid());
        tradeItem.setDishId(oShopcartItem.getSkuId());
        tradeItem.setDishName(oShopcartItem.getSkuName());
        tradeItem.setQuantity(quntity);
        tradeItem.setEnableWholePrivilege(oShopcartItem.getEnableWholePrivilege());
        IShopcartItem readonlyShopcartItemBase = new ReadonlyShopcartItem(tradeItem);
        return readonlyShopcartItemBase;
    }


    public IShopcartItem buildNoPropertyShopcartItem(IShopcartItem mShopcartItem, DinnertableTradeInfo toTradeVo) {
        TradeVo mTradeVo = null;
        if (toTradeVo != null) {
            mTradeVo = toTradeVo.getTradeVo();
        }

        TradeItem mTradeItem = new TradeItem();
                mTradeItem.setUuid(mShopcartItem.getUuid());
                mTradeItem.setQuantity(mShopcartItem.getTotalQty());
                mTradeItem.setPrice(mShopcartItem.getPrice());
                mTradeItem.setAmount(mShopcartItem.getAmount());
                mTradeItem.setPropertyAmount(BigDecimal.ZERO);
        mTradeItem.setFeedsAmount(BigDecimal.ZERO);
        BigDecimal propertyAmount = mShopcartItem.getPropertyAmount();
        if (propertyAmount == null) {
            propertyAmount = BigDecimal.ZERO;
        }
        BigDecimal feedsAmount = mShopcartItem.getFeedsAmount();
        if (feedsAmount == null) {
            feedsAmount = BigDecimal.ZERO;
        }
        BigDecimal actualAmount = mShopcartItem.getActualAmount().subtract(propertyAmount).subtract(feedsAmount);
        mTradeItem.setActualAmount(actualAmount);
        BigDecimal setmealPropertyAmount = BigDecimal.ZERO;
                if (mShopcartItem.getSetmealItems() != null) {
            for (ISetmealShopcartItem item : mShopcartItem.getSetmealItems()) {
                if (item.getPropertyAmount() != null) {
                    setmealPropertyAmount = setmealPropertyAmount.add(item.getPropertyAmount());
                }
                if (item.getFeedsAmount() != null) {
                    setmealPropertyAmount = setmealPropertyAmount.add(item.getFeedsAmount());
                }
            }
        }
        actualAmount = actualAmount.subtract(setmealPropertyAmount);
        mTradeItem.setActualAmount(actualAmount);
                        mTradeItem.setParentUuid(mShopcartItem.getParentUuid());
                mTradeItem.setDishName(mShopcartItem.getSkuName());
                mTradeItem.setSkuUuid(mShopcartItem.getSkuUuid());
        mTradeItem.setDishId(mShopcartItem.getSkuId());
                if (mShopcartItem instanceof SetmealShopcartItem) {
            SetmealShopcartItem setmealItem = (SetmealShopcartItem) mShopcartItem;
            mTradeItem.setDishSetmealGroupId(setmealItem.getSetmealGroupId());
        }
                                                mTradeItem.setTradeMemo(mShopcartItem.getMemo());
        mTradeItem.setType(mShopcartItem.getType());

                mTradeItem.setUnitName(mShopcartItem.getUnitName());
                mTradeItem.setSaleType(mShopcartItem.getSaleType());
        if (mTradeVo != null && mTradeVo.getTrade() != null) {
            mTradeItem.setTradeUuid(mTradeVo.getTrade().getUuid());
            mTradeItem.setTradeId(mTradeVo.getTrade().getId());
        }
        mTradeItem.setIssueStatus(mShopcartItem.getIssueStatus());

                if (mTradeVo != null && Utils.isNotEmpty(mTradeVo.getTradeTableList())) {
            mTradeItem.setTradeTableUuid(mTradeVo.getTradeTableList().get(0).getUuid());
            mTradeItem.setTradeTableId(mTradeVo.getTradeTableList().get(0).getId());
        }
                mTradeItem.setEnableWholePrivilege(mShopcartItem.getEnableWholePrivilege());
                mTradeItem.setIsChangePrice(mShopcartItem.getIsChangePrice());

        mTradeItem.setRelateTradeItemId(mShopcartItem.getRelateTradeItemId());
        mTradeItem.setRelateTradeItemUuid(mShopcartItem.getRelateTradeItemUuid());
        mTradeItem.setInvalidType(mShopcartItem.getInvalidType());
        mTradeItem.setStatusFlag(mShopcartItem.getStatusFlag());
        IShopcartItem readonlyShopcartItemBase = new ReadonlyShopcartItem(mTradeItem);
        return readonlyShopcartItemBase;
    }
}
