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

/**
 * 购物车
 *
 * @Description: TODO
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class ShoppingCart extends BaseShoppingCart {

    private static final String TAG = ShoppingCart.class.getSimpleName();

    private static ShoppingCart instance = null;

    public ShoppingCartVo fastFootShoppingCartVo = new ShoppingCartVo();


    private TradeVo mOldTradeVo;

    //加菜数据
    private ArrayList<TradeItem> mAddTradeItems;

    //删菜数据
    private ArrayList<TradeItem> mDeleteTradeItems;

    //发生库存改变的菜品
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
        if (BuildConfig.DEBUG) {
            Log.d(ShoppingCart.class.getSimpleName(), "registerListener tag " + listenerTag + ", after put " + arrayListener.keySet().toString());
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
        if (BuildConfig.DEBUG) {
            Log.d(ShoppingCart.class.getSimpleName(), "clear arrayListener, after clear " + arrayListener.keySet().toString());
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
        if (BuildConfig.DEBUG) {
            Log.d(ShoppingCart.class.getSimpleName(), "remove tag " + tag + ", after remove " + arrayListener.keySet().toString());
        }
    }

    /**
     * 设置号牌
     *
     * @Title: setCardNo
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
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

    /**
     * 设置桌台
     *
     * @Title: setTable
     * @Description: TODO
     * @Param @param tradeTable TODO
     * @Return void 返回类型
     */
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
                // 自助时，如有一单多桌，则第一个桌子添加全部人数，其他桌台记人数为0
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
            // fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setNumberPlate("");

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

    /**
     * @Title: addPeople
     * @Description: 添加就餐人数
     * @Param count TODO
     * @Return void 返回类型
     */
    public void addPeople(int count) {
        checkNeedBuildMainOrder(fastFootShoppingCartVo.getmTradeVo());
        if (fastFootShoppingCartVo.getmTradeVo().getTrade() != null) {
            fastFootShoppingCartVo.getmTradeVo().getTrade().setTradePeopleCount(count);
        }
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).setTradePeopleCount(count);
        }

        // 将人数添加到桌台
        List<TradeTable> listTradeTable = fastFootShoppingCartVo.getmTradeVo().getTradeTableList();
        if (listTradeTable != null) {
            for (TradeTable mTradeTable : listTradeTable) {
                mTradeTable.setTablePeopleCount(count);
            }
        }
        // 计算订单总价格
        List<IShopcartItem> allItem = mergeShopcartItem(fastFootShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(allItem, fastFootShoppingCartVo.getmTradeVo());

        CheckGiftCouponIsActived(fastFootShoppingCartVo);

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).addExtraCharge(fastFootShoppingCartVo.getmTradeVo(),
                    fastFootShoppingCartVo.getmTradeVo().getExtraChargeMap());
        }
    }

    /**
     * @Title: addShippingToCart
     * @Description: 添加菜品到购物车
     * @Param @param mShopcartItem
     * @Param @param isTempDish ture:是临时保存菜品 false:不是临时保存菜品
     * 是时时加入购物车
     * @Return void 返回类型
     */
    public void addtFastFoodDishToCart(ShopcartItem mShopcartItem, Boolean isTempDish) {
        addOneDishToCart(mShopcartItem, isTempDish);
        mathDishPriceAndPrivilege(mShopcartItem);
        List<IShopcartItem> allIttem = mergeShopcartItem(fastFootShoppingCartVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).addToShoppingCart(allIttem, fastFootShoppingCartVo.getmTradeVo(), mShopcartItem);
        }
    }

    /**
     * @Title: addBatchDishToCart
     * @Description: 批量添加菜品到购物车
     * @Param @param shopcartItems
     * @Param @param isTempDish ture:是临时保存菜品 false:不是临时保存菜品
     * 是时时加入购物车
     * @Return void 返回类型
     */
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

    /**
     * @Title: addSigleDishToCart
     * @Description: 添加一个菜品到购物车
     * @Param @param mShopcartItem
     * @Param @param isTempDish ture:是临时保存菜品 false:不是临时保存菜品
     * 是时时加入购物车
     * @Return void 返回类型
     */
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
                || mShopcartItem.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_PRICE)) {
            setDishMemberPrivilege(fastFootShoppingCartVo, mShopcartItem, false);
        } else if ((value == null || mShopcartItem.getIsChangedPrice() == Bool.NO) && mShopcartItem.getPrivilege() == null) {
            setDishMemberPrivilege(fastFootShoppingCartVo, mShopcartItem, false);
        }

        addShippingToCart(fastFootShoppingCartVo, mShopcartItem, isTempDish);
    }


    /**
     * @Title: mathDishPriceAndPrivilege
     * @Description: 当前菜品的优惠、营销活动、重新计算价格
     * @Param @param mShopcartItem
     * @Return void 返回类型
     */
    private void mathDishPriceAndPrivilege(ShopcartItem mShopcartItem) {
        List<IShopcartItem> allIttem = mergeShopcartItem(fastFootShoppingCartVo);
        autoAddSalesPromotion(fastFootShoppingCartVo);

        // 计算订单总价格
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

    /**
     * @Title: isAddTempDish
     * @Description: 变更临时菜品状态
     * @Param @param mShopcartItem
     * @Param @param isAdd isAdd true:确认临时套餐，false:移除临时套餐
     * @Return void 返回类型
     */
    public void isCheckAdd(ShopcartItem mShopcartItem, Boolean isAdd) {
        if (!isAdd) {
            removeDish(fastFootShoppingCartVo, mShopcartItem);
        }
        fastFootShoppingCartVo.setTempShopItem(null);
    }

    /**
     * @Title: setFastFoodCustomer
     * @Description: 设置登录会员
     * @Param @param mTradeCustomer TODO
     * @Return void 返回类型
     */
    public void setFastFoodCustomer(TradeCustomer mTradeCustomer) {
        // // 如果为null表示注销会员登录
        // if (mTradeCustomer == null) {
        // List<TradeCustomer> listCustomer =
        // fastFootShoppingCartVo.getmTradeVo().getTradeCustomerList();
        // if (listCustomer != null) {
        // for (int i = listCustomer.size() - 1; i >= 0;
        // i--) {
        // TradeCustomer customer = listCustomer.get(i);
        // if (customer.getCustomerType() ==
        // CustomerType.MEMBER) {
        // listCustomer.remove(i);
        // break;
        // }
        // }
        // }
        // }
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

    /**
     * 设置呼入电话关联的用户信息即预订客户信息
     *
     * @Title: setCallCustomer
     * @Description: TODO
     * @Param @param mTradeCustomer TODO
     * @Return void 返回类型
     */
    public void setCallCustomer(TradeCustomer mTradeCustomer) {
        if (fastFootShoppingCartVo.getArrayTradeCustomer() == null) {
            fastFootShoppingCartVo.setArrayTradeCustomer(new HashMap<Integer, TradeCustomer>());
        }

        fastFootShoppingCartVo.getArrayTradeCustomer().put(CustomerType.BOOKING.value(), mTradeCustomer);
    }

    /**
     * 设置外卖地址
     *
     * @Title: setAddress
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    public void setAddress(TakeOutInfo entity) {
        fastFootShoppingCartVo.setmTakeOutInfo(entity);

        checkNeedBuildMainOrder(fastFootShoppingCartVo.getmTradeVo());

        if (entity != null) {
            if (fastFootShoppingCartVo.getmTradeVo().getTradeExtra() == null) {
                fastFootShoppingCartVo.getmTradeVo().setTradeExtra(new TradeExtra());
            }
            // 发票抬头
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setInvoiceTitle(entity.getInvoiceTitle());
            // 期望送达时间
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setExpectTime(entity.getExpectTime());

            // 收货人电话
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setReceiverPhone(entity.getReceiverTel());
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setNation(entity.getNation());
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setCountry(entity.getCountry());
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setNationalTelCode(entity.getNationalTelCode());
            // 收货人姓名
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setReceiverName(entity.getReceiverName());
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setDeliveryAddressId(entity.getDeliveryAddressID());
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setDeliveryAddress(entity.getDeliveryAddress());
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setReceiverSex(entity.getReceiverSex());

        } else {
            if (fastFootShoppingCartVo.getmTradeVo().getTradeExtra() == null) {
                return;
            }
            // 发票抬头
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setInvoiceTitle("");
            // 期望送达时间
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setExpectTime(null);
            // 收货人电话
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setReceiverPhone("");
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setNation("");
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setCountry("");
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setNationalTelCode("");
            // 收货人姓名
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setReceiverName("");
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setDeliveryAddressId(null);
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setDeliveryAddress("");
            fastFootShoppingCartVo.getmTradeVo().getTradeExtra().setReceiverSex(null);
        }

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).setOrderUserMessage(entity);
        }

    }

    /**
     * @Title: setCouponPrivilege
     * @Description: 设置优惠劵
     * @Param TODO
     * @Return void 返回类型
     */
    public void setCouponPrivilege(CouponPrivilegeVo mCouponPrivilegeVo) {
        // 移除手动折扣（因手动整单折扣与优惠劵不可并存）
        // fastFootShoppingCartVo.getmTradeVo().setTradePrivileges(null);
        // //移除免单理由
        // removeFreeReason();
        checkNeedBuildMainOrder(fastFootShoppingCartVo.getmTradeVo());
        //礼品券赠菜
        if (mCouponPrivilegeVo.getCoupon().getCouponType() == CouponType.GIFT
                && mCouponPrivilegeVo.getShopcartItem() != null) {

            setGiftCouponPrivilege(mCouponPrivilegeVo);

        } else {
            removeOrderTreadePrivilege(fastFootShoppingCartVo.getmTradeVo());
            BuildPrivilegeTool.buildCouponPrivilege(fastFootShoppingCartVo.getmTradeVo(), mCouponPrivilegeVo);
            // 计算订单总价格
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


    /**
     * @Title: setGiftCouponPrivilege
     * @Description: 礼品优惠劵
     * @Param TODO
     * @Return void 返回类型
     */

    private void setGiftCouponPrivilege(CouponPrivilegeVo mCouponPrivilegeVo) {

        ShopcartItem tempItem = mCouponPrivilegeVo.getShopcartItem();

        boolean marketFlag = false;

        //判断些菜是否已经参加营销活动
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
        if (!marketFlag) {//如果没有参加营销活动
            //判断是否已经包含礼品券
            for (IShopcartItem item : mergeShopcartItem(fastFootShoppingCartVo)) {
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
                || value.getPrivilege().getPrivilegeType() == PrivilegeType.REBATE
                || value.getPrivilege().getPrivilegeType() == PrivilegeType.FREE
                || value.getPrivilege().getPrivilegeType() == PrivilegeType.GIVE
                || value.getPrivilege().getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT)) {
            value.setPrivilege(null);
        }
        if (value instanceof ShopcartItem) {
            addtFastFoodDishToCart((ShopcartItem) value, false);
        } else {
            // 计算订单总价格
            MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());

            CheckGiftCouponIsActived(fastFootShoppingCartVo);

            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).updateDish(mergeShopcartItem(fastFootShoppingCartVo),
                        fastFootShoppingCartVo.getmTradeVo());
            }
        }
    }


    /**
     * @Title: removeCouponPrivilege
     * @Description: 移除优惠劵
     * @Param TODO
     * @Return void 返回类型
     */
    public void removeCouponPrivilege() {
        // 移除优惠劵
        removeAllCouponPrivilege(fastFootShoppingCartVo, true);
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


    /**
     * @Title: addWeiXinCouponsPrivilege
     * @Description: 添加微信卡卷
     * @Param mWeiXinCouponsInfo TODO
     * @Return void 返回类型
     */
    public void addWeiXinCouponsPrivilege(WeiXinCouponsInfo mWeiXinCouponsInfo) {
        addWeiXinCouponsVo(fastFootShoppingCartVo.getmTradeVo(), mWeiXinCouponsInfo);
        // 计算订单总价格
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(fastFootShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, fastFootShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).addWeiXinCouponsPrivilege(shopcartItemList, fastFootShoppingCartVo.getmTradeVo());

        }
    }

    /**
     * @Title: removeWeiXinCouponsPrivilege
     * @Description: 移除微信卡卷
     * @Param tradePrivileges
     * @Return void 返回类型
     */
    public void removeWeiXinCouponsPrivilege(List<TradePrivilege> tradePrivileges) {
        for (TradePrivilege tradePrivilege : tradePrivileges) {
            removeWeiXinCouponsVo(fastFootShoppingCartVo.getmTradeVo(), tradePrivilege);
        }
        // 计算订单总价格
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(fastFootShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, fastFootShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).removeWeiXinCouponsPrivilege(shopcartItemList, fastFootShoppingCartVo.getmTradeVo());

        }
    }

    /**
     * @Title: removeAllWXC
     * @Description: 移除所有微信卡卷
     * @Param TODO
     * @Return void 返回类型
     */
    public void removeAllWXC() {
        fastFootShoppingCartVo.getmTradeVo().getmWeiXinCouponsVo().clear();
        // 计算订单总价格
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(fastFootShoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, fastFootShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).removeWeiXinCouponsPrivilege(shopcartItemList, fastFootShoppingCartVo.getmTradeVo());

        }
    }

    /**
     * 设置手动整单打折
     *
     * @Title: setOrderDiscount
     * @Description: TODO
     * @Param @param type
     * @Param @param dishcount TODO
     * @Return void 返回类型
     */
    public void setOrderPrivilege(TradePrivilege tradePrivilege, Reason mReason) {
        checkNeedBuildMainOrder(fastFootShoppingCartVo.getmTradeVo());

        // 移除优惠劵（因优惠劵与手动整单折扣不可并存）
//		fastFootShoppingCartVo.getmTradeVo().setCouponPrivilege(null);
        removeAllCouponPrivilege(fastFootShoppingCartVo, true);

        if (tradePrivilege != null) {
            tradePrivilege.setUuid(SystemUtils.genOnlyIdentifier());
            tradePrivilege.validateCreate();
            tradePrivilege.setCreatorId(Session.getAuthUser().getId());
            tradePrivilege.setCreatorName(Session.getAuthUser().getName());
            tradePrivilege.setTradeUuid(fastFootShoppingCartVo.getmTradeVo().getTrade().getUuid());
        }

        fastFootShoppingCartVo.getmTradeVo().replaceTradePrivilege(tradePrivilege);
        // 设置免单理由
        if (tradePrivilege.getPrivilegeType() == PrivilegeType.FREE && mReason != null) {
            setTradeFreeReasonRel(fastFootShoppingCartVo.getmTradeVo(), mReason, OperateType.TRADE_FASTFOOD_FREE);
        } else {
            removeFreeReason(fastFootShoppingCartVo.getmTradeVo());
        }

        // 如果有使用折扣卷则移除折扣卷
        // if
        // (fastFootShoppingCartVo.getmTradeVo().getCouponPrivilege()
        // != null && fastFootShoppingCartVo.getmTradeVo()
        // .getCouponPrivilege()
        // .getCoupon()
        // .getCouponType()
        // .value() != CouponType.GIFT.value()) {
        // fastFootShoppingCartVo.getmTradeVo().setCouponPrivilege(null);
        // }

        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).orderDiscount(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }

    }

    /**
     *

     * @Title: removeFreeReason
     * @Description: 移除免单理由
     * @Param TODO
     * @Return void 返回类型
     */
    // private void removeFreeReason() {
    // // 移除免单理由
    // List<TradeReasonRel> listReason =
    // fastFootShoppingCartVo.getmTradeVo().getTradeReasonRelList();
    // if (listReason != null) {
    // for (int i = 0; i < listReason.size(); i++) {
    // if (listReason.get(i).getOperateType() ==
    // OperateType.TRADE_FASTFOOD_FREE) {
    // listReason.remove(i);
    // }
    // }
    // }
    // }

    /**
     * @Title: removeOrderPrivilege
     * @Description: 移除优惠券整单打折并计算
     * @Param TODO
     * @Return void 返回类型
     */
    public void removeOrderPrivilege() {
        // fastFootShoppingCartVo.getmTradeVo().setTradePrivileges(null);
        //
        // // 移除免单理由
        // removeFreeReason();
        removeAllCouponPrivilege(fastFootShoppingCartVo, false);
        removeOrderTreadePrivilege(fastFootShoppingCartVo.getmTradeVo());
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeDiscount(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo(),
                    null);
        }
    }


    /**
     * @Title: removeOrderPrivilege
     * @Description: 移除手动整单打折并计算
     * @Param TODO
     * @Return void 返回类型
     */
    public void removeOrderTradePrivilege() {
        removeOrderTreadePrivilege(fastFootShoppingCartVo.getmTradeVo());

        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeDiscount(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo(),
                    null);
        }
    }

    /**
     * @Title: batchPrivilege
     * @Description: 批量折扣
     * @Param @param listShopcartItem TODO
     * @Return void 返回类型
     */
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

        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).batchPrivilege(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }
    }

    /**
     * @Title: removeDishPrivilege
     * @Description: 移除菜品折扣
     * @Param @param mShopcartItem TODO
     * @Return void 返回类型
     */
    public void removeDishPrivilege(IShopcartItemBase mShopcartItem) {
        if (mShopcartItem.getPrivilege() != null && (mShopcartItem.getPrivilege().getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT
                || mShopcartItem.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_PRICE)) {
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

        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeDiscount(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo(),
                    null);
        }
    }

    /**
     * @Title: memberPrivilege
     * @Description: 设置会员折扣
     * @Param TODO
     * @Return void 返回类型
     */
    public void memberPrivilege() {
        batchMemberPrivilege(fastFootShoppingCartVo);

        // 计算订单总价格
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
                if (item.getPrivilege() != null && (item.getPrivilege().getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT || item.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_PRICE)) {
                    return true;
                }
            }
        }

        return false;

    }

    /**
     * @Title: removeMemberPrivilege
     * @Description: 移除会员折扣
     * @Param TODO
     * @Return void 返回类型
     */
    public void removeMemberPrivilege() {
        for (IShopcartItemBase item : mergeShopcartItem(fastFootShoppingCartVo)) {
            if (item.getPrivilege() != null && (item.getPrivilege().getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT
                    || item.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_PRICE)) {
                item.setPrivilege(null);
                item.setPrivilege(null);
            }
        }
        autoAddSalesPromotion(fastFootShoppingCartVo);

        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).batchPrivilege(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }
    }

    /**
     * @Title: setIntegralCash
     * @Description: 设置积分抵现
     * @Param @param mCrmCustomerLevelRights
     * @Param @param integra TODO
     * @Return void 返回类型
     */
    public void setIntegralCash(IntegralCashPrivilegeVo mIntegralCashPrivilegeVo) {

        if (mIntegralCashPrivilegeVo == null || !mIntegralCashPrivilegeVo.hasRule()
                || mIntegralCashPrivilegeVo.getIntegral().compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        // 设置积分抵现优惠信息
        BuildPrivilegeTool.buildCashPrivilege(mIntegralCashPrivilegeVo, fastFootShoppingCartVo.getmTradeVo());
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).setIntegralCash(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
            arrayListener.get(key).updateDish(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }
    }

    /**
     * @Title: removeIntegralCash
     * @Description: 移除会员积分抵现
     * @Param TODO
     * @Return void 返回类型
     */
    public void removeIntegralCash() {
        fastFootShoppingCartVo.getmTradeVo().setIntegralCashPrivilegeVo(null);
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeIntegralCash(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
            arrayListener.get(key).updateDish(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }
    }

    /**
     * @Title: removeAllPrivilegeForCustomer
     * @Description: 移除会员相关的所有优惠信息
     * @Param TODO
     * @Return void 返回类型
     */
    public void removeAllPrivilegeForCustomer() {
        // 移除会员积分抵现
        fastFootShoppingCartVo.getmTradeVo().setIntegralCashPrivilegeVo(null);
        // 移除优惠劵
//		fastFootShoppingCartVo.getmTradeVo().setCouponPrivilege(null);
        removeAllCouponPrivilege(fastFootShoppingCartVo, false);
        //移除礼品券
        removeAllGiftPrivilege();

        // 移除会员折扣
        removeMemberPrivilege();

        // 移除tradeVo中的登录会员
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
        // 移除临时保存的会员信息
        if (fastFootShoppingCartVo.getArrayTradeCustomer() != null) {
            fastFootShoppingCartVo.getArrayTradeCustomer().remove(CustomerType.MEMBER.value());
            fastFootShoppingCartVo.getArrayTradeCustomer().remove(CustomerType.CARD.value());
        }
    }

    /**
     * @Title: removeAllGiftCoupon
     * @Description: 移除所有的礼品券
     * @Param TODO
     * @Return void 返回类型
     */
    public void removeAllGiftPrivilege() {
        for (IShopcartItem item : mergeShopcartItem(fastFootShoppingCartVo)) {
            if (item.getCouponPrivilegeVo() != null && item.getCouponPrivilegeVo().getTradePrivilege() != null) {
                item.setCouponPrivilegeVo(null);
            }
        }

        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
//			arrayListener.get(key).removeCouponPrivilege();
        }
    }

    /**
     * @param listExtraCharge TODO
     * @Title: addExtraCharge
     * @Description: 添加服务费
     * @Return void 返回类型
     */

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
        // 计算订单总价格

        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),

                fastFootShoppingCartVo.getmTradeVo());

        CheckGiftCouponIsActived(fastFootShoppingCartVo);

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).addExtraCharge(fastFootShoppingCartVo.getmTradeVo(),

                    fastFootShoppingCartVo.getmTradeVo().getExtraChargeMap());

        }

    }

    /**
     * @param id TODO 附件费ID
     * @Title: removeExtraCharge
     * @Description: 根据附加费id移除附加费
     * @Return void 返回类型
     */

    public void removeExtraCharge(Long id) {

        fastFootShoppingCartVo.getmTradeVo().getExtraChargeMap().remove(id);

        removeTradePrivilege(PrivilegeType.ADDITIONAL, fastFootShoppingCartVo.getmTradeVo());

        if (isHereOrTake()) {//如果内向或自取移动餐盒费要移除菜品上的打包标记
            Map<Long, ExtraCharge> extraChargeMap = fastFootShoppingCartVo.getmTradeVo().getExtraChargeMap();
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

        // 计算订单总价格

        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),

                fastFootShoppingCartVo.getmTradeVo());

        CheckGiftCouponIsActived(fastFootShoppingCartVo);

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).removeExtraCharge(fastFootShoppingCartVo.getmTradeVo(), id);

        }

    }

    /**
     * 设置整单备注
     *
     * @Title: setComment
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    public void setFastFoodRemarks(String remarks) {
        setRemarks(fastFootShoppingCartVo, remarks);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).setRemark(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }
    }

    /**
     * @Title: removeDishRemark
     * @Description: 移除菜品备注
     * @Param @param mShopcartItem TODO
     * @Return void 返回类型
     */
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

    /**
     * @Title: removeSetmealRemark
     * @Description: 移除子菜备注
     * @Param @param mSetmeal TODO
     * @Return void 返回类型
     */
    public void removeFastFoodSetmealRemark(SetmealShopcartItem mSetmeal) {
        removeSetmealRemark(fastFootShoppingCartVo, mSetmeal);

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeSetmealRemark(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo(),
                    mSetmeal);
        }
    }

    /**
     * @Title: removeRemark
     * @Description: 移除整单备注
     * @Param TODO
     * @Return void 返回类型
     */
    public void removeRemark() {
        fastFootShoppingCartVo.getmTradeVo().getTrade().setTradeMemo("");
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeRemark(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo(),
                    null);
        }
    }

    /**
     * @Title: createFastFoodOrder
     * @Description: 构建订单数据信息
     * @Param @param isGuaDan
     * @Param @return TODO
     * @Return TradeVo 返回类型
     */
    public TradeVo createFastFoodOrder(Boolean isGuaDan) {

        TradeVo tradeVo = createOrder(fastFootShoppingCartVo, isGuaDan);
        CreateTradeTool.updateTradeItemPrivilgeOfRelate(tradeVo, mergeShopcartItem(fastFootShoppingCartVo));

        if (isReturnCash() && mOldTradeVo != null) {//反结单删除的数据需要标记为无效

            buildReturnCashTradeVo(tradeVo);
        }

        //非挂单那么保存营销活动优惠，营销活动优惠不影响订单金额，仅用于后台统计
        //将TradeStatus为挂单状态的订单修正为已确认
        if (!isGuaDan) {
            setMarktingTradePrivilege(tradeVo);
            TradeStatus tradeStatus = tradeVo.getTrade().getTradeStatus();
            if (tradeStatus == TradeStatus.TEMPORARY) {
                tradeVo.getTrade().setTradeStatus(TradeStatus.CONFIRMED);
            }
        }
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());
        if (isReturnCash()) { //反结账的单子添加退库存
            tradeVo.inventoryVo = getInventoryVo(tradeVo);
        }
        return tradeVo;
    }

    /**
     * @Title: setTradeLog
     * @Description: 设置订单日志
     * @Return void 返回类型
     */
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
            /*PLog.d(PLog.QUICK_SERVICE_KEY, "info:快餐下单菜品数据" + "订单号:" + tradeVo.getTrade().getTradeNo()
                    + ";有效菜品个数:" + validItem + ";金额：" + validDishAmount.toString() + ";无效菜品个数：" + unValidItem + ";金额：" + unValidDishAmount);
            PLog.d(PLog.QUICK_SERVICE_KEY, "info：快餐下单优惠数据" + "订单号" + tradeVo.getTrade().getTradeNo() + ";优惠信息" + sb.toString() + ";订单金额：" + tradeVo.getTrade().getTradeAmount());*/
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void setAfterTradeLog(TradeVo tradeVo) {
        try {
            List<IShopcartItem> items = mergeShopcartItem(fastFootShoppingCartVo);
            //PLog.d(PLog.QUICK_SERVICE_KEY, "info:快餐构建订单后数据" + "订单号:" + tradeVo.getTrade().getTradeNo() + ";菜品数量：" + items.size() + ";订单金额:" + tradeVo.getTrade().getTradeAmount());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }

    /**
     * @Title: buildReturnCashTradeVo
     * @Description: 生成反结TradeVo
     * @Param @param tradeVo
     * @Return void 返回类型
     */

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
                //处理礼品劵
                if (newTradeItemVo.getCouponPrivilegeVo() == null && ordTradeItemVo.getCouponPrivilegeVo() != null) {
                    ordTradeItemVo.getCouponPrivilegeVo().getTradePrivilege().setStatusFlag(StatusFlag.INVALID);
                    ordTradeItemVo.getCouponPrivilegeVo().getTradePrivilege().setChanged(true);
                    newTradeItemVo.setCouponPrivilegeVo(ordTradeItemVo.getCouponPrivilegeVo());
                }

                // 单菜折扣
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
                //删菜
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

//        List<TradeItemVo> oldTradeItemList = mOldTradeVo.getTradeItemList();
//        for (String key : mNewTradeItemVos.keySet()) {
//            for (TradeItemVo tradeItemVo : oldTradeItemList) {
//                TradeItem tradeItem = tradeItemVo.getTradeItem();
//                String uuid = tradeItem.getUuid();
//                if (!key.equals(uuid) && !deleteTradeItemVo.contains(tradeItemVo)) {
//                    mAddTradeItems.add(mNewTradeItemVos.get(key).getTradeItem());
//                }
//            }
//        }


//        for (TradeItemVo tradeItemVo : oldTradeItemList) {
//            String oldTradeItemUuid = tradeItemVo.getTradeItem().getUuid();
//            for (String key : mNewTradeItemVos.keySet()) {
//                if (!key.equals(oldTradeItemUuid)) {
//                    mAddTradeItems.add(mNewTradeItemVos.get(key).getTradeItem());
//                }
//            }
//        }
        tradeVo.getTradeItemList().addAll(deleteTradeItemVo);

        //将所有打包标记添加
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

        //对原单，新单的打包数据处理
        if (mOldTradeVo.getTradeItemExtraList() != null) {
            if (tradeVo.getTradeItemExtraList() == null) {//新单没有，原单所有标记无效
                for (TradeItemExtra tradeItemExtra : mOldTradeVo.getTradeItemExtraList()) {
                    tradeItemExtra.setStatusFlag(StatusFlag.INVALID);
                    tradeItemExtra.setIsPack(Bool.NO);
                }
                tradeVo.setTradeItemExtraList(mOldTradeVo.getTradeItemExtraList());
            } else {//新单有，需把没有的标记为无效
                Map<String, TradeItemExtra> temp = new HashMap<String, TradeItemExtra>();
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

        //押金
        if (mOldTradeVo.getTradeDeposit() != null) {
            if (tradeVo.getTradeDeposit() != null) {//原单，新单都有，把原单信息更新了给新单
                mOldTradeVo.getTradeDeposit().setDepositPay(tradeVo.getTradeDeposit().getDepositPay());
                mOldTradeVo.getTradeDeposit().setDepositRefund(tradeVo.getTradeDeposit().getDepositRefund());
                tradeVo.setTradeDeposit(mOldTradeVo.getTradeDeposit());
            } else {//原单有，新单没有标记无效
                mOldTradeVo.getTradeDeposit().setStatusFlag(StatusFlag.INVALID);
                tradeVo.setTradeDeposit(mOldTradeVo.getTradeDeposit());
            }
        }

        //顾客
        if (mOldTradeVo.getTradeCustomerList() != null) {
            if (tradeVo.getTradeCustomerList() == null) {//原单有，新单没有，把会员标记无效
                List<TradeCustomer> temp = new ArrayList<TradeCustomer>();
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

        // 设置整单优惠和附加费
        List<TradePrivilege> oldTp = mOldTradeVo.getTradePrivileges();
        List<TradePrivilege> newTp = tradeVo.getTradePrivileges();

        if (oldTp != null) {
            // 新单
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
                        //Beans.copyProperties(sPrivilege, newTpMap.get(key));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        //营销活动
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

        // 设置积分
        if (IntegralCashPrivilegeVo.isNotNull(mOldTradeVo.getIntegralCashPrivilegeVo())) {

            if (IntegralCashPrivilegeVo.isNotNull(tradeVo.getIntegralCashPrivilegeVo())) {
                //将原单的ID给新单
                copyOnlyData(tradeVo.getIntegralCashPrivilegeVo().getTradePrivilege(), mOldTradeVo.getIntegralCashPrivilegeVo().getTradePrivilege());
                tradeVo.getIntegralCashPrivilegeVo().setTradePrivilege(mOldTradeVo.getIntegralCashPrivilegeVo().getTradePrivilege());
                tradeVo.getIntegralCashPrivilegeVo().getTradePrivilege().setStatusFlag(StatusFlag.VALID);
            } else {
                //原单的优惠设置无效给新单
                mOldTradeVo.getIntegralCashPrivilegeVo().getTradePrivilege().setStatusFlag(StatusFlag.INVALID);
                mOldTradeVo.getIntegralCashPrivilegeVo().getTradePrivilege().setChanged(true);
                tradeVo.setIntegralCashPrivilegeVo(mOldTradeVo.getIntegralCashPrivilegeVo());
            }
        }

        //整单优惠券处理
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

    /**
     * @Title: restorder
     * @Description: TODO
     * @Param @param mTradeVo
     * @Param @param listOrderDishshopVo TODO
     * @Return void 返回类型
     */
    public void restorder(TradeVo restTradeVo, List<ShopcartItem> mListOrderDishshopVo) {
        clearShoppingCart();
        fastFootShoppingCartVo = new ShoppingCartVo();

        fastFootShoppingCartVo.setmTradeVo(restTradeVo);

        // 修改挂单状态为确认状态
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

        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        CheckGiftCouponIsActived(fastFootShoppingCartVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).resetOrder(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }
    }

    /**
     * 获取整单打折信息
     *
     * @Title: getOrderDiscount
     * @Description: TODO
     * @Param @return TODO
     * @Return OrderDetailPrivilege 返回类型
     */
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

    /**
     * @Title: resetOrderDish
     * @Description: 订单回执
     * @Param @param mTradeVo TODO
     * @Return void 返回类型
     */
    public void resetOrderDish(TradeVo mTradeVo) {
        clearShoppingCart();
        List<ShopcartItem> listShopcart = CreateDishTool.tradeToDish(mTradeVo);
        fastFootShoppingCartVo.getListOrderDishshopVo().addAll(listShopcart);
        resetSelectDishQTY(fastFootShoppingCartVo);

        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo), mTradeVo);
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).resetOrder(mergeShopcartItem(fastFootShoppingCartVo), mTradeVo);
        }
    }

    /**
     * @Title: removeDinnerDish
     * @Description: 根据菜品将整个菜品（单菜、套餐、子菜）移除购车
     * @Param @param mShopcartItemBase TODO
     * @Return void 返回类型
     */
    public void removeFastFoodDish(IShopcartItemBase mShopcartItemBase) {
        removeDish(fastFootShoppingCartVo, mShopcartItemBase);
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        CheckGiftCouponIsActived(fastFootShoppingCartVo);

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeShoppingCart(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo(),
                    mShopcartItemBase);
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
    public void removeFastFoodShoppingcartItem(IShopcartItem mShopcartItem, SetmealShopcartItem mSetmealShopcartItem,
                                               ChangePageListener mChangePageListener, FragmentManager mFragmentManager) {

        //记录该菜营销活动ID
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

        // 计算订单总价格
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


    /**
     * 用于换菜操作是删除菜品
     *
     * @param mShopcartItem
     * @param mSetmealShopcartItem
     */
    public void removeFastFoodShoppingcartItemNoCheck(IShopcartItem mShopcartItem, SetmealShopcartItem mSetmealShopcartItem) {
        //记录该菜营销活动ID
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
        boolean autoMemberPrivilege;//判断是否自动带入会员优惠
        if (ruelId == -1) {
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

        if (autoMemberPrivilege) {//移除菜后，营销活动被移除需重新带入会员优惠
            memberPrivilege();
        }

        removeAllChf();

        // 计算订单总价格
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

    /**
     * @Title: updateDinnerDish
     * @Description: 修改正餐菜品信息
     * @Param @param mShopcartItemBase
     * @Param @param isTempDish TODO
     * @Return void 返回类型
     */
    public void updateFastFoodDish(IShopcartItemBase mShopcartItemBase, Boolean isTempDish) {
        /*updateDish(fastFootShoppingCartVo, mShopcartItemBase, isTempDish);
        // 计算订单总价格
		MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
			fastFootShoppingCartVo.getmTradeVo());
		resetSelectDishQTY(fastFootShoppingCartVo);*/

        IShopcartItem value = getShopcartItemFromList(fastFootShoppingCartVo, mShopcartItemBase.getUuid());

        if (value != null && value.getPrivilege() != null
                && value.getCouponPrivilegeVo() != null
                && value.getCouponPrivilegeVo().getTradePrivilege() != null) {
            value.setCouponPrivilegeVo(null);
        }

        if (mShopcartItemBase.getPrivilege() != null
                && (mShopcartItemBase.getPrivilege().getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT
                || mShopcartItemBase.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_PRICE)) {
            setDishMemberPrivilege(fastFootShoppingCartVo, mShopcartItemBase, false);
        } else if ((value == null || mShopcartItemBase.getIsChangedPrice() == Bool.NO) && mShopcartItemBase.getPrivilege() == null) {
            setDishMemberPrivilege(fastFootShoppingCartVo, mShopcartItemBase, false);
        }

        updateDish(fastFootShoppingCartVo, mShopcartItemBase, isTempDish);
        resetSelectDishQTY(fastFootShoppingCartVo);
        List<IShopcartItem> allIttem = mergeShopcartItem(fastFootShoppingCartVo);

        autoAddSalesPromotion(fastFootShoppingCartVo);
        // 计算订单总价格

        removeAllChf();

        MathShoppingCartTool.mathTotalPrice(allIttem, fastFootShoppingCartVo.getmTradeVo());

        CheckGiftCouponIsActived(fastFootShoppingCartVo);

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }

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
        return mergeShopcartItem(fastFootShoppingCartVo);
    }

    public ShopcartItem getTempShopItem() {
        return fastFootShoppingCartVo.getTempShopItem();
    }

    public void setTempShopItem(ShopcartItem tempShopItem) {
        fastFootShoppingCartVo.setTempShopItem(tempShopItem);
    }

    /**
     * 获取订单数据
     */
    public TradeVo getOrder() {
        return fastFootShoppingCartVo.getmTradeVo();
    }

    public void setIsSalesReturn(Boolean isSalesReturn) {
        checkNeedBuildMainOrder(fastFootShoppingCartVo.getmTradeVo());
        fastFootShoppingCartVo.getmTradeVo().setIsSalesReturn(isSalesReturn);
    }

    /**
     * @Title: getIsSalesReturn
     * @Description: 获取判断当前订单是否是无单退货
     * @Param @return TODO
     * @Return Boolean 返回类型
     */
    @Override
    public Boolean getIsSalesReturn() {
        return fastFootShoppingCartVo.getmTradeVo().getIsSalesReturn();
    }

    public ShoppingCartVo getShoppingCartVo() {
        return fastFootShoppingCartVo;
    }

    /**
     * @Title: getDeliveryType
     * @Description: 获取票据类型
     * @Param @return TODO
     * @Return DeliveryType 返回类型
     */
    public DeliveryType getDeliveryType() {
        return fastFootShoppingCartVo.getmTradeVo().getTrade().getDeliveryType();
    }

    /**
     * @Title: getAddTradeItems
     * @Description: 获取加菜数据
     * @Param @return @TODO
     * @Return ArrayList<TradeItem> 返回类型
     */
    public ArrayList<TradeItem> getAddTradeItems() {
        return mAddTradeItems;
    }

    /**
     * @Title: getDeleteTradeItems
     * @Description: 获取删菜数据
     * @Param @return @TODO
     * @Return ArrayList<TradeItem> 返回类型
     */
    public ArrayList<TradeItem> getDeleteTradeItems() {
        return mDeleteTradeItems;
    }

    public List<IShopcartItem> getReduceItems() {
        return mReduceItems;
    }

    public List<IShopcartItem> getAddChangeItems() {
        return mAddChangeItems;
    }

    /**
     * @Title: removeAll
     * @Description: 移除所有估清商品
     * @Param TODO
     * @Return void 返回类型
     */
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

    /**
     * @Title: haveGuQingProduct
     * @Description: 购物车中是否含有已估清菜品
     * @Param @return TODO
     * @Return Boolean 返回类型
     */
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

    /**
     * @Title: addTradeDeposit
     * @Description: 根据人数添加押金
     * @Param peopleCount 就餐人数
     * @Param money 人均金额
     * @Return void 返回类型
     */
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
            // 计算订单总价格
            MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).updateShoppingcartData();
            }
        }
    }

    /**
     * @Title: addTradeDeposit
     * @Description: 根据订单金额添加押金
     * @Param value TODO
     * @Return void 返回类型
     */
    private void addTradeDeposit(BigDecimal value) {

    }

    /**
     * @Title: addTradeDeposit
     * @Description: 添加押金
     * @Param @param mDepositInfo TODO
     * @Return void 返回类型
     */
    public void addTradeDeposit(DepositInfo mDepositInfo) {
        if (mDepositInfo != null) {
            BigDecimal depositPay = BigDecimal.ZERO;
            switch (mDepositInfo.getType()) {
                case 1://按人计算押金(默认)
                    Integer peopleCount = fastFootShoppingCartVo.getmTradeVo().getTrade().getTradePeopleCount();
                    if (peopleCount != null && mDepositInfo.getValue() != null) {
                        depositPay = mDepositInfo.getValue().multiply(new BigDecimal(peopleCount));
                    }
                    break;

                case 2://按订单算押金
                    if (mDepositInfo.getValue() != null) {
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
            // 计算订单总价格
            MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).updateShoppingcartData();
            }

        }
    }

    /**
     * @Title: removeTradeDeposit
     * @Description: 移除订单押金
     * @Param TODO
     * @Return void 返回类型
     */
    public void removeTradeDeposit() {
        fastFootShoppingCartVo.getmTradeVo().setTradeDeposit(null);
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateShoppingcartData();
        }
    }

    /**
     * 清空购物车
     *
     * @Title: clearShoppingCart
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    public void clearShoppingCart() {
        CustomerManager.getInstance().setLoginCustomer(null);
        CustomerManager.getInstance().setAccounts(null);
        fastFootShoppingCartVo = new ShoppingCartVo();
        mOldTradeVo = null;

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).clearShoppingCart();
        }
    }

    /**
     * 判断订单中是否包含此类优惠类型
     *
     * @param mPrivilegeType
     * @return true:包含 false:不包含
     */
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
                fastFootShoppingCartVo.getmTradeVo(),
                marketDishVo,
                false)) {
            return false;
        }
        //移除宴请
        removeBanquetOnly(fastFootShoppingCartVo.getmTradeVo());
        MathManualMarketTool.mathManualAddMarket(selectedItemList,
                fastFootShoppingCartVo.getmTradeVo(),
                marketDishVo,
                false);

        //如果选择的菜品有礼品券优惠移除
        removeGiftCouponPrivilege(selectedItemList, fastFootShoppingCartVo);

        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).addMarketActivity(fastFootShoppingCartVo.getmTradeVo());
        }
        return true;
    }

    /**
     * @Title: checkMarketActivity
     * @Description: 用于会员退出时校验营销活动是否有效
     */
    public void checkMarketActivity() {
        if (fastFootShoppingCartVo.getListIShopcatItem() == null)
            return;


        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeMarketActivity(fastFootShoppingCartVo.getmTradeVo());
        }
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

    /**
     * 获取订单中所有菜品
     *
     * @Title: getShoppingCartDish
     * @Description: TODO
     * @Param @return TODO
     * @Return List<Dish_Order_Entity> 返回类型
     */
    public List<IShopcartItem> getShoppingCartItems() {
        return mergeShopcartItem(fastFootShoppingCartVo);
    }

    /**
     * 将反结账返回的TradeVo转换成ShoppingCart需要的对象
     *
     * @Title: resetOrderFromOrderCenter
     * @Description: TODO
     * @Param @return TODO
     * @Return 返回类型
     */
    public void resetOrderFromOrderCenter(TradeVo tradeVo, List<Tables> tables) {
        clearShoppingCart();

        mOldTradeVo = tradeVo.clone();

        fastFootShoppingCartVo = new ShoppingCartVo();

        fastFootShoppingCartVo.setmTradeVo(tradeVo);

        fastFootShoppingCartVo.getmTradeVo().setmWeiXinCouponsVo(null);

        fastFootShoppingCartVo.setmTables(tables);

        if (tradeVo.getTrade() != null
                && (tradeVo.getTrade().getDeliveryType() == DeliveryType.TAKE
                || tradeVo.getTrade().getDeliveryType() == DeliveryType.SEND)) {//外送或自取需转换收货人信息
            toTakeoutInfo(tradeVo);
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

        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }
    }

    /**
     * @Title: toTakeoutInfo
     * @Description: TradeExtra转成TakeOutInfo
     * @Param @return TODO
     * @Return void 返回类型
     */

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

            //因为下单时会根据收货人信息创建顾客信息，故移除
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

    /**
     * @Title: isReturnCash
     * @Description: 判断当前订单是否是反结账单
     * @Param @return TODO
     * @Return Boolean 返回类型
     */
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

    /**
     * @Title: isHereOrTake
     * @Description: 判断当前票据类型是否内用和自取
     * @Param @return TODO
     * @Return Boolean 返回类型
     */
    public boolean isHereOrTake() {
        if (fastFootShoppingCartVo.getmTradeVo() != null && fastFootShoppingCartVo.getmTradeVo().getTrade() != null) {
            if (fastFootShoppingCartVo.getmTradeVo().getTrade().getDeliveryType() == DeliveryType.HERE ||
                    fastFootShoppingCartVo.getmTradeVo().getTrade().getDeliveryType() == DeliveryType.TAKE) {
                return true;
            }

        }
        return false;
    }

    /**
     * @Title: haveExtraChargeChf
     * @Description: 判断是否已经添加餐盒费
     * @Param @return TODO
     * @Return Boolean 返回类型
     */
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

    /**
     * @Title: resetPack
     * @Description: 重置菜品打包标记
     * @Param @return TODO
     * @Return 返回类型
     */
    public void resetPack() {

        for (IShopcartItem item : mergeShopcartItem(fastFootShoppingCartVo)) {
            item.setPack(false);
        }

        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                fastFootShoppingCartVo.getmTradeVo());

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).updateDish(mergeShopcartItem(fastFootShoppingCartVo),
                    fastFootShoppingCartVo.getmTradeVo());
        }

    }

    /**
     * @Title: removeAllChf
     * @Description: 判断是否要移除餐盒费
     * @Param @return TODO
     * @Return 返回类型
     */

    public void removeAllChf() {
        if (isHereOrTake()) {
            int count = 0;
            for (IShopcartItem item : mergeShopcartItem(fastFootShoppingCartVo)) {
                if (item.getPack() && item.getStatusFlag() == StatusFlag.VALID) {
                    count++;
                }
            }
            if (count == 0) {//没有一个菜品打包移除餐盒费
                if (fastFootShoppingCartVo.getmTradeVo() != null) {
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

    /**
     * @Title: removeAllExtraChage
     * @Description: 移除所有餐盒费
     * @Param @return TODO
     * @Return 返回类型
     */

    public void removeAllExtraChage() {
        if (fastFootShoppingCartVo.getmTradeVo() != null) {
            Map<Long, ExtraCharge> extraChargeMap = fastFootShoppingCartVo.getmTradeVo().getExtraChargeMap();
            if (extraChargeMap != null) {

                extraChargeMap.clear();
                removeTradePrivilege(PrivilegeType.ADDITIONAL, fastFootShoppingCartVo.getmTradeVo());

                // 计算订单总价格
                MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(fastFootShoppingCartVo),
                        fastFootShoppingCartVo.getmTradeVo());

                for (int key : arrayListener.keySet()) {

                    arrayListener.get(key).updateDish(mergeShopcartItem(fastFootShoppingCartVo), fastFootShoppingCartVo.getmTradeVo());

                }
            }
        }
    }

    /**
     * @param couponPrivilegeVo
     * @Title: isAllowAddCoupon
     * @Description: 是否允许加入整单的优惠劵
     * @Return true  允许，false 不允许
     */
    public boolean isAllowAddCoupon(CouponPrivilegeVo couponPrivilegeVo) {
        return isAllowAddCoupon(fastFootShoppingCartVo, couponPrivilegeVo);
    }

    /**
     * @param couponPrivilegeVo
     * @param isNeedListener
     * @Title: removeCouponPrivilege
     * @Description: 移除单张优惠券
     */
    public void removeCouponPrivilege(CouponPrivilegeVo couponPrivilegeVo, boolean isNeedListener) {
        removeCouponPrivilege(fastFootShoppingCartVo, couponPrivilegeVo, isNeedListener);
    }

    /**
     * @param openId
     * @Title: setOpenIdenty
     * @Description: 设置openId
     */
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
        // 如果是已下单菜品删除则只需修改菜品状态为无效
        if (mShopcartItem.getId() != null) {
            removeReadonlyShopcartItem(mShoppingCartVo, (ReadonlyShopcartItem) mShopcartItem);
            return;
        }

        // 表示删除的是整个套餐或单菜
        if (mSetmealShopcartItem == null) {
            removeDish(mShoppingCartVo, mShopcartItem);
            // 判断删除的是当前正在操作的菜品
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
            // 删除的是套餐子菜
            switch (mShopcartItem.getSetmealManager().testModify(mSetmealShopcartItem, BigDecimal.ZERO)) {
                case SUCCESSFUL:
                    removeDish(mShoppingCartVo, mSetmealShopcartItem);
                    mShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);
                    // 如果当前打开的删除子菜的属性界面则需要跳转
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
                    // 删除的子菜正处于当前操作套餐 并且不处在删除子菜列表界面
                    if (mShoppingCartVo.getTempShopItem() != null
                            && mShopcartItem.getUuid().equals(mShoppingCartVo.getTempShopItem().getUuid())
                            && mShoppingCartVo.getIndexPage() == ChangePageListener.DISHCOMBO) {
                        removeDish(mShoppingCartVo, mSetmealShopcartItem);
                        mShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);

                    } else if (mShoppingCartVo.getTempShopItem() != null && mShopcartItem != null
                            && mShopcartItem.getUuid().equals(mShoppingCartVo.getTempShopItem().getUuid())
                            && mShoppingCartVo.getIndexPage() == ChangePageListener.DISHPROPERTY
                            && mShoppingCartVo.getShowPropertyPageDishUUID().equals(mSetmealShopcartItem.getUuid())) {
                        // 删除的子菜正处于当前操作套餐 并且不处在删除子菜属性界面
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
                        // 删除的子菜正处于当前操作套餐，并且当前所处属性界面不是删除子菜属性界面
                        removeDish(mShoppingCartVo, mSetmealShopcartItem);
                        mShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);

                    } else if (mShoppingCartVo.getTempShopItem() != null
                            && !mShopcartItem.getUuid().equals(mShoppingCartVo.getTempShopItem().getUuid())) {
                        // 删除的子菜不是当前正在操作的菜品
                        isCheckVaild(mShoppingCartVo,
                                ChangePageListener.DISHCOMBO,
                                mChangePageListener,
                                mFragmentManager,
                                mShopcartItem,
                                mSetmealShopcartItem);
                    } else {
                        // 当前没有操作菜品
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
        // 操作菜品跟当前菜品不是同一个菜，并且当前操作菜品不满足条件
        if (mShoppingCartVo.getTempShopItem() != null && mShoppingCartVo.getTempShopItem().getSetmealManager() != null
                && !mShoppingCartVo.getTempShopItem().getSetmealManager().isValid()) {
            // 当前选择套餐满未满足套餐选择规则则填出对话框 确认是否离开此界面
            showCheckDialog(mShoppingCartVo,
                    mPageNo,
                    mChangePageListener,
                    mFragmentManager,
                    mShopcartItem,
                    mSetmealShopcartItem);

        } else {
            // 当前操作菜品满足规则。则跳转到因删除导致不满足套餐规则的菜品中
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

                        // 删除菜品
                        if (mSetmealShopcartItem != null) {
                            ShopcartItem deleteShopcartItem =
                                    getShopcartItemByUUID(mShoppingCartVo, mSetmealShopcartItem.getParentUuid());
                            deleteShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);
                            removeDish(mShoppingCartVo, mSetmealShopcartItem);
                        }

                        // 移除临时未完成套餐
                        if (mShoppingCartVo.getTempShopItem() != null) {
                            isCheckAdd(mShoppingCartVo, mShoppingCartVo.getTempShopItem(), false);
                            mShoppingCartVo.setTempShopItem(null);
                        }

                        // 调整界面
                        if (mChangePageListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID,
                                    mShopcartItem != null ? mShopcartItem.getUuid() : "");
                            bundle.putInt(Constant.EXTRA_LAST_PAGE, SettingManager.getSettings(IPanelItemSettings.class).getOrderHomePage());
                            bundle.putBoolean(Constant.NONEEDCHECK, true);
                            mChangePageListener.changePage(mPageNo, bundle);
                        }

                        // 计算订单总价格
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
                item.setCheckStatus(DishDataItem.DishCheckStatus.NOT_CHECK);
            } else {
                item.setCheckStatus(DishDataItem.DishCheckStatus.INVALIATE_CHECK);
            }
        }
    }


    /**
     * 添加促销活动到购物车
     *
     * @param salesPromotionRuleVo  指定促销活动
     * @param selectedShopcartItems 选择的购物车条目
     */
    public void addSalesPromotion(SalesPromotionRuleVo salesPromotionRuleVo, List<IShopcartItem> selectedShopcartItems) {
        addSalesPromotion(fastFootShoppingCartVo, salesPromotionRuleVo, selectedShopcartItems, CustomerManager.getInstance().getLoginCustomer());
    }

    /**
     * 从购物车删除促销活动
     *
     * @param planId 促销活动方案Id
     */
    public void removeSalesPromotion(Long planId) {
        removeSalesPromotion(fastFootShoppingCartVo, planId, CustomerManager.getInstance().getLoginCustomer(), false);
    }

    /**
     * 从购物车删除促销活动
     *
     * @param tradePlanUuid TradePlanActivity的UUID
     */
    public void removeSalesPromotion(String tradePlanUuid) {
        removeSalesPromotion(fastFootShoppingCartVo, tradePlanUuid, CustomerManager.getInstance().getLoginCustomer(), false);
    }
}
