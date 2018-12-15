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

/**
 * @Date：2016年6月7日 下午5:58:15
 * @Description: TODO
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class CopyMoveDishTool {
    /**
     * 原单
     */
    public static int SOURCE = 1;
    /**
     * 目标单
     */
    public static int TARGET = 2;

    /**
     * @Title: moveDish
     * @Description: 移菜功能
     * @Param fromTradeVo：被移菜品所以订单
     * @Param toTradeVo：菜品移向目标订单
     * @Param listShopcartItem
     * @Param @return TODO
     * @Return Map<String       ,       TradeVo> 返回类型
     */
    public Map<Integer, TradeVo> moveDish(DinnertableTradeInfo fromTradeVo, DinnertableTradeInfo toTradeVo, TradeTable mTradeTable, List<DishQuantityBean> qutityBeanList) {
        return rellayCopy(fromTradeVo, toTradeVo, mTradeTable, null, qutityBeanList, true, true);
    }


    /**
     * @Title: copyDish
     * @Description: 复制菜品功能
     * @Param fromTradeVo：被复制菜品所以订单
     * @Param toTradeVo：菜品复制到的目标订单
     * @Param listShopcartItem
     * @Param @return TODO
     * @Return TradeVo 返回类型
     */
    public Map<Integer, TradeVo> copyDish(DinnertableTradeInfo fromTradeVo, DinnertableTradeInfo toTradeVo, TradeTable mTradeTable,
                                          List<IShopcartItem> listShopcartItem, List<DishQuantityBean> qutityBeanList, boolean isCopyDishProperty) {
        return rellayCopy(fromTradeVo, toTradeVo, mTradeTable, listShopcartItem, qutityBeanList, false, isCopyDishProperty);
    }


    /**
     * @param fromTradeVo
     * @param toTradeVo
     * @param mTradeTable
     * @param listShopcartItem
     * @param qutityBeanList
     * @param isMove           是否是移菜
     *                         * @return
     */
    public Map<Integer, TradeVo> rellayCopy(DinnertableTradeInfo fromTradeVo, DinnertableTradeInfo toTradeVo, TradeTable mTradeTable,
                                            List<IShopcartItem> listShopcartItem, List<DishQuantityBean> qutityBeanList, boolean isMove, boolean isCopyDishProperty) {
        Map<Integer, TradeVo> tempMap = new HashMap<Integer, TradeVo>();

        //将原单中要移除的菜品从原单中移除
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
            //原单的所有优惠移除
            BaseShoppingCart.removeAllPrivilige(oTradeVo, true, true, false);
            //移菜后重新计算营销活动和价格
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
                //是否有会员的营销活动，营销活动和会员不共存问题
                boolean isHasTradePlan = BaseShoppingCart.isHasMemberPlanActivity(mIShopcartItem, tradeItemPlanActivityMap);
                if (customer != null && !isHasBanquet && !isHasTradePlan) {
                    nDinnerShoppingCart.setDishMemberPrivilege(nDinnerShoppingCart.getShoppingCartVo(), mIShopcartItem, customer, false);
                }
            }
        }

        //只有空闲桌台才需要增加服务费与消费税
        if (nTradeVo.getTrade().getId() == null) {
            //增加默认税率
            TaxRateInfo taxRateInfo = ServerSettingCache.getInstance().getmTaxRateInfo();
            if (taxRateInfo != null && taxRateInfo.isTaxSupplyOpen()) {
                TradeTax tradeTax = taxRateInfo.toTradeTax(null);
                nTradeVo.setTradeTaxs(Arrays.asList(tradeTax));
            }

            //加入服务费
            ExtraCharge serviceExtraCharge = ServerSettingCache.getInstance().getmServiceExtraCharge();
            if (serviceExtraCharge != null && serviceExtraCharge.isAutoJoinTrade()) {
                nTradeVo.setTradeInitConfigs(Arrays.asList(serviceExtraCharge.toTradeInitConfig()));
            }
        }

        MathShoppingCartTool.mathTotalPrice(targetIshopcartList, nTradeVo);
//		nDinnerShoppingCart.createOrder();
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

    /**
     * 创建临时IShopcartItem 主要用于价格计算
     *
     * @param dishQuantityBean 移的菜的封装对象
     * @param isNew:是否对目标单
     */
    public IShopcartItem buildNewShopcarItem(DishQuantityBean dishQuantityBean, boolean isNew) {
        IShopcartItem oShopcartItem = dishQuantityBean.shopcartItem;
        BigDecimal nItemAmount = BigDecimal.ZERO;
        BigDecimal quntity = dishQuantityBean.getQuantity();
        BigDecimal totalAmount = oShopcartItem.getAmount().add(oShopcartItem.getPropertyAmount()).add(oShopcartItem.getFeedsAmount());
        //子菜价格
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

    /**
     * 建立没有属性价格的tradeItem
     */
    public IShopcartItem buildNoPropertyShopcartItem(IShopcartItem mShopcartItem, DinnertableTradeInfo toTradeVo) {
        TradeVo mTradeVo = null;
        if (toTradeVo != null) {
            mTradeVo = toTradeVo.getTradeVo();
        }

        TradeItem mTradeItem = new TradeItem();
        // 本地生成的唯一标示
        mTradeItem.setUuid(mShopcartItem.getUuid());
        // 数量
        mTradeItem.setQuantity(mShopcartItem.getTotalQty());
        // 单价
        mTradeItem.setPrice(mShopcartItem.getPrice());
        // 金额,不包括优惠
        mTradeItem.setAmount(mShopcartItem.getAmount());
        // 各种特征的金额合计
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
        //子菜价格
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
        // 售价
        // 父记录UUID
        mTradeItem.setParentUuid(mShopcartItem.getParentUuid());
        // 商品名称
        mTradeItem.setDishName(mShopcartItem.getSkuName());
        // 商品UUID
        mTradeItem.setSkuUuid(mShopcartItem.getSkuUuid());
        mTradeItem.setDishId(mShopcartItem.getSkuId());
        // 是套餐明细时记录下明细分组ID
        if (mShopcartItem instanceof SetmealShopcartItem) {
            SetmealShopcartItem setmealItem = (SetmealShopcartItem) mShopcartItem;
            mTradeItem.setDishSetmealGroupId(setmealItem.getSetmealGroupId());
        }
        // 排序位
        // mTradeItem.setSort();
        // 交易订单ID
        // mTradeItem.setTradeId();
        // 备注
        mTradeItem.setTradeMemo(mShopcartItem.getMemo());
        mTradeItem.setType(mShopcartItem.getType());

        // 单位名称
        mTradeItem.setUnitName(mShopcartItem.getUnitName());
        // 销售类型
        mTradeItem.setSaleType(mShopcartItem.getSaleType());
        if (mTradeVo != null && mTradeVo.getTrade() != null) {
            mTradeItem.setTradeUuid(mTradeVo.getTrade().getUuid());
            mTradeItem.setTradeId(mTradeVo.getTrade().getId());
        }
//		// 打印状态
        mTradeItem.setIssueStatus(mShopcartItem.getIssueStatus());

        // 设置TradeItem 的桌台信息
        if (mTradeVo != null && Utils.isNotEmpty(mTradeVo.getTradeTableList())) {
            mTradeItem.setTradeTableUuid(mTradeVo.getTradeTableList().get(0).getUuid());
            mTradeItem.setTradeTableId(mTradeVo.getTradeTableList().get(0).getId());
        }
        // 设置菜品是否参与折扣
        mTradeItem.setEnableWholePrivilege(mShopcartItem.getEnableWholePrivilege());
        // 标记是否是自定义菜品
        mTradeItem.setIsChangePrice(mShopcartItem.getIsChangePrice());

        mTradeItem.setRelateTradeItemId(mShopcartItem.getRelateTradeItemId());
        mTradeItem.setRelateTradeItemUuid(mShopcartItem.getRelateTradeItemUuid());
        mTradeItem.setInvalidType(mShopcartItem.getInvalidType());
        mTradeItem.setStatusFlag(mShopcartItem.getStatusFlag());
        IShopcartItem readonlyShopcartItemBase = new ReadonlyShopcartItem(mTradeItem);
        return readonlyShopcartItemBase;
    }
}
