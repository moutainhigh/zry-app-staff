package com.zhongmei.bty.basemodule.shoppingcart;

import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.bty.basemodule.discount.bean.BanquetVo;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsVo;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.bty.basemodule.shoppingcart.bean.ShoppingCartVo;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathManualMarketTool;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShoppingCartTool;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradeType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class SeparateShoppingCart extends DinnerShoppingCart {

    private static final String TAG = SeparateShoppingCart.class.getSimpleName();

    private static SeparateShoppingCart instance = null;



    public static SeparateShoppingCart getInstance() {
        synchronized (SeparateShoppingCart.class) {
            if (instance == null) {
                instance = new SeparateShoppingCart();
            }
        }

        return instance;
    }



    public void batchAddShoppingCart(TradeVo mTradeVo, List<IShopcartItem> listShopcartItem) {
        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());
        checkSeparateOrder();
        for (IShopcartItem item : listShopcartItem) {

            if (item instanceof ReadonlyShopcartItem) {

                dinnerShoppingCartVo.getListIShopcatItem().add(item);

            } else {

                dinnerShoppingCartVo.getListOrderDishshopVo().add((ShopcartItem) item);

            }

        }
        List<IShopcartItem> tempShopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);

        MathShoppingCartTool.mathTotalPrice(tempShopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());
                try {
            for (int key : arrayListener.keySet()) {

                arrayListener.get(key).separateOrder(tempShopcartItemList,

                        dinnerShoppingCartVo.getmTradeVo());

            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }



    public void addShoppingCart(IShopcartItem mShopcartItem, boolean isNeedListener) {

        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());
        checkSeparateOrder();

        if (mShopcartItem instanceof ReadonlyShopcartItem) {

            dinnerShoppingCartVo.getListIShopcatItem().add(mShopcartItem);

        } else {

            dinnerShoppingCartVo.getListOrderDishshopVo().add((ShopcartItem) mShopcartItem);

        }
        List<IShopcartItem> tempShopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
                memberPrivilege(mShopcartItem, false, false);
        MathShoppingCartTool.mathTotalPrice(tempShopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());
        CheckGiftCouponIsActived(dinnerShoppingCartVo);
        if (isNeedListener) {
            try {
                for (int key : arrayListener.keySet()) {

                    arrayListener.get(key).separateOrder(tempShopcartItemList,

                            dinnerShoppingCartVo.getmTradeVo());

                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    public void removeShoppingCart(IShopcartItem mShopcartItem) {
        removeShoppingCart(mShopcartItem, true);
    }



    public void removeShoppingCart(IShopcartItem mShopcartItem, boolean isNeedListener) {

        List<IShopcartItem> listItem = dinnerShoppingCartVo.getListIShopcatItem();

        for (int i = listItem.size() - 1; i >= 0; i--) {

            IShopcartItem item = listItem.get(i);

            if (item.getUuid().equals(mShopcartItem.getUuid())) {

                listItem.remove(i);

                break;

            }

        }

        List<ShopcartItem> listDishItem = dinnerShoppingCartVo.getListOrderDishshopVo();

        for (int i = listDishItem.size() - 1; i >= 0; i--) {

            ShopcartItem item = listDishItem.get(i);

            if (item.getUuid().equals(mShopcartItem.getUuid())) {

                listDishItem.remove(i);

                break;

            }

        }

        checkSeparateOrder();

        MathManualMarketTool.removeShopcartItem(dinnerShoppingCartVo.getmTradeVo(),
                mergeShopcartItem(dinnerShoppingCartVo),
                mShopcartItem,
                true,
                true);
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);

        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());
        CheckGiftCouponIsActived(dinnerShoppingCartVo);
        if (isNeedListener) {
            for (int key : arrayListener.keySet()) {

                arrayListener.get(key).removeShoppingCart(shopcartItemList,

                        dinnerShoppingCartVo.getmTradeVo(), mShopcartItem);

            }
        }

    }

        private void checkSeparateOrder() {
        DinnerShoppingCart dinnerShoppCart = DinnerShoppingCart.getInstance();
        if (isAllOrder(mergeShopcartItem(dinnerShoppCart.getShoppingCartVo()))) {
            dinnerShoppingCartVo.getmTradeVo().getTrade().setTradePeopleCount(
                    dinnerShoppCart.getShoppingCartVo().getmTradeVo().getTrade().getTradePeopleCount());
        } else {
            dinnerShoppingCartVo.getmTradeVo().getTrade().setTradePeopleCount(0);
        }

    }




    public void setSeparateCustomer(TradeCustomer mTradeCustomer) {
        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());

        setCustomer(dinnerShoppingCartVo, mTradeCustomer);

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).setCustomer(mTradeCustomer);

        }

    }



    public TradeVo createSeparateOrder() {

        DinnerShoppingCart dinnerShoppCart = DinnerShoppingCart.getInstance();

                List<IShopcartItem> splitItemList = mergeShopcartItem(dinnerShoppingCartVo);

                TradeVo mTradeVo = createOrder(dinnerShoppingCartVo, false);

        mTradeVo.getTrade().setBusinessType(BusinessType.DINNER);

        mTradeVo.getTrade().setDeliveryType(DeliveryType.HERE);

        mTradeVo.getTrade().setTradeType(TradeType.SPLIT);

        mTradeVo.getTrade().setRelateTradeId(dinnerShoppCart.getShoppingCartVo().getmTradeVo().getTrade().getId());

        mTradeVo.getTrade()

                .setRelateTradeUuid(dinnerShoppCart.getShoppingCartVo().getmTradeVo().getTrade().getUuid());

        ShopcartItemUtils.completeTradeTableOfSplit(dinnerShoppingCartVo.getmTradeVo(),
                dinnerShoppCart.getShoppingCartVo().getmTradeVo().getTradeTableList());
        validateSplitWeixinCoupon();
                setMarktingTradePrivilege(mTradeVo);

                MathShoppingCartTool.mathTotalPrice(splitItemList, dinnerShoppingCartVo.getmTradeVo());
                if (dinnerShoppCart.getTradeUser() != null) {
            mTradeVo.setTradeUser(ShopcartItemUtils.copyTradeUser(dinnerShoppCart.getTradeUser()));
        }
        return mTradeVo;

    }

        public void updateDataFromTradeVo(TradeVo tradeVo) {
        this.updateDataFromTradeVo(tradeVo, false);
    }

    public void updateDataWithTrade(Trade trade) {        if (trade != null && this.dinnerShoppingCartVo != null && this.dinnerShoppingCartVo.getmTradeVo() != null) {
            this.dinnerShoppingCartVo.getmTradeVo().setTrade(trade);
        }
    }

    public void updateDataFromTradeVo(TradeVo tradeVo, Boolean isCallback) {
        if (dinnerShoppingCartVo == null || tradeVo == null) {
            return;
        }
        DinnertableTradeInfo dinnertableTradeInfo = dinnerShoppingCartVo.getDinnertableTradeInfo();
        if (dinnertableTradeInfo == null) {
            dinnertableTradeInfo = new DinnertableTradeInfo(tradeVo);
        }
        dinnertableTradeInfo.setTradeVo(tradeVo);
        resetOrderFromTable(dinnertableTradeInfo, isCallback);
    }


    public void updateDataFromDB(String tradeUuid) {
        if (dinnerShoppingCartVo == null) {
            return;
        }
        DinnertableTradeInfo dinnertableTradeInfo = dinnerShoppingCartVo.getDinnertableTradeInfo();
        TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
        try {
            if (TextUtils.isEmpty(tradeUuid)) {
                tradeUuid = getOrder().getTrade().getUuid();
            }
            TradeVo tradeVo = tradeDal.findTrade(tradeUuid, false);
            if (dinnertableTradeInfo == null) {
                dinnertableTradeInfo = new DinnertableTradeInfo(tradeVo);
            }
            dinnertableTradeInfo.setTradeVo(tradeVo);
            resetOrderFromTable(dinnertableTradeInfo, true);
        } catch (Exception e) {
            Log.e(SeparateShoppingCart.class.getSimpleName(), "", e);
        }

    }


    private void copyBanquet(TradeVo sourceTradeVo, TradeVo splitTradeVo) {

        if (BanquetVo.isNotNull(splitTradeVo.getBanquetVo())) {

            BanquetVo privilegeVo = splitTradeVo.getBanquetVo().clone();

            if (BanquetVo.isNotNull(sourceTradeVo.getBanquetVo())) {


                TradePrivilege tp = sourceTradeVo.getBanquetVo().getTradePrivilege();

                copyOnlyData(privilegeVo.getTradePrivilege(), tp);

                privilegeVo.setTradePrivilege(tp);

            } else {


                privilegeVo.getTradePrivilege().setTradeId(sourceTradeVo.getTrade().getId());

                privilegeVo.getTradePrivilege().setTradeUuid(sourceTradeVo.getTrade().getUuid());

            }

            sourceTradeVo.setBanquetVo(privilegeVo);

        } else if (BanquetVo.isNotNull(sourceTradeVo.getBanquetVo())) {


            TradePrivilege tp = sourceTradeVo.getBanquetVo().getTradePrivilege();

            tp.setStatusFlag(StatusFlag.INVALID);
            tp.setChanged(true);
        }
    }


    private void validateSplitWeixinCoupon() {
                List<WeiXinCouponsVo> nListWX = SeparateShoppingCart.getInstance().getOrder().getmWeiXinCouponsVo();
        List<WeiXinCouponsVo> oListWX = DinnerShoppingCart.getInstance().getOrder().getmWeiXinCouponsVo();
        if (nListWX == null || oListWX == null) {
            return;
        }
        Map<Long, WeiXinCouponsVo> tempOwx = new HashMap<Long, WeiXinCouponsVo>();
        for (WeiXinCouponsVo wx : oListWX) {
            tempOwx.put(wx.getmTradePrivilege().getPromoId(), wx);
        }

        for (WeiXinCouponsVo nwx : nListWX) {
                        WeiXinCouponsVo oldWX = tempOwx.get(nwx.getmTradePrivilege().getPromoId());
            if (oldWX == null) {
                continue;
            }
            if (oldWX.getmTradePrivilege() != null) {
                oldWX.getmTradePrivilege().setStatusFlag(StatusFlag.INVALID);
                oldWX.getmTradePrivilege().setChanged(true);
            }

        }
    }


    private void copyPalActivity(TradeVo tradeVo, TradeVo splitTradeVo) {
        List<TradePlanActivity> oldPlanActivity = tradeVo.getTradePlanActivityList();

        List<TradePlanActivity> newPlanActivity = splitTradeVo.getTradePlanActivityList();


                if ((oldPlanActivity == null || oldPlanActivity.size() == 0) && newPlanActivity != null) {

            List<TradePlanActivity> tempPlanActivity = new ArrayList<TradePlanActivity>();
            for (TradePlanActivity planActivity : newPlanActivity) {

                TradePlanActivity mTradePlanActivity = new TradePlanActivity();
                mTradePlanActivity = planActivity.copyTradePlanActivity(planActivity, mTradePlanActivity);

                mTradePlanActivity.setTradeId(tradeVo.getTrade().getId());
                mTradePlanActivity.setTradeUuid(tradeVo.getTrade().getUuid());

                mTradePlanActivity.setChanged(true);
                tempPlanActivity.add(mTradePlanActivity);
            }
            tradeVo.setTradePlanActivityList(tempPlanActivity);
        } else if (oldPlanActivity != null && newPlanActivity != null) {            Map<String, TradePlanActivity> tempOldPlan = new HashMap<String, TradePlanActivity>();
                        for (TradePlanActivity mPlan : oldPlanActivity) {
                tempOldPlan.put(mPlan.getUuid(), mPlan);
            }
            List<TradePlanActivity> oldPlanList = new ArrayList<TradePlanActivity>();
            for (TradePlanActivity mPlan : newPlanActivity) {
                TradePlanActivity oldPlan = tempOldPlan.get(mPlan.getUuid());

                if (oldPlan != null) {                    oldPlan.setOfferValue(mPlan.getOfferValue());
                    oldPlan.setPlanUsageCount(mPlan.getPlanUsageCount());
                    oldPlan.setClientUpdateTime(mPlan.getClientCreateTime());
                    oldPlan.setStatusFlag(mPlan.getStatusFlag());
                    oldPlan.setChanged(true);
                    oldPlanList.add(oldPlan);
                                        tempOldPlan.remove(mPlan.getUuid());
                } else {                    mPlan.setTradeId(tradeVo.getTrade().getId());
                    mPlan.setTradeUuid(tradeVo.getTrade().getUuid());
                    mPlan.setChanged(true);
                    oldPlanList.add(mPlan);
                }
            }
                        if (tempOldPlan.size() != 0) {
                for (String key : tempOldPlan.keySet()) {
                    TradePlanActivity mTradePlanActivity = tempOldPlan.get(key);
                    mTradePlanActivity.setStatusFlag(StatusFlag.INVALID);
                    mTradePlanActivity.setChanged(true);
                    oldPlanList.add(mTradePlanActivity);
                }
            }

            tradeVo.setTradePlanActivityList(oldPlanList);

        } else if (oldPlanActivity != null && newPlanActivity == null) {                        for (TradePlanActivity mTradePlanActivity : oldPlanActivity) {
                mTradePlanActivity.setStatusFlag(StatusFlag.INVALID);
                mTradePlanActivity.setChanged(true);
            }

        }


        List<TradeItemPlanActivity> oldItenPlanActivity = tradeVo.getTradeItemPlanActivityList();
        List<TradeItemPlanActivity> newItenPlanActivity = splitTradeVo.getTradeItemPlanActivityList();

        Map<String, IShopcartItem> separateMap = new HashMap<String, IShopcartItem>();

        List<IShopcartItem> listShopcartItems = mergeShopcartItem(dinnerShoppingCartVo);
        if (listShopcartItems != null) {
            for (IShopcartItem mIShopcartItem : listShopcartItems) {
                separateMap.put(mIShopcartItem.getUuid(), mIShopcartItem);
            }
        }

                if (oldItenPlanActivity == null && newItenPlanActivity != null) {
            List<TradeItemPlanActivity> tempItenPlanActivity = new ArrayList<TradeItemPlanActivity>();
            for (TradeItemPlanActivity itemPlanItemActivity : newItenPlanActivity) {

                TradeItemPlanActivity mTradeItemPlanActivity = new TradeItemPlanActivity();
                mTradeItemPlanActivity =
                        itemPlanItemActivity.copyTradeItemPlanActivity(itemPlanItemActivity, mTradeItemPlanActivity);

                IShopcartItem mIShopcartItem = separateMap.get(itemPlanItemActivity.getTradeItemUuid());
                mTradeItemPlanActivity.setTradeId(tradeVo.getTrade().getId());
                mTradeItemPlanActivity.setTradeUuid(tradeVo.getTrade().getUuid());
                                                                mTradeItemPlanActivity.setTradeItemId(mIShopcartItem.getRelateTradeItemId());
                mTradeItemPlanActivity.setTradeItemUuid(mIShopcartItem.getRelateTradeItemUuid());

                mTradeItemPlanActivity.setChanged(true);
                tempItenPlanActivity.add(mTradeItemPlanActivity);
            }
            tradeVo.setTradeItemPlanActivityList(tempItenPlanActivity);

        } else if (oldItenPlanActivity != null && newItenPlanActivity != null) {
            Map<String, TradeItemPlanActivity> tempPlanMap = new HashMap<String, TradeItemPlanActivity>();

            List<TradeItemPlanActivity> tempItemPlanActivity = new ArrayList<TradeItemPlanActivity>();

            for (TradeItemPlanActivity oItemPlanActivity : oldItenPlanActivity) {
                tempPlanMap.put(oItemPlanActivity.getTradeItemUuid(), oItemPlanActivity);
            }

            for (TradeItemPlanActivity nItemPlanActivity : newItenPlanActivity) {
                if (nItemPlanActivity.getStatusFlag() == StatusFlag.INVALID) {
                                        tempItemPlanActivity.add(nItemPlanActivity);
                    continue;
                }
                IShopcartItem newIShopcartItem = separateMap.get(nItemPlanActivity.getTradeItemUuid());
                if (newIShopcartItem == null) {
                    continue;
                }

                                                                TradeItemPlanActivity planActivity = tempPlanMap.get(newIShopcartItem.getRelateTradeItemUuid());

                if (planActivity != null) {                    planActivity = planActivity.copyTradeItemPlanActivity(nItemPlanActivity, planActivity);
                    planActivity.setTradeId(tradeVo.getTrade().getId());
                    planActivity.setTradeUuid(tradeVo.getTrade().getUuid());
                                                                                planActivity.setTradeItemId(newIShopcartItem.getRelateTradeItemId());
                    planActivity.setTradeItemUuid(newIShopcartItem.getRelateTradeItemUuid());

                    planActivity.setChanged(true);
                    tempItemPlanActivity.add(planActivity);
                    tempPlanMap.remove(nItemPlanActivity.getTradeItemUuid());
                } else {                    TradeItemPlanActivity mTradeItemPlanActivity = new TradeItemPlanActivity();
                    mTradeItemPlanActivity =
                            nItemPlanActivity.copyTradeItemPlanActivity(nItemPlanActivity, mTradeItemPlanActivity);

                    mTradeItemPlanActivity.setTradeId(tradeVo.getTrade().getId());
                    mTradeItemPlanActivity.setTradeUuid(tradeVo.getTrade().getUuid());
                    mTradeItemPlanActivity.setTradeItemId(newIShopcartItem.getRelateTradeItemId());
                    mTradeItemPlanActivity.setTradeItemUuid(newIShopcartItem.getRelateTradeItemUuid());

                    mTradeItemPlanActivity.setChanged(true);
                    tempItemPlanActivity.add(mTradeItemPlanActivity);
                }
            }

            tradeVo.setTradeItemPlanActivityList(tempItemPlanActivity);

        } else if (oldItenPlanActivity != null && newItenPlanActivity == null) {            for (TradeItemPlanActivity itemPlanActivity : oldItenPlanActivity) {
                itemPlanActivity.setStatusFlag(StatusFlag.INVALID);
                itemPlanActivity.setChanged(true);
            }
        }

    }




    public boolean isAllOrder(List<IShopcartItem> listShopcartItem) {

        List<IShopcartItem> splitItemList = mergeShopcartItem(dinnerShoppingCartVo);
        return isAllOrder(listShopcartItem, splitItemList);
    }


    public boolean isAllOrder(List<IShopcartItem> listShopcartItem, List<IShopcartItem> splitItemList) {
        if (splitItemList.size() == listShopcartItem.size()) {
            return true;

        }
        for (IShopcartItem item : listShopcartItem) {



            if (item.getStatusFlag() == StatusFlag.VALID && item.getTotalQty().compareTo(BigDecimal.ZERO) != 0) {


                return false;

            }

        }

        return true;
    }



    public TradeVo getOrder() {
        return dinnerShoppingCartVo.getmTradeVo();
    }

    public ShoppingCartVo getShoppingCartVo() {

        return dinnerShoppingCartVo;

    }



    public void clearShoppingCart() {

        dinnerShoppingCartVo = new ShoppingCartVo();

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).clearShoppingCart();

        }

    }

    public void clearShoppingCartData() {
        dinnerShoppingCartVo = new ShoppingCartVo();
    }



    public List<IShopcartItem> getShoppingCartItems() {
        return mergeShopcartItem(dinnerShoppingCartVo);
    }
}
