package com.zhongmei.bty.basemodule.discount.salespromotion.cache;

import android.util.Log;

import com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConvertHelper;
import com.zhongmei.bty.basemodule.discount.salespromotion.cache.holder.BasicHolder;
import com.zhongmei.bty.basemodule.discount.salespromotion.cache.holder.RuleActivityDishHolder;
import com.zhongmei.bty.basemodule.discount.salespromotion.cache.holder.RuleMarketRuleHolder;
import com.zhongmei.bty.basemodule.discount.salespromotion.cache.holder.RulePlanHolder;
import com.zhongmei.bty.basemodule.discount.salespromotion.cache.holder.RulePolicyDishHolder;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;

/**
 * Created by demo on 2018/12/15
 */
public class SalesPromotionCache {

    private static final String TAG = SalesPromotionCache.class.getSimpleName();
    private final RuleActivityDishHolder mRuleActivityDishHolder;
    private final RuleMarketRuleHolder mRuleMarketRuleHolder;
    private final RulePlanHolder mRulePlanHolder;
    private final RulePolicyDishHolder mRulePolicyDishHolder;

    private boolean isInit = false;

    private SalesPromotionCache() {
        mRuleActivityDishHolder = new RuleActivityDishHolder();
        mRuleMarketRuleHolder = new RuleMarketRuleHolder();
        mRulePlanHolder = new RulePlanHolder();
        mRulePolicyDishHolder = new RulePolicyDishHolder();
    }

    /**
     * 返回活动菜品缓存数据
     */
    public static RuleActivityDishHolder getRuleActivityDishHolder() {
        return SalesPromotionCache.LazySingletonHolder.INSTANCE.mRuleActivityDishHolder;
    }

    /**
     * 返回活动细则缓存数据
     */
    public static RuleMarketRuleHolder getRuleMarketRuleHolder() {
        return SalesPromotionCache.LazySingletonHolder.INSTANCE.mRuleMarketRuleHolder;
    }

    /**
     * 返回活动主表缓存数据
     */
    public static RulePlanHolder getRulePlanHolder() {
        return SalesPromotionCache.LazySingletonHolder.INSTANCE.mRulePlanHolder;
    }

    /**
     * 返回赠送商品缓存数据
     */
    public static RulePolicyDishHolder getRulePolicyDishHolder() {
        return SalesPromotionCache.LazySingletonHolder.INSTANCE.mRulePolicyDishHolder;
    }

    private static class LazySingletonHolder {
        private static final SalesPromotionCache INSTANCE = new SalesPromotionCache();
    }

    /**
     * 刷新所有缓存数据
     */
    public synchronized static void init() {
        try {
            SalesPromotionCache.LazySingletonHolder.INSTANCE.refresh();
        } catch (Exception e) {
            Log.e(TAG, "refresh error!", e);
        }
        SalesPromotionCache.LazySingletonHolder.INSTANCE.checkData();
    }

    /**
     * 刷新所有缓存数据
     */
    private synchronized void refresh() throws Exception {
        Log.i(TAG, "refresh...");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            refresh(helper, mRuleActivityDishHolder);
            refresh(helper, mRuleMarketRuleHolder);
            refresh(helper, mRulePlanHolder);
            refresh(helper, mRulePolicyDishHolder);
            isInit = true;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    private void refresh(DatabaseHelper helper, BasicHolder<?> holder) throws Exception {
        holder.unregisterObserver();
        holder.refresh(helper);
        holder.registerObserver();
        SalesPromotionConvertHelper.getInstance().buildSalesPromotionRuleVo();
    }

    private void checkData() {
        try {
            Log.i(TAG, "isInit=" + isInit);
            Log.i(TAG, "RulePolicyDishHolder.count=" + mRuleActivityDishHolder.getCount());
            Log.i(TAG, "RuleMarketRuleHolder.count=" + mRuleMarketRuleHolder.getCount());
            Log.i(TAG, "RulePlanHolder.count=" + mRulePlanHolder.getCount());
            Log.i(TAG, "RulePolicyDishHolder.count=" + mRulePolicyDishHolder.getCount());
        } catch (Exception e) {
            Log.e(TAG, "checkData error!", e);
        }
    }

}
