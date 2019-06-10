package com.zhongmei.bty.basemodule.shoppingcart;

import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.commonmodule.database.enums.Status;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.discount.bean.BanquetVo;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.IntegralCashPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.MarketRuleVo;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsInfo;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsVo;
import com.zhongmei.bty.basemodule.discount.cache.MarketRuleCache;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.enums.ChargePrivilegeType;
import com.zhongmei.yunfu.db.enums.CouponType;
import com.zhongmei.bty.basemodule.discount.enums.UserType;
import com.zhongmei.bty.basemodule.discount.utils.BuildPrivilegeTool;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryItem;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryVo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.DishMenuVo;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.shoppingcart.bean.ShoppingCartVo;
import com.zhongmei.bty.basemodule.shoppingcart.listerner.ModifyShoppingCartListener;
import com.zhongmei.bty.basemodule.shoppingcart.utils.CardServiceTool;
import com.zhongmei.bty.basemodule.shoppingcart.utils.CreateTradeTableTool;
import com.zhongmei.bty.basemodule.shoppingcart.utils.CreateTradeTool;
import com.zhongmei.bty.basemodule.shoppingcart.utils.DinnerUnionShopcartUtil;
import com.zhongmei.bty.basemodule.shoppingcart.utils.GroupShoppingCartTool;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathManualMarketTool;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShoppingCartTool;
import com.zhongmei.bty.basemodule.trade.bean.BuffetComboVo;
import com.zhongmei.bty.basemodule.trade.bean.CustomerTypeBean;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeModel;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertable;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;
import com.zhongmei.bty.basemodule.trade.bean.MealShellVo;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradeGroupInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeTableInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.yunfu.db.entity.trade.TradeCreditLog;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.yunfu.context.AppBuildConfig;
import com.zhongmei.yunfu.util.Beans;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.IssueStatus;
import com.zhongmei.yunfu.db.enums.OperateType;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.util.ValueEnums;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @Date：2015年10月11日 上午9:50:00
 * @Description: 正餐购物车
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class DinnerShoppingCart extends BaseShoppingCart {

    private static final String TAG = DinnerShoppingCart.class.getSimpleName();

    private static DinnerShoppingCart instance = null;

    protected ShoppingCartVo dinnerShoppingCartVo = new ShoppingCartVo();
    //主单的tradeInfo对象
    protected DinnertableTradeInfo mainTradeInfo;

    public DinnertableTradeInfo getCurrentTradeInfo() {
        return currentTradeInfo;
    }

    //当前选择单据的tradeInfo对象
    protected DinnertableTradeInfo currentTradeInfo;

    /**
     * @Title: getInstance
     * @Description: 获取正餐购物车单例
     * @Param @return TODO
     * @Return DinnerShoppingCart 返回类型
     */
    public static DinnerShoppingCart getInstance() {
        synchronized (DinnerShoppingCart.class) {
            if (instance == null) {
                instance = new DinnerShoppingCart();
            }
        }

        return instance;
    }

    public void setOpenIdenty(String openIdenty) {
        setOpenIdenty(getShoppingCartVo(), openIdenty);
    }

    /**
     * 注册购物车监听
     *
     * @Title: registerListener
     * @Description: TODO
     * @Param @param mModifyShoppingCartListener TODO
     * @Return void 返回类型
     */
    public void registerListener(int listenerTag, ModifyShoppingCartListener mModifyShoppingCartListener) {
        arrayListener.put(listenerTag, mModifyShoppingCartListener);
        if (AppBuildConfig.DEBUG) {
            Log.d(DinnerShoppingCart.class.getSimpleName(), "registerListener tag " + listenerTag + ", after put " + arrayListener.keySet().toString());
        }
    }

    /**
     * @Title: unRegisterListener
     * @Description: 清空所有监听队列
     * @Param TODO
     * @Return void 返回类型
     */
    public void unRegisterListener() {
        arrayListener.clear();
        if (AppBuildConfig.DEBUG) {
            Log.d(DinnerShoppingCart.class.getSimpleName(), "clear arrayListener, after clear " + arrayListener.keySet().toString());
        }
    }

    /**
     * @Title: unRegisterListenerByTag
     * @Description: 根据监听tag反注册监听
     * @Param @param tag TODO
     * @Return void 返回类型
     */
    public void unRegisterListenerByTag(int tag) {
        arrayListener.remove(tag);
        if (AppBuildConfig.DEBUG) {
            Log.d(DinnerShoppingCart.class.getSimpleName(), "remove tag " + tag + ", after remove " + arrayListener.keySet().toString());
        }
    }

    /**
     * @Title: setDinnerBusinessType
     * @Description: 设置正餐业务形态
     * @Param @param mBusinessType TODO
     * @Return void 返回类型
     */
    public void setDinnerBusinessType(BusinessType mBusinessType) {
        setOrderBusinessType(dinnerShoppingCartVo, mBusinessType);
    }

    /**
     * @Title: setDinnerOrderType
     * @Description: 设置正餐票据类型
     * @Param @param orderType TODO
     * @Return void 返回类型
     */
    public void setDinnerOrderType(DeliveryType orderType) {
        setOrderType(dinnerShoppingCartVo, orderType);
    }

    /**
     * 设置自助餐的套餐信息
     *
     * @param comboVo
     * @param waiter
     */
    public void setMealShellVo(BuffetComboVo comboVo, AuthUser waiter) {
        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());
        if (waiter == null) {
            waiter = Session.getAuthUser();
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        dinnerShoppingCartVo.getmTradeVo().setTradeBuffetPeoples(new ArrayList<TradeBuffetPeople>());
        for (CustomerTypeBean customerTypeBean : comboVo.getDishCarteNorms()) {
            dinnerShoppingCartVo.getmTradeVo().getTradeBuffetPeoples().add(createTradeBuffetPeople(customerTypeBean, waiter));
            totalAmount = totalAmount.add(customerTypeBean.getCount().multiply(customerTypeBean.getPrice()));
        }

        MealShellVo mealShellVo = new MealShellVo();
        DishMenuVo dishMMenuVO = new DishMenuVo();
        dishMMenuVO.setDishCarte(comboVo.getDishCarte());
        dishMMenuVO.setPrice(comboVo.getDishCarte().getPrice());
        mealShellVo.buildHell(dishMMenuVO, DishType.BUFFET_COMBO_SHELL, comboVo.getTotalCount(), totalAmount);
        mealShellVo.setTradeRelate(dinnerShoppingCartVo.getmTradeVo().getTrade());
        dinnerShoppingCartVo.getmTradeVo().setMealHullVo(mealShellVo);
    }


    public void addTradeDeposit(TradeDeposit tradeDeposit) {
        if (dinnerShoppingCartVo != null) {
            dinnerShoppingCartVo.getmTradeVo().setTradeDeposit(tradeDeposit);
        }
    }

    private TradeBuffetPeople createTradeBuffetPeople(CustomerTypeBean customerTypeBean, AuthUser waiter) {
        TradeBuffetPeople tradeBuffetPeople = new TradeBuffetPeople();
        tradeBuffetPeople.setCarteNormsId(customerTypeBean.getId());
        tradeBuffetPeople.setCarteNormsName(customerTypeBean.getName());
        tradeBuffetPeople.setPeopleCount(customerTypeBean.getCount());
        tradeBuffetPeople.setCartePrice(customerTypeBean.getPrice());
        tradeBuffetPeople.setClientCreateTime(System.currentTimeMillis());
        tradeBuffetPeople.setClientUpdateTime(System.currentTimeMillis());
        tradeBuffetPeople.setCreatorId(waiter.getId());
        tradeBuffetPeople.setCreatorName(waiter.getName());
        tradeBuffetPeople.setChanged(true);
        tradeBuffetPeople.setStatusFlag(StatusFlag.VALID);
        return tradeBuffetPeople;
    }

    /**
     * @Title: openTable
     * @Description: 开台
     * @Param @param mTradeTable TODO
     * @Return void 返回类型
     */
    public void openTable(TradeTable mTradeTable) {
        if (mTradeTable == null) {
            return;
        }
        clearShoppingCart();

        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());

        List<TradeTable> mTradetables = new ArrayList<TradeTable>();
        mTradeTable.setTradeUuid(dinnerShoppingCartVo.getmTradeVo().getTrade().getUuid());
        mTradetables.add(mTradeTable);
        dinnerShoppingCartVo.getmTradeVo().setTradeTableList(mTradetables);
    }

    /**
     * @Title: updateTable
     * @Description: 更新桌台信息
     * @Param @param mTradeTable TODO
     * @Return void 返回类型
     */
    public void updateTable(TradeTable mTradeTable) {
        List<TradeTable> listTradeTable = dinnerShoppingCartVo.getmTradeVo().getTradeTableList();

        if (Utils.isEmpty(listTradeTable)) {//联台主单没有tradeTable数据
            return;
        }

        for (int i = 0; i < listTradeTable.size(); i++) {
            TradeTable mTable = listTradeTable.get(i);
            if (mTradeTable.getId().equals(mTable.getId())) {
                listTradeTable.set(i, mTradeTable);
                break;
            }
        }
    }

    /**
     * @Title: setDinnerCustomer
     * @Description: 设置登录会员
     * @Param @param mTradeCustomer TODO
     * @Return void 返回类型
     */
    public void setDinnerCustomer(TradeCustomer mTradeCustomer) {
        if (dinnerShoppingCartVo.getArrayTradeCustomer() == null) {
            dinnerShoppingCartVo.setArrayTradeCustomer(new HashMap<Integer, TradeCustomer>());
        }

        if (mTradeCustomer == null) {
            // 如果原订单中有会员信息则将该会员信息设置为无效并存放起来
            if (dinnerShoppingCartVo.getmTradeMemer() != null) {
                dinnerShoppingCartVo.getmTradeMemer().setStatusFlag(StatusFlag.INVALID);
                dinnerShoppingCartVo.getArrayTradeCustomer().put(null, dinnerShoppingCartVo.getmTradeMemer());
            }
            removeAllArrayTradeCustomer();
            List<TradeCustomer> listTradeCustomer = dinnerShoppingCartVo.getmTradeVo().getTradeCustomerList();
            if (Utils.isNotEmpty(listTradeCustomer)) {
                for (int i = listTradeCustomer.size() - 1; i >= 0; i--) {
                    TradeCustomer customer = listTradeCustomer.get(i);
                    if (customer.getCustomerType() == CustomerType.MEMBER || customer.getCustomerType() == CustomerType.CUSTOMER
                            || customer.getCustomerType() == CustomerType.CARD) {
                        listTradeCustomer.remove(i);
                        break;
                    }
                }
            }
        } else {
            if (dinnerShoppingCartVo.getArrayTradeCustomer() == null) {
                dinnerShoppingCartVo.setArrayTradeCustomer(new HashMap<Integer, TradeCustomer>());
            } else {
                removeAllArrayTradeCustomer();
            }
            dinnerShoppingCartVo.getArrayTradeCustomer().put(mTradeCustomer.getCustomerType().value(), mTradeCustomer);
        }
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).setCustomer(mTradeCustomer);
        }
    }

    /**
     * 将shoppingCartVo中的tradeCustomer移除
     */
    private void removeAllArrayTradeCustomer() {
        dinnerShoppingCartVo.getArrayTradeCustomer().remove(CustomerType.MEMBER.value());
        dinnerShoppingCartVo.getArrayTradeCustomer().remove(CustomerType.CUSTOMER.value());
        dinnerShoppingCartVo.getArrayTradeCustomer().remove(CustomerType.CARD.value());
    }


    /**
     * @Title: addTable
     * @Description: 为订单添加桌台
     * @Param @param mTradeTable TODO
     * @Return void 返回类型
     */
    public void addTable(TradeTable mTradeTable) {
        if (mTradeTable == null) {
            return;
        }
        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());

        mTradeTable.setTradeUuid(dinnerShoppingCartVo.getmTradeVo().getTrade().getUuid());
        CreateTradeTableTool.openTable(dinnerShoppingCartVo.getmTradeVo(), mTradeTable);
    }

    /**
     * 创建或者更新tradeTable
     *
     * @param tablesList
     */
    public void updateOrCreateTables(List<Tables> tablesList, boolean isCallback) {
        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());
        dinnerShoppingCartVo.getmTradeVo().setTradeTableList(CreateTradeTableTool
                .changeTables(dinnerShoppingCartVo.getmTradeVo().getTradeTableList(), tablesList,
                        dinnerShoppingCartVo.getmTradeVo().getTrade().getUuid()));
        if (!isCallback) {
            return;
        }
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).resetOrder(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo());
        }

    }

    /**
     * @Title: getDinnertableTradeVo
     * @Description: 获取订单桌台信息
     * @Param @return TODO
     * @Return DinnertableTradeVo 返回类型
     */
    public DinnertableTradeInfo getDinnertableTradeInfo() {
        return dinnerShoppingCartVo.getDinnertableTradeInfo();
    }

    public DinnertableTradeInfo getMainTradeInfo() {
        return mainTradeInfo;
    }

    /**
     * 是否需要设置分组
     *
     * @param mShopcartItem
     * @param isTempDish
     * @param isNeedSetGroup
     */
    public void addDishToShoppingCart(ShopcartItem mShopcartItem, Boolean isTempDish, boolean isNeedSetGroup) {
        if (mShopcartItem == null) {
            return;
        }
        if (isNeedSetGroup)
            mShopcartItem.setIsGroupDish(dinnerShoppingCartVo.isGroupMode());
        addShippingToCart(dinnerShoppingCartVo, mShopcartItem, isTempDish);
        //有宴请的情况下会员折扣不生效
        if (!isHasValidBanquet(dinnerShoppingCartVo.getmTradeVo())) {
            setDishMemberPrivilege(dinnerShoppingCartVo, mShopcartItem, CustomerManager.getInstance().getDinnerLoginCustomer(), false);
        }
        List<IShopcartItem> allIttem = mergeShopcartItem(dinnerShoppingCartVo);
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(allIttem, dinnerShoppingCartVo.getmTradeVo());
        CheckGiftCouponIsActived(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).addToShoppingCart(allIttem, dinnerShoppingCartVo.getmTradeVo(), mShopcartItem);
        }
    }


    /**
     * 获取子单数量
     *
     * @return
     */
    public BigDecimal getSubTradeCount() {
        if (getOrder() == null || getOrder().getTrade() == null)
            return BigDecimal.ONE;
        if (getOrder().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
            return getOrder().getSubTradeCount();
        } else
            return BigDecimal.ONE;
    }

    /**
     * @Title: addDishToShoppingCart
     * @Description: 正餐添加菜品到购物车
     * @Param @param mShopcartItem
     * @Param @param isTempDish TODO
     * @Return void 返回类型
     */
    public void addDishToShoppingCart(ShopcartItem mShopcartItem, Boolean isTempDish) {
        addDishToShoppingCart(mShopcartItem, isTempDish, true);
    }

    /**
     * 恢复已删除菜品
     *
     * @param shopcartItemBase
     */
    public void recoverInvalidDish(ReadonlyShopcartItemBase shopcartItemBase) {
        shopcartItemBase.recoveryDelete();

        List<IShopcartItem> allIttem = mergeShopcartItem(dinnerShoppingCartVo);
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(allIttem, dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(allIttem, dinnerShoppingCartVo.getmTradeVo());
        }
    }

    /**
     * @Title: removeDinnerTradeItem
     * @Description: 根据TradeItemVo移除数据
     * @Param @param mTradeItemVo TODO
     * @Return void 返回类型
     */
    public void removeDinnerTradeItem(TradeItemVo mTradeItemVo) {
        ShopcartItem mShopcartItem = removeTradeItem(dinnerShoppingCartVo, mTradeItemVo);
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeShoppingCart(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo(),
                    mShopcartItem);
        }
    }

    /**
     * @Title: removeDinnerDish
     * @Description: 根据菜品将整个菜品（单菜、套餐）移除购车
     * @Param @param shopcartItem TODO
     * @Return void 返回类型
     */
    public void removeDinnerDish(IShopcartItem shopcartItem) {
        removeDish(dinnerShoppingCartVo, shopcartItem);
        // 判断当前操作菜品是套餐壳或者单菜，并且该菜品是因退回菜品/修改菜品新产生的数据。
        if (!TextUtils.isEmpty(shopcartItem.getRelateTradeItemUuid())
                && Utils.isNotEmpty(dinnerShoppingCartVo.getListIShopcatItem())) {
            IShopcartItemBase iShopcartItem = getIShopcartItemByUUID(dinnerShoppingCartVo, shopcartItem.getRelateTradeItemUuid());
            if (iShopcartItem instanceof ReadonlyShopcartItem) {
                ReadonlyShopcartItem relateShopcartItem = ((ReadonlyShopcartItem) iShopcartItem);
                if (relateShopcartItem.getInvalidType() == InvalidType.RETURN_QTY) {
                    shopcartItem.deleteReturnQty();
                    relateShopcartItem.cancelReturnQty();
                } else if (relateShopcartItem.getInvalidType() == InvalidType.MODIFY_DISH) {
                    shopcartItem.deleteModifyDish();
                    relateShopcartItem.cancelModifyDish();
                }
            }
        }
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());
        CheckGiftCouponIsActived(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeShoppingCart(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo(),
                    shopcartItem);
        }
    }

    /**
     * @Title: removeFastFoodShoppingcartItem
     * @Description: 从购物车中删除单菜或删除套餐子菜
     * @Param @param mShopcartItem
     * @Param @param mSetmealShopcartItem
     * @Param @param mChangePageListener
     * @Param @param mFragmentManager TODO
     * @Return void 返回类型
     */
    public void removeDinnerShoppingcartItem(IShopcartItem mShopcartItem, SetmealShopcartItem mSetmealShopcartItem,
                                             ChangePageListener mChangePageListener, FragmentManager mFragmentManager) {

        removeShoppingcartItem(dinnerShoppingCartVo,
                mShopcartItem,
                mSetmealShopcartItem,
                mChangePageListener,
                mFragmentManager);

        // 判断当前操作菜品是套餐壳或者单菜，并且该菜品是因退回菜品/修改菜品新产生的数据。
        if (mSetmealShopcartItem == null && !TextUtils.isEmpty(mShopcartItem.getRelateTradeItemUuid())
                && Utils.isNotEmpty(dinnerShoppingCartVo.getListIShopcatItem())) {
            IShopcartItem iShopcartItem = getIShopcartItemByUUID(dinnerShoppingCartVo, mShopcartItem.getRelateTradeItemUuid());
            if (iShopcartItem instanceof ReadonlyShopcartItem) {
                ReadonlyShopcartItem relateShopcartItem = ((ReadonlyShopcartItem) iShopcartItem);
                if (relateShopcartItem.getInvalidType() == InvalidType.RETURN_QTY) {
                    mShopcartItem.deleteReturnQty();
                    relateShopcartItem.cancelReturnQty();
                } else if (relateShopcartItem.getInvalidType() == InvalidType.MODIFY_DISH) {
                    mShopcartItem.deleteModifyDish();
                    relateShopcartItem.cancelModifyDish();
                }
            }
        }

        resetSelectDishQTY(dinnerShoppingCartVo);
        List<IShopcartItem> allShopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        // 计算营销活动,解绑
        MathManualMarketTool.removeShopcartItem(dinnerShoppingCartVo.getmTradeVo(),
                allShopcartItemList,
                mShopcartItem,
                true,
                false);

        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(allShopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        CheckGiftCouponIsActived(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeShoppingCart(allShopcartItemList,
                    dinnerShoppingCartVo.getmTradeVo(),
                    mShopcartItem != null ? mShopcartItem : mSetmealShopcartItem);
        }
    }

    public void removeDinnerShoppingcartItem(IShopcartItem mShopcartItem) {
        removeDish(dinnerShoppingCartVo, mShopcartItem);
        resetSelectDishQTY(dinnerShoppingCartVo);
        List<IShopcartItem> allShopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        // 计算营销活动,解绑
        MathManualMarketTool.removeShopcartItem(dinnerShoppingCartVo.getmTradeVo(),
                allShopcartItemList,
                mShopcartItem,
                true,
                false);
    }

    /**
     * 移除小程序item
     * @param shopcartItem
     */
    public void removeAppletItem(IShopcartItem shopcartItem){
        removeDinnerShoppingcartItem(shopcartItem);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeMarketActivity(dinnerShoppingCartVo.getmTradeVo());
        }
    }

    /**
     * @Title: returnQTY
     * @Description: TODO
     * @Param @param mShopcartItem 退回菜品新生成的数据
     * @Return void 返回类型
     */
    public void returnQTY(IShopcartItem mShopcartItem) {
        OperateShoppingCart.addToReadOnlyShoppingCart(dinnerShoppingCartVo.getmTradeVo(),
                dinnerShoppingCartVo.getListIShopcatItem(),
                mShopcartItem);
        resetSelectDishQTY(dinnerShoppingCartVo);
        // 重新计算营销活动
        MathManualMarketTool.mathMarketPlan(dinnerShoppingCartVo.getmTradeVo(),
                mergeShopcartItem(dinnerShoppingCartVo),
                true,
                false);
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo());
        }
    }

    /**
     * @Title: modifyDish
     * @Description: TODO
     * @Param @param mShopcartItem 改菜新生成的数据
     * @Return void 返回类型
     */
    public void modifyDish(ShopcartItem mShopcartItem) {
        if (dinnerShoppingCartVo.getDinnertableTradeInfo() != null) {
            DinnertableTradeInfo info = dinnerShoppingCartVo.getDinnertableTradeInfo();
            mShopcartItem.setTradeTable(info.getTradeTableUuid(), info.getTradeTableId());
        }
        OperateShoppingCart.addToShoppingCart(dinnerShoppingCartVo.getmTradeVo(),
                dinnerShoppingCartVo.getListOrderDishshopVo(),
                mShopcartItem);
        //有宴请的情况下会员折扣不生效
        if (!isHasValidBanquet(dinnerShoppingCartVo.getmTradeVo())) {
            setDishMemberPrivilege(dinnerShoppingCartVo, mShopcartItem, CustomerManager.getInstance().getDinnerLoginCustomer(), false);
        }
        List<IShopcartItem> allIttem = mergeShopcartItem(dinnerShoppingCartVo);
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(allIttem, dinnerShoppingCartVo.getmTradeVo());
        CheckGiftCouponIsActived(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).addToShoppingCart(allIttem, dinnerShoppingCartVo.getmTradeVo(), mShopcartItem);
        }
    }

    /**
     * @Title: updateDinnerDish
     * @Description: 修改正餐菜品信息
     * @Param @param mShopcartItemBase
     * @Param @param isTempDish TODO
     * @Return void 返回类型
     */
    public void updateDinnerDish(IShopcartItemBase mShopcartItemBase, Boolean isTempDish) {
        updateDish(dinnerShoppingCartVo, mShopcartItemBase, isTempDish);
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());
        resetSelectDishQTY(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo());
        }

    }

    /**
     * * @Description: 修改正餐菜品信息
     *
     * @param dataItems
     */
    public void updateDinnerDish(List<DishDataItem> dataItems) {
        for (DishDataItem item : dataItems) {
            if ((item.getType() == ItemType.WEST_CHILD || item.getType() == ItemType.CHILD) && item.getBase() != null)
                updateDish(dinnerShoppingCartVo, item.getBase(), false);
            else if (item.getItem() != null)
                updateDish(dinnerShoppingCartVo, item.getItem(), false);
        }

        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());
        resetSelectDishQTY(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo());
        }
    }

    /**
     * 更新菜品操作
     *
     * @param itemOperations
     */
    public void updateOperations(List<TradeItemOperation> itemOperations) {
        HashMap<Long, List<TradeItemOperation>> map = new HashMap<>();
        for (TradeItemOperation operation : itemOperations) {
            if (map.get(operation.getTradeItemId()) == null) {
                List<TradeItemOperation> list = new ArrayList<>();
                list.add(operation);
                map.put(operation.getTradeItemId(), list);
            } else {
                map.get(operation.getTradeItemId()).add(operation);
            }
        }

        List<IShopcartItem> shopcartItems = dinnerShoppingCartVo.getListIShopcatItem();
        for (IShopcartItem item : shopcartItems) {
            if (map.get(item.getId()) != null)
                item.setTradeItemOperations(map.get(item.getId()));
            List<? extends ISetmealShopcartItem> iSetmealShopcartItems = item.getSetmealItems();
            if (iSetmealShopcartItems == null || iSetmealShopcartItems.isEmpty())
                continue;
            for (ISetmealShopcartItem iSetmealShopcartItem : iSetmealShopcartItems) {
                if (iSetmealShopcartItem != null && map.get(iSetmealShopcartItem.getId()) != null)
                    iSetmealShopcartItem.setTradeItemOperations(map.get(iSetmealShopcartItem.getId()));
            }
        }

        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());
        resetSelectDishQTY(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo());
        }
    }

    /**
     * @Title: getDinnerShopcartItem
     * @Description: TODO
     * @Param @param 根据套餐、子菜 或套餐子菜uuid从购物车中获取未下过单的菜品信息
     * @Param @return TODO
     * @Return ShopcartItem 返回类型
     */
    public ShopcartItem getDinnerShopcartItem(String uuid) {
        return getShopcartItem(dinnerShoppingCartVo, uuid);
    }

    /**
     * @Title: getDinnerShoppingCartItem
     * @Description: 根据套餐、子菜 或套餐子菜uuid获取菜品信息
     * @Param @param uuid
     * @Param @return TODO
     * @Return IShopcartItem 返回类型
     */
    public IShopcartItemBase getDinnerShoppingCartItem(String uuid) {
        return getIShopcartItem(dinnerShoppingCartVo, uuid);
    }

    /**
     * @Title: getDinnerShoppingCartItem
     * @Description: 根据套餐、子菜 或套餐子菜uuid获取菜品信息
     * @Param @param uuid
     * @Param @return TODO
     * @Return IShopcartItem 返回类型
     */
    public IShopcartItemBase getDinnerShoppingCartItem(Long id) {
        return getIShopcartItem(dinnerShoppingCartVo, id);
    }

    /**
     * @Title: getDinnerDishSelectQTY
     * @Description: 获取正餐中菜品选中数量
     * @Param @return TODO
     * @Return Map<String       ,       BigDecimal> 返回类型
     */
    public Map<String, BigDecimal> getDinnerDishSelectQTY() {
        return getDishSelectQTY(dinnerShoppingCartVo);
    }

    /**
     * 获取票据类型
     */
    public DeliveryType getOrderType() {
        return dinnerShoppingCartVo.getmTradeVo().getTrade().getDeliveryType();
    }

    /**
     * 获取订单数据
     */
    public TradeVo getOrder() {
        return dinnerShoppingCartVo.getmTradeVo();
    }

    /**
     * 获取正餐中菜品添加品项数量
     */
    public int getSelectDishCount() {
        return mergeShopcartItem(dinnerShoppingCartVo).size();
    }

    /**
     * 获取订单中所有菜品
     *
     * @Title: getShoppingCartDish
     * @Description: TODO
     * @Param @return TODO
     * @Return List<Dish_Order_Entity> 返回类型
     */
    public List<IShopcartItem> getShoppingCartDish() {
        return mergeShopcartItem(dinnerShoppingCartVo);
    }

    public void setTradeVo(TradeVo mTradeVo) {
        dinnerShoppingCartVo.setmTradeVo(mTradeVo);
    }

    /**
     * @Title: resetOrderFromTable
     * @Description: TODO
     * @Param dinnertableTradeInfo
     * @Return void 返回类型
     */
    public void resetOrderFromTable(DinnertableTradeInfo dinnertableTradeInfo, boolean isCallback) {
        resetOrderFromTable(null, dinnertableTradeInfo, isCallback);
    }


    public void resetOrderFromTable(DinnertableTradeInfo mainInfo, DinnertableTradeInfo currentTradeInfo, boolean isCallback) {
        resetOrderFromTable(mainInfo, currentTradeInfo, false, isCallback);
    }

    /**
     * @param mainInfo          主单info
     * @param currentTradeInfo  当前选中订单info
     * @param isBUnionMainTrade 合单收银之前是否是联台主单
     */
    public void resetOrderFromTable(DinnertableTradeInfo mainInfo, DinnertableTradeInfo currentTradeInfo, boolean isBUnionMainTrade, boolean isCallback) {
        boolean isGroupMode = dinnerShoppingCartVo.isGroupMode();
        clearShoppingCart(isCallback);
        dinnerShoppingCartVo.setGroupMode(isGroupMode);
        dinnerShoppingCartVo.setDinnertableTradeInfo(currentTradeInfo);
        dinnerShoppingCartVo.setmTradeVo(currentTradeInfo.getTradeVo());
        dinnerShoppingCartVo.getmTradeVo().setBUnionMainTrade(isBUnionMainTrade);
        setOldDeskCount();
        setDeskId(currentTradeInfo);
        initTableTradeInfo(mainInfo, currentTradeInfo);

        //add v8.1 销售员
        if (currentTradeInfo.getTradeVo() != null)
            dinnerShoppingCartVo.setTradeUser(currentTradeInfo.getTradeVo().getTradeUser());
        List<IShopcartItem> listShopcartItem = currentTradeInfo.getItems();
        if (listShopcartItem != null) {
            DinnerUnionShopcartUtil.initSubTradeBatchItem(this.mainTradeInfo, currentTradeInfo, dinnerShoppingCartVo.getListIShopcatItem());
            for (int i = 0; i < listShopcartItem.size(); i++) {
                IShopcartItem mIShopcartItem = listShopcartItem.get(i);
                mIShopcartItem.setIndex(i);
                dinnerShoppingCartVo.getListIShopcatItem().add(mIShopcartItem);
            }
            if (currentTradeInfo.getTradeVo().isBuffetUnionMainTrade()) {
                DinnerUnionShopcartUtil.initBuffetMainTradeSubItems(currentTradeInfo, dinnerShoppingCartVo.getListIShopcatItem());
            } else {
                DinnerUnionShopcartUtil.initMainTradeSubItems(currentTradeInfo, dinnerShoppingCartVo.getListIShopcatItem());
            }
        }
        convertCustomerToArrayCustomer();
        resetSelectDishQTY(dinnerShoppingCartVo);

        //modify 20161213
        if (getOrder().getTrade().getTradePayStatus() != TradePayStatus.PAYING) {
            MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo), currentTradeInfo.getTradeVo());
            CheckGiftCouponIsActived(dinnerShoppingCartVo);
        }
        //modify 20161213
        if (isCallback) {
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).resetOrder(mergeShopcartItem(dinnerShoppingCartVo),
                        dinnerShoppingCartVo.getmTradeVo());
            }
        }
    }

    private void setDeskId(DinnertableTradeInfo currentTradeInfo) {
        if (currentTradeInfo.getiDinnertableTrade() == null || currentTradeInfo.getiDinnertableTrade().getDinnertable() == null) {
            return;
        }
        currentTradeInfo.getTradeVo().setTableId(currentTradeInfo.getiDinnertableTrade().getDinnertable().getId());
        currentTradeInfo.getTradeVo().setTableName(currentTradeInfo.getiDinnertableTrade().getDinnertable().getName());
    }

    /**
     * 将tradeinfo缓存到购物车，方便连桌计算,跳到点菜界面或者结算界面时调用
     *
     * @param mainInfo         不是连桌时，可传空
     * @param currentTradeInfo 当前选择的tradeInfo对象
     */
    public void initTableTradeInfo(DinnertableTradeInfo mainInfo, DinnertableTradeInfo currentTradeInfo) {
        this.mainTradeInfo = mainInfo;
        this.currentTradeInfo = currentTradeInfo;

        if (currentTradeInfo == null || Utils.isEmpty(currentTradeInfo.getSubTradeInfoList())) {
            return;
        }
        List<Long> subTradeIdList = new ArrayList<>();
        Map<Long, TradeTable> subTableMap = new HashMap<>();
        Map<Long, TradeTable> subTradeTableMap = new HashMap<>();
        for (DinnertableTradeInfo dinnertableTradeInfo : currentTradeInfo.getSubTradeInfoList()) {
            subTradeIdList.add(dinnertableTradeInfo.getTradeVo().getTrade().getId());
            for (TradeTable tradeTable : dinnertableTradeInfo.getTradeVo().getTradeTableList()) {
                subTableMap.put(tradeTable.getId(), tradeTable);
                subTradeTableMap.put(tradeTable.getTradeId(), tradeTable);
            }
        }
        currentTradeInfo.getTradeVo().setSubTradeIdList(subTradeIdList);
        currentTradeInfo.getTradeVo().setSubTableMap(subTableMap);
        currentTradeInfo.getTradeVo().setSubTradeTableMap(subTradeTableMap);

    }

    private void convertCustomerToArrayCustomer() {
        // 保存订单中的登录会员信息
        List<TradeCustomer> listTradeCustomer = dinnerShoppingCartVo.getmTradeVo().getTradeCustomerList();
        if (listTradeCustomer != null) {
            for (TradeCustomer mCustomer : listTradeCustomer) {
                if (dinnerShoppingCartVo.getArrayTradeCustomer() == null) {
                    dinnerShoppingCartVo.setArrayTradeCustomer(new HashMap<Integer, TradeCustomer>());
                }
                switch (mCustomer.getCustomerType()) {
                    case MEMBER:
                        dinnerShoppingCartVo.setmTradeMemer(mCustomer);
                        dinnerShoppingCartVo.getArrayTradeCustomer().put(CustomerType.MEMBER.value(), mCustomer);
                        break;
                    case BOOKING:
                        dinnerShoppingCartVo.getArrayTradeCustomer().put(CustomerType.BOOKING.value(), mCustomer);
                        break;
                    case CUSTOMER:
                        dinnerShoppingCartVo.setmTradeMemer(mCustomer);
                        dinnerShoppingCartVo.getArrayTradeCustomer().put(CustomerType.CUSTOMER.value(), mCustomer);
                        break;
                    case CARD:
                        dinnerShoppingCartVo.setmTradeMemer(mCustomer);
                        dinnerShoppingCartVo.getArrayTradeCustomer().put(CustomerType.CARD.value(), mCustomer);
                        break;
                }
            }
        }
    }

    /**
     * @param tradeVo
     * @param isUnionMainTrade 是否是联台主单
     */
    public void updateTradeVoNoTradeInfo(TradeVo tradeVo, boolean isUnionMainTrade, boolean isCallback) {
        DinnertableTradeInfo dinnertableTradeInfo = new DinnertableTradeInfo(tradeVo);
        dinnerShoppingCartVo.setDinnertableTradeInfo(dinnertableTradeInfo);
        resetOrderFromTable(null, dinnertableTradeInfo, isUnionMainTrade, isCallback);
    }

    /**
     * 更新联台主单tradevo到购物车
     *
     * @param tradeVoList
     */
    public void updateUnionMainTradeInfo(List<TradeVo> tradeVoList) {
        List<DinnertableTradeInfo> subTradeInfos = new ArrayList<>();
        mainTradeInfo = getTableTradeInfo(tradeVoList, subTradeInfos);
        resetOrderFromTable(mainTradeInfo, mainTradeInfo, true);
    }

    /**
     * 只更新开台数据
     *
     * @param tradeVo
     */
    public void updateTradeVoNoTradeInfo(TradeVo tradeVo) {
        updateTradeVoNoTradeInfo(tradeVo, false, true);
    }

    //modify 20170608 begin
    public void updateDataFromTradeVo(TradeVo tradeVo) {
        this.updateDataFromTradeVo(tradeVo, true);
    }

    public void updateDataWithTrade(Trade trade) {//add 20171103
        if (trade != null && this.dinnerShoppingCartVo != null && this.dinnerShoppingCartVo.getmTradeVo() != null) {
            this.dinnerShoppingCartVo.getmTradeVo().setTrade(trade);
        }
    }

    public void updateDataFromTradeVo(TradeVo tradeVo, boolean isCallback) {
        if (tradeVo == null || dinnerShoppingCartVo == null || dinnerShoppingCartVo.getDinnertableTradeInfo() == null) {
            return;
        }
        DinnertableTradeInfo dinnertableTradeInfo = dinnerShoppingCartVo.getDinnertableTradeInfo();
        dinnertableTradeInfo.setTradeVo(tradeVo);
        resetOrderFromTable(dinnertableTradeInfo, isCallback);
    }
    //modify 20170608 end

    /**
     * 刷新购物车的催菜
     *
     * @Title: refreshRemindDish
     * @Return void 返回类型
     */
    public void refreshTradeItemOperations(Map<String, List<TradeItemOperation>> tradeItemOperationMap) {
        if (dinnerShoppingCartVo == null || dinnerShoppingCartVo.getDinnertableTradeInfo() == null) {
            return;
        }
        boolean changed = false;
        if (tradeItemOperationMap != null && tradeItemOperationMap.size() > 0) {
            for (Entry<String, List<TradeItemOperation>> entry : tradeItemOperationMap.entrySet()) {
                IShopcartItemBase item = getDinnerShoppingCartItem(entry.getKey());
                if (item != null && item instanceof IShopcartItem) {
                    ((IShopcartItem) item).setTradeItemOperations(entry.getValue());
                    changed = true;
                }
            }
        }

        if (changed) {
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).updateDish(mergeShopcartItem(dinnerShoppingCartVo),
                        dinnerShoppingCartVo.getmTradeVo());
            }
        }
    }

    public void updateBeautyDataFromTradeVo(TradeVo tradeVo) {
        this.updateBeautyDataFromTradeVo(tradeVo, true);
    }

    public void updateBeautyDataFromTradeVo(TradeVo tradeVo, boolean isCallback) {
        //余额充值时，不用更新购物车。
        if (tradeVo == null || dinnerShoppingCartVo == null || tradeVo.getTrade().getBusinessType().equalsValue(BusinessType.ONLINE_RECHARGE.value())) {
            return;
        }

        if (dinnerShoppingCartVo.getDinnertableTradeInfo() != null){ // 美业有可能有桌台有可能没有
            DinnertableTradeInfo dinnertableTradeInfo = dinnerShoppingCartVo.getDinnertableTradeInfo();
            dinnertableTradeInfo.setTradeVo(tradeVo);
            resetOrderFromTable(dinnertableTradeInfo,isCallback);
        } else {
            DinnertableTradeInfo tradeInfo=DinnertableTradeInfo.createNoTableBuffet(tradeVo);
            resetOrderFromTable(null,tradeInfo,true);
        }
    }

    /**
     * 为菜品批量添加操作记录
     *
     * @Title: addTradeItemOperations
     * @Return void 返回类型 key表示菜品，value表示要添加的操作记录类型
     */
    public void addTradeItemOperations(Map<IShopcartItemBase, PrintOperationOpType> map) {
        if (dinnerShoppingCartVo == null || dinnerShoppingCartVo.getDinnertableTradeInfo() == null) {
            return;
        }
        if (map == null || map.size() <= 0) {
            return;
        }

        for (Entry<IShopcartItemBase, PrintOperationOpType> entry : map.entrySet()) {
            IShopcartItemBase item = entry.getKey();

            if (entry.getValue() == PrintOperationOpType.WAKE_UP
                    || entry.getValue() == PrintOperationOpType.WAKE_UP_CANCEL
                    || entry.getValue() == PrintOperationOpType.RISE_DISH
                    || entry.getValue() == PrintOperationOpType.RISE_DISH_CANCEL) {
                List<TradeItemOperation> tradeItemOperations = item.getTradeItemOperations();
                if (tradeItemOperations != null && !tradeItemOperations.isEmpty()) {
                    for (TradeItemOperation operation : tradeItemOperations) {
                        if (operation.getOpType() == PrintOperationOpType.WAKE_UP
                                || operation.getOpType() == PrintOperationOpType.WAKE_UP_CANCEL
                                || operation.getOpType() == PrintOperationOpType.RISE_DISH
                                || operation.getOpType() == PrintOperationOpType.RISE_DISH_CANCEL) {
                            operation.setStatusFlag(StatusFlag.INVALID);
                            operation.setChanged(true);
                        }
                    }
                }
            }

            if (item != null) {
                item.addOperation(entry.getValue());
            }
        }

    }

    /**
     * 为菜品批量添加操作记录
     *
     * @Title: removeTradeItemOperations
     * @Return void 返回类型 key表示菜品，value表示要添加的操作记录类型
     */
    public void removeTradeItemOperations(Map<IShopcartItemBase, PrintOperationOpType> map) {
        if (dinnerShoppingCartVo == null || dinnerShoppingCartVo.getDinnertableTradeInfo() == null) {
            return;
        }
        if (map == null || map.size() <= 0) {
            return;
        }
        for (Entry<IShopcartItemBase, PrintOperationOpType> entry : map.entrySet()) {
            IShopcartItemBase item = entry.getKey();
            if (item != null) {
                item.removeOperation(entry.getValue());
            }
        }

    }

    public void resetShopcartPrintData(List<TradeItem> tradeItems, List<TradeItemOperation> tradeItemOperations, List<TradeExtra> tradeExtraList) {
        if (dinnerShoppingCartVo == null || tradeItems == null) {
            return;
        }
        /*
         * 在对菜品进行打印后，
         * 服务端会修改tradeItem的issueStatus和serverUpdateTime，
         * 这时购物车中的tradeItem与服务器上的就不一致了
         * ，所以需要更新购物车中菜品的时间戳和出单状态。
         */

        boolean itemRefreshed = false;
        if (Utils.isNotEmpty(tradeItems)) {
            for (TradeItem tradeItemRef : tradeItems) {
                IShopcartItemBase item = getDinnerShoppingCartItem(tradeItemRef.getUuid());
                if (item != null && item instanceof ReadonlyShopcartItemBase) {
                    ReadonlyShopcartItemBase itemBase = (ReadonlyShopcartItemBase) item;
                    if (Beans.comparator(itemBase.tradeItem).eq(tradeItemRef, "batchNo", "issueStatus", "serverUpdateTime", "clientUpdateTime", "itemRefreshed")) {
                        itemBase.tradeItem.setBatchNo(tradeItemRef.getBatchNo());
                        itemBase.tradeItem.setIssueStatus(tradeItemRef.getIssueStatus());
                        itemBase.tradeItem.setServerUpdateTime(tradeItemRef.getServerUpdateTime());
                        itemBase.tradeItem.setClientUpdateTime(tradeItemRef.getClientUpdateTime());
                        itemBase.tradeItem.setChanged(tradeItemRef.isChanged());

                        itemRefreshed = true;
                    }
                }
            }
        }

        //更新菜品的tradeItemOperation
        if (Utils.isNotEmpty(tradeItemOperations)) {
            for (TradeItemOperation tradeItemOperation : tradeItemOperations) {
                if (tradeItemOperation.getTradeItemId() == null) {
                    continue;
                }

                IShopcartItemBase item = getDinnerShoppingCartItem(tradeItemOperation.getTradeItemId());
                if (item == null) {
                    continue;
                }

                List<TradeItemOperation> tios = item.getTradeItemOperations();
                if (tios != null) {
                    for (int i = tios.size() - 1; i >= 0; i--) {
                        TradeItemOperation tio = tios.get(i);
                        if (Utils.equals(tradeItemOperation.getId(), tio.getId())) {
                            tios.set(i, tradeItemOperation);
                        }
                    }
                }
            }
        }

        if (Utils.isNotEmpty(tradeExtraList)) {
            TradeExtra tradeExtra = tradeExtraList.get(0);
            String currentTradeUuid = getOrder().getTrade().getUuid();
            if (tradeExtra.getTradeUuid().equalsIgnoreCase(currentTradeUuid)) {
                dinnerShoppingCartVo.getmTradeVo().setTradeExtra(tradeExtra);
            }
        }
        if (itemRefreshed) {
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).resetOrder(mergeShopcartItem(dinnerShoppingCartVo),
                        dinnerShoppingCartVo.getmTradeVo());
            }
        }
    }

    /**
     * 从数据库中获取数据刷新购物车中的条目。此方法将阻塞调用线程。
     * 在对购物车的菜品进行了保存后，应该传入null参数全部刷新。 在对菜品进行打印后，应该传入List
     * <TradeItem>刷新被打印的菜品。
     *
     * @param tradeItems 为null时全部刷新，在对菜品打印成功后传入被打印后的菜品
     * @throws Exception
     */
    public void resetShopcartItemFromDB(List<TradeItem> tradeItems) {
        if (dinnerShoppingCartVo == null || dinnerShoppingCartVo.getDinnertableTradeInfo() == null) {
            return;
        }
        if (tradeItems != null) {
            resetShopcartPrintData(tradeItems, null, null);
        } else {
            DinnertableTradeInfo dinnertableTradeInfo = dinnerShoppingCartVo.getDinnertableTradeInfo();
            TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
            try {
                TradeVo tradeVo = tradeDal.findTrade(dinnertableTradeInfo.getTradeVo().getTrade().getUuid(), false);
                dinnertableTradeInfo.setTradeVo(tradeVo);
                resetOrderFromTable(dinnertableTradeInfo, true);

            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    }


    public void resetShopcartItemFromDBEx(List<String> opDishs) {
        if (dinnerShoppingCartVo == null || dinnerShoppingCartVo.getDinnertableTradeInfo() == null) {
            return;
        }
        List<ShopcartItem> orderDishs = dinnerShoppingCartVo.getListOrderDishshopVo();

        List<IShopcartItem> orderDishsStore = dinnerShoppingCartVo.getListIShopcatItem();
        Map<String, IShopcartItem> modifyDishsMap = new HashMap<>();
        if (orderDishsStore != null && !orderDishsStore.isEmpty()) {
            for (IShopcartItem iShopcartItem : orderDishsStore) {
                if (iShopcartItem.isChanged())       //已修改菜品
                    modifyDishsMap.put(iShopcartItem.getUuid(), iShopcartItem);
            }
        }

        if (opDishs != null && !opDishs.isEmpty()) {
            Iterator<ShopcartItem> iterator = orderDishs.iterator();
            while (iterator.hasNext()) {
                ShopcartItem shopcartItem = iterator.next();
                if (opDishs.contains(shopcartItem.getUuid()))
                    iterator.remove();
            }
        }

        DinnertableTradeInfo dinnertableTradeInfo = dinnerShoppingCartVo.getDinnertableTradeInfo();

        boolean isGroupMode = dinnerShoppingCartVo.isGroupMode();
        clearShoppingCart();
        dinnerShoppingCartVo.setGroupMode(isGroupMode);
        dinnerShoppingCartVo.setDinnertableTradeInfo(dinnertableTradeInfo);
        dinnerShoppingCartVo.setListOrderDishshopVo(orderDishs);


        try {
            TradeDal tradeDal = OperatesFactory.create(TradeDal.class);

            if (dinnertableTradeInfo.getTradeVo().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN
                    || dinnertableTradeInfo.getTradeVo().getTrade().getTradeType() == TradeType.UNOIN_TABLE_SUB) {
                List<Trade> trades = tradeDal.getUnionTradesByTrade(dinnertableTradeInfo.getTradeVo().getTrade());
                List<TradeVo> tradeVos = tradeDal.getTradeVosByTrades(trades);

                DinnertableTradeInfo mainTradeInfo = null;
                List<DinnertableTradeInfo> subTradeInfos = new ArrayList<>();
                mainTradeInfo = getTableTradeInfo(tradeVos, subTradeInfos);
                if (dinnertableTradeInfo.getTradeVo().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
                    initTableTradeInfo(mainTradeInfo, mainTradeInfo);
                } else {
                    DinnertableTradeInfo subInfo = null;
                    for (DinnertableTradeInfo info : subTradeInfos) {
                        if (info.getTradeVo().getTrade().getUuid().equals(dinnertableTradeInfo.getTradeVo().getTrade().getUuid())) {
                            subInfo = info;
                            break;
                        }
                    }
                    initTableTradeInfo(mainTradeInfo, subInfo);
                }
                dinnerShoppingCartVo.setmTradeVo(currentTradeInfo.getTradeVo());
            } else {
                TradeVo tradeVo = tradeDal.findTrade(dinnertableTradeInfo.getTradeVo().getTrade().getUuid(), false);
                dinnertableTradeInfo.setTradeVo(tradeVo);
                currentTradeInfo = dinnertableTradeInfo;
                dinnerShoppingCartVo.setmTradeVo(dinnertableTradeInfo.getTradeVo());
            }
        } catch (Exception e) {
            //LogUtil.writeLog("resetShopcartItemFromDBEx", e.toString());
            e.printStackTrace();
        }

        //add v8.1 销售员
        if (currentTradeInfo.getTradeVo() != null)
            dinnerShoppingCartVo.setTradeUser(currentTradeInfo.getTradeVo().getTradeUser());
        List<IShopcartItem> listShopcartItem = currentTradeInfo.getItems();
        if (listShopcartItem != null) {
            DinnerUnionShopcartUtil.initSubTradeBatchItem(this.mainTradeInfo, currentTradeInfo, dinnerShoppingCartVo.getListIShopcatItem());
            for (int i = 0; i < listShopcartItem.size(); i++) {
                IShopcartItem mIShopcartItem = listShopcartItem.get(i);
                mIShopcartItem.setIndex(i);
                dinnerShoppingCartVo.getListIShopcatItem().add(mIShopcartItem);
            }
            DinnerUnionShopcartUtil.initMainTradeSubItems(currentTradeInfo, dinnerShoppingCartVo.getListIShopcatItem());
        }
        convertCustomerToArrayCustomer();
        resetSelectDishQTY(dinnerShoppingCartVo);

        //modify 20161213
        if (getOrder().getTrade().getTradePayStatus() != TradePayStatus.PAYING) {
            MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo), currentTradeInfo.getTradeVo());
            CheckGiftCouponIsActived(dinnerShoppingCartVo);
        }


        List<TradeItemVo> tradeItemVos = dinnerShoppingCartVo.getmTradeVo().getTradeItemList();
        orderDishsStore = dinnerShoppingCartVo.getListIShopcatItem();
        for (int i = 0; i < orderDishsStore.size(); i++) {
            IShopcartItem oldShopcartItem = modifyDishsMap.get(orderDishsStore.get(i).getUuid());
            if (oldShopcartItem != null) {
                orderDishsStore.remove(i);
                orderDishsStore.add(0, modifyDishsMap.get(oldShopcartItem.getUuid()));
                modifyDishsMap.remove(oldShopcartItem.getUuid());
                if (oldShopcartItem instanceof ReadonlyShopcartItemBase) {
                    ReadonlyShopcartItemBase itemBase = (ReadonlyShopcartItemBase) oldShopcartItem;
                    for (TradeItemVo tradeItemVo : tradeItemVos) {
                        if (tradeItemVo.getTradeItem().getUuid().equals(itemBase.getUuid())) {
                            tradeItemVo.setTradeItem(itemBase.tradeItem);
                        }
                    }
                }
            }
        }
        if (!modifyDishsMap.isEmpty()) {
            orderDishsStore.addAll(modifyDishsMap.values());
        }


        //modify 20161213
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).resetOrder(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo());
        }
    }

    /**
     * 获取主单子单DinnertableTradeInfo
     *
     * @param tradeVos
     * @param subInfos
     */
    public static DinnertableTradeInfo getTableTradeInfo(List<TradeVo> tradeVos, List<DinnertableTradeInfo> subInfos) {
        DinnertableTradeInfo mainInfo = null;
        subInfos.clear();
        if (Utils.isNotEmpty(tradeVos)) {
            for (TradeVo tradeVo : tradeVos) {
                if (tradeVo != null) {
                    DinnertableTradeInfo info = new DinnertableTradeInfo(tradeVo);
                    if (tradeVo.getTrade().getTradeType() == TradeType.UNOIN_TABLE_SUB) {
                        subInfos.add(info);
                    } else if (tradeVo.getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
                        mainInfo = info;
                    }
                }
            }
        }
        if (mainInfo != null) {
            mainInfo.setSubTradeInfoList(subInfos);
        }
        return mainInfo;
    }

    /**
     * @Title: updatePrintStatus
     * @Description: 设置菜品停止出单打印
     * @Param @param mShopcartItemBase
     * @Param @param mIssueStatus TODO
     * @Return void 返回类型
     */
    public void updatePrintStatus(IShopcartItemBase mShopcartItemBase, IssueStatus mIssueStatus) {
        List<IShopcartItem> listDish = mergeShopcartItem(dinnerShoppingCartVo);
        Map<String, IssueStatus> uuidIssueStatusMap = new HashMap<>();
        if (Utils.isNotEmpty(listDish)) {
            for (IShopcartItem mIShopcartItem : listDish) {
                if (mIShopcartItem.getUuid().equals(mShopcartItemBase.getUuid())) {
                    boolean isIssueStatusFinished = false;
                    // 修改子菜打印状态
                    List<? extends ISetmealShopcartItem> listSetmeal = mIShopcartItem.getSetmealItems();
                    if (listSetmeal != null && !listSetmeal.isEmpty()) {
                        for (ISetmealShopcartItem setmeal : listSetmeal) {
                            //判断状态是否已出单
                            if (isIssueStatusFinished(mIssueStatus, setmeal)) {
                                isIssueStatusFinished = true;
                                uuidIssueStatusMap.put(setmeal.getUuid(), IssueStatus.FINISHED);
                                setmeal.setIssueStatus(IssueStatus.FINISHED);
                            } else {
                                uuidIssueStatusMap.put(setmeal.getUuid(), mIssueStatus);
                                setmeal.setIssueStatus(mIssueStatus);
                            }
                        }
                    }

                    if (isIssueStatusFinished) {
                        uuidIssueStatusMap.put(mIShopcartItem.getUuid(), IssueStatus.FINISHED);
                        mIShopcartItem.setIssueStatus(IssueStatus.FINISHED);
                    } else {
                        uuidIssueStatusMap.put(mIShopcartItem.getUuid(), mIssueStatus);
                        mIShopcartItem.setIssueStatus(mIssueStatus);
                    }
                    break;
                }
            }
        }

        List<TradeItemVo> listTradeItem = dinnerShoppingCartVo.getmTradeVo().getTradeItemList();
        if (Utils.isNotEmpty(listTradeItem)) {
            for (TradeItemVo item : listTradeItem) {
                /*if (item.getTradeItem().getUuid().equals(mShopcartItemBase.getUuid())
                        || TextUtils.equals(item.getTradeItem().getParentUuid(), mShopcartItemBase.getUuid())) {
                    item.getTradeItem().setIssueStatus(mIssueStatus);
                }*/
                IssueStatus issueStatus = uuidIssueStatusMap.get(item.getTradeItem().getUuid());
                if (issueStatus != null) {
                    item.getTradeItem().setIssueStatus(issueStatus);
                }
            }
        }
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo());
        }
    }

    public boolean isIssueStatusFinished(IssueStatus mIssueStatus, ISetmealShopcartItem setmeal) {
        return (mIssueStatus == IssueStatus.DIRECTLY || mIssueStatus == IssueStatus.ISSUING)
                && !TextUtils.isEmpty(setmeal.getBatchNo());
    }

    /**
     * 设置整单备注
     *
     * @Title: setComment
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    public void setDinnerRemarks(String remarks) {
        setRemarks(dinnerShoppingCartVo, remarks);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).setRemark(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo());
        }
    }

    /**
     * @Title: resetSeparateDish
     * @Description: 恢复已拆单菜品
     * @Param @param uuid TODO
     * @Return void 返回类型
     */
    public void resetSeparateDish(String uuid) {
        List<IShopcartItem> itemList = mergeShopcartItem(dinnerShoppingCartVo);
        for (IShopcartItem shopcartItem : itemList) {

            if (uuid.equals(shopcartItem.getUuid())) {
                shopcartItem.cancelSplit();
                break;
            }
        }
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(itemList, dinnerShoppingCartVo.getmTradeVo());
    }

    /**
     * @Title: getOrderAmount
     * @Description: 获取商品总价，除优惠和附加费
     * @Param @return TODO
     * @Return BigDecimal 返回类型
     */
    public BigDecimal getOrderAmount() {
        BigDecimal saleAmount = dinnerShoppingCartVo.getmTradeVo().getTrade().getSaleAmount();
        List<TradePrivilege> listPrivilege = dinnerShoppingCartVo.getmTradeVo().getTradePrivileges();
        if (listPrivilege != null) {
            for (TradePrivilege mTradePrivilege : listPrivilege) {
                if ((mTradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL || mTradePrivilege.getPrivilegeType() == PrivilegeType.SERVICE) && mTradePrivilege.isValid()) {
                    saleAmount = saleAmount.subtract(mTradePrivilege.getPrivilegeAmount());
                }
            }
        }

        if (Utils.isNotEmpty(dinnerShoppingCartVo.getmTradeVo().getTradeTaxs())) {
            for (TradeTax tradeTax : dinnerShoppingCartVo.getmTradeVo().getTradeTaxs()) {
                if (!tradeTax.isValid()) {
                    continue;
                }
                saleAmount = saleAmount.subtract(tradeTax.getTaxAmount());
            }
        }

        // add 20160614 start
        List<IShopcartItem> listShopcarItems = mergeShopcartItem(dinnerShoppingCartVo);
        if (listShopcarItems != null) {
            TradePrivilege privilege = null;
            for (IShopcartItem item : listShopcarItems) {
                privilege = item.getPrivilege();
                if (item.getStatusFlag() == StatusFlag.VALID && privilege != null
                        && privilege.getStatusFlag() == StatusFlag.VALID
                        && (privilege.getPrivilegeType() == PrivilegeType.FREE || privilege.getPrivilegeType() == PrivilegeType.GIVE)) {
                    saleAmount = saleAmount.add(privilege.getPrivilegeAmount());
                }
            }
        }
        // add 20160614 end
        return saleAmount;
    }

    /**
     * @Title: setTradePeopleCount
     * @Description: 设置订单就餐人数
     * @Param @param count TODO
     * @Return void 返回类型
     */
    public void setTradePeopleCount(int count) {
        if (dinnerShoppingCartVo.getmTradeVo() != null && dinnerShoppingCartVo.getmTradeVo().getTrade() != null) {
            dinnerShoppingCartVo.getmTradeVo().getTrade().setTradePeopleCount(count);
        }
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());
    }


    /**
     * @Title: isReturnCash
     * @Description: 判断当前订单是否是反结账单
     * @Param @return TODO
     * @Return Boolean 返回类型
     */
    public Boolean isReturnCash() {
        TradeType mTradeType = null;
        if (dinnerShoppingCartVo.getmTradeVo() != null && dinnerShoppingCartVo.getmTradeVo().getTrade() != null) {
            mTradeType = dinnerShoppingCartVo.getmTradeVo().getTrade().getTradeType();
        }
        if (mTradeType != null && mTradeType == TradeType.SELL_FOR_REPEAT) {
            return true;
        }
        return false;

        // // 测试用
        // return true;
    }

    /**
     * @Title: filterByTable
     * @Description: 根据桌台过滤菜品
     * @Param @param tableId TODO
     * @Return void 返回类型
     */
    public void filterByTable(String tableId) {

    }

    public ShopcartItem getTempShopItem() {
        return dinnerShoppingCartVo.getTempShopItem();
    }

    public void setTempShopItem(ShopcartItem tempShopItem) {
        dinnerShoppingCartVo.setTempShopItem(tempShopItem);
    }

    public ShoppingCartVo getShoppingCartVo() {
        return dinnerShoppingCartVo;
    }

    public Map<Integer, TradeCustomer> getArrayTradeCustomer() {
        return dinnerShoppingCartVo.getArrayTradeCustomer();
    }

    public int getIndexPage() {
        return dinnerShoppingCartVo.getIndexPage();
    }

    public void setIndexPage(int indexPage) {
        dinnerShoppingCartVo.setIndexPage(indexPage);
    }

    /**
     * @Title: createDinnerTradeVo
     * @Description: 创建正餐订单
     * @Param @return TODO
     * @Return TradeVo 返回类型
     */
    public TradeVo createDinnerTradeVo() {
        TradeVo mTradeVo = createOrder(dinnerShoppingCartVo, false);

        // 将退回商品中得tradeItem添加到trade中
        List<TradeItemVo> listTradeItem = mTradeVo.getTradeItemList();
        Map<String, TradeItemVo> tempTradeItem = new HashMap<String, TradeItemVo>();
        for (TradeItemVo mTradeItemVo : listTradeItem) {
            tempTradeItem.put(mTradeItemVo.getTradeItem().getUuid(), mTradeItemVo);
        }

        // 获取所以只读菜品
        if (dinnerShoppingCartVo.getListIShopcatItem() != null) {
            for (IShopcartItem mIShopcartItem : dinnerShoppingCartVo.getListIShopcatItem()) {

                // 将退菜理由数据信息添加到原单的tradeItem中
                TradeItemVo mTradeItemVo = tempTradeItem.get(mIShopcartItem.getUuid());
                if (mTradeItemVo != null && mTradeItemVo.getRejectQtyReason() != null) {
                    mTradeItemVo.setRejectQtyReason(mIShopcartItem.getReturnQtyReason());
                }
                // 将退回菜品生成的新只读菜品转换为tradeItemVo
                if (tempTradeItem.get(mIShopcartItem.getUuid()) == null
                        && mIShopcartItem instanceof ReadonlyShopcartItemBase) {

                    ReadonlyShopcartItemBase mReadonlyShopcartItemBase = (ReadonlyShopcartItemBase) mIShopcartItem;
                    listTradeItem.add(CreateTradeTool.buildReadOnlyTradeItemVo(mReadonlyShopcartItemBase));

                    // 如果是套餐则需要将子菜对应的tradeItem也添加到tradeVo中
                    List<? extends ISetmealShopcartItem> listSetmeal = mIShopcartItem.getSetmealItems();
                    if (listSetmeal != null) {
                        for (ISetmealShopcartItem item : listSetmeal) {
                            ReadonlyShopcartItemBase readonlySetmeal = (ReadonlyShopcartItemBase) item;
                            if (item instanceof ReadonlyShopcartItemBase) {
                                listTradeItem.add(CreateTradeTool.buildReadOnlyTradeItemVo(readonlySetmeal));
                            }

                            // 加料
                            Collection<ReadonlyExtraShopcartItem> listExtra = mReadonlyShopcartItemBase.getExtraItems();
                            if (listExtra != null) {
                                for (ReadonlyExtraShopcartItem mReadonlyExtraShopcartItem : listExtra) {
                                    listTradeItem
                                            .add(CreateTradeTool.buildReadOnlyTradeItemVo(mReadonlyExtraShopcartItem));
                                }
                            }
                        }
                    }

                    // 加料
                    Collection<ReadonlyExtraShopcartItem> listExtra = mReadonlyShopcartItemBase.getExtraItems();
                    if (listExtra != null) {
                        for (ReadonlyExtraShopcartItem mReadonlyExtraShopcartItem : listExtra) {
                            listTradeItem.add(CreateTradeTool.buildReadOnlyTradeItemVo(mReadonlyExtraShopcartItem));
                        }
                    }

                }
            }
        }

        // 将营销活动添加到TradePrivilege中
        setMarktingTradePrivilege(mTradeVo);

        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());

        return mTradeVo;
    }

    /**
     * @Title: setDishPrivilege
     * @Description: 设置菜品折扣
     * @Param mIShopcartItemBase
     * @Param mPrivilege TODO
     * @Return void 返回类型
     */
    public void setShopcartItemPrivilege(IShopcartItemBase mIShopcartItemBase, Reason reason) {
        //礼品劵和单品优惠不共存
        removeShopcartItemCoupon(mIShopcartItemBase);

        BuildPrivilegeTool.buildPrivilege(mIShopcartItemBase, dinnerShoppingCartVo.getmTradeVo().getTrade().getUuid());
        //添加理由
        if (reason != null) {
            if (mIShopcartItemBase.getDiscountReasonRel() != null) {
                mIShopcartItemBase.getDiscountReasonRel().setReasonContent(reason.getContent());
                mIShopcartItemBase.getDiscountReasonRel().setReasonId(reason.getId());
                mIShopcartItemBase.getDiscountReasonRel().setStatusFlag(StatusFlag.VALID);
                mIShopcartItemBase.getDiscountReasonRel().validateUpdate();
            } else {
                TradeReasonRel reasonRel = setTradeItemReasonRel(mIShopcartItemBase, reason, OperateType.ITEM_GIVE);
                mIShopcartItemBase.setDiscountReasonRel(reasonRel);
            }
        } else {
            mIShopcartItemBase.setDiscountReasonRel(null);
        }
        //移除宴请
        removeBanquetOnly(dinnerShoppingCartVo.getmTradeVo());
        //判断该菜是否已有tradeItem
        List<TradeItemVo> listTradeItem = dinnerShoppingCartVo.getmTradeVo().getTradeItemList();
        if (listTradeItem != null) {
            for (TradeItemVo item : listTradeItem) {
                if (item.getTradeItem().getUuid().equals(mIShopcartItemBase.getUuid())) {
                    if (item.getTradeItemPrivilege() != null) {
                        copyOnlyData(mIShopcartItemBase.getPrivilege(), item.getTradeItemPrivilege());
                        item.getTradeItemPrivilege().setChanged(true);
                    } else {
                        item.setTradeItemPrivilege(mIShopcartItemBase.getPrivilege());
                        item.getTradeItem().setChanged(true);
                        item.getTradeItemPrivilege().setChanged(true);
                    }
                    break;
                }
            }
        }

        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).batchPrivilege(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo());

        }
    }


    /**
     * @Title: removeShopcarItemPrivilege
     * @Description: 移除菜品折扣
     * @Param @param mIShopcartItemBase TODO
     * @Return void 返回类型
     */
    public void removeShopcarItemPrivilege(IShopcartItemBase mShopcartItem) {
        TradePrivilege mTradePrivilege = mShopcartItem.getPrivilege();
        if (mTradePrivilege == null) {
            return;
        }
        if (mTradePrivilege.getId() == null) {
            if (mTradePrivilege.getPrivilegeType() == PrivilegeType.DISCOUNT ||
                    mTradePrivilege.getPrivilegeType() == PrivilegeType.REBATE ||
                    mTradePrivilege.getPrivilegeType() == PrivilegeType.FREE
                    || mTradePrivilege.getPrivilegeType() == PrivilegeType.GIVE) {
                mShopcartItem.setPrivilege(null);
                mShopcartItem.setDiscountReasonRel(null);
                //判断该菜是否已有tradeItem
                List<TradeItemVo> listTradeItem = dinnerShoppingCartVo.getmTradeVo().getTradeItemList();
                if (listTradeItem != null) {
                    for (TradeItemVo item : listTradeItem) {
                        if (item.getTradeItem().getUuid().equals(mShopcartItem.getUuid())) {
                            if (item.getTradeItemPrivilege() != null) {
                                item.setTradeItemPrivilege(null);
                                TradeReasonRel tradeReasonRel = item.getReasonLast();
                                if (tradeReasonRel != null) {
                                    item.removeTradeReasonRel(tradeReasonRel, tradeReasonRel.getOperateType());
                                }
                            }
                            break;
                        }
                    }
                }
            }
        } else {
            mTradePrivilege.setInValid();
        }
        if (mShopcartItem.getDiscountReasonRel() != null && mShopcartItem.getDiscountReasonRel().getId() != null) {
            mShopcartItem.getDiscountReasonRel().setStatusFlag(StatusFlag.INVALID);
            mShopcartItem.getDiscountReasonRel().validateUpdate();
        }

        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).batchPrivilege(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo());
        }
    }

    /**
     * 在需要处理订单金额为负时处理
     */
    public void mathNegativeTrade() {
        if (dinnerShoppingCartVo.getmTradeVo().getTrade() != null && dinnerShoppingCartVo.getmTradeVo().getTrade().getTradeAmount().compareTo(BigDecimal.ZERO) < 0) {
            List<TradePrivilege> tradePrivilegeList = dinnerShoppingCartVo.getmTradeVo().getTradePrivileges();
            if (tradePrivilegeList != null) {
                for (TradePrivilege tradePrivilege : tradePrivilegeList) {
                    if (tradePrivilege.getPrivilegeType() == PrivilegeType.REBATE) {
                        tradePrivilege.setStatusFlag(StatusFlag.INVALID);
                        tradePrivilege.setChanged(true);
                        tradePrivilege.validateUpdate();
                        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo), dinnerShoppingCartVo.getmTradeVo());
                        break;
                    }
                }
            }
        }
    }


    /**
     * 删除整单让价
     */
    public void removeTradeRebate() {
        if (getOrder() != null && Utils.isNotEmpty(getOrder().getTradePrivileges())) {
            List<TradePrivilege> privilegeList = getOrder().getTradePrivileges();
            for (int i = 0; i < privilegeList.size(); i++) {
                TradePrivilege privilege = privilegeList.get(i);
                if (privilege.getPrivilegeType() == PrivilegeType.REBATE && TextUtils.isEmpty(privilege.getTradeItemUuid())) {
                    if (privilege.getId() == null) {
                        privilegeList.remove(i);
                    } else {
                        privilege.setStatusFlag(StatusFlag.INVALID);
                    }

                    List<IShopcartItem> allIttem = mergeShopcartItem(dinnerShoppingCartVo);
                    // 计算订单总价格
                    MathShoppingCartTool.mathTotalPrice(allIttem, dinnerShoppingCartVo.getmTradeVo());
                    for (int key : arrayListener.keySet()) {
                        arrayListener.get(key).updateDish(allIttem, dinnerShoppingCartVo.getmTradeVo());
                    }
                    break;
                }
            }
        }
    }

    /**
     * 修改订单人数
     */
    public void modifyCustomerCount(int customerCount) {
        TradeVo tradeVo = getOrder();
        if (tradeVo != null && tradeVo.getTrade() != null && Utils.isNotEmpty(tradeVo.getTradeTableList())) {
            TradeTable tradeTable = tradeVo.getTradeTableList().get(0);
            tradeTable.setTablePeopleCount(customerCount);
            tradeTable.setChanged(true);

            tradeVo.getTrade().setTradePeopleCount(customerCount);

            List<IShopcartItem> allIttem = mergeShopcartItem(dinnerShoppingCartVo);
            // 计算订单总价格
            MathShoppingCartTool.mathTotalPrice(allIttem, dinnerShoppingCartVo.getmTradeVo());
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).updateDish(allIttem, dinnerShoppingCartVo.getmTradeVo());
            }
        }
    }

    /**
     * 刷新菜品
     */
    public void refreshDish() {
        List<IShopcartItem> allIttem = mergeShopcartItem(dinnerShoppingCartVo);
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(allIttem, dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(allIttem, dinnerShoppingCartVo.getmTradeVo());
        }
    }

    /**
     * 编辑押金
     */
    public void modifyDeposit(Double value) {
        dinnerShoppingCartVo.getmTradeVo().getTradeDeposit().setChanged(true);
        dinnerShoppingCartVo.getmTradeVo().getTradeDeposit().setUnitPrice(BigDecimal.valueOf(value));
        dinnerShoppingCartVo.getmTradeVo().getTradeDeposit().setDepositPay(BigDecimal.valueOf(value));
        dinnerShoppingCartVo.getmTradeVo().getTradeDeposit().setType(3);        //自定义押金类型
        List<IShopcartItem> allIttem = mergeShopcartItem(dinnerShoppingCartVo);
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(allIttem, dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(allIttem, dinnerShoppingCartVo.getmTradeVo());
        }
    }

    /**
     * 清空购物车
     *
     * @param isNeedCallback 是否需要回调
     * @Title: clearShoppingCart
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    public void clearShoppingCart(boolean isNeedCallback) {
        dinnerShoppingCartVo = new ShoppingCartVo();
        if (!isNeedCallback) {
            return;
        }
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).clearShoppingCart();
        }
    }

    public void clearShoppingCart() {
        clearShoppingCart(true);
    }

    /**
     * @Date 2016/9/1
     * @Description:获取传入购物车的 IDinnertableTrade
     * @Param
     * @Return
     */
    public IDinnertableTrade getIDinnertableTrade() {
        DinnertableTradeInfo tradeInfo = dinnerShoppingCartVo.getDinnertableTradeInfo();
        if (tradeInfo != null) {
            return tradeInfo.getiDinnertableTrade();
        } else {
            return null;
        }


    }

    /**
     * 重新计算包括营销活动的解绑
     */
    public void onlyMath() {
        List<IShopcartItem> allIttem = mergeShopcartItem(dinnerShoppingCartVo);

        MathManualMarketTool.mathMarketPlan(dinnerShoppingCartVo.getmTradeVo(),
                allIttem, true, false);
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(allIttem, dinnerShoppingCartVo.getmTradeVo());

    }

    /**
     * 购物车中是否包含有效菜品
     *
     * @return
     */
    public boolean hasValidItems() {
        List<IShopcartItem> mListOrderDishshopVo = mergeShopcartItem(dinnerShoppingCartVo);
        for (IShopcartItem shopcartItem : mListOrderDishshopVo) {
            if (shopcartItem.getStatusFlag() == StatusFlag.VALID
                    && shopcartItem.getSingleQty().compareTo(BigDecimal.ZERO) > 0) {
                return true;
            }
        }
        return false;
    }


    //各种优惠计算

    /**
     * @Title: setDefineTradePrivilege
     * @Description: 设置手动整单折扣
     * @Param tradePrivilege
     * @Param mReason
     * @Param needMath 是否需要计算订单金额：默认为计算 true 计算 false 不计算
     * @Param needListener 是否需要回调：true 需要回调 false 不需要回调
     * @Return void 返回类型
     */

    public void setDefineTradePrivilege(TradePrivilege tradePrivilege, Reason mReason, Boolean needMath,
                                        Boolean needListener) {

        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());
        //移除宴请
        removeBanquetOnly(dinnerShoppingCartVo.getmTradeVo());
        // 移除优惠劵（因优惠劵与手动整单折扣不可并存）
//        dinnerShoppingCartVo.getmTradeVo().setCouponPrivilege(null);
        removeAllCouponPrivilege(dinnerShoppingCartVo, false);
        if (tradePrivilege != null) {
            if (tradePrivilege.getId() == null) {
                tradePrivilege.setUuid(SystemUtils.genOnlyIdentifier());
                tradePrivilege.validateCreate();
            } else {
                tradePrivilege.validateUpdate();
            }
            tradePrivilege.setTradeUuid(dinnerShoppingCartVo.getmTradeVo().getTrade().getUuid());

        }

        dinnerShoppingCartVo.getmTradeVo().replaceTradePrivilege(tradePrivilege);

        // 设置免单理由

        if (mReason != null) {
            if (tradePrivilege.getPrivilegeType() == PrivilegeType.FREE || tradePrivilege.getPrivilegeType() == PrivilegeType.GIVE) {
                setTradeFreeReasonRel(dinnerShoppingCartVo.getmTradeVo(), mReason, OperateType.TRADE_DINNER_FREE);
            } else if (tradePrivilege.getPrivilegeType() == PrivilegeType.DISCOUNT) {
                setTradeFreeReasonRel(dinnerShoppingCartVo.getmTradeVo(), mReason, OperateType.TRADE_DISCOUNT);
            } else if (tradePrivilege.getPrivilegeType() == PrivilegeType.REBATE) {
                setTradeFreeReasonRel(dinnerShoppingCartVo.getmTradeVo(), mReason, OperateType.TRADE_REBATE);
            }
        } else {
            if (tradePrivilege.getPrivilegeType() == PrivilegeType.FREE || tradePrivilege.getPrivilegeType() == PrivilegeType.GIVE) {
                removeFreeReason(dinnerShoppingCartVo.getmTradeVo());
            } else if (tradePrivilege.getPrivilegeType() == PrivilegeType.DISCOUNT || tradePrivilege.getPrivilegeType() == PrivilegeType.DISCOUNT) {
                removeReason(dinnerShoppingCartVo.getmTradeVo(), OperateType.TRADE_DISCOUNT);
            } else {
                removeReason(dinnerShoppingCartVo.getmTradeVo(), OperateType.TRADE_DISCOUNT);
                removeReason(dinnerShoppingCartVo.getmTradeVo(), OperateType.TRADE_DINNER_FREE);
            }
        }
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        // 计算订单总价格
        if (needMath) {
            MathShoppingCartTool.mathTotalPrice(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        }

        if (needListener) {
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).batchPrivilege(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());

            }
        }

    }

    /**
     * @Title: removeOrderPrivilege
     * @Description: 移除手动整单打折并计算
     * @Param TODO
     * @Return void 返回类型
     */

    public void removeOrderPrivilege() {
        removeOrderTreadePrivilege(dinnerShoppingCartVo.getmTradeVo());
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        // 计算订单总价格

        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).batchPrivilege(shopcartItemList,

                    dinnerShoppingCartVo.getmTradeVo());

        }

    }

    /**
     * 折扣设置
     */
    public void setDishPrivilege(TradePrivilege mPrivilege, Reason reason) {
        setDishPrivilege(mPrivilege, reason, null);
    }

    public void setDishPrivilege (TradePrivilege mPrivilege, Reason reason, OperateType operateType,String uuid){
        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());

        dinnerShoppingCartVo.setDishTradePrivilege(mPrivilege);


                for (IShopcartItemBase mShopcartItemBase : mergeShopcartItem(dinnerShoppingCartVo)) {

                    // 判断是否包含在选中的菜品中

                    if (mShopcartItemBase.getUuid().equals(uuid)) {

                        //礼品劵和单品优惠不共存
                        removeShopcartItemCoupon(mShopcartItemBase);

                        if (mPrivilege != null) {

                            TradePrivilege mTradePrivilege = new TradePrivilege();

                            mTradePrivilege.setPrivilegeType(mPrivilege.getPrivilegeType());

                            mTradePrivilege.setPrivilegeValue(mPrivilege.getPrivilegeValue());

                            mTradePrivilege.setPrivilegeName(mPrivilege.getPrivilegeName());

                            mShopcartItemBase.setPrivilege(mTradePrivilege);

                            mTradePrivilege = BuildPrivilegeTool.buildPrivilege(mShopcartItemBase,

                                    dinnerShoppingCartVo.getmTradeVo().getTrade().getUuid());
                            mShopcartItemBase.setPrivilege(mTradePrivilege);
                            mShopcartItemBase.setDiscountReasonRel(null);
                            if (reason != null) {
                                TradeReasonRel tradeReasonRel = setTradeItemReasonRel(mShopcartItemBase, reason, operateType);
                                mShopcartItemBase.setDiscountReasonRel(tradeReasonRel);
                            }

                        } else {

//                            mShopcartItemBase.setPrivilege(null);
                            //移除折扣的同时移除理由
//                            mShopcartItemBase.setDiscountReasonRel(null);
                            if (mShopcartItemBase.getPrivilege() != null && mShopcartItemBase.getPrivilege().getId() != null) {
                                mShopcartItemBase.getPrivilege().setInValid();
                            }
                            if (mShopcartItemBase.getDiscountReasonRel() != null && mShopcartItemBase.getDiscountReasonRel().getId() != null) {
                                mShopcartItemBase.getDiscountReasonRel().setStatusFlag(StatusFlag.INVALID);
                                mShopcartItemBase.getDiscountReasonRel().validateUpdate();
                            }
                        }

                        break;

                    }

                }

        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        // 计算订单总价格

        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).batchPrivilege(shopcartItemList,

                    dinnerShoppingCartVo.getmTradeVo());

        }
    }

    public void setDishPrivilege(TradePrivilege mPrivilege, Reason reason, OperateType operateType) {
        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());

        dinnerShoppingCartVo.setDishTradePrivilege(mPrivilege);

        if (dinnerShoppingCartVo.getDinnerListShopcartItem() != null) {

            // 是你选中要打折的菜品，而我们实际需要修改的却是购物车中实际的菜品

            for (IShopcartItemBase mdinnerShopcartItemBase : dinnerShoppingCartVo.getDinnerListShopcartItem()) {

                for (IShopcartItemBase mShopcartItemBase : mergeShopcartItem(dinnerShoppingCartVo)) {

                    // 判断是否包含在选中的菜品中

                    if (mdinnerShopcartItemBase.getUuid().equals(mShopcartItemBase.getUuid())) {

                        //礼品劵和单品优惠不共存
                        removeShopcartItemCoupon(mShopcartItemBase);

                        if (mPrivilege != null) {

                            TradePrivilege mTradePrivilege = new TradePrivilege();

                            mTradePrivilege.setPrivilegeType(mPrivilege.getPrivilegeType());

                            mTradePrivilege.setPrivilegeValue(mPrivilege.getPrivilegeValue());

                            mTradePrivilege.setPrivilegeName(mPrivilege.getPrivilegeName());

                            mShopcartItemBase.setPrivilege(mTradePrivilege);

                            mTradePrivilege = BuildPrivilegeTool.buildPrivilege(mShopcartItemBase,

                                    dinnerShoppingCartVo.getmTradeVo().getTrade().getUuid());
                            mShopcartItemBase.setPrivilege(mTradePrivilege);
                            mShopcartItemBase.setDiscountReasonRel(null);
                            if (reason != null) {
                                TradeReasonRel tradeReasonRel = setTradeItemReasonRel(mShopcartItemBase, reason, operateType);
                                mShopcartItemBase.setDiscountReasonRel(tradeReasonRel);
                            }

                        } else {

//                            mShopcartItemBase.setPrivilege(null);
                            //移除折扣的同时移除理由
//                            mShopcartItemBase.setDiscountReasonRel(null);
                            if (mShopcartItemBase.getPrivilege() != null && mShopcartItemBase.getPrivilege().getId() != null) {
                                mShopcartItemBase.getPrivilege().setInValid();
                            }
                            if (mShopcartItemBase.getDiscountReasonRel() != null && mShopcartItemBase.getDiscountReasonRel().getId() != null) {
                                mShopcartItemBase.getDiscountReasonRel().setStatusFlag(StatusFlag.INVALID);
                                mShopcartItemBase.getDiscountReasonRel().validateUpdate();
                            }
                        }

                        break;

                    }

                }

            }

        }
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        // 计算订单总价格

        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).batchPrivilege(shopcartItemList,

                    dinnerShoppingCartVo.getmTradeVo());

        }

    }


    /**
     * @Title: batchDishPrivilege
     * @Description: 批量添加选中菜品进行折扣
     * @Param @param listShopcartItem TODO
     * @Return void 返回类型
     */
    public void batchDishPrivilege(List<IShopcartItemBase> listShopcartItem) {
        batchDishPrivilege(listShopcartItem, true);
    }

    public void batchDishPrivilege(List<IShopcartItemBase> listShopcartItem, boolean isRemoveBanquet) {
        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());

        //移除宴请
        if (isRemoveBanquet) {
            removeBanquetOnly(dinnerShoppingCartVo.getmTradeVo());
        }

        dinnerShoppingCartVo.getDinnerListShopcartItem().clear();

        dinnerShoppingCartVo.getDinnerListShopcartItem().addAll(listShopcartItem);

        if (dinnerShoppingCartVo.getDishTradePrivilege() == null) {

            return;

        }

        for (IShopcartItemBase item : listShopcartItem) {

            for (IShopcartItem sitem : mergeShopcartItem(dinnerShoppingCartVo)) {

                if (item.getUuid().equals(sitem.getUuid())) {
                    //礼品劵和单品优惠不共存
                    if (sitem.getCouponPrivilegeVo() != null && sitem.getCouponPrivilegeVo().getTradePrivilege() != null) {
                        if (sitem.getCouponPrivilegeVo().getTradePrivilege().getId() != null) {
                            sitem.getCouponPrivilegeVo().getTradePrivilege().setStatusFlag(StatusFlag.INVALID);
                            sitem.getCouponPrivilegeVo().getTradePrivilege().setChanged(true);
                        } else {
                            sitem.setCouponPrivilegeVo(null);
                        }
                    }

                    TradePrivilege mTradePrivilege = new TradePrivilege();
                    mTradePrivilege.setPrivilegeName(dinnerShoppingCartVo.getDishTradePrivilege().getPrivilegeName());

                    mTradePrivilege.setPrivilegeType(dinnerShoppingCartVo.getDishTradePrivilege().getPrivilegeType());

                    mTradePrivilege

                            .setPrivilegeValue(dinnerShoppingCartVo.getDishTradePrivilege().getPrivilegeValue());

                    sitem.setPrivilege(mTradePrivilege);

                    mTradePrivilege = BuildPrivilegeTool.buildPrivilege(sitem,

                            dinnerShoppingCartVo.getmTradeVo().getTrade().getUuid());

                    sitem.setPrivilege(mTradePrivilege);
                    break;

                }

            }

        }
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        // 计算订单总价格

        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).batchPrivilege(shopcartItemList,

                    dinnerShoppingCartVo.getmTradeVo());

        }

    }

    /**
     * @Title: getDishPrivilege
     * @Description: 获取单菜折扣比例设置信息
     * @Param TODO
     * @Return void 返回类型
     */

    public TradePrivilege getDishPrivilege() {

        return dinnerShoppingCartVo.getDishTradePrivilege();

    }


    /**
     * @Title: removeDishPrivilege
     * @Description: 移除菜品优惠折扣
     * @Param @param mIshopcartItem TODO
     * @Return void 返回类型
     */

    public void removeDishPrivilege(IShopcartItem mIshopcartItem) {

        if (dinnerShoppingCartVo.getDinnerListShopcartItem() != null) {

            for (IShopcartItemBase mShopcartItemBase : mergeShopcartItem(dinnerShoppingCartVo)) {

                if (mIshopcartItem.getUuid().equals(mShopcartItemBase.getUuid())) {

                    mShopcartItemBase.setPrivilege(null);
                    if (mShopcartItemBase.getDiscountReasonRel() != null && mShopcartItemBase.getDiscountReasonRel().getId() != null) {
                        mShopcartItemBase.getDiscountReasonRel().setStatusFlag(StatusFlag.INVALID);
                        mShopcartItemBase.getDiscountReasonRel().validateUpdate();
                    }
                }

            }

        }
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        // 重新计算营销活动
        MathManualMarketTool.mathMarketPlan(dinnerShoppingCartVo.getmTradeVo(), shopcartItemList, true, DinnerShopManager.getInstance().isSepartShopCart());
        // 计算订单总价格

        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).batchPrivilege(shopcartItemList,

                    dinnerShoppingCartVo.getmTradeVo());

        }

    }

    /**
     * @Title: removeAllDishPrivilege
     * @Description: 移除所有选择菜品折扣信息
     * @Param TODO
     * @Return void 返回类型
     */

    public void removeAllSelectedPrivilege() {

        if (dinnerShoppingCartVo.getDinnerListShopcartItem() != null) {

            for (IShopcartItemBase mdinnerShopcartItemBase : dinnerShoppingCartVo.getDinnerListShopcartItem()) {

                for (IShopcartItemBase mShopcartItemBase : mergeShopcartItem(dinnerShoppingCartVo)) {

                    if (mdinnerShopcartItemBase.getUuid().equals(mShopcartItemBase.getUuid())) {

                        mShopcartItemBase.setPrivilege(null);

                    }

                }

            }

        }
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        // 计算订单总价格

        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).batchPrivilege(shopcartItemList,

                    dinnerShoppingCartVo.getmTradeVo());

        }

    }


    public void removedPrivilege(String uuid) {


        for (IShopcartItemBase mShopcartItemBase : mergeShopcartItem(dinnerShoppingCartVo)) {

            if (mShopcartItemBase.getUuid().equals(uuid)) {

                mShopcartItemBase.setPrivilege(null);
                break;

            }

        }

        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        // 计算订单总价格

        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).batchPrivilege(shopcartItemList,

                    dinnerShoppingCartVo.getmTradeVo());

        }

    }

    /**
     * @Title: removeAllItemsPrivilege
     * @Description: 移除所有菜品单品折扣信息
     * @Param TODO
     * @Return void 返回类型
     */

    public void removeAllItemsPrivilege() {
        removeAllItemsPrivilege(true, true);
    }

    /**
     * @param isNeedCallback 是否需要回调
     * @param isRemoveSaved  是否移菜保存过的数据
     */
    private void removeAllItemsPrivilege(boolean isNeedCallback, boolean isRemoveSaved) {
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        dinnerShoppingCartVo.setDishTradePrivilege(null);
        if (shopcartItemList != null) {
            for (IShopcartItemBase mdinnerShopcartItemBase : shopcartItemList) {
                if (!isRemoveSaved && mdinnerShopcartItemBase.getId() != null) {
                    continue;
                }
                mdinnerShopcartItemBase.setPrivilege(null);
            }
        }

        // 计算订单总价格

        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());

        if (isNeedCallback) {
            for (int key : arrayListener.keySet()) {

                arrayListener.get(key).batchPrivilege(shopcartItemList,

                        dinnerShoppingCartVo.getmTradeVo());

            }
        }
    }

    /**
     * @Title: setCouponPrivilege
     * @Description: 设置优惠劵价
     * @Param mCouponPrivilegeVo
     * @Param needMath 是否需要计算订单金额：默认为计算 true 计算 false 不计算
     * @Param needListener 是否需要回调：true 需要回调 false 不需要回调
     * @Return void 返回类型
     */
    public void setCouponPrivilege(CouponPrivilegeVo mCouponPrivilegeVo, Boolean needMath, Boolean needListener) {

        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());
        //移除宴请
        removeBanquetOnly(dinnerShoppingCartVo.getmTradeVo());
        // 移除手动整单折扣（因手动整单折扣与优惠劵不可并存）
        removeOrderTreadePrivilege(dinnerShoppingCartVo.getmTradeVo());
        //未核销过的才创建新的
        if (!mCouponPrivilegeVo.isUsed())
            BuildPrivilegeTool.buildCouponPrivilege(dinnerShoppingCartVo.getmTradeVo(), mCouponPrivilegeVo);
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        // 计算订单总价格
        if (needMath) {
            MathShoppingCartTool.mathTotalPrice(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        }

        if (needListener) {
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).setCouponPrivi1lege(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
            }
        }

    }


    /**
     * 添加优惠劵，包括单菜的礼品劵
     */
    public boolean setCoupon(CouponPrivilegeVo mCouponPrivilegeVo, Boolean needMath, Boolean needListener) {
        //单品礼品劵
        if (mCouponPrivilegeVo.getCoupon().getCouponType() == CouponType.GIFT
                && mCouponPrivilegeVo.getShopcartItem() != null) {
            return setGiftCouponPrivilege(mCouponPrivilegeVo);
        } else {
            if (isAllowAddCoupon(dinnerShoppingCartVo, mCouponPrivilegeVo)) {
                setCouponPrivilege(mCouponPrivilegeVo, needMath, needListener);
                return true;
            }
        }
        return false;
    }

    public void removeCouponPrivilege(CouponPrivilegeVo couponPrivilegeVo, boolean isNeedListener) {
        removeCouponPrivilege(dinnerShoppingCartVo, couponPrivilegeVo, isNeedListener);
    }

    /**
     * @Title: addWeiXinCouponsPrivilege
     * @Description: 添加微信卡卷
     * @Param mWeiXinCouponsInfo TODO
     * @Return void 返回类型
     */
    public void addWeiXinCouponsPrivilege(WeiXinCouponsInfo mWeiXinCouponsInfo) {
        //移除宴请
        removeBanquetOnly(dinnerShoppingCartVo.getmTradeVo());
        addWeiXinCouponsVo(dinnerShoppingCartVo.getmTradeVo(), mWeiXinCouponsInfo);
        // 计算订单总价格
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).addWeiXinCouponsPrivilege(shopcartItemList,
                    dinnerShoppingCartVo.getmTradeVo());

        }
    }

    /**
     * @Title: removeWeiXinCouponsPrivilege
     * @Description: 移除微信卡卷
     * @Param mTradePrivilege TODO
     * @Return void 返回类型
     */
    public void removeWeiXinCouponsPrivilege(TradePrivilege mTradePrivilege) {
        removeWeiXinCouponsVo(dinnerShoppingCartVo.getmTradeVo(), mTradePrivilege);
        // 计算订单总价格
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).removeWeiXinCouponsPrivilege(shopcartItemList,
                    dinnerShoppingCartVo.getmTradeVo());

        }
    }

    /**
     * 移除所有的微信卡劵
     */
    public void removeAllWeiXinCouponsPrivilege() {
        if (dinnerShoppingCartVo.getmTradeVo().getmWeiXinCouponsVo() != null) {
            for (WeiXinCouponsVo weixinCouponsVo : dinnerShoppingCartVo.getmTradeVo().getmWeiXinCouponsVo()) {
                removeWeiXinCouponsPrivilege(weixinCouponsVo.getmTradePrivilege());
            }
        }
    }

    /**
     * 移除微信卡劵优惠
     *
     * @param promoIds
     */
    public void removeWeiXinCoupons(List<Long> promoIds) {
        if (promoIds == null || promoIds.isEmpty()) {
            return;
        }
        List<WeiXinCouponsVo> listWX = dinnerShoppingCartVo.getmTradeVo().getmWeiXinCouponsVo();
        if (listWX == null || listWX.isEmpty()) {
            return;
        }
        for (Long promoId : promoIds) {
            for (int i = listWX.size() - 1; i >= 0; i--) {
                WeiXinCouponsVo couponsVo = listWX.get(i);
                TradePrivilege tradePrivilege = couponsVo.getmTradePrivilege();
                if (tradePrivilege != null && tradePrivilege.getPromoId() != null && tradePrivilege.getPromoId().longValue() == promoId.longValue()) {
                    //保存过服务器
                    if (tradePrivilege.getId() != null) {
                        tradePrivilege.setStatusFlag(StatusFlag.INVALID);
                        tradePrivilege.setChanged(true);
                        couponsVo.setActived(false);
                    } else {
                        listWX.remove(i);
                    }
                }
            }
        }

        // 计算订单总价格
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeWeiXinCouponsPrivilege(shopcartItemList,
                    dinnerShoppingCartVo.getmTradeVo());

        }
    }

    /**
     * @Title: setIntegralCash
     * @Description: 设置积分抵现
     * @Param @param mCrmCustomerLevelRights
     * @Param @param integra TODO
     * @Param needMath 是否需要计算订单金额：默认为计算 true 计算 false 不计算
     * @Param needListener 是否需要回调：true 需要回调 false 不需要回调
     * @Return void 返回类型
     */

    public void setIntegralCash(IntegralCashPrivilegeVo mIntegralCashPrivilegeVo, Boolean needMath,
                                Boolean needListener) {

        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());
        //移除宴请
        removeBanquetOnly(dinnerShoppingCartVo.getmTradeVo());
        if (mIntegralCashPrivilegeVo == null) {

            return;

        }

        if (!mIntegralCashPrivilegeVo.hasRule() || mIntegralCashPrivilegeVo.getIntegral().compareTo(BigDecimal.ZERO) == 0 || mIntegralCashPrivilegeVo.isUsed()) {

            //设置一个临时的IntegralCashPrivilegeVo，供登录会员后进行刷新
            dinnerShoppingCartVo.getmTradeVo().setIntegralCashPrivilegeVo(mIntegralCashPrivilegeVo);
        } else {
            // 设置积分抵现优惠信息
            BuildPrivilegeTool.buildCashPrivilege(mIntegralCashPrivilegeVo, dinnerShoppingCartVo.getmTradeVo());
        }
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        // 计算订单总价格
        if (needMath) {
            MathShoppingCartTool.mathTotalPrice(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        }

        if (needListener) {
            for (int key : arrayListener.keySet()) {

                arrayListener.get(key).setIntegralCash(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());

            }
        }

    }

    /**
     * 更新积分
     */
    public void updateIntegral(boolean needListener) {
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        if (needListener) {
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).setIntegralCash(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
            }
        }
    }

    /**
     * @Title: removeIntegralCash
     * @Description: 移除会员积分抵现
     * @Param TODO
     * @Return void 返回类型
     */

    public void removeIntegralCash() {
        removeIntegralCash(true);
    }

    public void removeIntegralCash(boolean isNeedListener) {
        if (dinnerShoppingCartVo.getmTradeVo().getIntegralCashPrivilegeVo() != null) {
            TradePrivilege tradePrivilege = dinnerShoppingCartVo.getmTradeVo().getIntegralCashPrivilegeVo().getTradePrivilege();
            if (tradePrivilege != null) {
                if (tradePrivilege.getId() == null) {
                    dinnerShoppingCartVo.getmTradeVo().setIntegralCashPrivilegeVo(null);
                } else {
                    tradePrivilege.setInValid();
                }
            }
        }

        // 计算订单总价格
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());
        if (isNeedListener) {
            for (int key : arrayListener.keySet()) {

                arrayListener.get(key).removeIntegralCash(shopcartItemList,

                        dinnerShoppingCartVo.getmTradeVo());

            }
        }
    }

    /**
     * 移除押金
     */
    public void removeDeposit() {
        TradeDeposit tradeDeposit = dinnerShoppingCartVo.getmTradeVo().getTradeDeposit();
        if (tradeDeposit != null) {
            tradeDeposit.setInvalid();
            MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo), dinnerShoppingCartVo.getmTradeVo());//重新计算金额

            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).removeDeposit(dinnerShoppingCartVo.getmTradeVo());
            }


        }
    }

    /**
     * @Title: memberPrivilege
     * @Description: 设置会员折扣
     * @Param needMath 是否需要计算订单金额：默认为计算 true 计算 false 不计算
     * @Param needListener 是否需要回调：true 需要回调 false 不需要回调
     * @Return void 返回类型
     */

    public void memberPrivilege(Boolean needMath, Boolean needListener) {
        //有宴请，不能添加会员折扣
        if (isHasValidBanquet(dinnerShoppingCartVo.getmTradeVo())) {
            return;
        }

        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());
        CustomerResp mCustomer = DinnerShopManager.getInstance().getLoginCustomer();
        batchMemberPrivilege(dinnerShoppingCartVo, mCustomer, true);
        batchMemberChargePrivilege(dinnerShoppingCartVo, mCustomer);
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        // 重新计算营销活动
        MathManualMarketTool.mathMarketPlan(dinnerShoppingCartVo.getmTradeVo(), shopcartItemList, true, DinnerShopManager.getInstance().isSepartShopCart());
        // 计算订单总价格
        if (needMath) {
            MathShoppingCartTool.mathTotalPrice(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        }
        CheckGiftCouponIsActived(dinnerShoppingCartVo);
        if (needListener) {
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).batchPrivilege(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());

            }
        }

    }

    /**
     * 处理会员储值支付折扣信息
     * @param mShoppingCartVo
     * @param mCustomer
     */
    public void batchMemberChargePrivilege(ShoppingCartVo mShoppingCartVo, CustomerResp mCustomer){
        //设置整单的储值折扣
        boolean checkSwitch=ServerSettingCache.getInstance().isChargePrivilegeWhenPay();
        boolean isTure=(!checkSwitch && mCustomer.getCustomerType().equalsValue(CustomerType.MEMBER.value())) || (checkSwitch && mCustomer.getCustomerType().equalsValue(CustomerType.PAY.value()));
        if(isTure && mShoppingCartVo.getmTradeVo().getTradeChargePrivilege()==null){//是否储值支付是才加入储值打折优惠  false 会员登陆就需要加入，true 储值支付的时候才加入
            TradePrivilege chargePrivilege=BuildPrivilegeTool.buildChargePrivilege(mShoppingCartVo,mCustomer);
            if(chargePrivilege!=null){
//                setDefineTradePrivilege(chargePrivilege,null,true,true);
                List<TradePrivilege> tradePrivileges=mShoppingCartVo.getmTradeVo().getTradePrivileges();
                if(tradePrivileges==null){
                    mShoppingCartVo.getmTradeVo().setTradePrivileges(new ArrayList<TradePrivilege>());
                }
                mShoppingCartVo.getmTradeVo().getTradePrivileges().add(chargePrivilege);
            }
        }

//        if(mShoppingCartVo.getmTradeVo().getTradeChargePrivilege()==null){//是否储值支付是才加入储值打折优惠  false 会员登陆就需要加入，true 储值支付的时候才加入
//            mCustomer.storedFullAmount=BigDecimal.ZERO;
//            mCustomer.storedPrivilegeType= ChargePrivilegeType.DISCOUNT.value();
//            mCustomer.storedPrivilegeValue=new BigDecimal(8.5);
//            TradePrivilege chargePrivilege=BuildPrivilegeTool.buildChargePrivilege(mShoppingCartVo,mCustomer);
//            if(chargePrivilege!=null){
//                setDefineTradePrivilege(chargePrivilege,null,true,true);
//            }
//        }
    }

    /**
     *
     */
    public void batchMemberChargePrivilege(boolean needMath,boolean needListener){
        CustomerResp mCustomer = DinnerShopManager.getInstance().getLoginCustomer();
        batchMemberChargePrivilege(dinnerShoppingCartVo, mCustomer);

        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);

        if (needMath) {
            MathShoppingCartTool.mathTotalPrice(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        }
        CheckGiftCouponIsActived(dinnerShoppingCartVo);
        if (needListener) {
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).batchPrivilege(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());

            }
        }
    }

    /**
     * @Title: memberPrivilege
     * @Description: 设置菜品会员折扣
     * @Param @param mIShopcartItemBase TODO
     * @Return void 返回类型
     */

    public void memberPrivilege(IShopcartItemBase mIShopcartItemBase, boolean isNeedMath, boolean isNeedListener) {
        //如果有宴请不进行会员折扣操作
        if (isHasValidBanquet(dinnerShoppingCartVo.getmTradeVo())) {
            return;
        }
        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());
        setDishMemberPrivilege(dinnerShoppingCartVo, mIShopcartItemBase, DinnerShopManager.getInstance().isSepartShopCart());
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);

        // 计算订单总价格

        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());
        CheckGiftCouponIsActived(dinnerShoppingCartVo);
        if (isNeedListener) {
            for (int key : arrayListener.keySet()) {

                arrayListener.get(key).batchPrivilege(shopcartItemList,

                        dinnerShoppingCartVo.getmTradeVo());

            }
        }

    }

    /**
     * @Title: memberPrivilegeForSelected
     * @Description: 选中的菜品设置会员折扣，批量折扣被移除时调用
     * @Param @param mIShopcartItemBase TODO
     * @Return void 返回类型
     */

    public void memberPrivilegeForSelected() {

        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());

        if (dinnerShoppingCartVo.getDinnerListShopcartItem() != null) {

            for (IShopcartItemBase shopcartItem : dinnerShoppingCartVo.getDinnerListShopcartItem()) {
                setDishMemberPrivilege(dinnerShoppingCartVo, shopcartItem, DinnerShopManager.getInstance().isSepartShopCart());
            }

        }
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        // 重新计算营销活动
        MathManualMarketTool.mathMarketPlan(dinnerShoppingCartVo.getmTradeVo(), shopcartItemList, true, DinnerShopManager.getInstance().isSepartShopCart());

        // 计算订单总价格

        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());
        CheckGiftCouponIsActived(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).batchPrivilege(shopcartItemList,

                    dinnerShoppingCartVo.getmTradeVo());

        }

    }

    /**
     * 移除所有的储值优惠
     */
    private void removeChargePrivilege(){
        TradePrivilege privilege=dinnerShoppingCartVo.getmTradeVo().getTradeChargePrivilege();
        if(privilege==null){
            return;
        }

        if(privilege.getId()!=null){
            privilege.setChanged(true);
            privilege.setStatusFlag(StatusFlag.INVALID);
        }else{
            dinnerShoppingCartVo.getmTradeVo().getTradePrivileges().remove(privilege);
        }
    }

    public void removeChargePrivilege(boolean needMath,boolean needListener){
        removeChargePrivilege();
        if(!needMath){
            return;
        }
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        // 计算订单总价格

        MathShoppingCartTool.mathTotalPrice(shopcartItemList,dinnerShoppingCartVo.getmTradeVo());

        if(needListener){
            for (int key : arrayListener.keySet()) {

                arrayListener.get(key).batchPrivilege(shopcartItemList,

                        dinnerShoppingCartVo.getmTradeVo());

            }
        }
    }

    /**
     * @Title: removeMemberPrivilege
     * @Description: 移除会员折扣
     * @Param TODO
     * @Return void 返回类型
     */

    public void removeMemberPrivilege() {

        for (IShopcartItemBase item : mergeShopcartItem(dinnerShoppingCartVo)) {
            // 登出会员是，将菜品中所以得会员折扣和会员特价移除
            if (item.getPrivilege() != null && (item.getPrivilege().getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT
                    || item.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_PRICE
                    || item.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_REBATE)) {

                item.setPrivilege(null);

            }

        }
    }

    /**
     * 移除会员价带计算
     */
    public void removeAllMemberPrivileges() {
        removeMemberPrivilege();
        removeChargePrivilege();
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(shopcartItemList,
                dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).batchPrivilege(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo());

        }
    }

    /**
     * @param isMarketNeedCallback 营销活动是否需要回调
     * @Title: removeAllPrivilegeForCustomer
     * @Description: 移除会员相关的所有优惠信息
     * @Param TODO
     * @Return void 返回类型
     */

    public void removeAllPrivilegeForCustomer(boolean isNeedMathAndCallback, boolean isMarketNeedCallback) {

        // 移除会员积分抵现
        removeIntegralCash(false);
        // 移除优惠劵
        removeAllCouponPrivilege(dinnerShoppingCartVo, false);
        // 移除会员折扣
        //移除礼品劵
        removeAllGiftCoupon(false);
        removeMemberPrivilege();
        removeChargePrivilege();//移除储值优惠金额

        // 移除营销活动
        removeTradePlanActivity(isNeedMathAndCallback, isMarketNeedCallback);
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        removeAllCardServicePrivilege(shopcartItemList);
        // 计算营销活动
        MathManualMarketTool.mathMarketPlan(dinnerShoppingCartVo.getmTradeVo(), shopcartItemList, true, DinnerShopManager.getInstance().isSepartShopCart());
        if (!isNeedMathAndCallback) {
            return;
        }
        // 计算订单总价格

        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).removeCustomerPrivilege(shopcartItemList,

                    dinnerShoppingCartVo.getmTradeVo());

        }

    }

    /**
     * 移除所有的次卡服务优惠
     */
    private void removeAllCardServicePrivilege(List<IShopcartItem> shopcartItemList) {
        for (IShopcartItem iShopcartItem : shopcartItemList) {
            CardServiceTool.removeService(iShopcartItem);
        }
    }

    /**
     * @param isCustomer true表示移除会员相关的，false表示移除所有的
     */
    public void removeTradePlanActivity(boolean isCustomer, boolean isnNeedCallback) {
        List<TradePlanActivity> tradePlanActivities = dinnerShoppingCartVo.getmTradeVo().getTradePlanActivityList();
        List<TradeItemPlanActivity> tradeItemPlanActivities =
                dinnerShoppingCartVo.getmTradeVo().getTradeItemPlanActivityList();
        if (Utils.isNotEmpty(tradePlanActivities) && Utils.isNotEmpty(tradeItemPlanActivities)) {
            for (int i = 0; i < tradePlanActivities.size(); i++) {
                TradePlanActivity tradePlanActivity = tradePlanActivities.get(i);
                if (isCustomer) {
                    // 查询是不是会员的
                    MarketRuleVo marketRuleVo = MarketRuleCache.getMarketDishVoByRule(tradePlanActivity.getRuleId());
                    if (marketRuleVo != null && Utils.isNotEmpty(marketRuleVo.getUserTypes())
                            && marketRuleVo.getUserTypes().size() == 1
                            && marketRuleVo.getUserTypes().contains(UserType.MEMBER)) {
                        MathManualMarketTool.unBindTradePlanByTradePlanUuid(tradePlanActivities,
                                tradeItemPlanActivities,
                                tradePlanActivity.getUuid(),
                                true);
                    }
                } else {
                    // 挨个移除
                    MathManualMarketTool.unBindTradePlanByTradePlanUuid(tradePlanActivities,
                            tradeItemPlanActivities,
                            tradePlanActivity.getUuid(),
                            true);
                }
            }
            if (isnNeedCallback) {
                for (int key : arrayListener.keySet()) {
                    arrayListener.get(key).removeMarketActivity(dinnerShoppingCartVo.getmTradeVo());
                }
            }
        }
    }

    /**
     * @param
     * @Title: addExtraCharge
     * @Description: 添加服务费
     * @Return void 返回类型
     */

    public void addExtraCharge(List<ExtraCharge> listExtraCharge, Boolean needMath, Boolean needListener) {

        Map<Long, ExtraCharge> extraChargeMap = dinnerShoppingCartVo.getmTradeVo().getExtraChargeMap();

        if (extraChargeMap == null) {

            extraChargeMap = new HashMap<Long, ExtraCharge>();

        }

        if (listExtraCharge != null) {

            for (int i = 0; i < listExtraCharge.size(); i++) {

                ExtraCharge mExtraCharge = listExtraCharge.get(i);
                if (mExtraCharge != null) {
                    extraChargeMap.put(mExtraCharge.getId(), mExtraCharge);
                }

            }

        }
        dinnerShoppingCartVo.getmTradeVo().setExtraChargeMap(extraChargeMap);

        // 计算订单总价格
        if (needMath) {
            MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo());
        }
        CheckGiftCouponIsActived(dinnerShoppingCartVo);
        if (needListener) {
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).addExtraCharge(dinnerShoppingCartVo.getmTradeVo(),
                        dinnerShoppingCartVo.getmTradeVo().getExtraChargeMap());

            }
        }

    }

    /**
     * @param id TODO 附件费ID
     * @Title: removeExtraCharge
     * @Description: 根据附加费id移除附加费
     * @Return void 返回类型
     */

    public void removeExtraCharge(Long id) {
        if (dinnerShoppingCartVo.getmTradeVo().getExtraChargeMap() == null) {
            return;
        }
        dinnerShoppingCartVo.getmTradeVo().getExtraChargeMap().remove(id);
        removeTradePrivilege(PrivilegeType.ADDITIONAL, dinnerShoppingCartVo.getmTradeVo());

        // 计算订单总价格

        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),

                dinnerShoppingCartVo.getmTradeVo());
        CheckGiftCouponIsActived(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).removeExtraCharge(dinnerShoppingCartVo.getmTradeVo(), id);

        }

    }

    public void removeMinconsumExtra() {
        dinnerShoppingCartVo.getmTradeVo().setEnableMinConsum(false);
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),

                dinnerShoppingCartVo.getmTradeVo());
        CheckGiftCouponIsActived(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).removeExtraCharge(dinnerShoppingCartVo.getmTradeVo(), null);

        }
    }

    /**
     * 移除超时费
     *
     * @param uuid
     */
    public void removeOutTimePrivilege(String uuid) {
        removeOuttimeCharge(uuid, dinnerShoppingCartVo.getmTradeVo());

        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo), dinnerShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeCustomerPrivilege(mergeShopcartItem(dinnerShoppingCartVo), dinnerShoppingCartVo.getmTradeVo());
        }
    }


    /**
     * @Title: checkIsHaveWXC
     * @Description: 验证微信卡卷是否已存在
     * @Param code
     * @Param @return TODO true:已存在，false:未存在
     * @Return Boolean 返回类型
     */
    public Boolean checkIsHaveWXC(String code) {
        code = getNewWxCode(code);
        List<WeiXinCouponsVo> listWX = dinnerShoppingCartVo.getmTradeVo().getmWeiXinCouponsVo();
        if (listWX == null) {
            return false;
        } else {
            for (WeiXinCouponsVo wx : listWX) {
                if (wx.getmWeiXinCouponsInfo() != null && code.equals(wx.getmWeiXinCouponsInfo().getCode())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @Title: addMarketActivity
     * @Description: 添加活动规则到单据中
     * @Param @param marketDishVo 活动规则相关的vo
     * @Param @param selectedItemList 这次活动选择的商品
     * @Param @return TODO
     * @Return boolean 返回类型 true:添加生效，false：添加失败
     */
    public boolean addMarketActivity(MarketRuleVo marketDishVo, List<IShopcartItem> selectedItemList) {

        if (!MathManualMarketTool.isCanAddMarket(selectedItemList,
                dinnerShoppingCartVo.getmTradeVo(),
                marketDishVo,
                true)) {
            return false;
        }
        //移除宴请
        removeBanquetOnly(dinnerShoppingCartVo.getmTradeVo());
        //如果选择的菜品有礼品券优惠移除
        removeGiftInMarkertActivity(selectedItemList);
        MathManualMarketTool.mathManualAddMarket(selectedItemList,
                dinnerShoppingCartVo.getmTradeVo(),
                marketDishVo,
                true);

        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).addMarketActivity(dinnerShoppingCartVo.getmTradeVo());
        }
        return true;
    }

    /**
     * @Title: removeGiftInMarkertActivity
     * @Description: 删除掉已经参与营销活动商品的礼品券优惠
     * @Param @param List<IShopcartItem> selectedItemList已选择的营销活动菜品
     * @Return void 返回类型
     */
    public void removeGiftInMarkertActivity(List<IShopcartItem> selectedItemList) {
        for (IShopcartItem item : mergeShopcartItem(dinnerShoppingCartVo)) {
            for (IShopcartItem selectItem : selectedItemList) {
                if (item.getCouponPrivilegeVo() != null && item.getCouponPrivilegeVo().getTradePrivilege() != null
                        && item.getUuid().equals(selectItem.getUuid())) {
                    item.setCouponPrivilegeVo(null);
                }
            }
        }
    }

    /**
     * @Title: removeMarketActivity
     * @Description: 删除单据中的活动
     * @Param @param tradePlanUuid tradePlanActivity表的uuid
     * @Return void 返回类型
     */
    public void removeMarketActivity(String tradePlanUuid) {
        TradeVo mTradeVo = dinnerShoppingCartVo.getmTradeVo();
        MathManualMarketTool.unBindTradePlanByTradePlanUuid(mTradeVo.getTradePlanActivityList(),
                mTradeVo.getTradeItemPlanActivityList(),
                tradePlanUuid,
                true);
        CustomerResp customer = DinnerShopManager.getInstance().getLoginCustomer();
        batchMemberPrivilege(dinnerShoppingCartVo, customer, true);
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeMarketActivity(dinnerShoppingCartVo.getmTradeVo());
        }
    }

    /**
     * @Title: doDishActivityIsCheck
     * @Description: 选择活动后，组装菜品是否可选
     * @Param @param unItemList 未参加活动的菜品
     * @Param @param ruleVo TODO选择的活动
     * @Return void 返回类型
     */
    public void doDishActivityIsCheck(List<DishDataItem> unItemList, MarketRuleVo ruleVo) {
        if (unItemList == null || unItemList.isEmpty() || ruleVo == null) {
            return;
        }
        for (DishDataItem item : unItemList) {
            if (MathManualMarketTool.isItemCanJoinPlanActivity(item.getBase(), ruleVo)) {
                item.setCheckStatus(DishDataItem.DishCheckStatus.CHECKED);
            } else {
                item.setCheckStatus(DishDataItem.DishCheckStatus.INVALIATE_CHECK);
            }
        }
    }

    /**
     * 宴请
     */
    public void doBanquet(Reason reason) {
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        removePrivilegeNotBanquet(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        BanquetVo banquetVo = new BanquetVo();
        BuildPrivilegeTool.buildBanquetPrivilege(banquetVo, dinnerShoppingCartVo.getmTradeVo());
        if (reason != null) {
            setTradeFreeReasonRel(dinnerShoppingCartVo.getmTradeVo(), reason, OperateType.TRADE_BANQUET);
        } else {
            removeReason(dinnerShoppingCartVo.getmTradeVo(), OperateType.TRADE_BANQUET);
        }
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).doBanquet(dinnerShoppingCartVo.getmTradeVo());
        }
    }

    /**
     * 移除宴请外的优惠
     */
    public void removePrivilegeNotBanquet(List<IShopcartItem> shopcartItemList, TradeVo mTradeVo) {
        // 移除会员积分抵现
        removeIntegralCash();
        // 移除优惠劵
        removeAllCouponPrivilege(dinnerShoppingCartVo, false);
        // 移除会员折扣

        removeMemberPrivilege();
        removeAllItemsPrivilege();
        removeOrderPrivilege();
        removeAllWeixinCoupon(mTradeVo);

        // 移除营销活动
        MathManualMarketTool.removeAllActivity(mTradeVo);
        // 计算营销活动
        MathManualMarketTool.mathMarketPlan(mTradeVo, shopcartItemList, true, DinnerShopManager.getInstance().isSepartShopCart());
    }

    /**
     * 移除宴请
     */
    public void removeBanquet() {
        removeBanquetOnly(dinnerShoppingCartVo.getmTradeVo());
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo), dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeBanquet(dinnerShoppingCartVo.getmTradeVo());
        }
    }

    /**
     * 清空结算界面中所有选中的数据
     *
     * @Title: removeAllDinnerListItems
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */

    public void removeAllDinnerListItems() {

        dinnerShoppingCartVo.getDinnerListShopcartItem().clear();

    }

    /**
     * @Title: getPlanUsageCountById
     * @Description: 根据规则id获取在订单中营销活动使用次数
     * @Param @param ruleId
     * @Param @return TODO
     * @Return int 返回类型
     */
    public int getPlanUsageCountById(Long ruleId) {
        int count = 0;
        List<TradePlanActivity> listPlan = dinnerShoppingCartVo.getmTradeVo().getTradePlanActivityList();
        if (listPlan != null) {
            for (TradePlanActivity planActivity : listPlan) {
                if (planActivity != null && planActivity.getRuleId().compareTo(ruleId) == 0
                        && planActivity.isValid()) {
                    count = count + planActivity.getPlanUsageCount();
                }

            }
        }
        return count;
    }

    /***

     * @Title: setTradeActivity
     * @Description: 将原单活动添加到拆单购物车
     * @Param @param tradePlanList TODO
     * @Return void 返回类型
     */
    public void setTradeActivity(List<TradePlanActivity> tradePlanList, List<TradeItemPlanActivity> tradeItemPlanList) {
        TradeVo tradeVo = dinnerShoppingCartVo.getmTradeVo();
        if (tradePlanList != null) {
            tradeVo.setTradePlanActivityList(tradePlanList);
        }
        if (tradeItemPlanList != null) {
            tradeVo.setTradeItemPlanActivityList(tradeItemPlanList);
        }
    }

    /**
     * 判断订单中是否包含此类优惠类型
     *
     * @param mPrivilegeType
     * @return true:包含 false:不包含
     */
    public boolean havePrivilegeByType(PrivilegeType mPrivilegeType) {
        List<TradePrivilege> listTP = dinnerShoppingCartVo.getmTradeVo().getTradePrivileges();
        if (listTP == null) {
            return false;
        }
        for (TradePrivilege mTradePrivilege : listTP) {
            if (mTradePrivilege.getPrivilegeType() == mPrivilegeType && mTradePrivilege.isValid()) {
                return true;
            }
        }
        return false;
    }


    /**
     * @Title: setGiftCouponPrivilege
     * @Description: 礼品优惠劵
     * @Param TODO
     * @Return void 返回类型
     */

    public boolean setGiftCouponPrivilege(CouponPrivilegeVo mCouponPrivilegeVo) {

        ShopcartItem tempItem = mCouponPrivilegeVo.getShopcartItem();
        if (tempItem == null || tempItem.getSingleQty() == null) {
            return false;
        }
        boolean marketFlag = false;
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        //判断些菜是否已经参加营销活动
        List<TradeItemPlanActivity> tradeItemPlanActivityList = dinnerShoppingCartVo.getmTradeVo().getTradeItemPlanActivityList();
        if (tradeItemPlanActivityList != null && tradeItemPlanActivityList.size() > 0) {
            for (IShopcartItem item : shopcartItemList) {
                for (TradeItemPlanActivity tradeItemPlanActivity : tradeItemPlanActivityList) {
                    if (tradeItemPlanActivity.getTradeItemUuid().compareTo(item.getUuid()) == 0
                            && tempItem.getSkuUuid().compareTo(item.getSkuUuid()) == 0
                            && tradeItemPlanActivity.isValid()
                            ) {
                        marketFlag = true;
                        break;
                    }
                }

            }

        }

        IShopcartItem value = null;
        if (!marketFlag) {//如果没有参加营销活动
            //判断是否已经包含礼品券,礼品劵生效的条件是和菜品数量一致
            for (IShopcartItem item : shopcartItemList) {
                //过滤掉无效的菜品
                if (item.getStatusFlag() == StatusFlag.INVALID || item.getDishShop() == null
                        || tempItem.getDishShop() == null) {
                    continue;
                }
                if (item.getDishShop().getBrandDishId().equals(tempItem.getDishShop().getBrandDishId())
                        && item.getSingleQty().compareTo(tempItem.getSingleQty()) == 0
                        && (item.getCouponPrivilegeVo() == null || item.getCouponPrivilegeVo().getTradePrivilege() == null
                        || item.getCouponPrivilegeVo().getTradePrivilege() != null &&
                        !item.getCouponPrivilegeVo().getTradePrivilege().isValid())) {
                    value = item;
                    break;
                }
            }
        }
        if (value != null && value.getCouponPrivilegeVo() != null && value.getCouponPrivilegeVo().getTradePrivilege() != null && tempItem.getCouponPrivilegeVo() != null) {
            if (value.getCouponPrivilegeVo().getTradePrivilege().getPromoId().compareTo(
                    tempItem.getCouponPrivilegeVo().getTradePrivilege().getPromoId()) == 0) {
                return false;
            }
        } else {
            if (value == null) {
                return false;
            }
        }
        TradePrivilege mTradePrivilege = BuildPrivilegeTool.buildGiftCouponsPrivilege(value, mCouponPrivilegeVo, dinnerShoppingCartVo.getmTradeVo().getTrade().getUuid());
        mCouponPrivilegeVo.setTradePrivilege(mTradePrivilege);
        mCouponPrivilegeVo.setActived(false);

        if (value.getPrivilege() != null
                && value.getPrivilege().isCommonPrivilege()) {
            if (value.getPrivilege().getId() != null) {
                value.getPrivilege().setStatusFlag(StatusFlag.INVALID);
                value.getPrivilege().setChanged(true);
            } else {
                value.setPrivilege(null);
            }
        }
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(shopcartItemList,
                dinnerShoppingCartVo.getmTradeVo());

        CheckGiftCouponIsActived(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).setCouponPrivi1lege(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        }
        return true;
    }


    /**
     * 移除所有的礼品劵
     */
    public void removeAllGiftCoupon(boolean isNeedListener) {
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        for (IShopcartItem shopcartItem : shopcartItemList) {
            if (shopcartItem.getCouponPrivilegeVo() != null && shopcartItem.getCouponPrivilegeVo().getTradePrivilege() != null) {
                if (shopcartItem.getCouponPrivilegeVo().getTradePrivilege().getId() != null
                        && shopcartItem.getCouponPrivilegeVo().getTradePrivilege().isValid()
                        ) {
                    shopcartItem.getCouponPrivilegeVo().getTradePrivilege().setStatusFlag(StatusFlag.INVALID);
                    shopcartItem.getCouponPrivilegeVo().getTradePrivilege().setChanged(true);
                } else {
                    shopcartItem.getCouponPrivilegeVo().setTradePrivilege(null);
                }
            }
        }
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        if (isNeedListener) {
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).removeCouponPrivilege(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
            }
        }
    }

    /**
     * 移除所有无效的礼品劵
     */
    public void removeAllInValidGiftCoupon(boolean isNeedListener) {
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        for (IShopcartItem shopcartItem : shopcartItemList) {
            if (shopcartItem.getCouponPrivilegeVo() != null && shopcartItem.getCouponPrivilegeVo().getTradePrivilege() != null) {
                if (shopcartItem.getCouponPrivilegeVo().isValid() &&
                        !shopcartItem.getCouponPrivilegeVo().isActived()
                        ) {
                    if (shopcartItem.getCouponPrivilegeVo().getTradePrivilege().getId() != null) {
                        shopcartItem.getCouponPrivilegeVo().getTradePrivilege().setStatusFlag(StatusFlag.INVALID);
                        shopcartItem.getCouponPrivilegeVo().getTradePrivilege().setChanged(true);
                    } else {
                        shopcartItem.getCouponPrivilegeVo().setTradePrivilege(null);
                    }
                }
            }
        }
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        if (isNeedListener) {
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).removeCouponPrivilege(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
            }
        }
    }

    /**
     * 移除所有未激活的微信卡劵
     */
    public void removeAllInUnActiveWeixinCode(boolean isNeedListener) {
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        List<WeiXinCouponsVo> weiXinCouponsVoList = getShoppingCartVo().getmTradeVo().getmWeiXinCouponsVo();
        if (Utils.isNotEmpty(weiXinCouponsVoList)) {
            for (WeiXinCouponsVo weiXinCouponsVo : weiXinCouponsVoList) {
                if (weiXinCouponsVo.isValid() && !weiXinCouponsVo.isActived()) {
                    removeWeiXinCouponsVo(getShoppingCartVo().getmTradeVo(), weiXinCouponsVo);
                }
            }
        }
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        if (isNeedListener) {
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).removeCouponPrivilege(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
            }
        }
    }

    /**
     * 更新tradevo和shopcartItem的关系
     *
     * @return
     */
    public TradeVo createOrder() {
        TradeVo tradeVo = createOrder(dinnerShoppingCartVo, false);
        CreateTradeTool.updateTradeItemPrivilgeOfRelate(tradeVo, mergeShopcartItem(dinnerShoppingCartVo));
        setMarktingTradePrivilege(tradeVo);
        tradeVo.inventoryVo = getInventoryVo();
        return tradeVo;
    }


    /**
     * \
     * 移除shopcarteItem中的礼品劵
     */
    public void removeShopcartItemCoupon(IShopcartItemBase mShopcartItemBase) {
        //礼品劵和单品优惠不共存
        if (mShopcartItemBase.getCouponPrivilegeVo() != null && mShopcartItemBase.getCouponPrivilegeVo().getTradePrivilege() != null) {
            if (mShopcartItemBase.getCouponPrivilegeVo().getTradePrivilege().getId() != null) {
                mShopcartItemBase.getCouponPrivilegeVo().getTradePrivilege().setStatusFlag(StatusFlag.INVALID);
                mShopcartItemBase.getCouponPrivilegeVo().getTradePrivilege().setChanged(true);
            } else {
                mShopcartItemBase.setCouponPrivilegeVo(null);
            }
        }
    }

    /**
     * 更新异步开台信息
     *
     * @param trade
     * @param mTradeTable
     * @param tradeExtra
     */
    public void updateAsyncOpenTableInfo(Trade trade, TradeTable mTradeTable, TradeExtra tradeExtra, List<TradeInitConfig> tradeInitConfigs, List<TradeTax> tradeTaxs) {
        Long tradeId = trade.getId();
        String tradeUuid = trade.getUuid();
        TradeVo tradeVo = getOrder();
        if (tradeVo == null || tradeId == null || mTradeTable == null) {
            return;
        }
        //更新trade
        tradeVo.getTrade().setId(tradeId);
        tradeVo.getTrade().setServerCreateTime(trade.getServerCreateTime());
        tradeVo.getTrade().setServerUpdateTime(trade.getServerUpdateTime());
        tradeVo.getTrade().setTradeNo(trade.getTradeNo());
//        tradeVo.getTrade().setTradeTime(trade.getTradeTime());

        List<TradeTable> tradeTableList = tradeVo.getTradeTableList();
        if (Utils.isNotEmpty(tradeTableList)) {
            for (TradeTable tradeTable : tradeTableList) {
                tradeTable.setId(mTradeTable.getId());
                tradeTable.setServerCreateTime(mTradeTable.getServerCreateTime());
                tradeTable.setServerUpdateTime(mTradeTable.getServerUpdateTime());
                tradeTable.setTradeUuid(tradeUuid);
                tradeTable.setTradeId(tradeId);
            }
        }
        if (tradeVo.getTradeExtra() != null) {
            tradeVo.getTradeExtra().setTradeId(tradeId);
            tradeVo.getTradeExtra().setTradeUuid(tradeUuid);
            if (tradeExtra != null) {
                tradeVo.getTradeExtra().setId(tradeExtra.getId());
                tradeVo.getTradeExtra().setServerCreateTime(tradeExtra.getServerCreateTime());
                tradeVo.getTradeExtra().setServerUpdateTime(tradeExtra.getServerUpdateTime());
                tradeVo.getTradeExtra().setSerialNumber(tradeExtra.getSerialNumber());
            }
        }
        if (Utils.isNotEmpty(tradeVo.getTradePrivileges())) {
            for (TradePrivilege tradePrivilege : tradeVo.getTradePrivileges()) {
                tradePrivilege.setTradeId(tradeId);
                tradePrivilege.setTradeUuid(tradeUuid);
            }
        }

        if (tradeVo.getBanquetVo() != null && tradeVo.getBanquetVo().getTradePrivilege() != null) {
            tradeVo.getBanquetVo().getTradePrivilege().setTradeId(tradeId);
            tradeVo.getBanquetVo().getTradePrivilege().setTradeUuid(tradeUuid);
        }
        if (Utils.isNotEmpty(tradeVo.getCouponPrivilegeVoList())) {
            for (CouponPrivilegeVo couponPrivilegeVo : tradeVo.getCouponPrivilegeVoList()) {
                if (couponPrivilegeVo != null && couponPrivilegeVo.getTradePrivilege() != null) {
                    couponPrivilegeVo.getTradePrivilege().setTradeId(tradeId);
                    couponPrivilegeVo.getTradePrivilege().setTradeUuid(tradeUuid);
                }
            }
        }
        if (tradeVo.getIntegralCashPrivilegeVo() != null && tradeVo.getIntegralCashPrivilegeVo().getTradePrivilege() != null) {
            tradeVo.getIntegralCashPrivilegeVo().getTradePrivilege().setTradeId(tradeId);
            tradeVo.getIntegralCashPrivilegeVo().getTradePrivilege().setTradeUuid(tradeUuid);
        }

        if (Utils.isNotEmpty(tradeVo.getTradeCustomerList())) {
            for (TradeCustomer tradeCustomer : tradeVo.getTradeCustomerList()) {
                tradeCustomer.setTradeId(tradeId);
                tradeCustomer.setTradeUuid(tradeUuid);
            }
        }

        if (Utils.isNotEmpty(tradeVo.getTradeItemList())) {
            for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {
                tradeItemVo.updateAsyncTradeInfo(tradeId, tradeUuid, mTradeTable);
            }
        }

        if (Utils.isNotEmpty(tradeVo.getTradePlanActivityList())) {
            for (TradePlanActivity tradePlanActivity : tradeVo.getTradePlanActivityList()) {
                tradePlanActivity.setTradeId(tradeId);
                tradePlanActivity.setTradeUuid(tradeUuid);
            }
        }

        if (Utils.isNotEmpty(tradeVo.getTradeItemPlanActivityList())) {
            for (TradeItemPlanActivity tradeItemPlanActivity : tradeVo.getTradeItemPlanActivityList()) {
                tradeItemPlanActivity.setTradeId(tradeId);
                tradeItemPlanActivity.setTradeUuid(tradeUuid);
            }
        }

        if (Utils.isNotEmpty(tradeVo.getmWeiXinCouponsVo())) {
            for (WeiXinCouponsVo weiXinCouponsVo : tradeVo.getmWeiXinCouponsVo()) {
                if (weiXinCouponsVo.getmTradePrivilege() != null) {
                    weiXinCouponsVo.getmTradePrivilege().setTradeId(tradeId);
                    weiXinCouponsVo.getmTradePrivilege().setTradeUuid(tradeUuid);
                }
            }
        }

        if (tradeVo.getTradeDeposit() != null) {
            tradeVo.getTradeDeposit().setTradeId(tradeId);
            tradeVo.getTradeDeposit().setTradeUuid(tradeUuid);
        }

        if (Utils.isNotEmpty(tradeVo.getTradeCreditLogList())) {
            for (TradeCreditLog tradeCreditLog : tradeVo.getTradeCreditLogList()) {
                tradeCreditLog.setTradeId(tradeId);
            }
        }

        if (Utils.isNotEmpty(tradeVo.getTradeTaxs())) {
            for (TradeTax tradeTax : tradeVo.getTradeTaxs()) {
                tradeTax.setTradeId(tradeId);
            }
        }
        //异步更新付加费 v8.15 修复bug
        if (Utils.isNotEmpty(tradeInitConfigs)) {
            tradeVo.setTradeInitConfigs(tradeInitConfigs);
        }
        //异步更新消费税 v8.15 修复bug
        if (Utils.isNotEmpty(tradeTaxs)) {
            tradeVo.setTradeTaxs(tradeTaxs);
        }

        //刷新DinnertableTradeInfo
        if (dinnerShoppingCartVo != null && dinnerShoppingCartVo.getDinnertableTradeInfo() != null
                && dinnerShoppingCartVo.getDinnertableTradeInfo().getiDinnertableTrade() != null
                && dinnerShoppingCartVo.getDinnertableTradeInfo().getiDinnertableTrade().getDinnertable() != null) {
            IDinnertable dinnertable = dinnerShoppingCartVo.getDinnertableTradeInfo().getiDinnertableTrade().getDinnertable();
            TradeTableInfo tradeTableInfo = new TradeTableInfo(trade, mTradeTable,
                    DinnertableStatus.UNISSUED, null, null);
            DinnertableTradeModel tradeModel = new DinnertableTradeModel(tradeTableInfo, dinnertable);
            DinnertableTradeInfo info = DinnertableTradeInfo.create(tradeModel, tradeVo);
            dinnerShoppingCartVo.setDinnertableTradeInfo(info);
        }
    }

    /**
     * 当前订单是否支付中
     *
     * @return
     */
    public boolean isPaying() {
        if (getOrder() != null && getOrder().getTrade() != null) {
            TradePayStatus tradePayStatus = getOrder().getTrade().getTradePayStatus();
            return tradePayStatus == TradePayStatus.PAYING || tradePayStatus == TradePayStatus.PREPAID;
        }
        return false;
    }


    /**
     * 将菜单和开台数据带入购物车
     *
     * @param dishMenuVo 模板可能为null
     */
    public void addMenuToShopcart(DishMenuVo dishMenuVo, TradeVo tradeVo) {
        updateTradeVoNoTradeInfo(tradeVo);
        GroupShoppingCartTool.addMenuToShopcart(dishMenuVo, this);
    }

    /**
     * 团餐开台
     *
     * @param dishMenuVo    模板可能为null
     * @param tableList     选中的桌台
     * @param tradeCustomer 顾客信息
     * @param groupInfo     团餐信息
     */
    public void openGroupTable(DishMenuVo dishMenuVo, List<Tables> tableList, TradeCustomer tradeCustomer, TradeGroupInfo groupInfo) {
        if (dishMenuVo == null || tableList == null) {
            return;
        }
        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());
        setDinnerBusinessType(BusinessType.GROUP);
        setDinnerOrderType(DeliveryType.HERE);
        //设置餐标外壳
        MealShellVo mealHullVo = new MealShellVo();
        BigDecimal count = new BigDecimal(tableList.size());
        mealHullVo.buildHell(dishMenuVo, count);
        mealHullVo.setTradeRelate(dinnerShoppingCartVo.getmTradeVo().getTrade());
        groupInfo.setTradeId(dinnerShoppingCartVo.getmTradeVo().getTrade().getId());
        groupInfo.setTradeUuid(dinnerShoppingCartVo.getmTradeVo().getTrade().getUuid());
        getShoppingCartVo().getmTradeVo().setMealHullVo(mealHullVo);
        if (dinnerShoppingCartVo.getmTradeVo().getTradeCustomerList() == null) {
            dinnerShoppingCartVo.getmTradeVo().setTradeCustomerList(new ArrayList<TradeCustomer>());
        } else {
            dinnerShoppingCartVo.getmTradeVo().getTradeCustomerList().clear();
        }
        tradeCustomer.setTradeUuid(dinnerShoppingCartVo.getmTradeVo().getTrade().getUuid());
        dinnerShoppingCartVo.getmTradeVo().getTradeCustomerList().add(tradeCustomer);
        TradeExtra tradeExtra = null;
        if (dinnerShoppingCartVo.getmTradeVo().getTradeExtra() == null) {
            tradeExtra = new TradeExtra();
            dinnerShoppingCartVo.getmTradeVo().setTradeExtra(tradeExtra);
        } else {
            tradeExtra = dinnerShoppingCartVo.getmTradeVo().getTradeExtra();
        }
        if (tradeExtra.getUuid() == null) {
            tradeExtra.setUuid(SystemUtils.genOnlyIdentifier());
        }
        tradeExtra.validateCreate();
        tradeExtra.setTradeId(dinnerShoppingCartVo.getmTradeVo().getTrade().getId());
        tradeExtra.setTradeUuid(dinnerShoppingCartVo.getmTradeVo().getTrade().getUuid());
        List<TradeTable> tradeTables = buildTradeTables(tableList);
        dinnerShoppingCartVo.getmTradeVo().setTradeTableList(tradeTables);
        dinnerShoppingCartVo.getmTradeVo().setTradeGroup(groupInfo);
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo), dinnerShoppingCartVo.getmTradeVo());
    }

    /**
     * 设置前一次的桌台数
     */
    public void setOldDeskCount() {
        dinnerShoppingCartVo.getmTradeVo().setOldDeskCount(dinnerShoppingCartVo.getmTradeVo().getDeskCount());
    }

    /**
     * 团餐修改桌台时构建数据
     *
     * @param dishMenuVo
     * @param tablesList
     * @param groupInfo
     */
    public void modifyGroup(DishMenuVo dishMenuVo, List<Tables> tablesList, TradeCustomer tradeCustomer, TradeGroupInfo groupInfo) {
        modifyTables(tablesList);
        MealShellVo mealShellVo = dinnerShoppingCartVo.getmTradeVo().getMealShellVo();
        mealShellVo.modify(dishMenuVo, dinnerShoppingCartVo.getmTradeVo().getDeskCount());
        if (Utils.isEmpty(dinnerShoppingCartVo.getmTradeVo().getTradeCustomerList())) {
            tradeCustomer.setTradeId(dinnerShoppingCartVo.getmTradeVo().getTrade().getId());
            tradeCustomer.setTradeUuid(dinnerShoppingCartVo.getmTradeVo().getTrade().getUuid());
            dinnerShoppingCartVo.getmTradeVo().getTradeCustomerList().add(tradeCustomer);
        }
        //改tradeItem数量
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo), dinnerShoppingCartVo.getmTradeVo());
        TradeVo tradeVo = createOrder(dinnerShoppingCartVo, false);
        CreateTradeTool.updateTradeItemPrivilgeOfRelate(tradeVo, mergeShopcartItem(dinnerShoppingCartVo));
    }

    /**
     * 修改桌台
     *
     * @param tableList
     */
    public void modifyTables(List<Tables> tableList) {
        List<TradeTable> oldTables = dinnerShoppingCartVo.getmTradeVo().getTradeTableList();
        if (Utils.isEmpty(oldTables)) {
            buildTradeTables(tableList);
        }
        Map<Long, Tables> newTableMap = new HashMap<>();
        //为了不影响原来的list 外层有应用
        List<Tables> newTablesList = new ArrayList<>();
        for (Tables tables : tableList) {
            newTableMap.put(tables.getId(), tables);
            newTablesList.add(tables);
        }
        for (TradeTable tradeTable : oldTables) {
            Tables tables = newTableMap.get(tradeTable.getTableId());
            if (tables == null) {
                tradeTable.setStatusFlag(StatusFlag.INVALID);
                tradeTable.validateUpdate();
            } else {
                newTablesList.remove(tables);
            }
        }
        List<TradeTable> newTradeTableList = new ArrayList<>();
        for (Tables tables : newTablesList) {
            TradeTable newTradeTable = buildTradeTable(tables);
            newTradeTable.setTradeId(getOrder().getTrade().getId());
            newTradeTable.setTradeUuid(getOrder().getTrade().getUuid());
            newTradeTableList.add(newTradeTable);
        }
        dinnerShoppingCartVo.getmTradeVo().getTradeTableList().addAll(newTradeTableList);
    }

    /**
     * 通过桌台构建tradeTable
     *
     * @param tableList
     * @return
     */
    private List<TradeTable> buildTradeTables(List<Tables> tableList) {

        List<TradeTable> tradeTableList = new ArrayList<TradeTable>();
        int i = 0;
        for (Tables table : tableList) {
            tradeTableList.add(buildTradeTable(table));
        }
        return tradeTableList;
    }

    public TradeTable buildTradeTable(Tables table) {
        String tableName = "";
        TradeTable tradeTable = new TradeTable();
        tradeTable.setUuid(SystemUtils.genOnlyIdentifier());
        tradeTable.setTableId(table.getId());
        tradeTable.setTableName(table.getTableName());
        tradeTable.setTradeUuid(dinnerShoppingCartVo.getmTradeVo().getTrade().getUuid());
        tradeTable.validateCreate();
        tradeTable.setTablePeopleCount(table.getTablePersonCount());
        tradeTable.setWaiterId(Session.getAuthUser().getId());
        tradeTable.setWaiterName(Session.getAuthUser().getName());
        if (tableName.equals("")) {
            tableName = table.getTableName();
        } else {
            tableName = tableName + "," + table.getTableName();
        }
        tradeTable.setTableName(tableName);
        return tradeTable;
    }

    /**
     * 是否是自助餐模式下的菜品
     *
     * @param iShopcartItemBase
     * @return
     */
    public boolean isBuffetDish(IShopcartItemBase iShopcartItemBase) {
        if (getOrder() != null && getOrder().getMealShellVo() != null) {
            MealShellVo mealShellVo = getOrder().getMealShellVo();
            TradeItem tradeItem = mealShellVo.getTradeItem();
            if (tradeItem != null && TextUtils.equals(iShopcartItemBase.getRelateTradeItemUuid(), tradeItem.getUuid())) {
                return true;
            }
        }

        return false;
    }

    public void addReturnInventoryList(List<InventoryItem> returnInventoryList) {
        dinnerShoppingCartVo.putReturnInventoryMap(returnInventoryList);
    }


    public void setInventoryVoValue(TradeVo tradeVo) {
        if (dinnerShoppingCartVo.getInventoryVo() == null) {
            dinnerShoppingCartVo.setInventoryVo(new InventoryVo());
        }


    }

    public InventoryVo getInventoryVo() {
        if (dinnerShoppingCartVo.getInventoryVo() == null) {
            dinnerShoppingCartVo.setInventoryVo(new InventoryVo());
        }
        return dinnerShoppingCartVo.getInventoryVo();
    }

    //获取销售员add v8.1
    public TradeUser getTradeUser() {
        if (this.dinnerShoppingCartVo != null) {
            return dinnerShoppingCartVo.getTradeUser();
        }
        return null;
    }

    // 添加销售员add v8.1
    public void setTradeUser(TradeUser tradeUser) {
        if (this.dinnerShoppingCartVo != null) {
            this.dinnerShoppingCartVo.setTradeUser(tradeUser);
        }
    }

    /**
     * 移除tradeVo中的所有优惠
     */
    public void removeAllTradeVoPrivileges() {
        removeAllPrivilegeForCustomer(false, true);
        removeOrderTreadePrivilege(dinnerShoppingCartVo.getmTradeVo());
        removeBanquetOnly(dinnerShoppingCartVo.getmTradeVo());
        removeAllWeiXinCouponsPrivilege();
        removeAllInValidGiftCoupon(false);
        removeAllItemsPrivilege();
    }

    /**
     * 移出针对联台主单中的所有优惠
     */
    public void removeAllUnionTradeVoPrivileges() {
        removeAllPrivilegeForCustomer(false, true);
        removeOrderTreadePrivilege(dinnerShoppingCartVo.getmTradeVo());
        removeBanquetOnly(dinnerShoppingCartVo.getmTradeVo());
        removeAllExtrages();
        removeAllWeiXinCouponsPrivilege();
        removeAllInValidGiftCoupon(false);
        removeAllItemsPrivilege(false, false);
    }

    /**
     * 移除所有的附加费
     */
    private void removeAllExtrages() {
        if (dinnerShoppingCartVo.getmTradeVo().getExtraChargeMap() != null)
            dinnerShoppingCartVo.getmTradeVo().getExtraChargeMap().clear();
        removeTradePrivilege(PrivilegeType.ADDITIONAL, dinnerShoppingCartVo.getmTradeVo());
    }

    /**
     * 更新技师、用户信息等改变
     */
    public void updateUserInfo() {
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateUserInfo();
        }
    }

    /**
     * 添加次卡服务
     *
     * @param dishShop
     */
    public void addCardService(DishShop dishShop, Long serverRecordId) {
        ShopcartItem shopcartItem = CardServiceTool.createCardService(getOrder().getTrade(), dishShop, serverRecordId);
        addDishToShoppingCart(shopcartItem, false);
    }

    /**
     * 移除所有的菜品
     */
    public void removeAllDishs() {
        for (IShopcartItem shopcartItem : mergeShopcartItem(dinnerShoppingCartVo)) {
            if (shopcartItem.getId() != null) {
                removeReadonlyShopcartItem(dinnerShoppingCartVo, (ReadonlyShopcartItem) shopcartItem);
            } else {
                removeDish(dinnerShoppingCartVo, shopcartItem);
            }
        }
        resetSelectDishQTY(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).clearShoppingCart();
        }
    }

    /**
     * 移除所有的tradeUser
     */
    public void removeAllTradeUser() {
        List<TradeUser> tradeUserList = dinnerShoppingCartVo.getmTradeVo().getTradeUsers();
        if (Utils.isEmpty(tradeUserList)) {
            return;
        }
        Iterator<TradeUser> iterator = tradeUserList.iterator();
        while (iterator.hasNext()) {
            TradeUser tradeUser = (TradeUser) (iterator.next());
            if (tradeUser.getId() == null) {
                iterator.remove();
            } else {
                tradeUser.setStatusFlag(StatusFlag.INVALID);
                tradeUser.validateUpdate();
            }
        }
    }

}
