package com.zhongmei.bty.cashier.shoppingcart;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.IntegralCashPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.MarketRuleVo;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsInfo;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsVo;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionRuleVo;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.discount.manager.ExtraManager;
import com.zhongmei.bty.basemodule.discount.utils.BuildPrivilegeTool;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryItem;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryVo;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.shoppingcart.BaseShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.OperateShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.bean.ShoppingCartVo;
import com.zhongmei.bty.basemodule.shoppingcart.listerner.ModifyShoppingCartListener;
import com.zhongmei.bty.basemodule.shoppingcart.utils.CreateTradeTool;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathManualMarketTool;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShoppingCartTool;
import com.zhongmei.bty.basemodule.trade.bean.DepositInfo;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TakeOutInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.bty.basemodule.trade.settings.IPanelItemSettings;
import com.zhongmei.bty.snack.orderdish.DishDataItem;
import com.zhongmei.util.SettingManager;
import com.zhongmei.yunfu.BuildConfig;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.CouponType;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.OperateType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.util.ToastUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShoppingCart extends BaseShoppingCart {

    private static final String TAG = ShoppingCart.class.getSimpleName();

    private static ShoppingCart instance = null;

    public ShoppingCartVo fastFootShoppingCartVo = new ShoppingCartVo();


    private TradeVo mOldTradeVo;

        private ArrayList<TradeItem> mAddTradeItems;

        private ArrayList<TradeItem> mDeleteTradeItems;

        private List<IShopcartItem> mReduceItems;

    private List<IShopcartItem> mAddChangeItems;


    private ShoppingCart() {

    }

    public static ShoppingCart getInstance() {
        synchronized (ShoppingCart.class) {
            if (instance == null) {
                instance = new ShoppingCart();

            }
        }

        return instance;
    }


    public void registerListener(int listenerTag, ModifyShoppingCartListener mModifyShoppingCartListener) {
        arrayListener.put(listenerTag, mModifyShoppingCartListener);
        if (BuildConfig.DEBUG) {
            Log.d(ShoppingCart.class.getSimpleName(), "registerListener tag " + listenerTag + ", after put " + arrayListener.keySet().toString());
        }
    }


    public void unRegisterListener() {
        arrayListener.clear();
        if (BuildConfig.DEBUG) {
            Log.d(ShoppingCart.class.getSimpleName(), "clear arrayListener, after clear " + arrayListener.keySet().toString());
        }
    }


    public void unRegisterListenerByTag(int tag) {
        arrayListener.remove(tag);
        if (BuildConfig.DEBUG) {
            Log.d(ShoppingCart.class.getSimpleName(), "remove tag " + tag + ", after remove " + arrayListener.keySet().toString());
        }
    }


    public void setCardNo(String cardNo) {
        checkNeedBuildMainOrder(fastFootShoppingCartVo.getmTradeVo());

        if (fastFootShoppingCartVo.getmTradeVo().getTradeExtra() == null) {
            fastFootShoppingCartVo.getmTradeVo().setTradeExtra(new TradeExtra());
        }
        fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setNumberPlate(cardNo);
        fastFootShoppingCartVo.getmTradeVo().setTradeTableList(null);

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).setCardNo(cardNo);
        }
    }


    public void setTable(List<Tables> mTables) {
        checkNeedBuildMainOrder(fastFootShoppingCartVo.getmTradeVo());

        fastFootShoppingCartVo.setmTables(mTables);
        if (mTables != null) {
            if (fastFootShoppingCartVo.getmTradeVo().getTradeExtra() == null) {
                fastFootShoppingCartVo.getmTradeVo().setTradeExtra(new TradeExtra());
            }
            String tableName = "";
            List<TradeTable> tradeTableList = new ArrayList<TradeTable>();
            int i = 0;
            for (Tables table : mTables) {
                TradeTable tradeTable = new TradeTable();
                tradeTable.setUuid(SystemUtils.genOnlyIdentifier());
                tradeTable.setTableId(table.getId());
                tradeTable.setTableName(table.getTableName());
                tradeTable.setTradeUuid(fastFootShoppingCartVo.getmTradeVo().getTrade().getUuid());
                tradeTable.validateCreate();
                                if (i == 0) {
                    tradeTable
                            .setTablePeopleCount(fastFootShoppingCartVo.getmTradeVo().getTrade().getTradePeopleCount());
                } else {
                    tradeTable.setTablePeopleCount(0);
                }
                i++;

                tradeTableList.add(tradeTable);

                if (tableName.equals("")) {
                    tableName = table.getTableName();
                } else {
                    tableName = tableName + "," + table.getTableName();
                }

            }

            fastFootShoppingCartVo.getmTradeVo().setTradeTableList(tradeTableList);

            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).setCardNo(tableName);
            }
        } else {
            if (fastFootShoppingCartVo.getmTradeVo().getTradeExtra() != null) {
                fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setNumberPlate(null);
            }
            fastFootShoppingCartVo.getmTradeVo().setTradeTableList(null);
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).setCardNo("");
            }
        }

    }

    public List<Tables> getTable() {
        return fastFootShoppingCartVo.getmTables();
    }


    public void addPeople(int count) {
        checkNeedBuildMainOrder(fastFootShoppingCartVo.getmTradeVo());
        if (fastFootShoppingCartVo.getmTradeVo().getTrade() != null) {
            fastFootShoppingCartVo.getmTradeVo().getTrade().setTradePeopleCount(count);
        }
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).setTradePeopleCount(count);
        }

                List<TradeTable> listTradeTable = fastFootShoppingCartVo.getmTradeVo().getTradeTableList();
        if (listTradeTable != null) {
            for (TradeTable mTradeTable : listTradeTable) {
                mTradeTable.setTablePeopleCount(count);
            }
        }
                List<IShopcartItem> allItem = mergeShopcartItem(fastFootShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(allItem, fastFootShoppingCartVo.getmTradeVo());

        CheckGiftCouponIsActived(fastFootShoppingCartVo);

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).addExtraCharge(fastFootShoppingCartVo.getmTradeVo(),
                    fastFootShoppingCartVo.getmTradeVo().getExtraChargeMap());
        }
    }


    public void addtFastFoodDishToCart(ShopcartItem mShopcartItem, Boolean isTempDish) {
        addOneDishToCart(mShopcartItem, isTempDish);
        mathDishPriceAndPrivilege(mShopcartItem);
        List<IShopcartItem> allIttem = mergeShopcartItem(fastFootShoppingCartVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).addToShoppingCart(allIttem, fastFootShoppingCartVo.getmTradeVo(), mShopcartItem);
        }
    }


    public void addBatchDishToCart(List<ShopcartItem> shopcartItems, Boolean isTempDish) {
        if (Utils.isNotEmpty(shopcartItems)) {
            for (ShopcartItem item : shopcartItems) {
                addOneDishToCart(item, isTempDish);
                mathDishPriceAndPrivilege(item);
            }
            List<IShopcartItem> allIttem = mergeShopcartItem(fastFootShoppingCartVo);
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).addToShoppingCart(allIttem, fastFootShoppingCartVo.getmTradeVo(), shopcartItems);
            }
        }
    }


    private void addOneDishToCart(ShopcartItem mShopcartItem, Boolean isTempDish) {
        if (mShopcartItem == null) {
            return;
        }

        checkNeedBuildMainOrder(fastFootShoppingCartVo.getmTradeVo());

        IShopcartItem value = getShopcartItemFromList(fastFootShoppingCartVo, mShopcartItem.getUuid());

        if (value != null && value.getPrivilege() != null
                && value.getCouponPrivilegeVo() != null
                && value.getCouponPrivilegeVo().getTradePrivilege() != null) {
            value.setCouponPrivilegeVo(null);
        }

        if (mShopcartItem.getPrivilege() != null
                && (mShopcartItem.getPrivilege().getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT
                || mShopcartItem.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_PRICE
                || mShopcartItem.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_REBATE)) {
            setDishMemberPrivilege(fastFootShoppingCartVo, mShopcartItem, false);
        } else if ((value == null || mShopcartItem.getIsChangedPrice() == Bool.NO) && mShopcartItem.getPrivilege() == null) {
            setDishMemberPrivilege(fastFootShoppingCartVo, mShopcartItem, false);
        }

        addShippingToCart(fastFootShoppingCartVo, mShopcartItem, isTempDish);
    }



    private void mathDishPriceAndPrivilege(ShopcartItem mShopcartItem) {
        List<IShopcartItem> allIttem = mergeShopcartItem(fastFootShoppingCartVo);
        autoAddSalesPromotion(fastFootShoppingCartVo);

                MathShoppingCartTool.mathTotalPrice(allIttem, fastFootShoppingCartVo.getmTradeVo());

        CheckGiftCouponIsActived(fastFootShoppingCartVo);
        if (BuildConfig.DEBUG) {
            if (arrayListener == null) {
                Log.d(ShoppingCart.class.getSimpleName(), "arrayListener size 0");
            } else {
                Log.d(ShoppingCart.class.getSimpleName(), "arrayListener size " + arrayListener.size() + " " + arrayListener.keySet().toString());
            }
        }
    }


    public void isCheckAdd(ShopcartItem mShopcartItem, Boolean isAdd) {
        if (!isAdd) {
            removeDish(fastFootShoppingCartVo, mShopcartItem);
        }
        fastFootShoppingCartVo.setTempShopItem(null);
    }


    public void setFastFoodCustomer(TradeCustomer mTradeCustomer) {
                                                                                                                                        if (mTradeCustomer == null) {
            setOpenIdenty(fastFootShoppingCartVo, "");
        }
        setCustomer(fastFootShoppingCartVo, mTradeCustomer);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).setCustomer(mTradeCustomer);
        }
    }

    public Map<Integer, TradeCustomer> getArrayTradeCustomer() {
        return fastFootShoppingCartVo.getArrayTradeCustomer();
    }


    public void setCallCustomer(TradeCustomer mTradeCustomer) {
        if (fastFootShoppingCartVo.getArrayTradeCustomer() == null) {
            fastFootShoppingCartVo.setArrayTradeCustomer(new HashMap<Integer, TradeCustomer>());
        }

        fastFootShoppingCartVo.getArrayTradeCustomer().put(CustomerType.BOOKING.value(), mTradeCustomer);
    }


    public void setAddress(TakeOutInfo entity) {
        fastFootShoppingCartVo.setmTakeOutInfo(entity);

        checkNeedBuildMainOrder(fastFootShoppingCartVo.getmTradeVo());

        if (entity != null) {
            if (fastFootShoppingCartVo.getmTradeVo().getTradeExtra() == null) {
                fastFootShoppingCartVo.getmTradeVo().setTradeExtra(new TradeExtra());
            }
                        fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setInvoiceTitle(entity.getInvoiceTitle());
                        fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setExpectTime(entity.getExpectTime());

                        fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setReceiverPhone(entity.getReceiverTel());
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setNation(entity.getNation());
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setCountry(entity.getCountry());
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setNationalTelCode(entity.getNationalTelCode());
                        fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setReceiverName(entity.getReceiverName());
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setDeliveryAddressId(entity.getDeliveryAddressID());
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setDeliveryAddress(entity.getDeliveryAddress());
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setReceiverSex(entity.getReceiverSex());

        } else {
            if (fastFootShoppingCartVo.getmTradeVo().getTradeExtra() == null) {
                return;
            }
                        fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setInvoiceTitle("");
                        fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setExpectTime(null);
                        fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setReceiverPhone("");
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setNation("");
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setCountry("");
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setNationalTelCode("");
                        fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setReceiverName("");
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setDeliveryAddressId(null);
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setDeliveryAddress("");
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setReceiverSex(null);
        }

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).setOrderUserMessage(entity);
        }

    }


    public void setCouponPrivilege(CouponPrivilegeVo mCouponPrivilegeVo) {
                                        checkNeedBuildMainOrder(fastFootShoppingCartVo.getmTradeVo());
                if (mCouponPrivilegeVo.getCoupon().getCouponType() == CouponType.GIFT
                && mCouponPrivilegeVo.getShopcartItem() != null) {

            setGiftCouponPrivilege(mCouponPrivilegeVo);

        } else {
            removeOrderTreadePrivilege(fastFootShoppingCartVo.getmTradeVo());
            BuildPrivilegeTool.buildCouponPrivilege(fastFootShoppingCartVo.getmTradeVo(), mCouponPrivilegeVo);
                        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
            arrayListener.get(key).setCouponPrivi1lege(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }


    }




    private void setGiftCouponPrivilege(CouponPrivilegeVo mCouponPrivilegeVo) {

        ShopcartItem tempItem = mCouponPrivilegeVo.getShopcartItem();

        boolean marketFlag = false;

                List<TradeItemPlanActivity> tradeItemPlanActivityList = fastFootShoppingCartVo.getmTradeVo().getTradeItemPlanActivityList();
        if (tradeItemPlanActivityList != null && tradeItemPlanActivityList.size() > 0) {
            for (IShopcartItem item : mergeShopcartItem(fastFootShoppingCartVo)) {
                for (TradeItemPlanActivity tradeItemPlanActivity : tradeItemPlanActivityList) {
                    if (tradeItemPlanActivity.getTradeItemUuid().compareTo(item.getUuid()) == 0
                            && tempItem.getSkuUuid().compareTo(item.getSkuUuid()) == 0) {
                        marketFlag = true;
                        break;
                    }
                }
            }

        }

        IShopcartItem value = null;
        if (!marketFlag) {                        for (IShopcartItem item : mergeShopcartItem(fastFootShoppingCartVo)) {
                if (item.getStatusFlag() == StatusFlag.VALID
                        && item.getDishShop().getBrandDishId().equals(tempItem.getDishShop().getBrandDishId())
                        && item.getSingleQty().compareTo(tempItem.getSingleQty()) == 0 && item.getCouponPrivilegeVo() == null) {
                    value = item;
                    break;
                }
            }
        }
        if (value != null && value.getCouponPrivilegeVo() != null && value.getCouponPrivilegeVo().getTradePrivilege() != null) {
            if (value.getCouponPrivilegeVo().getTradePrivilege().getPromoId().compareTo(
                    tempItem.getCouponPrivilegeVo().getTradePrivilege().getPromoId()) == 0) {
                return;
            }
        } else {
            if (value == null) {
                value = tempItem;
            }
        }
        value.setCouponPrivilegeVo(mCouponPrivilegeVo);
        TradePrivilege mTradePrivilege = BuildPrivilegeTool.buildGiftCouponsPrivilege(value, fastFootShoppingCartVo.getmTradeVo().getTrade().getUuid());

        mCouponPrivilegeVo.setTradePrivilege(mTradePrivilege);
        mCouponPrivilegeVo.setActived(false);

        if (value.getPrivilege() != null
                && (value.getPrivilege().getPrivilegeType() == PrivilegeType.DISCOUNT
                || value.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_PRICE
                || value.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_REBATE
                || value.getPrivilege().getPrivilegeType() == PrivilegeType.REBATE
                || value.getPrivilege().getPrivilegeType() == PrivilegeType.FREE
                || value.getPrivilege().getPrivilegeType() == PrivilegeType.GIVE
                || value.getPrivilege().getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT)) {
            value.setPrivilege(null);
        }
        if (value instanceof ShopcartItem) {
            addtFastFoodDishToCart((ShopcartItem) value, false);
        } else {
                        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());

            CheckGiftCouponIsActived(fastFootShoppingCartVo);

            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).updateDish(mergeShopcartItem(fastFootShoppingCartVo),
                        fastFootShoppingCartVo.getmTradeVo());
            }
        }
    }



    public void removeCouponPrivilege() {
                removeAllCouponPrivilege(fastFootShoppingCartVo, true);
    }


    public Boolean checkIsHaveWXC(String code) {
        code = getNewWxCode(code);
        List<WeiXinCouponsVo> listWX = fastFootShoppingCartVo.getmTradeVo().getmWeiXinCouponsVo();
        if (listWX == null) {
            return false;
        } else {
            for (WeiXinCouponsVo wx : listWX) {
                if (String.valueOf(wx.getmTradePrivilege().getPromoId()).equals(code)) {
                    return true;
                }
            }
        }
        return false;
    }



    public void addWeiXinCouponsPrivilege(WeiXinCouponsInfo mWeiXinCouponsInfo) {
        addWeiXinCouponsVo(fastFootShoppingCartVo.getmTradeVo(), mWeiXinCouponsInfo);
                List<IShopcartItem> shopcartItemList = mergeShopcartItem(fastFootShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, fastFootShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).addWeiXinCouponsPrivilege(shopcartItemList, fastFootShoppingCartVo.getmTradeVo());

        }
    }


    public void removeWeiXinCouponsPrivilege(List<TradePrivilege> tradePrivileges) {
        for (TradePrivilege tradePrivilege : tradePrivileges) {
            removeWeiXinCouponsVo(fastFootShoppingCartVo.getmTradeVo(), tradePrivilege);
        }
                List<IShopcartItem> shopcartItemList = mergeShopcartItem(fastFootShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, fastFootShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).removeWeiXinCouponsPrivilege(shopcartItemList, fastFootShoppingCartVo.getmTradeVo());

        }
    }


    public void removeAllWXC() {
        fastFootShoppingCartVo.getmTradeVo().getmWeiXinCouponsVo().clear();
                List<IShopcartItem> shopcartItemList = mergeShopcartItem(fastFootShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, fastFootShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).removeWeiXinCouponsPrivilege(shopcartItemList, fastFootShoppingCartVo.getmTradeVo());

        }
    }


    public void setOrderPrivilege(TradePrivilege tradePrivilege, Reason mReason) {
        checkNeedBuildMainOrder(fastFootShoppingCartVo.getmTradeVo());

                removeAllCouponPrivilege(fastFootShoppingCartVo, true);

        if (tradePrivilege != null) {
            tradePrivilege.setUuid(SystemUtils.genOnlyIdentifier());
            tradePrivilege.validateCreate();
            tradePrivilege.setCreatorId(Session.getAuthUser().getId());
            tradePrivilege.setCreatorName(Session.getAuthUser().getName());
            tradePrivilege.setTradeUuid(fastFootShoppingCartVo.getmTradeVo().getTrade().getUuid());
        }

        fastFootShoppingCartVo.getmTradeVo().replaceTradePrivilege(tradePrivilege);
                if (tradePrivilege.getPrivilegeType() == PrivilegeType.FREE && mReason != null) {
            setTradeFreeReasonRel(fastFootShoppingCartVo.getmTradeVo(), mReason, OperateType.TRADE_FASTFOOD_FREE);
        } else {
            removeFreeReason(fastFootShoppingCartVo.getmTradeVo());
        }


                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).orderDiscount(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }

    }




    public void removeOrderPrivilege() {
                                        removeAllCouponPrivilege(fastFootShoppingCartVo, false);
        removeOrderTreadePrivilege(fastFootShoppingCartVo.getmTradeVo());
                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeDiscount(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo(),
                    null);
        }
    }



    public void removeOrderTradePrivilege() {
        removeOrderTreadePrivilege(fastFootShoppingCartVo.getmTradeVo());

                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeDiscount(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo(),
                    null);
        }
    }


    public void batchPrivilege(List<IShopcartItemBase> listShopcartItem) {

        for (IShopcartItemBase item : listShopcartItem) {
            for (IShopcartItem sitem : mergeShopcartItem(fastFootShoppingCartVo)) {
                if (item.getUuid().equals(sitem.getUuid())) {
                    TradePrivilege mTradePrivilege = BuildPrivilegeTool.buildPrivilege(item,
                            fastFootShoppingCartVo.getmTradeVo().getTrade().getUuid());
                    if (sitem.getCouponPrivilegeVo() != null && sitem.getCouponPrivilegeVo().getTradePrivilege() != null) {
                        sitem.setCouponPrivilegeVo(null);
                    }
                    sitem.setPrivilege(mTradePrivilege);
                    break;
                }
            }
        }

                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).batchPrivilege(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }
    }


    public void removeDishPrivilege(IShopcartItemBase mShopcartItem) {
        if (mShopcartItem.getPrivilege() != null && (mShopcartItem.getPrivilege().getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT
                || mShopcartItem.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_PRICE
                || mShopcartItem.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_REBATE)) {
            for (IShopcartItem item : mergeShopcartItem(fastFootShoppingCartVo)) {
                if (item.getUuid().equals(mShopcartItem.getUuid())) {
                    item.setPrivilege(null);
                    break;
                }
            }
        } else {
            if (mShopcartItem.getCouponPrivilegeVo() != null && mShopcartItem.getCouponPrivilegeVo().getTradePrivilege() != null) {
                removeGiftCouponePrivilege(mShopcartItem.getCouponPrivilegeVo().getTradePrivilege().getPromoId(), fastFootShoppingCartVo, false);
            } else {
                for (IShopcartItem item : mergeShopcartItem(fastFootShoppingCartVo)) {
                    if (item.getUuid().equals(mShopcartItem.getUuid())) {
                        item.setPrivilege(null);
                        break;
                    }
                }

                setDishMemberPrivilege(fastFootShoppingCartVo, mShopcartItem, false);
            }

        }

        autoAddSalesPromotion(fastFootShoppingCartVo);

                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeDiscount(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo(),
                    null);
        }
    }


    public void memberPrivilege() {
        batchMemberPrivilege(fastFootShoppingCartVo);

                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).batchPrivilege(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }
    }

    public boolean isHaveMemberPrivilege(Boolean isDinner) {
        for (IShopcartItemBase item : mergeShopcartItem(getShoppingCartVo())) {
            if (!isDinner) {
                if (item.getPrivilege() != null && (item.getPrivilege().getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT || item.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_PRICE || item.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_REBATE)) {
                    return true;
                }
            }
        }

        return false;

    }


    public void removeMemberPrivilege() {
        for (IShopcartItemBase item : mergeShopcartItem(fastFootShoppingCartVo)) {
            if (item.getPrivilege() != null && (item.getPrivilege().getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT
                    || item.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_PRICE
                    || item.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_REBATE)) {
                item.setPrivilege(null);
                item.setPrivilege(null);
            }
        }
        autoAddSalesPromotion(fastFootShoppingCartVo);

                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).batchPrivilege(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }
    }


    public void setIntegralCash(IntegralCashPrivilegeVo mIntegralCashPrivilegeVo) {

        if (mIntegralCashPrivilegeVo == null || !mIntegralCashPrivilegeVo.hasRule()
                || mIntegralCashPrivilegeVo.getIntegral().compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
                BuildPrivilegeTool.buildCashPrivilege(mIntegralCashPrivilegeVo, fastFootShoppingCartVo.getmTradeVo());
                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).setIntegralCash(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
            arrayListener.get(key).updateDish(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }
    }


    public void removeIntegralCash() {
        fastFootShoppingCartVo.getmTradeVo().setIntegralCashPrivilegeVo(null);
                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeIntegralCash(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
            arrayListener.get(key).updateDish(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }
    }


    public void removeAllPrivilegeForCustomer() {
                fastFootShoppingCartVo.getmTradeVo().setIntegralCashPrivilegeVo(null);
                removeAllCouponPrivilege(fastFootShoppingCartVo, false);
                removeAllGiftPrivilege();

                removeMemberPrivilege();

                List<TradeCustomer> listCustomer = fastFootShoppingCartVo.getmTradeVo().getTradeCustomerList();
        if (listCustomer != null) {
            for (int i = listCustomer.size() - 1; i >= 0; i--) {
                TradeCustomer customer = listCustomer.get(i);
                if (customer.getCustomerType() == CustomerType.MEMBER) {
                    listCustomer.remove(i);

                    for (int key : arrayListener.keySet()) {
                        arrayListener.get(key).setCustomer(null);
                    }
                    break;
                }
            }
        }
                if (fastFootShoppingCartVo.getArrayTradeCustomer() != null) {
            fastFootShoppingCartVo.getArrayTradeCustomer().remove(CustomerType.MEMBER.value());
            fastFootShoppingCartVo.getArrayTradeCustomer().remove(CustomerType.CARD.value());
        }
    }


    public void removeAllGiftPrivilege() {
        for (IShopcartItem item : mergeShopcartItem(fastFootShoppingCartVo)) {
            if (item.getCouponPrivilegeVo() != null && item.getCouponPrivilegeVo().getTradePrivilege() != null) {
                item.setCouponPrivilegeVo(null);
            }
        }

                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }
    }



    public void addExtraCharge(List<ExtraCharge> listExtraCharge) {

        Map<Long, ExtraCharge> extraChargeMap = fastFootShoppingCartVo.getmTradeVo().getExtraChargeMap();

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
        fastFootShoppingCartVo.getmTradeVo().setExtraChargeMap(extraChargeMap);

        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),

                fastFootShoppingCartVo.getmTradeVo());

        CheckGiftCouponIsActived(fastFootShoppingCartVo);

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).addExtraCharge(fastFootShoppingCartVo.getmTradeVo(),

                    fastFootShoppingCartVo.getmTradeVo().getExtraChargeMap());

        }

    }



    public void removeExtraCharge(Long id) {

        fastFootShoppingCartVo.getmTradeVo().getExtraChargeMap().remove(id);

        removeTradePrivilege(PrivilegeType.ADDITIONAL, fastFootShoppingCartVo.getmTradeVo());

        if (isHereOrTake()) {            Map<Long, ExtraCharge> extraChargeMap = fastFootShoppingCartVo.getmTradeVo().getExtraChargeMap();
            boolean hasChf = false;
            if (extraChargeMap != null) {
                for (Long key : extraChargeMap.keySet()) {
                    ExtraCharge mExtraCharge = extraChargeMap.get(key);
                    if (mExtraCharge.getCode() != null && mExtraCharge.getCode().equals(ExtraManager.mealFee)) {
                        hasChf = true;
                        break;
                    }
                }
                if (!hasChf) {
                    for (IShopcartItem item : mergeShopcartItem(fastFootShoppingCartVo)) {
                        item.setPack(false);
                    }
                }

            }
        }


        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),

                fastFootShoppingCartVo.getmTradeVo());

        CheckGiftCouponIsActived(fastFootShoppingCartVo);

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).removeExtraCharge(fastFootShoppingCartVo.getmTradeVo(), id);

        }

    }


    public void setFastFoodRemarks(String remarks) {
        setRemarks(fastFootShoppingCartVo, remarks);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).setRemark(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }
    }


    public void removeFastFoodRemark(ShopcartItem mShopcartItem) {
        OperateShoppingCart.removeDishRemark(fastFootShoppingCartVo.getmTradeVo(),
                fastFootShoppingCartVo.getListOrderDishshopVo(),
                mShopcartItem);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeRemark(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo(),
                    mShopcartItem);
        }
    }


    public void removeFastFoodSetmealRemark(SetmealShopcartItem mSetmeal) {
        removeSetmealRemark(fastFootShoppingCartVo, mSetmeal);

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeSetmealRemark(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo(),
                    mSetmeal);
        }
    }


    public void removeRemark() {
        fastFootShoppingCartVo.getmTradeVo().getTrade().setTradeMemo("");
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeRemark(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo(),
                    null);
        }
    }


    public TradeVo createFastFoodOrder(Boolean isGuaDan) {

        TradeVo tradeVo = createOrder(fastFootShoppingCartVo, isGuaDan);
        CreateTradeTool.updateTradeItemPrivilgeOfRelate(tradeVo, mergeShopcartItem(fastFootShoppingCartVo));

        if (isReturnCash() && mOldTradeVo != null) {
            buildReturnCashTradeVo(tradeVo);
        }

                        if (!isGuaDan) {
            setMarktingTradePrivilege(tradeVo);
            TradeStatus tradeStatus = tradeVo.getTrade().getTradeStatus();
            if (tradeStatus == TradeStatus.TEMPORARY) {
                tradeVo.getTrade().setTradeStatus(TradeStatus.CONFIRMED);
            }
        }
                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());
        if (isReturnCash()) {             tradeVo.inventoryVo = getInventoryVo(tradeVo);
        }
        return tradeVo;
    }


    private void setTradeLog() {
        try {
            int validItem = 0;
            int unValidItem = 0;
            TradeVo tradeVo = fastFootShoppingCartVo.getmTradeVo();
            BigDecimal validDishAmount = BigDecimal.ZERO;
            BigDecimal unValidDishAmount = BigDecimal.ZERO;
            for (IShopcartItem item : mergeShopcartItem(fastFootShoppingCartVo)) {
                if (item.getStatusFlag() == StatusFlag.VALID) {
                    validItem++;
                    validDishAmount = validDishAmount.add(item.getActualAmount());
                } else {
                    unValidItem++;
                    unValidDishAmount = unValidDishAmount.add(item.getActualAmount());
                }
            }

            List<TradePrivilege> tradePrivileges = tradeVo.getTradePrivileges();
            StringBuffer sb = new StringBuffer();
            if (Utils.isNotEmpty(tradePrivileges)) {
                for (TradePrivilege tradePrivilege : tradePrivileges) {
                    if (tradePrivilege.getStatusFlag() == StatusFlag.VALID) {
                        sb = sb.append("优惠类型：" + tradePrivilege.getPrivilegeType() + "优惠金额:" + tradePrivilege.getPrivilegeAmount() + "\n");
                    }
                }
            }
            List<TradePlanActivity> planActivity = tradeVo.getTradePlanActivityList();
            if (Utils.isNotEmpty(planActivity)) {
                for (TradePlanActivity tradePlanActivity : planActivity) {
                    if (tradePlanActivity.getStatusFlag() == StatusFlag.VALID) {
                        sb = sb.append("营销活动优惠金额:" + tradePlanActivity.getOfferValue());
                    }
                }

            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void setAfterTradeLog(TradeVo tradeVo) {
        try {
            List<IShopcartItem> items = mergeShopcartItem(fastFootShoppingCartVo);
                    } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }



    private void buildReturnCashTradeVo(TradeVo tradeVo) {
        tradeVo.getTrade().setChanged(true);

        if (tradeVo.getTradeExtra() != null) {
            tradeVo.getTradeExtra().setChanged(true);
        }

        Map<String, TradeItemVo> mNewTradeItemVos = new HashMap<String, TradeItemVo>();

        for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {
            TradeItem tradeItem = tradeItemVo.getTradeItem();
            mNewTradeItemVos.put(tradeItem.getUuid(), tradeItemVo);
        }

        mAddTradeItems = new ArrayList<TradeItem>();
        mDeleteTradeItems = new ArrayList<TradeItem>();
        List<TradeItemVo> deleteTradeItemVo = new ArrayList<TradeItemVo>();

        for (TradeItemVo ordTradeItemVo : mOldTradeVo.getTradeItemList()) {
            TradeItem tradeItem = ordTradeItemVo.getTradeItem();
            if (mNewTradeItemVos.containsKey(tradeItem.getUuid())) {
                TradeItemVo newTradeItemVo = mNewTradeItemVos.get(tradeItem.getUuid());
                                if (newTradeItemVo.getCouponPrivilegeVo() == null && ordTradeItemVo.getCouponPrivilegeVo() != null) {
                    ordTradeItemVo.getCouponPrivilegeVo().getTradePrivilege().setStatusFlag(StatusFlag.INVALID);
                    ordTradeItemVo.getCouponPrivilegeVo().getTradePrivilege().setChanged(true);
                    newTradeItemVo.setCouponPrivilegeVo(ordTradeItemVo.getCouponPrivilegeVo());
                }

                                if (newTradeItemVo.getTradeItemPrivilege() == null && ordTradeItemVo.getTradeItemPrivilege() != null) {
                    TradePrivilege tradePrivilege = CreateTradeTool.cloneTradePrivilege(ordTradeItemVo.getTradeItemPrivilege());
                    tradePrivilege.setStatusFlag(StatusFlag.INVALID);
                    tradePrivilege.setChanged(true);
                    newTradeItemVo.setTradeItemPrivilege(tradePrivilege);
                }
            } else {

            }

            if (!mNewTradeItemVos.containsKey(tradeItem.getUuid())
                    || (mNewTradeItemVos.containsKey(tradeItem.getUuid())
                    && mNewTradeItemVos.get(tradeItem.getUuid()).getTradeItem().getStatusFlag() == StatusFlag.INVALID)) {
                                mDeleteTradeItems.add(ordTradeItemVo.getTradeItem());
                if (ordTradeItemVo.getTradeItem().getId() == null) {
                    tradeItem.setStatusFlag(StatusFlag.INVALID);
                    tradeItem.setChanged(true);

                    if (ordTradeItemVo.getTradeItemPrivilege() != null) {
                        ordTradeItemVo.getTradeItemPrivilege().setStatusFlag(StatusFlag.INVALID);
                        ordTradeItemVo.getTradeItemPrivilege().setChanged(true);
                    }

                    if (ordTradeItemVo.getCouponPrivilegeVo() != null) {
                        ordTradeItemVo.getCouponPrivilegeVo().getTradePrivilege().setStatusFlag(StatusFlag.INVALID);
                        ordTradeItemVo.getCouponPrivilegeVo().getTradePrivilege().setChanged(true);
                    }

                    deleteTradeItemVo.add(ordTradeItemVo);
                }
            }

        }

        Map<String, TradeItemVo> oldMap = list2Map(mOldTradeVo.getTradeItemList());
        Map<String, TradeItemVo> delMap = list2Map(deleteTradeItemVo);

        List<TradeItemVo> newTradeItemList = tradeVo.getTradeItemList();

        for (TradeItemVo newTradeItemVo : newTradeItemList) {
            TradeItem newTradeItem = newTradeItemVo.getTradeItem();
            String uuid = newTradeItem.getUuid();
            if (!oldMap.containsKey(uuid) && !delMap.containsKey(uuid)) {
                mAddTradeItems.add(newTradeItem);
            }
        }



        tradeVo.getTradeItemList().addAll(deleteTradeItemVo);

                if (mOldTradeVo.getTradeItemList() != null) {
            for (TradeItemVo tradeItemVo : mOldTradeVo.getTradeItemList()) {
                if (tradeItemVo.getTradeItemExtra() != null) {
                    tradeItemVo.getTradeItemExtra().setClientCreateTime(System.currentTimeMillis());
                    if (mOldTradeVo.getTradeItemExtraList() != null) {
                        mOldTradeVo.getTradeItemExtraList().add(tradeItemVo.getTradeItemExtra());
                    } else {
                        List<TradeItemExtra> temp = new ArrayList<TradeItemExtra>();
                        temp.add(tradeItemVo.getTradeItemExtra());
                        mOldTradeVo.setTradeItemExtraList(temp);
                    }
                }
            }
        }

                if (mOldTradeVo.getTradeItemExtraList() != null) {
            if (tradeVo.getTradeItemExtraList() == null) {                for (TradeItemExtra tradeItemExtra : mOldTradeVo.getTradeItemExtraList()) {
                    tradeItemExtra.setStatusFlag(StatusFlag.INVALID);
                    tradeItemExtra.setIsPack(Bool.NO);
                }
                tradeVo.setTradeItemExtraList(mOldTradeVo.getTradeItemExtraList());
            } else {                Map<String, TradeItemExtra> temp = new HashMap<String, TradeItemExtra>();
                for (TradeItemExtra tradeItemExtra1 : tradeVo.getTradeItemExtraList()) {
                    temp.put(tradeItemExtra1.getUuid(), tradeItemExtra1);
                }
                for (TradeItemExtra tradeItemExtra : mOldTradeVo.getTradeItemExtraList()) {
                    if (!temp.containsKey(tradeItemExtra.getUuid())) {
                        tradeItemExtra.setStatusFlag(StatusFlag.INVALID);
                        tradeItemExtra.setIsPack(Bool.NO);
                    }
                    tradeVo.getTradeItemExtraList().add(tradeItemExtra);

                }
            }
        }

                if (mOldTradeVo.getTradeDeposit() != null) {
            if (tradeVo.getTradeDeposit() != null) {                mOldTradeVo.getTradeDeposit().setDepositPay(tradeVo.getTradeDeposit().getDepositPay());
                mOldTradeVo.getTradeDeposit().setDepositRefund(tradeVo.getTradeDeposit().getDepositRefund());
                tradeVo.setTradeDeposit(mOldTradeVo.getTradeDeposit());
            } else {                mOldTradeVo.getTradeDeposit().setStatusFlag(StatusFlag.INVALID);
                tradeVo.setTradeDeposit(mOldTradeVo.getTradeDeposit());
            }
        }

                if (mOldTradeVo.getTradeCustomerList() != null) {
            if (tradeVo.getTradeCustomerList() == null) {                List<TradeCustomer> temp = new ArrayList<TradeCustomer>();
                for (TradeCustomer tradeCustomer : mOldTradeVo.getTradeCustomerList()) {
                    tradeCustomer.setStatusFlag(StatusFlag.INVALID);
                    tradeCustomer.setChanged(true);
                    temp.add(tradeCustomer);
                }
                tradeVo.setTradeCustomerList(temp);
            } else if (tradeVo.getTradeCustomerList() != null) {
                for (TradeCustomer tradeCustomer : mOldTradeVo.getTradeCustomerList()) {
                    tradeCustomer.setStatusFlag(StatusFlag.INVALID);
                    tradeCustomer.setChanged(true);
                    tradeVo.getTradeCustomerList().add(tradeCustomer);
                }
            }
        }

                List<TradePrivilege> oldTp = mOldTradeVo.getTradePrivileges();
        List<TradePrivilege> newTp = tradeVo.getTradePrivileges();

        if (oldTp != null) {
                        Map<String, TradePrivilege> newTpMap = new HashMap<String, TradePrivilege>();

            for (TradePrivilege sPrivilege : newTp) {
                sPrivilege.setChanged(true);
                if (sPrivilege.getPromoId() != null) {
                    newTpMap.put(
                            sPrivilege.getPromoId().toString() + "_" + sPrivilege.getPrivilegeType().value().toString(),
                            sPrivilege);
                } else {
                    Boolean isForAllOrder = false;
                    if (TextUtils.isEmpty(sPrivilege.getTradeItemUuid())) {
                        isForAllOrder = true;
                    } else {
                        isForAllOrder = false;
                    }
                    String key = getPrivilegeKey(sPrivilege.getPrivilegeType(), isForAllOrder);
                    newTpMap.put(key, sPrivilege);
                }
            }

            String key = null;
            for (TradePrivilege sPrivilege : oldTp) {

                if (sPrivilege.getPromoId() != null) {
                    key = sPrivilege.getPromoId().toString() + "_" + sPrivilege.getPrivilegeType().value().toString();
                } else {
                    Boolean isForAllOrder = false;
                    if (TextUtils.isEmpty(sPrivilege.getTradeItemUuid())) {
                        isForAllOrder = true;
                    } else {
                        isForAllOrder = false;
                    }
                    key = getPrivilegeKey(sPrivilege.getPrivilegeType(), isForAllOrder);
                }
                if (!newTpMap.containsKey(key)) {
                    sPrivilege.setStatusFlag(StatusFlag.INVALID);
                    sPrivilege.setChanged(true);
                    tradeVo.getTradePrivileges().add(sPrivilege);
                } else {
                    try {
                                            } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

                if (Utils.isNotEmpty(mOldTradeVo.getTradePlanActivityList()) && Utils.isEmpty(tradeVo.getTradePlanActivityList())) {
            for (TradePlanActivity tradePlanActivity : mOldTradeVo.getTradePlanActivityList()) {
                if (tradePlanActivity.getId() == null || tradePlanActivity.getId().longValue() == 0) {
                    tradePlanActivity.setStatusFlag(StatusFlag.INVALID);
                    tradePlanActivity.setChanged(true);
                }
            }
            tradeVo.setTradePlanActivityList(mOldTradeVo.getTradePlanActivityList());

            for (TradeItemPlanActivity tradeItemPlanActivity : mOldTradeVo.getTradeItemPlanActivityList()) {
                tradeItemPlanActivity.setStatusFlag(StatusFlag.INVALID);
                tradeItemPlanActivity.setChanged(true);
            }
            tradeVo.setTradeItemPlanActivityList(mOldTradeVo.getTradeItemPlanActivityList());

        } else if (Utils.isNotEmpty(mOldTradeVo.getTradePlanActivityList()) && Utils.isNotEmpty(tradeVo.getTradePlanActivityList())) {
            Map<String, TradePlanActivity> tempTradePlanActivity = new HashMap<>();
            for (TradePlanActivity tradePlanActivity : tradeVo.getTradePlanActivityList()) {
                tempTradePlanActivity.put(tradePlanActivity.getUuid(), tradePlanActivity);
            }
            for (TradePlanActivity tradePlanActivity : mOldTradeVo.getTradePlanActivityList()) {
                if (!tempTradePlanActivity.containsKey(tradePlanActivity.getUuid())) {
                    tradePlanActivity.setStatusFlag(StatusFlag.INVALID);
                    tradePlanActivity.setChanged(true);
                    tradeVo.getTradePlanActivityList().add(tradePlanActivity);
                }
            }

            Map<String, TradeItemPlanActivity> tempTradeItemPlanActivity = new HashMap<>();
            for (TradeItemPlanActivity tradeItemPlanActivity : tradeVo.getTradeItemPlanActivityList()) {
                tempTradeItemPlanActivity.put(tradeItemPlanActivity.getTradeItemUuid(), tradeItemPlanActivity);
            }
            for (TradeItemPlanActivity oldTradeItemPlanActivity : mOldTradeVo.getTradeItemPlanActivityList()) {
                if (!tempTradeItemPlanActivity.containsKey(oldTradeItemPlanActivity.getTradeItemUuid())) {
                    oldTradeItemPlanActivity.setStatusFlag(StatusFlag.INVALID);
                    oldTradeItemPlanActivity.setChanged(true);
                    tradeVo.getTradeItemPlanActivityList().add(oldTradeItemPlanActivity);
                }
            }

        }

                if (IntegralCashPrivilegeVo.isNotNull(mOldTradeVo.getIntegralCashPrivilegeVo())) {

            if (IntegralCashPrivilegeVo.isNotNull(tradeVo.getIntegralCashPrivilegeVo())) {
                                copyOnlyData(tradeVo.getIntegralCashPrivilegeVo().getTradePrivilege(), mOldTradeVo.getIntegralCashPrivilegeVo().getTradePrivilege());
                tradeVo.getIntegralCashPrivilegeVo().setTradePrivilege(mOldTradeVo.getIntegralCashPrivilegeVo().getTradePrivilege());
                tradeVo.getIntegralCashPrivilegeVo().getTradePrivilege().setStatusFlag(StatusFlag.VALID);
            } else {
                                mOldTradeVo.getIntegralCashPrivilegeVo().getTradePrivilege().setStatusFlag(StatusFlag.INVALID);
                mOldTradeVo.getIntegralCashPrivilegeVo().getTradePrivilege().setChanged(true);
                tradeVo.setIntegralCashPrivilegeVo(mOldTradeVo.getIntegralCashPrivilegeVo());
            }
        }

                List<CouponPrivilegeVo> oldCouponPrivilegeVoList = mOldTradeVo.getCouponPrivilegeVoList();
        if (oldCouponPrivilegeVoList != null) {
            List<CouponPrivilegeVo> couponPrivilegeVoList = tradeVo.getCouponPrivilegeVoList();
            Map<String, CouponPrivilegeVo> mapCouponPrivilegeVos = new HashMap<>();
            if (couponPrivilegeVoList != null) {
                for (CouponPrivilegeVo item : couponPrivilegeVoList) {
                    mapCouponPrivilegeVos.put(item.getTradePrivilege().getUuid(), item);
                }
            }

            for (CouponPrivilegeVo item : oldCouponPrivilegeVoList) {
                if (!mapCouponPrivilegeVos.containsKey(item.getTradePrivilege().getUuid())) {
                    item.getTradePrivilege().setStatusFlag(StatusFlag.INVALID);
                    if (couponPrivilegeVoList == null) {
                        couponPrivilegeVoList = new ArrayList<>();
                    }
                    couponPrivilegeVoList.add(item);
                }
            }
            tradeVo.setCouponPrivilegeVoList(couponPrivilegeVoList);
        }
    }


    public void restorder(TradeVo restTradeVo, List<ShopcartItem> mListOrderDishshopVo) {
        clearShoppingCart();
        fastFootShoppingCartVo = new ShoppingCartVo();

        fastFootShoppingCartVo.setmTradeVo(restTradeVo);

                fastFootShoppingCartVo.getmTradeVo().getTrade().setTradeStatus(TradeStatus.CONFIRMED);
        fastFootShoppingCartVo.getmTradeVo().setTradeItemList(null);
        TradeExtra tradeExtra = fastFootShoppingCartVo.getmTradeVo().getTradeExtra();
        if (tradeExtra == null) {
            tradeExtra = new TradeExtra();
        }
        for (ShopcartItem mItem : mListOrderDishshopVo) {
            OperateShoppingCart.addToShoppingCart(fastFootShoppingCartVo.getmTradeVo(),
                    fastFootShoppingCartVo.getListOrderDishshopVo(),
                    mItem);
        }
        resetSelectDishQTY(fastFootShoppingCartVo);

                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        CheckGiftCouponIsActived(fastFootShoppingCartVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).resetOrder(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }
    }


    public TradePrivilege getOrderPrivilege() {
        if (fastFootShoppingCartVo.getmTradeVo() != null) {
            return fastFootShoppingCartVo.getmTradeVo().getTradePrivilege();
        } else {
            return null;
        }

    }

    public int getIndexPage() {
        return fastFootShoppingCartVo.getIndexPage();
    }

    public void setIndexPage(int indexPage) {
        fastFootShoppingCartVo.setIndexPage(indexPage);
    }

    public String getShowPropertyPageDishUUID() {
        return fastFootShoppingCartVo.getShowPropertyPageDishUUID();
    }


    public void resetOrderDish(TradeVo mTradeVo) {
        clearShoppingCart();
        List<ShopcartItem> listShopcart = CreateDishTool.tradeToDish(mTradeVo);
        fastFootShoppingCartVo.getListOrderDishshopVo().addAll(listShopcart);
        resetSelectDishQTY(fastFootShoppingCartVo);

                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo), mTradeVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).resetOrder(mergeShopcartItem(fastFootShoppingCartVo), mTradeVo);
        }
    }


    public void removeFastFoodDish(IShopcartItemBase mShopcartItemBase) {
        removeDish(fastFootShoppingCartVo, mShopcartItemBase);
                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        CheckGiftCouponIsActived(fastFootShoppingCartVo);

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeShoppingCart(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo(),
                    mShopcartItemBase);
        }
    }


    public void removeFastFoodShoppingcartItem(IShopcartItem mShopcartItem, SetmealShopcartItem mSetmealShopcartItem,
                                               ChangePageListener mChangePageListener, FragmentManager mFragmentManager) {

                long ruelId = -1;
        List<TradeItemPlanActivity> tradeItemPlanActivities = fastFootShoppingCartVo.getmTradeVo().getTradeItemPlanActivityList();
        if (tradeItemPlanActivities != null) {
            for (TradeItemPlanActivity tradeItemPlanActivity : tradeItemPlanActivities) {
                if (tradeItemPlanActivity.getTradeItemUuid().equals(mShopcartItem.getUuid())) {
                    ruelId = tradeItemPlanActivity.getRuleId();
                    break;
                }

            }
        }

        removeShoppingcartItem(fastFootShoppingCartVo,
                mShopcartItem,
                mSetmealShopcartItem,
                mChangePageListener,
                mFragmentManager);

        resetSelectDishQTY(fastFootShoppingCartVo);

        List<IShopcartItem> temp = new ArrayList<IShopcartItem>();
        for (IShopcartItem item : mergeShopcartItem(fastFootShoppingCartVo)) {
            if (item.getStatusFlag() == StatusFlag.VALID) {
                temp.add(item);
            }
        }

        MathManualMarketTool.removeShopcartItem(fastFootShoppingCartVo.getmTradeVo(),
                temp,
                mShopcartItem,
                true,
                true);

        autoAddSalesPromotion(getShoppingCartVo());
        memberPrivilege();
        removeAllChf();

                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        CheckGiftCouponIsActived(fastFootShoppingCartVo);

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeShoppingCart(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo(),
                    mShopcartItem != null ? mShopcartItem : mSetmealShopcartItem);
            arrayListener.get(key).removeMarketActivity(fastFootShoppingCartVo.getmTradeVo());
        }

    }



    public void removeFastFoodShoppingcartItemNoCheck(IShopcartItem mShopcartItem, SetmealShopcartItem mSetmealShopcartItem) {
                long ruelId = -1;
        List<TradeItemPlanActivity> tradeItemPlanActivities = fastFootShoppingCartVo.getmTradeVo().getTradeItemPlanActivityList();
        if (tradeItemPlanActivities != null) {
            for (TradeItemPlanActivity tradeItemPlanActivity : tradeItemPlanActivities) {
                if (tradeItemPlanActivity.getTradeItemUuid().equals(mShopcartItem.getUuid())) {
                    ruelId = tradeItemPlanActivity.getRuleId();
                    break;
                }

            }
        }
        removeDish(fastFootShoppingCartVo, mSetmealShopcartItem);
        mShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);
        resetSelectDishQTY(fastFootShoppingCartVo);

        List<IShopcartItem> temp = new ArrayList<IShopcartItem>();
        for (IShopcartItem item : mergeShopcartItem(fastFootShoppingCartVo)) {
            if (item.getStatusFlag() == StatusFlag.VALID) {
                temp.add(item);
            }
        }

        MathManualMarketTool.removeShopcartItem(fastFootShoppingCartVo.getmTradeVo(),
                temp,
                mShopcartItem,
                true,
                true);
        boolean autoMemberPrivilege;        if (ruelId == -1) {
            autoMemberPrivilege = false;
        } else {
            autoMemberPrivilege = true;
            List<TradePlanActivity> tradePlanActivitys = fastFootShoppingCartVo.getmTradeVo().getTradePlanActivityList();
            if (tradePlanActivitys != null && ruelId != -1) {
                for (TradePlanActivity tradePlanActivity : tradePlanActivitys) {
                    if (tradePlanActivity.getRuleId().compareTo(ruelId) == 0) {
                        autoMemberPrivilege = false;
                    }
                }
            }
        }

        if (autoMemberPrivilege) {            memberPrivilege();
        }

        removeAllChf();

                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        CheckGiftCouponIsActived(fastFootShoppingCartVo);

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeShoppingCart(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo(),
                    mShopcartItem != null ? mShopcartItem : mSetmealShopcartItem);
            arrayListener.get(key).removeMarketActivity(fastFootShoppingCartVo.getmTradeVo());
        }
    }


    public void updateFastFoodDish(IShopcartItemBase mShopcartItemBase, Boolean isTempDish) {


        IShopcartItem value = getShopcartItemFromList(fastFootShoppingCartVo, mShopcartItemBase.getUuid());

        if (value != null && value.getPrivilege() != null
                && value.getCouponPrivilegeVo() != null
                && value.getCouponPrivilegeVo().getTradePrivilege() != null) {
            value.setCouponPrivilegeVo(null);
        }

        if (mShopcartItemBase.getPrivilege() != null
                && (mShopcartItemBase.getPrivilege().getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT
                || mShopcartItemBase.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_PRICE
                || mShopcartItemBase.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_REBATE)) {
            setDishMemberPrivilege(fastFootShoppingCartVo, mShopcartItemBase, false);
        } else if ((value == null || mShopcartItemBase.getIsChangedPrice() == Bool.NO) && mShopcartItemBase.getPrivilege() == null) {
            setDishMemberPrivilege(fastFootShoppingCartVo, mShopcartItemBase, false);
        }

        updateDish(fastFootShoppingCartVo, mShopcartItemBase, isTempDish);
        resetSelectDishQTY(fastFootShoppingCartVo);
        List<IShopcartItem> allIttem = mergeShopcartItem(fastFootShoppingCartVo);

        autoAddSalesPromotion(fastFootShoppingCartVo);

        removeAllChf();

        MathShoppingCartTool.mathTotalPrice(allIttem, fastFootShoppingCartVo.getmTradeVo());

        CheckGiftCouponIsActived(fastFootShoppingCartVo);

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }

    }


    public List<IShopcartItem> getShoppingCartDish() {
        return mergeShopcartItem(fastFootShoppingCartVo);
    }

    public ShopcartItem getTempShopItem() {
        return fastFootShoppingCartVo.getTempShopItem();
    }

    public void setTempShopItem(ShopcartItem tempShopItem) {
        fastFootShoppingCartVo.setTempShopItem(tempShopItem);
    }


    public TradeVo getOrder() {
        return fastFootShoppingCartVo.getmTradeVo();
    }

    public void setIsSalesReturn(Boolean isSalesReturn) {
        checkNeedBuildMainOrder(fastFootShoppingCartVo.getmTradeVo());
        fastFootShoppingCartVo.getmTradeVo().setIsSalesReturn(isSalesReturn);
    }


    @Override
    public Boolean getIsSalesReturn() {
        return fastFootShoppingCartVo.getmTradeVo().getIsSalesReturn();
    }

    public ShoppingCartVo getShoppingCartVo() {
        return fastFootShoppingCartVo;
    }


    public DeliveryType getDeliveryType() {
        return fastFootShoppingCartVo.getmTradeVo().getTrade().getDeliveryType();
    }


    public ArrayList<TradeItem> getAddTradeItems() {
        return mAddTradeItems;
    }


    public ArrayList<TradeItem> getDeleteTradeItems() {
        return mDeleteTradeItems;
    }

    public List<IShopcartItem> getReduceItems() {
        return mReduceItems;
    }

    public List<IShopcartItem> getAddChangeItems() {
        return mAddChangeItems;
    }


    public void removeGuQingProducts(ChangePageListener mChangePageListener, FragmentManager mFragmentManager) {
        List<ShopcartItem> listShopCartItem = fastFootShoppingCartVo.getListOrderDishshopVo();

        for (int i = listShopCartItem.size() - 1; i >= 0; i--) {
            ShopcartItem item = listShopCartItem.get(i);
            if (item.getOrderDish().isClear()) {
                removeFastFoodShoppingcartItem(item, null, mChangePageListener, mFragmentManager);
            } else {
                List<SetmealShopcartItem> setmealShopcartItems = item.getSetmealItems();
                if (Utils.isNotEmpty(setmealShopcartItems)) {
                    for (SetmealShopcartItem setmealShopcartItem : setmealShopcartItems) {
                        if (setmealShopcartItem.getOrderDish().isClear()) {
                            removeFastFoodShoppingcartItem(item, null, mChangePageListener, mFragmentManager);
                        }
                    }
                }
            }

        }

    }


    public Boolean haveGuQingProduct() {
        List<ShopcartItem> listShopCartItem = fastFootShoppingCartVo.getListOrderDishshopVo();

        for (ShopcartItem item : listShopCartItem) {
            if (item.getOrderDish().isClear()) {
                return true;
            }
            List<SetmealShopcartItem> setmealShopcartItems = item.getSetmealItems();
            if (Utils.isNotEmpty(setmealShopcartItems)) {
                for (SetmealShopcartItem setmealShopcartItem : setmealShopcartItems) {
                    if (setmealShopcartItem.getOrderDish().isClear()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public void addTradeDeposit(BigDecimal peopleCount, BigDecimal money) {
        if (peopleCount != null && money != null) {

            BigDecimal depositPay = peopleCount.multiply(money);
            TradeDeposit mTradeDeposit = new TradeDeposit();
            mTradeDeposit.setShopIdenty(Utils.toLong(ShopInfoCfg.getInstance().shopId));
            mTradeDeposit.setBrandIdenty(Utils.toLong(ShopInfoCfg.getInstance().commercialGroupId));
            mTradeDeposit.setTradeId(fastFootShoppingCartVo.getmTradeVo().getTrade().getId());
            mTradeDeposit.setTradeUuid(fastFootShoppingCartVo.getmTradeVo().getTrade().getUuid());
            mTradeDeposit.setDepositPay(depositPay);
            mTradeDeposit.setUuid(SystemUtils.genOnlyIdentifier());
            mTradeDeposit.setStatusFlag(StatusFlag.VALID);

            fastFootShoppingCartVo.getmTradeVo().setTradeDeposit(mTradeDeposit);
                        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).updateShoppingcartData();
            }
        }
    }


    private void addTradeDeposit(BigDecimal value) {

    }


    public void addTradeDeposit(DepositInfo mDepositInfo) {
        if (mDepositInfo != null) {
            BigDecimal depositPay = BigDecimal.ZERO;
            switch (mDepositInfo.getType()) {
                case 1:                    Integer peopleCount = fastFootShoppingCartVo.getmTradeVo().getTrade().getTradePeopleCount();
                    if (peopleCount != null && mDepositInfo.getValue() != null) {
                        depositPay = mDepositInfo.getValue().multiply(new BigDecimal(peopleCount));
                    }
                    break;

                case 2:                    if (mDepositInfo.getValue() != null) {
                        depositPay = mDepositInfo.getValue();
                    }
                    break;
                default:
                    break;
            }

            TradeDeposit mTradeDeposit = new TradeDeposit();
            mTradeDeposit.setShopIdenty(Utils.toLong(ShopInfoCfg.getInstance().shopId));
            mTradeDeposit.setBrandIdenty(Utils.toLong(ShopInfoCfg.getInstance().commercialGroupId));
            mTradeDeposit.setTradeId(fastFootShoppingCartVo.getmTradeVo().getTrade().getId());
            mTradeDeposit.setTradeUuid(fastFootShoppingCartVo.getmTradeVo().getTrade().getUuid());
            mTradeDeposit.setDepositPay(depositPay);
            mTradeDeposit.setUuid(SystemUtils.genOnlyIdentifier());
            mTradeDeposit.setStatusFlag(StatusFlag.VALID);

            fastFootShoppingCartVo.getmTradeVo().setTradeDeposit(mTradeDeposit);
                        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).updateShoppingcartData();
            }

        }
    }


    public void removeTradeDeposit() {
        fastFootShoppingCartVo.getmTradeVo().setTradeDeposit(null);
                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateShoppingcartData();
        }
    }


    public void clearShoppingCart() {
        CustomerManager.getInstance().setLoginCustomer(null);
        CustomerManager.getInstance().setAccounts(null);
        fastFootShoppingCartVo = new ShoppingCartVo();
        mOldTradeVo = null;

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).clearShoppingCart();
        }
    }


    public boolean havePrivilegeByType(PrivilegeType mPrivilegeType) {
        List<TradePrivilege> listTP = fastFootShoppingCartVo.getmTradeVo().getTradePrivileges();
        if (listTP != null) {
            for (TradePrivilege mTradePrivilege : listTP) {
                if (mTradePrivilege.getPrivilegeType() == mPrivilegeType) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean addMarketActivity(MarketRuleVo marketDishVo, List<IShopcartItem> selectedItemList) {

        if (!MathManualMarketTool.isCanAddMarket(selectedItemList,
                fastFootShoppingCartVo.getmTradeVo(),
                marketDishVo,
                false)) {
            return false;
        }
                removeBanquetOnly(fastFootShoppingCartVo.getmTradeVo());
        MathManualMarketTool.mathManualAddMarket(selectedItemList,
                fastFootShoppingCartVo.getmTradeVo(),
                marketDishVo,
                false);

                removeGiftCouponPrivilege(selectedItemList, fastFootShoppingCartVo);

                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).addMarketActivity(fastFootShoppingCartVo.getmTradeVo());
        }
        return true;
    }


    public void checkMarketActivity() {
        if (fastFootShoppingCartVo.getListIShopcatItem() == null)
            return;


                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeMarketActivity(fastFootShoppingCartVo.getmTradeVo());
        }
    }


    public int getPlanUsageCountById(Long ruleId) {
        int count = 0;
        List<TradePlanActivity> listPlan = fastFootShoppingCartVo.getmTradeVo().getTradePlanActivityList();
        if (listPlan != null) {
            for (TradePlanActivity planActivity : listPlan) {
                if (planActivity != null && planActivity.getRuleId().compareTo(ruleId) == 0
                        && planActivity.getStatusFlag() == StatusFlag.VALID) {
                    count = count + planActivity.getPlanUsageCount();
                }

            }
        }
        return count;
    }


    public List<IShopcartItem> getShoppingCartItems() {
        return mergeShopcartItem(fastFootShoppingCartVo);
    }


    public void resetOrderFromOrderCenter(TradeVo tradeVo, List<Tables> tables) {
        clearShoppingCart();

        mOldTradeVo = tradeVo.clone();

        fastFootShoppingCartVo = new ShoppingCartVo();

        fastFootShoppingCartVo.setmTradeVo(tradeVo);

        fastFootShoppingCartVo.getmTradeVo().setmWeiXinCouponsVo(null);

        fastFootShoppingCartVo.setmTables(tables);

        if (tradeVo.getTrade() != null
                && (tradeVo.getTrade().getDeliveryType() == DeliveryType.TAKE
                || tradeVo.getTrade().getDeliveryType() == DeliveryType.SEND)) {            toTakeoutInfo(tradeVo);
        }

        List<IShopcartItem> items = DinnertableTradeInfo.buildShopcartItem(tradeVo);
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                IShopcartItem mIShopcartItem = items.get(i);
                mIShopcartItem.setIndex(i);
                fastFootShoppingCartVo.getListIShopcatItem().add(mIShopcartItem);
            }
        }


        resetSelectDishQTY(fastFootShoppingCartVo);

        CheckGiftCouponIsActived(fastFootShoppingCartVo);

                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }
    }



    private void toTakeoutInfo(TradeVo tradeVo) {
        TakeOutInfo takeOutInfo = new TakeOutInfo();
        TradeExtra tradeExtra = tradeVo.getTradeExtra();
        if (tradeVo != null && tradeExtra != null) {
            takeOutInfo.setReceiverTel(tradeExtra.getReceiverPhone());
            takeOutInfo.setReceiverName(tradeExtra.getReceiverName());
            takeOutInfo.setReceiverSex(tradeExtra.getReceiverSex());
            takeOutInfo.setDeliveryAddress(tradeExtra.getDeliveryAddress());
            if (tradeExtra.getDeliveryAddressId() != null) {
                takeOutInfo.setDeliveryAddressID(tradeExtra.getDeliveryAddressId());
            }
            if (tradeExtra.getExpectTime() != null) {
                takeOutInfo.setExpectTime(tradeExtra.getExpectTime());
            }

            takeOutInfo.setInvoiceTitle(tradeExtra.getInvoiceTitle());
            fastFootShoppingCartVo.setmTakeOutInfo(takeOutInfo);

                        List<TradeCustomer> listCustomer = tradeVo.getTradeCustomerList();
            if (listCustomer != null) {
                for (int i = listCustomer.size() - 1; i >= 0; i--) {
                    if (listCustomer.get(i).getCustomerType() == CustomerType.BOOKING) {
                        listCustomer.remove(i);
                        break;
                    }
                }
            }

        }
    }


    public Boolean isReturnCash() {
        TradeType mTradeType;
        if (fastFootShoppingCartVo.getmTradeVo() != null && fastFootShoppingCartVo.getmTradeVo().getTrade() != null) {
            mTradeType = fastFootShoppingCartVo.getmTradeVo().getTrade().getTradeType();
            if (mTradeType != null && mTradeType == TradeType.SELL_FOR_REPEAT) {
                return true;
            }
        }
        return false;
    }


    public boolean isHereOrTake() {
        if (fastFootShoppingCartVo.getmTradeVo() != null && fastFootShoppingCartVo.getmTradeVo().getTrade() != null) {
            if (fastFootShoppingCartVo.getmTradeVo().getTrade().getDeliveryType() == DeliveryType.HERE ||
                    fastFootShoppingCartVo.getmTradeVo().getTrade().getDeliveryType() == DeliveryType.TAKE) {
                return true;
            }

        }
        return false;
    }


    public Boolean haveExtraChargeChf() {
        if (fastFootShoppingCartVo.getmTradeVo() != null) {
            Map<Long, ExtraCharge> extraChargeMap = fastFootShoppingCartVo.getmTradeVo().getExtraChargeMap();
            if (extraChargeMap != null) {
                for (Map.Entry<Long, ExtraCharge> entry : extraChargeMap.entrySet()) {
                    ExtraCharge temp = entry.getValue();
                    if (temp.getCode().equals(ExtraManager.mealFee)) {
                        return true;
                    }

                }
            }
        }
        return false;

    }


    public void resetPack() {

        for (IShopcartItem item : mergeShopcartItem(fastFootShoppingCartVo)) {
            item.setPack(false);
        }

                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }

    }



    public void removeAllChf() {
        if (isHereOrTake()) {
            int count = 0;
            for (IShopcartItem item : mergeShopcartItem(fastFootShoppingCartVo)) {
                if (item.getPack() && item.getStatusFlag() == StatusFlag.VALID) {
                    count++;
                }
            }
            if (count == 0) {                if (fastFootShoppingCartVo.getmTradeVo() != null) {
                    Map<Long, ExtraCharge> extraChargeMap = fastFootShoppingCartVo.getmTradeVo().getExtraChargeMap();
                    if (extraChargeMap != null) {
                        for (Map.Entry<Long, ExtraCharge> entry : extraChargeMap.entrySet()) {
                            ExtraCharge temp = entry.getValue();
                            if (temp.getCode().equals(ExtraManager.mealFee)) {
                                extraChargeMap.remove(entry.getKey());
                                removeTradePrivilege(PrivilegeType.ADDITIONAL, fastFootShoppingCartVo.getmTradeVo());
                                break;
                            }
                        }
                    }
                }

            }
        }

    }



    public void removeAllExtraChage() {
        if (fastFootShoppingCartVo.getmTradeVo() != null) {
            Map<Long, ExtraCharge> extraChargeMap = fastFootShoppingCartVo.getmTradeVo().getExtraChargeMap();
            if (extraChargeMap != null) {

                extraChargeMap.clear();
                removeTradePrivilege(PrivilegeType.ADDITIONAL, fastFootShoppingCartVo.getmTradeVo());

                                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                        fastFootShoppingCartVo.getmTradeVo());

                for (int key : arrayListener.keySet()) {

                    arrayListener.get(key).updateDish(mergeShopcartItem(fastFootShoppingCartVo), fastFootShoppingCartVo.getmTradeVo());

                }
            }
        }
    }


    public boolean isAllowAddCoupon(CouponPrivilegeVo couponPrivilegeVo) {
        return isAllowAddCoupon(fastFootShoppingCartVo, couponPrivilegeVo);
    }


    public void removeCouponPrivilege(CouponPrivilegeVo couponPrivilegeVo, boolean isNeedListener) {
        removeCouponPrivilege(fastFootShoppingCartVo, couponPrivilegeVo, isNeedListener);
    }


    public void setOpenIdenty(String openId) {
        setOpenIdenty(fastFootShoppingCartVo, openId);
    }


    public List<IShopcartItem> mergeShopcartItem(ShoppingCartVo mShoppingCartVo) {
        List<IShopcartItem> tempList = new ArrayList<IShopcartItem>();
        tempList.addAll(mShoppingCartVo.getListIShopcatItem());
        tempList.addAll(mShoppingCartVo.getListOrderDishshopVo());
        return tempList;
    }

    @Override
    public void setOrderType(ShoppingCartVo mShoppingCartVo, DeliveryType orderType) {
        super.setOrderType(mShoppingCartVo, orderType);

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).setOrderType(orderType);
        }

    }

    public void removeShoppingcartItem(ShoppingCartVo mShoppingCartVo, IShopcartItem mShopcartItem,
                                       SetmealShopcartItem mSetmealShopcartItem, ChangePageListener mChangePageListener,
                                       FragmentManager mFragmentManager) {
                if (mShopcartItem.getId() != null) {
            removeReadonlyShopcartItem(mShoppingCartVo, (ReadonlyShopcartItem) mShopcartItem);
            return;
        }

                if (mSetmealShopcartItem == null) {
            removeDish(mShoppingCartVo, mShopcartItem);
                        if (mShoppingCartVo.getTempShopItem() != null
                    && mShoppingCartVo.getTempShopItem().getUuid().equals(mShopcartItem.getUuid())) {
                mShoppingCartVo.setTempShopItem(null);
                if (mChangePageListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constant.NONEEDCHECK, true);
                    mChangePageListener.changePage(SettingManager.getSettings(IPanelItemSettings.class).getOrderHomePage(), null);
                }
            }
        } else {
                        switch (mShopcartItem.getSetmealManager().testModify(mSetmealShopcartItem, BigDecimal.ZERO)) {
                case SUCCESSFUL:
                    removeDish(mShoppingCartVo, mSetmealShopcartItem);
                    mShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);
                                        if (mShoppingCartVo.getIndexPage() == ChangePageListener.DISHPROPERTY
                            && mShoppingCartVo.getShowPropertyPageDishUUID().equals(mSetmealShopcartItem.getUuid())
                            && mChangePageListener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Constant.NONEEDCHECK, true);
                        bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, mShopcartItem.getUuid());
                        bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.ORDERDISHLIST);
                        mChangePageListener.changePage(ChangePageListener.DISHCOMBO, bundle);
                    }

                    break;
                case FAILED_REMOVE_REQUISITE:
                    ToastUtil.showLongToast(
                            MainApplication.getInstance().getResources().getString(R.string.canont_delete_dish));

                    break;
                default:
                                        if (mShoppingCartVo.getTempShopItem() != null
                            && mShopcartItem.getUuid().equals(mShoppingCartVo.getTempShopItem().getUuid())
                            && mShoppingCartVo.getIndexPage() == ChangePageListener.DISHCOMBO) {
                        removeDish(mShoppingCartVo, mSetmealShopcartItem);
                        mShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);

                    } else if (mShoppingCartVo.getTempShopItem() != null && mShopcartItem != null
                            && mShopcartItem.getUuid().equals(mShoppingCartVo.getTempShopItem().getUuid())
                            && mShoppingCartVo.getIndexPage() == ChangePageListener.DISHPROPERTY
                            && mShoppingCartVo.getShowPropertyPageDishUUID().equals(mSetmealShopcartItem.getUuid())) {
                                                removeDish(mShoppingCartVo, mSetmealShopcartItem);
                        mShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);

                        if (mChangePageListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putBoolean(Constant.NONEEDCHECK, true);
                            bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, mShopcartItem.getUuid());
                            bundle.putInt(Constant.EXTRA_LAST_PAGE, SettingManager.getSettings(IPanelItemSettings.class).getOrderHomePage());
                            mChangePageListener.changePage(ChangePageListener.DISHCOMBO, bundle);
                        }

                    } else if (mShoppingCartVo.getTempShopItem() != null
                            && mShopcartItem.getUuid().equals(mShoppingCartVo.getTempShopItem().getUuid())
                            && mShoppingCartVo.getIndexPage() == ChangePageListener.DISHPROPERTY
                            && !mShoppingCartVo.getShowPropertyPageDishUUID().equals(mSetmealShopcartItem.getUuid())) {
                                                removeDish(mShoppingCartVo, mSetmealShopcartItem);
                        mShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);

                    } else if (mShoppingCartVo.getTempShopItem() != null
                            && !mShopcartItem.getUuid().equals(mShoppingCartVo.getTempShopItem().getUuid())) {
                                                isCheckVaild(mShoppingCartVo,
                                ChangePageListener.DISHCOMBO,
                                mChangePageListener,
                                mFragmentManager,
                                mShopcartItem,
                                mSetmealShopcartItem);
                    } else {
                                                removeDish(mShoppingCartVo, mSetmealShopcartItem);
                        mShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);

                        if (mChangePageListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putBoolean(Constant.NONEEDCHECK, true);
                            bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, mShopcartItem.getUuid());
                            bundle.putInt(Constant.EXTRA_LAST_PAGE, SettingManager.getSettings(IPanelItemSettings.class).getOrderHomePage());
                            mChangePageListener.changePage(ChangePageListener.DISHCOMBO, bundle);
                        }
                    }
                    break;
            }
        }
    }

    public void isCheckVaild(ShoppingCartVo mShoppingCartVo, int mPageNo, ChangePageListener mChangePageListener,
                             FragmentManager mFragmentManager, IShopcartItem mShopcartItem, SetmealShopcartItem mSetmealShopcartItem) {
                if (mShoppingCartVo.getTempShopItem() != null && mShoppingCartVo.getTempShopItem().getSetmealManager() != null
                && !mShoppingCartVo.getTempShopItem().getSetmealManager().isValid()) {
                        showCheckDialog(mShoppingCartVo,
                    mPageNo,
                    mChangePageListener,
                    mFragmentManager,
                    mShopcartItem,
                    mSetmealShopcartItem);

        } else {
                        removeDish(mShoppingCartVo, mSetmealShopcartItem);
            mShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);

            if (mChangePageListener != null) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.NONEEDCHECK, true);
                bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, mShopcartItem.getUuid());
                bundle.putInt(Constant.EXTRA_LAST_PAGE, SettingManager.getSettings(IPanelItemSettings.class).getOrderHomePage());
                mChangePageListener.changePage(ChangePageListener.DISHCOMBO, bundle);
            }
        }
    }

    public void showCheckDialog(final ShoppingCartVo mShoppingCartVo, final int mPageNo,
                                final ChangePageListener mChangePageListener, FragmentManager mFragmentManager,
                                final IShopcartItem mShopcartItem, final SetmealShopcartItem mSetmealShopcartItem) {
        CommonDialogFragment.CommonDialogFragmentBuilder cb = new CommonDialogFragment.CommonDialogFragmentBuilder(MainApplication.getInstance());
        cb.iconType(CommonDialogFragment.ICON_WARNING)
                .title(MainApplication.getInstance().getResources().getString(R.string.order_dish_setmeal_close_tip))
                .negativeText(R.string.calm_logout_no)
                .positiveText(R.string.close)
                .positiveLinstner(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                                                if (mSetmealShopcartItem != null) {
                            ShopcartItem deleteShopcartItem =
                                    getShopcartItemByUUID(mShoppingCartVo, mSetmealShopcartItem.getParentUuid());
                            deleteShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);
                            removeDish(mShoppingCartVo, mSetmealShopcartItem);
                        }

                                                if (mShoppingCartVo.getTempShopItem() != null) {
                            isCheckAdd(mShoppingCartVo, mShoppingCartVo.getTempShopItem(), false);
                            mShoppingCartVo.setTempShopItem(null);
                        }

                                                if (mChangePageListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID,
                                    mShopcartItem != null ? mShopcartItem.getUuid() : "");
                            bundle.putInt(Constant.EXTRA_LAST_PAGE, SettingManager.getSettings(IPanelItemSettings.class).getOrderHomePage());
                            bundle.putBoolean(Constant.NONEEDCHECK, true);
                            mChangePageListener.changePage(mPageNo, bundle);
                        }

                                                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(mShoppingCartVo),
                                mShoppingCartVo.getmTradeVo());
                        for (int key : arrayListener.keySet()) {
                            arrayListener.get(key).removeShoppingCart(mergeShopcartItem(mShoppingCartVo),
                                    mShoppingCartVo.getmTradeVo(),
                                    mShopcartItem != null ? mShopcartItem : mSetmealShopcartItem);
                        }
                    }

                })
                .build()
                .show(mFragmentManager, "closeSetmealFragment");
    }

    public void setReturnInventoryList(List<InventoryItem> returnInventoryList) {
        fastFootShoppingCartVo.putReturnInventoryMap(returnInventoryList);
    }

    public void setInventoryVo(TradeVo tradevo) {
        if (fastFootShoppingCartVo.getInventoryVo() == null) {
            fastFootShoppingCartVo.setInventoryVo(new InventoryVo());
        }
        List<TradeItem> newAddDishList = new ArrayList<>();
        if (tradevo != null && Utils.isNotEmpty(tradevo.getTradeItemList())) {
            for (TradeItemVo tradeItemVo : tradevo.getTradeItemList()) {
                if (tradeItemVo.getTradeItem().getId() == null && TextUtils.isEmpty(tradeItemVo.getTradeItem().getRelateTradeItemUuid())) {
                    newAddDishList.add(tradeItemVo.getTradeItem());
                }
            }
        }
        fastFootShoppingCartVo.getInventoryVo().addNewAddDishList(newAddDishList);
    }

    private InventoryVo getInventoryVo(TradeVo tradevo) {
        setInventoryVo(tradevo);
        return fastFootShoppingCartVo.getInventoryVo();
    }

    private static Map<String, TradeItemVo> list2Map(Collection<TradeItemVo> tradeItemVos) {
        Map<String, TradeItemVo> map = new HashMap<>();
        if (tradeItemVos != null) {
            for (TradeItemVo tradeItemVo : tradeItemVos) {
                map.put(tradeItemVo.getTradeItem().getUuid(), tradeItemVo);
            }
        }
        return map;
    }


    public void doDishActivityIsCheck(List<DishDataItem> unItemList, MarketRuleVo ruleVo) {
        if (unItemList == null || unItemList.isEmpty() || ruleVo == null) {
            return;
        }
        for (DishDataItem item : unItemList) {
            if (MathManualMarketTool.isItemCanJoinPlanActivity(item.getBase(), ruleVo)) {
                item.setCheckStatus(DishDataItem.DishCheckStatus.NOT_CHECK);
            } else {
                item.setCheckStatus(DishDataItem.DishCheckStatus.INVALIATE_CHECK);
            }
        }
    }



    public void addSalesPromotion(SalesPromotionRuleVo salesPromotionRuleVo, List<IShopcartItem> selectedShopcartItems) {
        addSalesPromotion(fastFootShoppingCartVo, salesPromotionRuleVo, selectedShopcartItems, CustomerManager.getInstance().getLoginCustomer());
    }


    public void removeSalesPromotion(Long planId) {
        removeSalesPromotion(fastFootShoppingCartVo, planId, CustomerManager.getInstance().getLoginCustomer(), false);
    }


    public void removeSalesPromotion(String tradePlanUuid) {
        removeSalesPromotion(fastFootShoppingCartVo, tradePlanUuid, CustomerManager.getInstance().getLoginCustomer(), false);
    }
}
