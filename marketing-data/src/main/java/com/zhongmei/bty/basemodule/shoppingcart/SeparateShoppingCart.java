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

/**
 * @Date：2015年10月30日 上午9:20:49
 * @Description: 拆分订单对应的购物车
 * @Version: 1.0
 * <p>
 * <p>
 * <p>
 * rights reserved.
 */

public class SeparateShoppingCart extends DinnerShoppingCart {

    private static final String TAG = SeparateShoppingCart.class.getSimpleName();

    private static SeparateShoppingCart instance = null;

    /**
     * @Title: getInstance
     * @Description: 获取正餐购物车单例
     * @Param @return TODO
     * @Return DinnerShoppingCart 返回类型
     */

    public static SeparateShoppingCart getInstance() {
        synchronized (SeparateShoppingCart.class) {
            if (instance == null) {
                instance = new SeparateShoppingCart();
            }
        }

        return instance;
    }

    /**
     * @Title: batchAddShoppingCart
     * @Description: 批量将菜品添加到购物车
     * @Param @param mTradeVo
     * @Param @param listShopcartItem TODO
     * @Return void 返回类型
     */

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
        // 计算订单总价格

        MathShoppingCartTool.mathTotalPrice(tempShopcartItemList,

                dinnerShoppingCartVo.getmTradeVo());
        //拆单支付时会有同时修改的问题
        try {
            for (int key : arrayListener.keySet()) {

                arrayListener.get(key).separateOrder(tempShopcartItemList,

                        dinnerShoppingCartVo.getmTradeVo());

            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }

    /**
     * @Title: addShoppingCart
     * @Description: 添加拆分菜品
     * @Param @param mShopcartItem TODO
     * @Return void 返回类型
     */

    public void addShoppingCart(IShopcartItem mShopcartItem, boolean isNeedListener) {

        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());
        checkSeparateOrder();

        if (mShopcartItem instanceof ReadonlyShopcartItem) {

            dinnerShoppingCartVo.getListIShopcatItem().add(mShopcartItem);

        } else {

            dinnerShoppingCartVo.getListOrderDishshopVo().add((ShopcartItem) mShopcartItem);

        }
        List<IShopcartItem> tempShopcartItemList = mergeShopcartItem(dinnerShoppingCartVo);
        // 计算订单总价格
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

    /**
     * @param isNeedListener 是否需要监听
     * @Title: removeShoppingCart
     * @Description: 移除购物车
     * @Param @param mShopcartItem TODO
     * @Return void 返回类型
     */

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
        // 计算订单总价格

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

    // 判断是否是原单
    private void checkSeparateOrder() {
        DinnerShoppingCart dinnerShoppCart = DinnerShoppingCart.getInstance();
        if (isAllOrder(mergeShopcartItem(dinnerShoppCart.getShoppingCartVo()))) {
            dinnerShoppingCartVo.getmTradeVo().getTrade().setTradePeopleCount(
                    dinnerShoppCart.getShoppingCartVo().getmTradeVo().getTrade().getTradePeopleCount());
        } else {
            dinnerShoppingCartVo.getmTradeVo().getTrade().setTradePeopleCount(0);
        }

    }


    /**
     * @Title: setFastFoodCustomer
     * @Description: 设置登录会员
     * @Param @param mTradeCustomer TODO
     * @Return void 返回类型
     */

    public void setSeparateCustomer(TradeCustomer mTradeCustomer) {
        checkNeedBuildMainOrder(dinnerShoppingCartVo.getmTradeVo());

        setCustomer(dinnerShoppingCartVo, mTradeCustomer);

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).setCustomer(mTradeCustomer);

        }

    }

    /**
     * @Title: createSeparateOrder
     * @Description: 构建订单数据信息
     * @Param @return TODO
     * @Return TradeVo 返回类型
     */

    public TradeVo createSeparateOrder() {

        DinnerShoppingCart dinnerShoppCart = DinnerShoppingCart.getInstance();

        // 如果是结算原单这将优惠信息赋值给原单菜品
        List<IShopcartItem> splitItemList = mergeShopcartItem(dinnerShoppingCartVo);

        // 新单
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
        // 将营销活动添加到TradePrivilege中
        setMarktingTradePrivilege(mTradeVo);

        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(splitItemList, dinnerShoppingCartVo.getmTradeVo());
        //add v8.1 销售员
        if (dinnerShoppCart.getTradeUser() != null) {
            mTradeVo.setTradeUser(ShopcartItemUtils.copyTradeUser(dinnerShoppCart.getTradeUser()));
        }
        return mTradeVo;

//		}
    }

    //modify 20170608 begin
    public void updateDataFromTradeVo(TradeVo tradeVo) {
        this.updateDataFromTradeVo(tradeVo, false);
    }

    public void updateDataWithTrade(Trade trade) {//add 20171103
        if (trade != null && this.dinnerShoppingCartVo != null && this.dinnerShoppingCartVo.getmTradeVo() != null) {
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
    //modify 20170608 end

    /**
     * 从数据库更新购物车数据
     */
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

    /**
     * 复制原单和新单的宴请
     *
     * @param sourceTradeVo
     * @param splitTradeVo
     */
    private void copyBanquet(TradeVo sourceTradeVo, TradeVo splitTradeVo) {
        // 设置优惠劵

        if (BanquetVo.isNotNull(splitTradeVo.getBanquetVo())) {

            BanquetVo privilegeVo = splitTradeVo.getBanquetVo().clone();

            if (BanquetVo.isNotNull(sourceTradeVo.getBanquetVo())) {

                // 原单和新单都有，将新单中的优惠信息复制给原单

                TradePrivilege tp = sourceTradeVo.getBanquetVo().getTradePrivilege();

                copyOnlyData(privilegeVo.getTradePrivilege(), tp);

                privilegeVo.setTradePrivilege(tp);

            } else {

                // 原单没有，新单有优惠，为将新单的优惠设置给原单

                privilegeVo.getTradePrivilege().setTradeId(sourceTradeVo.getTrade().getId());

                privilegeVo.getTradePrivilege().setTradeUuid(sourceTradeVo.getTrade().getUuid());

            }

            sourceTradeVo.setBanquetVo(privilegeVo);

        } else if (BanquetVo.isNotNull(sourceTradeVo.getBanquetVo())) {

            // 原单有，新单没有优惠劵，将原单的优惠信息置为无效

            TradePrivilege tp = sourceTradeVo.getBanquetVo().getTradePrivilege();

            tp.setStatusFlag(StatusFlag.INVALID);
            tp.setChanged(true);
        }
    }

    /**
     * 拆单时处理微信卡劵
     */
    private void validateSplitWeixinCoupon() {
        //微信卡卷
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
            //新单和原单都有同一条微信卡卷
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

    /**
     * @Title: copyPalActivity
     * @Description: 将新单中营销活动数据复制到原单中
     * @Param tradeVo
     * @Param splitTradeVo TODO
     * @Return void 返回类型
     */
    private void copyPalActivity(TradeVo tradeVo, TradeVo splitTradeVo) {
        List<TradePlanActivity> oldPlanActivity = tradeVo.getTradePlanActivityList();

        List<TradePlanActivity> newPlanActivity = splitTradeVo.getTradePlanActivityList();

        /**
         * 原单、新单营销方案TradePlanActivity处理
         */
        // 原单没有营销活动、新单中有营销活动，则将新单的营销活动复制到原单中
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
        } else if (oldPlanActivity != null && newPlanActivity != null) {// 原单有、新单也有营销活动
            Map<String, TradePlanActivity> tempOldPlan = new HashMap<String, TradePlanActivity>();
            // 将原单中的营销活动以map的形式缓存起来
            for (TradePlanActivity mPlan : oldPlanActivity) {
                tempOldPlan.put(mPlan.getUuid(), mPlan);
            }
            List<TradePlanActivity> oldPlanList = new ArrayList<TradePlanActivity>();
            for (TradePlanActivity mPlan : newPlanActivity) {
                TradePlanActivity oldPlan = tempOldPlan.get(mPlan.getUuid());

                if (oldPlan != null) {// 该方案原单新单都有,则将新单中的方案信息复制给原单对应的方案
                    oldPlan.setOfferValue(mPlan.getOfferValue());
                    oldPlan.setPlanUsageCount(mPlan.getPlanUsageCount());
                    oldPlan.setClientUpdateTime(mPlan.getClientCreateTime());
                    oldPlan.setStatusFlag(mPlan.getStatusFlag());
                    oldPlan.setChanged(true);
                    oldPlanList.add(oldPlan);
                    // 移除原单、新单都有的方案
                    tempOldPlan.remove(mPlan.getUuid());
                } else {// 该方案新单有，原单没有
                    mPlan.setTradeId(tradeVo.getTrade().getId());
                    mPlan.setTradeUuid(tradeVo.getTrade().getUuid());
                    mPlan.setChanged(true);
                    oldPlanList.add(mPlan);
                }
            }
            // 原单有，新单没有
            if (tempOldPlan.size() != 0) {
                for (String key : tempOldPlan.keySet()) {
                    TradePlanActivity mTradePlanActivity = tempOldPlan.get(key);
                    mTradePlanActivity.setStatusFlag(StatusFlag.INVALID);
                    mTradePlanActivity.setChanged(true);
                    oldPlanList.add(mTradePlanActivity);
                }
            }

            tradeVo.setTradePlanActivityList(oldPlanList);

        } else if (oldPlanActivity != null && newPlanActivity == null) {// 原单有、新单没有
            // 则将原单中对应的方案设置为无效
            for (TradePlanActivity mTradePlanActivity : oldPlanActivity) {
                mTradePlanActivity.setStatusFlag(StatusFlag.INVALID);
                mTradePlanActivity.setChanged(true);
            }

        }

        /**
         * 原单、新单参与营销活动菜品TradeItemPlanActivity处理
         */
        List<TradeItemPlanActivity> oldItenPlanActivity = tradeVo.getTradeItemPlanActivityList();
        List<TradeItemPlanActivity> newItenPlanActivity = splitTradeVo.getTradeItemPlanActivityList();

        Map<String, IShopcartItem> separateMap = new HashMap<String, IShopcartItem>();

        List<IShopcartItem> listShopcartItems = mergeShopcartItem(dinnerShoppingCartVo);
        if (listShopcartItems != null) {
            for (IShopcartItem mIShopcartItem : listShopcartItems) {
                separateMap.put(mIShopcartItem.getUuid(), mIShopcartItem);
            }
        }

        // 新单有，原单没有、则将新单中的营销活动关联菜品数据复制到原单中
        if (oldItenPlanActivity == null && newItenPlanActivity != null) {
            List<TradeItemPlanActivity> tempItenPlanActivity = new ArrayList<TradeItemPlanActivity>();
            for (TradeItemPlanActivity itemPlanItemActivity : newItenPlanActivity) {

                TradeItemPlanActivity mTradeItemPlanActivity = new TradeItemPlanActivity();
                mTradeItemPlanActivity =
                        itemPlanItemActivity.copyTradeItemPlanActivity(itemPlanItemActivity, mTradeItemPlanActivity);

                IShopcartItem mIShopcartItem = separateMap.get(itemPlanItemActivity.getTradeItemUuid());
                mTradeItemPlanActivity.setTradeId(tradeVo.getTrade().getId());
                mTradeItemPlanActivity.setTradeUuid(tradeVo.getTrade().getUuid());
                // 因拆单的tradeItem
                // 的RelateTradeItemId和RelateTradeItemUuid
                // 关联原单的TradeItemId和TradeItemUuid
                mTradeItemPlanActivity.setTradeItemId(mIShopcartItem.getRelateTradeItemId());
                mTradeItemPlanActivity.setTradeItemUuid(mIShopcartItem.getRelateTradeItemUuid());

                mTradeItemPlanActivity.setChanged(true);
                tempItenPlanActivity.add(mTradeItemPlanActivity);
            }
            tradeVo.setTradeItemPlanActivityList(tempItenPlanActivity);

        } else if (oldItenPlanActivity != null && newItenPlanActivity != null) {// 新单有、原单也有

            Map<String, TradeItemPlanActivity> tempPlanMap = new HashMap<String, TradeItemPlanActivity>();

            List<TradeItemPlanActivity> tempItemPlanActivity = new ArrayList<TradeItemPlanActivity>();

            for (TradeItemPlanActivity oItemPlanActivity : oldItenPlanActivity) {
                tempPlanMap.put(oItemPlanActivity.getTradeItemUuid(), oItemPlanActivity);
            }

            for (TradeItemPlanActivity nItemPlanActivity : newItenPlanActivity) {
                if (nItemPlanActivity.getStatusFlag() == StatusFlag.INVALID) {
                    // 不合法的原单新单对象一样
                    tempItemPlanActivity.add(nItemPlanActivity);
                    continue;
                }
                IShopcartItem newIShopcartItem = separateMap.get(nItemPlanActivity.getTradeItemUuid());
                if (newIShopcartItem == null) {
                    continue;
                }

                // 通过新单的TradePlanItemActivity
                // 找到新单对应的shopcartItem
                // ---relateTradeItemUuid
                TradeItemPlanActivity planActivity = tempPlanMap.get(newIShopcartItem.getRelateTradeItemUuid());

                if (planActivity != null) {// 原单有，新单也有同一菜品营销活动
                    planActivity = planActivity.copyTradeItemPlanActivity(nItemPlanActivity, planActivity);
                    planActivity.setTradeId(tradeVo.getTrade().getId());
                    planActivity.setTradeUuid(tradeVo.getTrade().getUuid());
                    // 因拆单的tradeItem
                    // 的RelateTradeItemId和RelateTradeItemUuid
                    // 关联原单的TradeItemId和TradeItemUuid
                    planActivity.setTradeItemId(newIShopcartItem.getRelateTradeItemId());
                    planActivity.setTradeItemUuid(newIShopcartItem.getRelateTradeItemUuid());

                    planActivity.setChanged(true);
                    tempItemPlanActivity.add(planActivity);
                    tempPlanMap.remove(nItemPlanActivity.getTradeItemUuid());
                } else {// 新单有，原单没有
                    TradeItemPlanActivity mTradeItemPlanActivity = new TradeItemPlanActivity();
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

        } else if (oldItenPlanActivity != null && newItenPlanActivity == null) {// 新单没有，原单有，则将原单的置为无效
            for (TradeItemPlanActivity itemPlanActivity : oldItenPlanActivity) {
                itemPlanActivity.setStatusFlag(StatusFlag.INVALID);
                itemPlanActivity.setChanged(true);
            }
        }

    }


    /**
     * @Title: isAllOrder
     * @Description: 判断是否是原单操作
     * @Param @param listShopcartItem : 原单中的菜品数据
     * @Param @return TODO
     * @Return Boolean 返回类型
     */

    public boolean isAllOrder(List<IShopcartItem> listShopcartItem) {

        List<IShopcartItem> splitItemList = mergeShopcartItem(dinnerShoppingCartVo);
        return isAllOrder(listShopcartItem, splitItemList);
    }

    /**
     * @param listShopcartItem dinnerShopcart菜品
     * @param splitItemList    拆单购物车菜品
     * @return
     */
    public boolean isAllOrder(List<IShopcartItem> listShopcartItem, List<IShopcartItem> splitItemList) {
        if (splitItemList.size() == listShopcartItem.size()) {
            return true;

        }
        for (IShopcartItem item : listShopcartItem) {

            /*
             *
             * 全退时，生成的新记录数量为0的有效记录，拆单界面会过滤掉数量为0的记录，
             *
             * 所以留下的有效记录都是数量为0时也是使用原单结算
             *
             */

            if (item.getStatusFlag() == StatusFlag.VALID && item.getTotalQty().compareTo(BigDecimal.ZERO) != 0) {

                // 返回false表示不是用原单结算

                return false;

            }

        }

        return true;
    }

    /**
     * 获取订单数据
     */

    public TradeVo getOrder() {
        return dinnerShoppingCartVo.getmTradeVo();
    }

    public ShoppingCartVo getShoppingCartVo() {

        return dinnerShoppingCartVo;

    }

    /**
     * 清除拆单数据
     */

    public void clearShoppingCart() {

        dinnerShoppingCartVo = new ShoppingCartVo();

        for (int key : arrayListener.keySet()) {

            arrayListener.get(key).clearShoppingCart();

        }

    }

    public void clearShoppingCartData() {
        dinnerShoppingCartVo = new ShoppingCartVo();
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
        return mergeShopcartItem(dinnerShoppingCartVo);
    }
}
