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


public class DinnerShoppingCart extends BaseShoppingCart {

    private static final String TAG = DinnerShoppingCart.class.getSimpleName();

    private static DinnerShoppingCart instance = null;

    protected ShoppingCartVo dinnerShoppingCartVo = new ShoppingCartVo();
    protected DinnertableTradeInfo mainTradeInfo;

    public DinnertableTradeInfo getCurrentTradeInfo() {
        return currentTradeInfo;
    }

    protected DinnertableTradeInfo currentTradeInfo;


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


    public void registerListener(int listenerTag, ModifyShoppingCartListener mModifyShoppingCartListener) {
        arrayListener.put(listenerTag, mModifyShoppingCartListener);
        if (AppBuildConfig.DEBUG) {
            Log.d(DinnerShoppingCart.class.getSimpleName(), "registerListener tag " + listenerTag + ", after put " + arrayListener.keySet().toString());
        }
    }


    public void unRegisterListener() {
        arrayListener.clear();
        if (AppBuildConfig.DEBUG) {
            Log.d(DinnerShoppingCart.class.getSimpleName(), "clear arrayListener, after clear " + arrayListener.keySet().toString());
        }
    }


    public void unRegisterListenerByTag(int tag) {
        arrayListener.remove(tag);
        if (AppBuildConfig.DEBUG) {
            Log.d(DinnerShoppingCart.class.getSimpleName(), "remove tag " + tag + ", after remove " + arrayListener.keySet().toString());
        }
    }


    public void setDinnerBusinessType(BusinessType mBusinessType) {
        setOrderBusinessType(dinnerShoppingCartVo, mBusinessType);
    }


    public void setDinnerOrderType(DeliveryType orderType) {
        setOrderType(dinnerShoppingCartVo, orderType);
    }


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


    public void updateTable(TradeTable mTradeTable) {
        List<TradeTable> listTradeTable = dinnerShoppingCartVo.getmTradeVo().getTradeTableList();

        if (Utils.isEmpty(listTradeTable)) {
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


    public void setDinnerCustomer(TradeCustomer mTradeCustomer) {
        if (dinnerShoppingCartVo.getArrayTradeCustomer() == null) {
            dinnerShoppingCartVo.setArrayTradeCustomer(new HashMap<Integer, TradeCustomer>());
        }

        if (mTradeCustomer == null) {
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


    private void removeAllArrayTradeCustomer() {
        dinnerShoppingCartVo.getArrayTradeCustomer().remove(CustomerType.MEMBER.value());
        dinnerShoppingCartVo.getArrayTradeCustomer().remove(CustomerType.CUSTOMER.value());
        dinnerShoppingCartVo.getArrayTradeCustomer().remove(CustomerType.CARD.value());
    }


    public void addTable(TradeTable mTradeTable) {
        if (mTradeTable == null) {
            return;
        }
        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());

        mTradeTable.setTradeUuid(dinnerShoppingCartVo.getmTradeVo().getTrade().getUuid());
        CreateTradeTableTool.openTable(dinnerShoppingCartVo.getmTradeVo(), mTradeTable);
    }


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


    public DinnertableTradeInfo getDinnertableTradeInfo() {
        return dinnerShoppingCartVo.getDinnertableTradeInfo();
    }

    public DinnertableTradeInfo getMainTradeInfo() {
        return mainTradeInfo;
    }


    public void addDishToShoppingCart(ShopcartItem mShopcartItem, Boolean isTempDish, boolean isNeedSetGroup) {
        if (mShopcartItem == null) {
            return;
        }
        if (isNeedSetGroup)
            mShopcartItem.setIsGroupDish(dinnerShoppingCartVo.isGroupMode());
        addShippingToCart(dinnerShoppingCartVo, mShopcartItem, isTempDish);
        if (!isHasValidBanquet(dinnerShoppingCartVo.getmTradeVo())) {
            setDishMemberPrivilege(dinnerShoppingCartVo, mShopcartItem, CustomerManager.getInstance().getDinnerLoginCustomer(), false);
        }
        List<IShopcartItem> allIttem = mergeShopcartItem(dinnerShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(allIttem, dinnerShoppingCartVo.getmTradeVo());
        CheckGiftCouponIsActived(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).addToShoppingCart(allIttem, dinnerShoppingCartVo.getmTradeVo(), mShopcartItem);
        }
    }


    public BigDecimal getSubTradeCount() {
        if (getOrder() == null || getOrder().getTrade() == null)
            return BigDecimal.ONE;
        if (getOrder().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
            return getOrder().getSubTradeCount();
        } else
            return BigDecimal.ONE;
    }


    public void addDishToShoppingCart(ShopcartItem mShopcartItem, Boolean isTempDish) {
        addDishToShoppingCart(mShopcartItem, isTempDish, true);
    }


    public void recoverInvalidDish(ReadonlyShopcartItemBase shopcartItemBase) {
        shopcartItemBase.recoveryDelete();

        List<IShopcartItem> allIttem = mergeShopcartItem(dinnerShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(allIttem, dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(allIttem, dinnerShoppingCartVo.getmTradeVo());
        }
    }


    public void removeDinnerTradeItem(TradeItemVo mTradeItemVo) {
        ShopcartItem mShopcartItem = removeTradeItem(dinnerShoppingCartVo, mTradeItemVo);
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeShoppingCart(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo(),
                    mShopcartItem);
        }
    }


    public void removeDinnerDish(IShopcartItem shopcartItem) {
        removeDish(dinnerShoppingCartVo, shopcartItem);
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
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());
        CheckGiftCouponIsActived(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeShoppingCart(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo(),
                    shopcartItem);
        }
    }


    public void removeDinnerShoppingcartItem(IShopcartItem mShopcartItem, SetmealShopcartItem mSetmealShopcartItem,
                                             ChangePageListener mChangePageListener, FragmentManager mFragmentManager) {

        removeShoppingcartItem(dinnerShoppingCartVo,
                mShopcartItem,
                mSetmealShopcartItem,
                mChangePageListener,
                mFragmentManager);

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
        MathManualMarketTool.removeShopcartItem(dinnerShoppingCartVo.getmTradeVo(),
                allShopcartItemList,
                mShopcartItem,
                true,
                false);

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
        MathManualMarketTool.removeShopcartItem(dinnerShoppingCartVo.getmTradeVo(),
                allShopcartItemList,
                mShopcartItem,
                true,
                false);
    }


    public void removeAppletItem(IShopcartItem shopcartItem) {
        removeDinnerShoppingcartItem(shopcartItem);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeMarketActivity(dinnerShoppingCartVo.getmTradeVo());
        }
    }


    public void returnQTY(IShopcartItem mShopcartItem) {
        OperateShoppingCart.addToReadOnlyShoppingCart(dinnerShoppingCartVo.getmTradeVo(),
                dinnerShoppingCartVo.getListIShopcatItem(),
                mShopcartItem);
        resetSelectDishQTY(dinnerShoppingCartVo);
        MathManualMarketTool.mathMarketPlan(dinnerShoppingCartVo.getmTradeVo(),
                mergeShopcartItem(dinnerShoppingCartVo),
                true,
                false);
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo());
        }
    }


    public void modifyDish(ShopcartItem mShopcartItem) {
        if (dinnerShoppingCartVo.getDinnertableTradeInfo() != null) {
            DinnertableTradeInfo info = dinnerShoppingCartVo.getDinnertableTradeInfo();
            mShopcartItem.setTradeTable(info.getTradeTableUuid(), info.getTradeTableId());
        }
        OperateShoppingCart.addToShoppingCart(dinnerShoppingCartVo.getmTradeVo(),
                dinnerShoppingCartVo.getListOrderDishshopVo(),
                mShopcartItem);
        if (!isHasValidBanquet(dinnerShoppingCartVo.getmTradeVo())) {
            setDishMemberPrivilege(dinnerShoppingCartVo, mShopcartItem, CustomerManager.getInstance().getDinnerLoginCustomer(), false);
        }
        List<IShopcartItem> allIttem = mergeShopcartItem(dinnerShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(allIttem, dinnerShoppingCartVo.getmTradeVo());
        CheckGiftCouponIsActived(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).addToShoppingCart(allIttem, dinnerShoppingCartVo.getmTradeVo(), mShopcartItem);
        }
    }


    public void updateDinnerDish(IShopcartItemBase mShopcartItemBase, Boolean isTempDish) {
        updateDish(dinnerShoppingCartVo, mShopcartItemBase, isTempDish);
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());
        resetSelectDishQTY(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo());
        }

    }


    public void updateDinnerDish(List<DishDataItem> dataItems) {
        for (DishDataItem item : dataItems) {
            if ((item.getType() == ItemType.WEST_CHILD || item.getType() == ItemType.CHILD) && item.getBase() != null)
                updateDish(dinnerShoppingCartVo, item.getBase(), false);
            else if (item.getItem() != null)
                updateDish(dinnerShoppingCartVo, item.getItem(), false);
        }

        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());
        resetSelectDishQTY(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo());
        }
    }


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

        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());
        resetSelectDishQTY(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo());
        }
    }


    public ShopcartItem getDinnerShopcartItem(String uuid) {
        return getShopcartItem(dinnerShoppingCartVo, uuid);
    }


    public IShopcartItemBase getDinnerShoppingCartItem(String uuid) {
        return getIShopcartItem(dinnerShoppingCartVo, uuid);
    }


    public IShopcartItemBase getDinnerShoppingCartItem(Long id) {
        return getIShopcartItem(dinnerShoppingCartVo, id);
    }


    public Map<String, BigDecimal> getDinnerDishSelectQTY() {
        return getDishSelectQTY(dinnerShoppingCartVo);
    }


    public DeliveryType getOrderType() {
        return dinnerShoppingCartVo.getmTradeVo().getTrade().getDeliveryType();
    }


    public TradeVo getOrder() {
        return dinnerShoppingCartVo.getmTradeVo();
    }


    public int getSelectDishCount() {
        return mergeShopcartItem(dinnerShoppingCartVo).size();
    }


    public List<IShopcartItem> getShoppingCartDish() {
        return mergeShopcartItem(dinnerShoppingCartVo);
    }

    public void setTradeVo(TradeVo mTradeVo) {
        dinnerShoppingCartVo.setmTradeVo(mTradeVo);
    }


    public void resetOrderFromTable(DinnertableTradeInfo dinnertableTradeInfo, boolean isCallback) {
        resetOrderFromTable(null, dinnertableTradeInfo, isCallback);
    }


    public void resetOrderFromTable(DinnertableTradeInfo mainInfo, DinnertableTradeInfo currentTradeInfo, boolean isCallback) {
        resetOrderFromTable(mainInfo, currentTradeInfo, false, isCallback);
    }


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

        if (getOrder().getTrade().getTradePayStatus() != TradePayStatus.PAYING) {
            MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo), currentTradeInfo.getTradeVo());
            CheckGiftCouponIsActived(dinnerShoppingCartVo);
        }
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


    public void updateTradeVoNoTradeInfo(TradeVo tradeVo, boolean isUnionMainTrade, boolean isCallback) {
        DinnertableTradeInfo dinnertableTradeInfo = new DinnertableTradeInfo(tradeVo);
        dinnerShoppingCartVo.setDinnertableTradeInfo(dinnertableTradeInfo);
        resetOrderFromTable(null, dinnertableTradeInfo, isUnionMainTrade, isCallback);
    }


    public void updateUnionMainTradeInfo(List<TradeVo> tradeVoList) {
        List<DinnertableTradeInfo> subTradeInfos = new ArrayList<>();
        mainTradeInfo = getTableTradeInfo(tradeVoList, subTradeInfos);
        resetOrderFromTable(mainTradeInfo, mainTradeInfo, true);
    }


    public void updateTradeVoNoTradeInfo(TradeVo tradeVo) {
        updateTradeVoNoTradeInfo(tradeVo, false, true);
    }

    public void updateDataFromTradeVo(TradeVo tradeVo) {
        this.updateDataFromTradeVo(tradeVo, true);
    }

    public void updateDataWithTrade(Trade trade) {
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
        if (tradeVo == null || dinnerShoppingCartVo == null || tradeVo.getTrade().getBusinessType().equalsValue(BusinessType.ONLINE_RECHARGE.value())) {
            return;
        }

        if (dinnerShoppingCartVo.getDinnertableTradeInfo() != null) {
            DinnertableTradeInfo dinnertableTradeInfo = dinnerShoppingCartVo.getDinnertableTradeInfo();
            dinnertableTradeInfo.setTradeVo(tradeVo);
            resetOrderFromTable(dinnertableTradeInfo, isCallback);
        } else {
            DinnertableTradeInfo tradeInfo = DinnertableTradeInfo.createNoTableBuffet(tradeVo);
            resetOrderFromTable(null, tradeInfo, true);
        }
    }


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
                if (iShopcartItem.isChanged())
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
            e.printStackTrace();
        }

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


        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).resetOrder(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo());
        }
    }


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


    public void updatePrintStatus(IShopcartItemBase mShopcartItemBase, IssueStatus mIssueStatus) {
        List<IShopcartItem> listDish = mergeShopcartItem(dinnerShoppingCartVo);
        Map<String, IssueStatus> uuidIssueStatusMap = new HashMap<>();
        if (Utils.isNotEmpty(listDish)) {
            for (IShopcartItem mIShopcartItem : listDish) {
                if (mIShopcartItem.getUuid().equals(mShopcartItemBase.getUuid())) {
                    boolean isIssueStatusFinished = false;
                    List<? extends ISetmealShopcartItem> listSetmeal = mIShopcartItem.getSetmealItems();
                    if (listSetmeal != null && !listSetmeal.isEmpty()) {
                        for (ISetmealShopcartItem setmeal : listSetmeal) {
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


    public void setDinnerRemarks(String remarks) {
        setRemarks(dinnerShoppingCartVo, remarks);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).setRemark(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo());
        }
    }


    public void resetSeparateDish(String uuid) {
        List<IShopcartItem> itemList = mergeShopcartItem(dinnerShoppingCartVo);
        for (IShopcartItem shopcartItem : itemList) {

            if (uuid.equals(shopcartItem.getUuid())) {
                shopcartItem.cancelSplit();
                break;
            }
        }
        MathShoppingCartTool.mathTotalPrice(itemList, dinnerShoppingCartVo.getmTradeVo());
    }


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
        return saleAmount;
    }


    public void setTradePeopleCount(int count) {
        if (dinnerShoppingCartVo.getmTradeVo() != null && dinnerShoppingCartVo.getmTradeVo().getTrade() != null) {
            dinnerShoppingCartVo.getmTradeVo().getTrade().setTradePeopleCount(count);
        }
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());
    }


    public Boolean isReturnCash() {
        TradeType mTradeType = null;
        if (dinnerShoppingCartVo.getmTradeVo() != null && dinnerShoppingCartVo.getmTradeVo().getTrade() != null) {
            mTradeType = dinnerShoppingCartVo.getmTradeVo().getTrade().getTradeType();
        }
        if (mTradeType != null && mTradeType == TradeType.SELL_FOR_REPEAT) {
            return true;
        }
        return false;

    }


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


    public TradeVo createDinnerTradeVo() {
        TradeVo mTradeVo = createOrder(dinnerShoppingCartVo, false);

        List<TradeItemVo> listTradeItem = mTradeVo.getTradeItemList();
        Map<String, TradeItemVo> tempTradeItem = new HashMap<String, TradeItemVo>();
        for (TradeItemVo mTradeItemVo : listTradeItem) {
            tempTradeItem.put(mTradeItemVo.getTradeItem().getUuid(), mTradeItemVo);
        }

        if (dinnerShoppingCartVo.getListIShopcatItem() != null) {
            for (IShopcartItem mIShopcartItem : dinnerShoppingCartVo.getListIShopcatItem()) {

                TradeItemVo mTradeItemVo = tempTradeItem.get(mIShopcartItem.getUuid());
                if (mTradeItemVo != null && mTradeItemVo.getRejectQtyReason() != null) {
                    mTradeItemVo.setRejectQtyReason(mIShopcartItem.getReturnQtyReason());
                }
                if (tempTradeItem.get(mIShopcartItem.getUuid()) == null
                        && mIShopcartItem instanceof ReadonlyShopcartItemBase) {

                    ReadonlyShopcartItemBase mReadonlyShopcartItemBase = (ReadonlyShopcartItemBase) mIShopcartItem;
                    listTradeItem.add(CreateTradeTool.buildReadOnlyTradeItemVo(mReadonlyShopcartItemBase));

                    List<? extends ISetmealShopcartItem> listSetmeal = mIShopcartItem.getSetmealItems();
                    if (listSetmeal != null) {
                        for (ISetmealShopcartItem item : listSetmeal) {
                            ReadonlyShopcartItemBase readonlySetmeal = (ReadonlyShopcartItemBase) item;
                            if (item instanceof ReadonlyShopcartItemBase) {
                                listTradeItem.add(CreateTradeTool.buildReadOnlyTradeItemVo(readonlySetmeal));
                            }

                            Collection<ReadonlyExtraShopcartItem> listExtra = mReadonlyShopcartItemBase.getExtraItems();
                            if (listExtra != null) {
                                for (ReadonlyExtraShopcartItem mReadonlyExtraShopcartItem : listExtra) {
                                    listTradeItem
                                            .add(CreateTradeTool.buildReadOnlyTradeItemVo(mReadonlyExtraShopcartItem));
                                }
                            }
                        }
                    }

                    Collection<ReadonlyExtraShopcartItem> listExtra = mReadonlyShopcartItemBase.getExtraItems();
                    if (listExtra != null) {
                        for (ReadonlyExtraShopcartItem mReadonlyExtraShopcartItem : listExtra) {
                            listTradeItem.add(CreateTradeTool.buildReadOnlyTradeItemVo(mReadonlyExtraShopcartItem));
                        }
                    }

                }
            }
        }

        setMarktingTradePrivilege(mTradeVo);

        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());

        return mTradeVo;
    }


    public void setShopcartItemPrivilege(IShopcartItemBase mIShopcartItemBase, Reason reason) {
        removeShopcartItemCoupon(mIShopcartItemBase);

        BuildPrivilegeTool.buildPrivilege(mIShopcartItemBase, dinnerShoppingCartVo.getmTradeVo().getTrade().getUuid());
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
        removeBanquetOnly(dinnerShoppingCartVo.getmTradeVo());
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

        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).batchPrivilege(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo());

        }
    }


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

        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).batchPrivilege(mergeShopcartItem(dinnerShoppingCartVo),
                    dinnerShoppingCartVo.getmTradeVo());
        }
    }


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
                    MathShoppingCartTool.mathTotalPrice(allIttem, dinnerShoppingCartVo.getmTradeVo());
                    for (int key : arrayListener.keySet()) {
                        arrayListener.get(key).updateDish(allIttem, dinnerShoppingCartVo.getmTradeVo());
                    }
                    break;
                }
            }
        }
    }


    public void modifyCustomerCount(int customerCount) {
        TradeVo tradeVo = getOrder();
        if (tradeVo != null && tradeVo.getTrade() != null && Utils.isNotEmpty(tradeVo.getTradeTableList())) {
            TradeTable tradeTable = tradeVo.getTradeTableList().get(0);
            tradeTable.setTablePeopleCount(customerCount);
            tradeTable.setChanged(true);

            tradeVo.getTrade().setTradePeopleCount(customerCount);

            List<IShopcartItem> allIttem = mergeShopcartItem(dinnerShoppingCartVo);
            MathShoppingCartTool.mathTotalPrice(allIttem, dinnerShoppingCartVo.getmTradeVo());
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).updateDish(allIttem, dinnerShoppingCartVo.getmTradeVo());
            }
        }
    }


    public void refreshDish() {
        List<IShopcartItem> allIttem = mergeShopcartItem(dinnerShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(allIttem, dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(allIttem, dinnerShoppingCartVo.getmTradeVo());
        }
    }


    public void modifyDeposit(Double value) {
        dinnerShoppingCartVo.getmTradeVo().getTradeDeposit().setChanged(true);
        dinnerShoppingCartVo.getmTradeVo().getTradeDeposit().setUnitPrice(BigDecimal.valueOf(value));
        dinnerShoppingCartVo.getmTradeVo().getTradeDeposit().setDepositPay(BigDecimal.valueOf(value));
        dinnerShoppingCartVo.getmTradeVo().getTradeDeposit().setType(3);
        List<IShopcartItem> allIttem = mergeShopcartItem(dinnerShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(allIttem, dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(allIttem, dinnerShoppingCartVo.getmTradeVo());
        }
    }


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


    public IDinnertableTrade getIDinnertableTrade() {
        DinnertableTradeInfo tradeInfo = dinnerShoppingCartVo.getDinnertableTradeInfo();
        if (tradeInfo != null) {
            return tradeInfo.getiDinnertableTrade();
        } else {
            return null;
        }


    }


    public void onlyMath() {
        List<IShopcartItem> allIttem = mergeShopcartItem(dinnerShoppingCartVo);

        MathManualMarketTool.mathMarketPlan(dinnerShoppingCartVo.getmTradeVo(),
                allIttem, true, false);
        MathShoppingCartTool.mathTotalPrice(allIttem, dinnerShoppingCartVo.getmTradeVo());

    }


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


    public void setDefineTradePrivilege(TradePrivilege tradePrivilege, Reason mReason, Boolean needMath,
                                        Boolean needListener) {

        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());
        removeBanquetOnly(dinnerShoppingCartVo.getmTradeVo());
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
        if (needMath) {
            MathShoppingCartTool.mathTotalPrice(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        }

        if (needListener) {
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).batchPrivilege(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());

            }
        }

    }


    public void removeOrderPrivilege() {
        removeOrderTreadePrivilege(dinnerShoppingCartVo.getmTradeVo());
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);

        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).batchPrivilege(shopcartItemList,

                    dinnerShoppingCartVo.getmTradeVo());

        }

    }


    public void setDishPrivilege(TradePrivilege mPrivilege, Reason reason) {
        setDishPrivilege(mPrivilege, reason, null);
    }

    public void setDishPrivilege(TradePrivilege mPrivilege, Reason reason, OperateType operateType, String uuid) {
        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());

        dinnerShoppingCartVo.setDishTradePrivilege(mPrivilege);


        for (IShopcartItemBase mShopcartItemBase : mergeShopcartItem(dinnerShoppingCartVo)) {


            if (mShopcartItemBase.getUuid().equals(uuid)) {

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


            for (IShopcartItemBase mdinnerShopcartItemBase : dinnerShoppingCartVo.getDinnerListShopcartItem()) {

                for (IShopcartItemBase mShopcartItemBase : mergeShopcartItem(dinnerShoppingCartVo)) {


                    if (mdinnerShopcartItemBase.getUuid().equals(mShopcartItemBase.getUuid())) {

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

        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).batchPrivilege(shopcartItemList,

                    dinnerShoppingCartVo.getmTradeVo());

        }

    }


    public void batchDishPrivilege(List<IShopcartItemBase> listShopcartItem) {
        batchDishPrivilege(listShopcartItem, true);
    }

    public void batchDishPrivilege(List<IShopcartItemBase> listShopcartItem, boolean isRemoveBanquet) {
        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());

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

        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).batchPrivilege(shopcartItemList,

                    dinnerShoppingCartVo.getmTradeVo());

        }

    }


    public TradePrivilege getDishPrivilege() {

        return dinnerShoppingCartVo.getDishTradePrivilege();

    }


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
        MathManualMarketTool.mathMarketPlan(dinnerShoppingCartVo.getmTradeVo(), shopcartItemList, true, DinnerShopManager.getInstance().isSepartShopCart());

        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).batchPrivilege(shopcartItemList,

                    dinnerShoppingCartVo.getmTradeVo());

        }

    }


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

        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).batchPrivilege(shopcartItemList,

                    dinnerShoppingCartVo.getmTradeVo());

        }

    }


    public void removeAllItemsPrivilege() {
        removeAllItemsPrivilege(true, true);
    }


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


        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());

        if (isNeedCallback) {
            for (int key : arrayListener.keySet()) {

                arrayListener.get(key).batchPrivilege(shopcartItemList,

                        dinnerShoppingCartVo.getmTradeVo());

            }
        }
    }


    public void setCouponPrivilege(CouponPrivilegeVo mCouponPrivilegeVo, Boolean needMath, Boolean needListener) {

        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());
        removeBanquetOnly(dinnerShoppingCartVo.getmTradeVo());
        removeOrderTreadePrivilege(dinnerShoppingCartVo.getmTradeVo());
        if (!mCouponPrivilegeVo.isUsed())
            BuildPrivilegeTool.buildCouponPrivilege(dinnerShoppingCartVo.getmTradeVo(), mCouponPrivilegeVo);
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        if (needMath) {
            MathShoppingCartTool.mathTotalPrice(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        }

        if (needListener) {
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).setCouponPrivi1lege(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
            }
        }

    }


    public boolean setCoupon(CouponPrivilegeVo mCouponPrivilegeVo, Boolean needMath, Boolean needListener) {
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


    public void addWeiXinCouponsPrivilege(WeiXinCouponsInfo mWeiXinCouponsInfo) {
        removeBanquetOnly(dinnerShoppingCartVo.getmTradeVo());
        addWeiXinCouponsVo(dinnerShoppingCartVo.getmTradeVo(), mWeiXinCouponsInfo);
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).addWeiXinCouponsPrivilege(shopcartItemList,
                    dinnerShoppingCartVo.getmTradeVo());

        }
    }


    public void removeWeiXinCouponsPrivilege(TradePrivilege mTradePrivilege) {
        removeWeiXinCouponsVo(dinnerShoppingCartVo.getmTradeVo(), mTradePrivilege);
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).removeWeiXinCouponsPrivilege(shopcartItemList,
                    dinnerShoppingCartVo.getmTradeVo());

        }
    }


    public void removeAllWeiXinCouponsPrivilege() {
        if (dinnerShoppingCartVo.getmTradeVo().getmWeiXinCouponsVo() != null) {
            for (WeiXinCouponsVo weixinCouponsVo : dinnerShoppingCartVo.getmTradeVo().getmWeiXinCouponsVo()) {
                removeWeiXinCouponsPrivilege(weixinCouponsVo.getmTradePrivilege());
            }
        }
    }


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

        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeWeiXinCouponsPrivilege(shopcartItemList,
                    dinnerShoppingCartVo.getmTradeVo());

        }
    }


    public void setIntegralCash(IntegralCashPrivilegeVo mIntegralCashPrivilegeVo, Boolean needMath,
                                Boolean needListener) {

        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());
        removeBanquetOnly(dinnerShoppingCartVo.getmTradeVo());
        if (mIntegralCashPrivilegeVo == null) {

            return;

        }

        if (!mIntegralCashPrivilegeVo.hasRule() || mIntegralCashPrivilegeVo.getIntegral().compareTo(BigDecimal.ZERO) == 0 || mIntegralCashPrivilegeVo.isUsed()) {

            dinnerShoppingCartVo.getmTradeVo().setIntegralCashPrivilegeVo(mIntegralCashPrivilegeVo);
        } else {
            BuildPrivilegeTool.buildCashPrivilege(mIntegralCashPrivilegeVo, dinnerShoppingCartVo.getmTradeVo());
        }
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        if (needMath) {
            MathShoppingCartTool.mathTotalPrice(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        }

        if (needListener) {
            for (int key : arrayListener.keySet()) {

                arrayListener.get(key).setIntegralCash(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());

            }
        }

    }


    public void updateIntegral(boolean needListener) {
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        if (needListener) {
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).setIntegralCash(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
            }
        }
    }


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


    public void removeDeposit() {
        TradeDeposit tradeDeposit = dinnerShoppingCartVo.getmTradeVo().getTradeDeposit();
        if (tradeDeposit != null) {
            tradeDeposit.setInvalid();
            MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo), dinnerShoppingCartVo.getmTradeVo());
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).removeDeposit(dinnerShoppingCartVo.getmTradeVo());
            }


        }
    }


    public void memberPrivilege(Boolean needMath, Boolean needListener) {
        if (isHasValidBanquet(dinnerShoppingCartVo.getmTradeVo())) {
            return;
        }

        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());
        CustomerResp mCustomer = DinnerShopManager.getInstance().getLoginCustomer();
        batchMemberPrivilege(dinnerShoppingCartVo, mCustomer, true);
        batchMemberChargePrivilege(dinnerShoppingCartVo, mCustomer);
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        MathManualMarketTool.mathMarketPlan(dinnerShoppingCartVo.getmTradeVo(), shopcartItemList, true, DinnerShopManager.getInstance().isSepartShopCart());
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


    public void batchMemberChargePrivilege(ShoppingCartVo mShoppingCartVo, CustomerResp mCustomer) {
        boolean checkSwitch = ServerSettingCache.getInstance().isChargePrivilegeWhenPay();
        boolean isTure = (!checkSwitch && mCustomer.getCustomerType().equalsValue(CustomerType.MEMBER.value())) || (checkSwitch && mCustomer.getCustomerType().equalsValue(CustomerType.PAY.value()));
        if (isTure && mShoppingCartVo.getmTradeVo().getTradeChargePrivilege() == null) {
            TradePrivilege chargePrivilege = BuildPrivilegeTool.buildChargePrivilege(mShoppingCartVo, mCustomer);
            if (chargePrivilege != null) {
                List<TradePrivilege> tradePrivileges = mShoppingCartVo.getmTradeVo().getTradePrivileges();
                if (tradePrivileges == null) {
                    mShoppingCartVo.getmTradeVo().setTradePrivileges(new ArrayList<TradePrivilege>());
                }
                mShoppingCartVo.getmTradeVo().getTradePrivileges().add(chargePrivilege);
            }
        }

    }


    public void batchMemberChargePrivilege(boolean needMath, boolean needListener) {
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


    public void memberPrivilege(IShopcartItemBase mIShopcartItemBase, boolean isNeedMath, boolean isNeedListener) {
        if (isHasValidBanquet(dinnerShoppingCartVo.getmTradeVo())) {
            return;
        }
        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());
        setDishMemberPrivilege(dinnerShoppingCartVo, mIShopcartItemBase, DinnerShopManager.getInstance().isSepartShopCart());
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);


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


    public void memberPrivilegeForSelected() {

        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());

        if (dinnerShoppingCartVo.getDinnerListShopcartItem() != null) {

            for (IShopcartItemBase shopcartItem : dinnerShoppingCartVo.getDinnerListShopcartItem()) {
                setDishMemberPrivilege(dinnerShoppingCartVo, shopcartItem, DinnerShopManager.getInstance().isSepartShopCart());
            }

        }
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        MathManualMarketTool.mathMarketPlan(dinnerShoppingCartVo.getmTradeVo(), shopcartItemList, true, DinnerShopManager.getInstance().isSepartShopCart());


        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());
        CheckGiftCouponIsActived(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).batchPrivilege(shopcartItemList,

                    dinnerShoppingCartVo.getmTradeVo());

        }

    }


    private void removeChargePrivilege() {
        TradePrivilege privilege = dinnerShoppingCartVo.getmTradeVo().getTradeChargePrivilege();
        if (privilege == null) {
            return;
        }

        if (privilege.getId() != null) {
            privilege.setChanged(true);
            privilege.setStatusFlag(StatusFlag.INVALID);
        } else {
            dinnerShoppingCartVo.getmTradeVo().getTradePrivileges().remove(privilege);
        }
    }

    public void removeChargePrivilege(boolean needMath, boolean needListener) {
        removeChargePrivilege();
        if (!needMath) {
            return;
        }
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);

        MathShoppingCartTool.mathTotalPrice(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());

        if (needListener) {
            for (int key : arrayListener.keySet()) {

                arrayListener.get(key).batchPrivilege(shopcartItemList,

                        dinnerShoppingCartVo.getmTradeVo());

            }
        }
    }


    public void removeMemberPrivilege() {

        for (IShopcartItemBase item : mergeShopcartItem(dinnerShoppingCartVo)) {
            if (item.getPrivilege() != null && (item.getPrivilege().getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT
                    || item.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_PRICE
                    || item.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_REBATE)) {

                item.setPrivilege(null);

            }

        }
    }


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


    public void removeAllPrivilegeForCustomer(boolean isNeedMathAndCallback, boolean isMarketNeedCallback) {

        removeIntegralCash(false);
        removeAllCouponPrivilege(dinnerShoppingCartVo, false);
        removeAllGiftCoupon(false);
        removeMemberPrivilege();
        removeChargePrivilege();
        removeTradePlanActivity(isNeedMathAndCallback, isMarketNeedCallback);
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        removeAllCardServicePrivilege(shopcartItemList);
        MathManualMarketTool.mathMarketPlan(dinnerShoppingCartVo.getmTradeVo(), shopcartItemList, true, DinnerShopManager.getInstance().isSepartShopCart());
        if (!isNeedMathAndCallback) {
            return;
        }

        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).removeCustomerPrivilege(shopcartItemList,

                    dinnerShoppingCartVo.getmTradeVo());

        }

    }


    private void removeAllCardServicePrivilege(List<IShopcartItem> shopcartItemList) {
        for (IShopcartItem iShopcartItem : shopcartItemList) {
            CardServiceTool.removeService(iShopcartItem);
        }
    }


    public void removeTradePlanActivity(boolean isCustomer, boolean isnNeedCallback) {
        List<TradePlanActivity> tradePlanActivities = dinnerShoppingCartVo.getmTradeVo().getTradePlanActivityList();
        List<TradeItemPlanActivity> tradeItemPlanActivities =
                dinnerShoppingCartVo.getmTradeVo().getTradeItemPlanActivityList();
        if (Utils.isNotEmpty(tradePlanActivities) && Utils.isNotEmpty(tradeItemPlanActivities)) {
            for (int i = 0; i < tradePlanActivities.size(); i++) {
                TradePlanActivity tradePlanActivity = tradePlanActivities.get(i);
                if (isCustomer) {
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


    public void removeExtraCharge(Long id) {
        if (dinnerShoppingCartVo.getmTradeVo().getExtraChargeMap() == null) {
            return;
        }
        dinnerShoppingCartVo.getmTradeVo().getExtraChargeMap().remove(id);
        removeTradePrivilege(PrivilegeType.ADDITIONAL, dinnerShoppingCartVo.getmTradeVo());


        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),

                dinnerShoppingCartVo.getmTradeVo());
        CheckGiftCouponIsActived(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).removeExtraCharge(dinnerShoppingCartVo.getmTradeVo(), id);

        }

    }

    public void removeMinconsumExtra() {
        dinnerShoppingCartVo.getmTradeVo().setEnableMinConsum(false);
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),

                dinnerShoppingCartVo.getmTradeVo());
        CheckGiftCouponIsActived(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).removeExtraCharge(dinnerShoppingCartVo.getmTradeVo(), null);

        }
    }


    public void removeOutTimePrivilege(String uuid) {
        removeOuttimeCharge(uuid, dinnerShoppingCartVo.getmTradeVo());

        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo), dinnerShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeCustomerPrivilege(mergeShopcartItem(dinnerShoppingCartVo), dinnerShoppingCartVo.getmTradeVo());
        }
    }


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


    public boolean addMarketActivity(MarketRuleVo marketDishVo, List<IShopcartItem> selectedItemList) {

        if (!MathManualMarketTool.isCanAddMarket(selectedItemList,
                dinnerShoppingCartVo.getmTradeVo(),
                marketDishVo,
                true)) {
            return false;
        }
        removeBanquetOnly(dinnerShoppingCartVo.getmTradeVo());
        removeGiftInMarkertActivity(selectedItemList);
        MathManualMarketTool.mathManualAddMarket(selectedItemList,
                dinnerShoppingCartVo.getmTradeVo(),
                marketDishVo,
                true);

        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).addMarketActivity(dinnerShoppingCartVo.getmTradeVo());
        }
        return true;
    }


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


    public void removeMarketActivity(String tradePlanUuid) {
        TradeVo mTradeVo = dinnerShoppingCartVo.getmTradeVo();
        MathManualMarketTool.unBindTradePlanByTradePlanUuid(mTradeVo.getTradePlanActivityList(),
                mTradeVo.getTradeItemPlanActivityList(),
                tradePlanUuid,
                true);
        CustomerResp customer = DinnerShopManager.getInstance().getLoginCustomer();
        batchMemberPrivilege(dinnerShoppingCartVo, customer, true);
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo),
                dinnerShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeMarketActivity(dinnerShoppingCartVo.getmTradeVo());
        }
    }


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


    public void removePrivilegeNotBanquet(List<IShopcartItem> shopcartItemList, TradeVo mTradeVo) {
        removeIntegralCash();
        removeAllCouponPrivilege(dinnerShoppingCartVo, false);

        removeMemberPrivilege();
        removeAllItemsPrivilege();
        removeOrderPrivilege();
        removeAllWeixinCoupon(mTradeVo);

        MathManualMarketTool.removeAllActivity(mTradeVo);
        MathManualMarketTool.mathMarketPlan(mTradeVo, shopcartItemList, true, DinnerShopManager.getInstance().isSepartShopCart());
    }


    public void removeBanquet() {
        removeBanquetOnly(dinnerShoppingCartVo.getmTradeVo());
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo), dinnerShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeBanquet(dinnerShoppingCartVo.getmTradeVo());
        }
    }


    public void removeAllDinnerListItems() {

        dinnerShoppingCartVo.getDinnerListShopcartItem().clear();

    }


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


    public void setTradeActivity(List<TradePlanActivity> tradePlanList, List<TradeItemPlanActivity> tradeItemPlanList) {
        TradeVo tradeVo = dinnerShoppingCartVo.getmTradeVo();
        if (tradePlanList != null) {
            tradeVo.setTradePlanActivityList(tradePlanList);
        }
        if (tradeItemPlanList != null) {
            tradeVo.setTradeItemPlanActivityList(tradeItemPlanList);
        }
    }


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


    public boolean setGiftCouponPrivilege(CouponPrivilegeVo mCouponPrivilegeVo) {

        ShopcartItem tempItem = mCouponPrivilegeVo.getShopcartItem();
        if (tempItem == null || tempItem.getSingleQty() == null) {
            return false;
        }
        boolean marketFlag = false;
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
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
        if (!marketFlag) {
            for (IShopcartItem item : shopcartItemList) {
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

            if (value == null) {
                addDishToShoppingCart(tempItem, true);
                shopcartItemList.add(tempItem);
                value = tempItem;
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
        MathShoppingCartTool.mathTotalPrice(shopcartItemList,
                dinnerShoppingCartVo.getmTradeVo());

        CheckGiftCouponIsActived(dinnerShoppingCartVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).setCouponPrivi1lege(shopcartItemList, dinnerShoppingCartVo.getmTradeVo());
        }
        return true;
    }


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


    public TradeVo createOrder() {
        TradeVo tradeVo = createOrder(dinnerShoppingCartVo, false);
        CreateTradeTool.updateTradeItemPrivilgeOfRelate(tradeVo, mergeShopcartItem(dinnerShoppingCartVo));
        setMarktingTradePrivilege(tradeVo);
        tradeVo.inventoryVo = getInventoryVo();
        return tradeVo;
    }


    public void removeShopcartItemCoupon(IShopcartItemBase mShopcartItemBase) {
        if (mShopcartItemBase.getCouponPrivilegeVo() != null && mShopcartItemBase.getCouponPrivilegeVo().getTradePrivilege() != null) {
            if (mShopcartItemBase.getCouponPrivilegeVo().getTradePrivilege().getId() != null) {
                mShopcartItemBase.getCouponPrivilegeVo().getTradePrivilege().setStatusFlag(StatusFlag.INVALID);
                mShopcartItemBase.getCouponPrivilegeVo().getTradePrivilege().setChanged(true);
            } else {
                mShopcartItemBase.setCouponPrivilegeVo(null);
            }
        }
    }


    public void updateAsyncOpenTableInfo(Trade trade, TradeTable mTradeTable, TradeExtra tradeExtra, List<TradeInitConfig> tradeInitConfigs, List<TradeTax> tradeTaxs) {
        Long tradeId = trade.getId();
        String tradeUuid = trade.getUuid();
        TradeVo tradeVo = getOrder();
        if (tradeVo == null || tradeId == null || mTradeTable == null) {
            return;
        }
        tradeVo.getTrade().setId(tradeId);
        tradeVo.getTrade().setServerCreateTime(trade.getServerCreateTime());
        tradeVo.getTrade().setServerUpdateTime(trade.getServerUpdateTime());
        tradeVo.getTrade().setTradeNo(trade.getTradeNo());

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
        if (Utils.isNotEmpty(tradeInitConfigs)) {
            tradeVo.setTradeInitConfigs(tradeInitConfigs);
        }
        if (Utils.isNotEmpty(tradeTaxs)) {
            tradeVo.setTradeTaxs(tradeTaxs);
        }

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


    public boolean isPaying() {
        if (getOrder() != null && getOrder().getTrade() != null) {
            TradePayStatus tradePayStatus = getOrder().getTrade().getTradePayStatus();
            return tradePayStatus == TradePayStatus.PAYING || tradePayStatus == TradePayStatus.PREPAID;
        }
        return false;
    }


    public void addMenuToShopcart(DishMenuVo dishMenuVo, TradeVo tradeVo) {
        updateTradeVoNoTradeInfo(tradeVo);
        GroupShoppingCartTool.addMenuToShopcart(dishMenuVo, this);
    }


    public void openGroupTable(DishMenuVo dishMenuVo, List<Tables> tableList, TradeCustomer tradeCustomer, TradeGroupInfo groupInfo) {
        if (dishMenuVo == null || tableList == null) {
            return;
        }
        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());
        setDinnerBusinessType(BusinessType.GROUP);
        setDinnerOrderType(DeliveryType.HERE);
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


    public void setOldDeskCount() {
        dinnerShoppingCartVo.getmTradeVo().setOldDeskCount(dinnerShoppingCartVo.getmTradeVo().getDeskCount());
    }


    public void modifyGroup(DishMenuVo dishMenuVo, List<Tables> tablesList, TradeCustomer tradeCustomer, TradeGroupInfo groupInfo) {
        modifyTables(tablesList);
        MealShellVo mealShellVo = dinnerShoppingCartVo.getmTradeVo().getMealShellVo();
        mealShellVo.modify(dishMenuVo, dinnerShoppingCartVo.getmTradeVo().getDeskCount());
        if (Utils.isEmpty(dinnerShoppingCartVo.getmTradeVo().getTradeCustomerList())) {
            tradeCustomer.setTradeId(dinnerShoppingCartVo.getmTradeVo().getTrade().getId());
            tradeCustomer.setTradeUuid(dinnerShoppingCartVo.getmTradeVo().getTrade().getUuid());
            dinnerShoppingCartVo.getmTradeVo().getTradeCustomerList().add(tradeCustomer);
        }
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(dinnerShoppingCartVo), dinnerShoppingCartVo.getmTradeVo());
        TradeVo tradeVo = createOrder(dinnerShoppingCartVo, false);
        CreateTradeTool.updateTradeItemPrivilgeOfRelate(tradeVo, mergeShopcartItem(dinnerShoppingCartVo));
    }


    public void modifyTables(List<Tables> tableList) {
        List<TradeTable> oldTables = dinnerShoppingCartVo.getmTradeVo().getTradeTableList();
        if (Utils.isEmpty(oldTables)) {
            buildTradeTables(tableList);
        }
        Map<Long, Tables> newTableMap = new HashMap<>();
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

    public TradeUser getTradeUser() {
        if (this.dinnerShoppingCartVo != null) {
            return dinnerShoppingCartVo.getTradeUser();
        }
        return null;
    }

    public void setTradeUser(TradeUser tradeUser) {
        if (this.dinnerShoppingCartVo != null) {
            this.dinnerShoppingCartVo.setTradeUser(tradeUser);
        }
    }


    public void removeAllTradeVoPrivileges() {
        removeAllPrivilegeForCustomer(false, true);
        removeOrderTreadePrivilege(dinnerShoppingCartVo.getmTradeVo());
        removeBanquetOnly(dinnerShoppingCartVo.getmTradeVo());
        removeAllWeiXinCouponsPrivilege();
        removeAllInValidGiftCoupon(false);
        removeAllItemsPrivilege();
    }


    public void removeAllUnionTradeVoPrivileges() {
        removeAllPrivilegeForCustomer(false, true);
        removeOrderTreadePrivilege(dinnerShoppingCartVo.getmTradeVo());
        removeBanquetOnly(dinnerShoppingCartVo.getmTradeVo());
        removeAllExtrages();
        removeAllWeiXinCouponsPrivilege();
        removeAllInValidGiftCoupon(false);
        removeAllItemsPrivilege(false, false);
    }


    private void removeAllExtrages() {
        if (dinnerShoppingCartVo.getmTradeVo().getExtraChargeMap() != null)
            dinnerShoppingCartVo.getmTradeVo().getExtraChargeMap().clear();
        removeTradePrivilege(PrivilegeType.ADDITIONAL, dinnerShoppingCartVo.getmTradeVo());
    }


    public void updateUserInfo() {
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateUserInfo();
        }
    }


    public void addCardService(DishShop dishShop, Long serverRecordId) {
        ShopcartItem shopcartItem = CardServiceTool.createCardService(getOrder().getTrade(), dishShop, serverRecordId);
        addDishToShoppingCart(shopcartItem, false);
    }


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
