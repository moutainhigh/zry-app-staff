package com.zhongmei.bty.basemodule.discount.cache;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.zhongmei.bty.basemodule.customer.entity.MemberPriceTemplet;
import com.zhongmei.bty.basemodule.customer.entity.MemberPriceTempletDetail;
import com.zhongmei.bty.basemodule.discount.bean.MarketPlanVo;
import com.zhongmei.bty.basemodule.discount.bean.MarketRuleVo;
import com.zhongmei.bty.basemodule.discount.entity.MarketActivity;
import com.zhongmei.bty.basemodule.discount.entity.MarketActivityDish;
import com.zhongmei.yunfu.db.entity.discount.MarketActivityRule;
import com.zhongmei.bty.basemodule.discount.entity.MarketActivityWeekday;
import com.zhongmei.yunfu.db.entity.discount.MarketDynamicCondition;
import com.zhongmei.yunfu.db.entity.discount.MarketPlan;
import com.zhongmei.bty.basemodule.discount.enums.ActivityType;
import com.zhongmei.bty.basemodule.discount.enums.PromotionType;
import com.zhongmei.bty.basemodule.discount.enums.UserType;
import com.zhongmei.bty.basemodule.discount.operates.interfaces.MarketDal;
import com.zhongmei.yunfu.context.util.ThreadUtils;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.entity.crm.CrmCustomerRightsConfig;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class MarketRuleCache {
    private static final String TAG = MarketRuleCache.class.getSimpleName();

    private static class LazySingletonHolder {
        private static final MarketRuleCache INSTANCE = new MarketRuleCache();
    }


    public static void doStart() {
        LazySingletonHolder.INSTANCE.start();

    }


    public static void doStop() {
        LazySingletonHolder.INSTANCE.stop();
    }



    public static List<MarketRuleVo> getFilteredMarketRule(UserType userType, BusinessType businessType,
                                                           DeliveryType deliveryType) {

        return LazySingletonHolder.INSTANCE.filterMarketRule(userType, businessType, deliveryType);
    }

    public static List<MarketRuleVo> getMarketDishVoListByType(ActivityType type) {
        return LazySingletonHolder.INSTANCE.getMarketRuleVoListByType(type);
    }

    public static MarketRuleVo getMarketDishVoByRule(Long ruleId) {
        return LazySingletonHolder.INSTANCE.getMarketRuleVoByRuleId(ruleId);
    }

    public static MarketRuleVo getMarketDishVoByPlan(Long planId) {
        return LazySingletonHolder.INSTANCE.getMarketRuleVoByPlanId(planId);
    }

    public static boolean isOpenCustomerPriceLimit() {
        return LazySingletonHolder.INSTANCE.isPriceLimit;
    }

    private DatabaseHelper.DataChangeObserver mObserver;

    private Map<Long, MarketRuleVo> marketRuleVoMap;

    private boolean isPriceLimit = false;

    private final long EXCUTETIME = 1000 * 60 * 30;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            try {
                backgroundRefreshMarket();
                startTimer();
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }

        ;
    };


    private void start() {
        if (mObserver == null) {
            mObserver = new MarketRuleCacheObserver();
            DatabaseHelper.Registry.register(mObserver);
        }
        startTimer();
        ThreadUtils.runOnWorkThread(new Runnable() {
            @Override
            public void run() {
                refreshMarketRules();
                refreshCustomerRights();            }
        });
    }


    private void stop() {
        if (mObserver != null) {
            DatabaseHelper.Registry.unregister(mObserver);
            mObserver = null;
        }
        stopTimer();
        if (marketRuleVoMap != null) {
            synchronized (this) {
                marketRuleVoMap.clear();
                marketRuleVoMap = null;
            }
        }
    }

    private void stopTimer() {
        mHandler.removeCallbacksAndMessages(null);
    }

    private void startTimer() {
        Message ms = mHandler.obtainMessage();
        mHandler.sendMessageDelayed(ms, EXCUTETIME);
    }


    private void backgroundRefreshMarket() {
        ThreadUtils.runOnWorkThread(new Runnable() {
            @Override
            public void run() {
                refreshMarketRules();
            }
        });     }


    private void refreshMarketRules() {
        Log.e(TAG, "refreshMarketRules...begin>" + System.currentTimeMillis());
        MarketDal dal = OperatesFactory.create(MarketDal.class);
        final Map<Long, MarketPlanVo> planVoMap = new HashMap<Long, MarketPlanVo>();
                final List<MarketActivityRule> ruleList = dal.queryAllRule();
                final List<MarketPlanVo> planVoList = dal.getMarketPlanvOList();
                if (!planVoList.isEmpty()) {
            for (MarketPlanVo planvo : planVoList) {
                planVoMap.put(planvo.getMarketPlan().getId(), planvo);
            }
        }
        Map<Long, MarketRuleVo> ruleVoMap = new LinkedHashMap<Long, MarketRuleVo>();

                if (ruleList != null && !ruleList.isEmpty()) {
            MarketPlanVo marketPlanVo = null;
            for (MarketActivityRule rule : ruleList) {
                if ((marketPlanVo = planVoMap.get(rule.getPlanId())) != null) {
                    MarketRuleVo ruleVo = new MarketRuleVo(marketPlanVo, rule);
                    ruleVoMap.put(ruleVo.getMarketActivityRule().getId(), ruleVo);
                                        List<MarketActivityWeekday> weekDayList =
                            dal.queryWeekdayByRuleId(ruleVo.getMarketActivityRule().getId());
                    if (weekDayList != null) {
                        ruleVo.setWeekdayList(weekDayList);
                    }
                                        if (ruleVo.getPromotionType() == PromotionType.SPECAILPRICE) {
                        ruleVo.setTempletVo(dal.getMarketTempletVoById(rule.getMenuId()));
                    } else {
                                                if (!ruleVo.isAllDish()) {
                            ruleVo.setMarketActivityDishList(dal.queryDishByRuleId(rule.getId()));
                        }
                    }
                }
            }
        }
        Log.e(TAG, "refreshMarketRules.....end>" + System.currentTimeMillis());
        synchronized (this) {
                        marketRuleVoMap = ruleVoMap;
        }
    }


    private synchronized List<MarketRuleVo> getMarketRuleVoListByType(ActivityType type) {
        List<MarketRuleVo> list = new ArrayList<MarketRuleVo>();
        if (marketRuleVoMap != null && !marketRuleVoMap.isEmpty()) {
            for (MarketRuleVo ruleVo : marketRuleVoMap.values()) {
                                if (type == null || type.equals(ruleVo.getActivityType())) {
                    list.add(ruleVo);
                }
            }
        }
        return list;
    }


    private synchronized MarketRuleVo getMarketRuleVoByRuleId(Long ruleId) {
        if (marketRuleVoMap != null) {
            return marketRuleVoMap.get(ruleId);
        } else {
            return null;
        }
    }


    private synchronized MarketRuleVo getMarketRuleVoByPlanId(Long planId) {
        if (marketRuleVoMap != null && planId != null) {
            for (MarketRuleVo marketRuleVo : marketRuleVoMap.values()) {
                if (marketRuleVo.getMarketPlan() != null && planId.equals(marketRuleVo.getMarketPlan().getId())) {
                    return marketRuleVo;
                }
            }
        }
        return null;
    }


    private synchronized List<MarketRuleVo> filterMarketRule(UserType userType, BusinessType businessType,
                                                             DeliveryType deliveryType) {
        List<MarketRuleVo> list = new ArrayList<MarketRuleVo>();
        if (marketRuleVoMap != null && !marketRuleVoMap.isEmpty()) {
            for (MarketRuleVo ruleVo : marketRuleVoMap.values()) {
                                if (userType != null && !ruleVo.isContainsUserType(userType)) {
                    continue;
                }
                                if (businessType != null && !ruleVo.isContainsBusinessType(businessType)) {
                    continue;
                }
                                if (deliveryType != null && !ruleVo.isContainsDeliveryType(deliveryType)) {
                    continue;
                }
                                if (!ruleVo.IsEnableCurrent()) {
                    continue;
                }
                                if (ruleVo.getPromotionType() == PromotionType.SPECAILPRICE) {
                    continue;
                }
                list.add(ruleVo);
            }
        }
        return list;
    }

    private static final Uri URI_PLAN = DBHelperManager.getUri(MarketPlan.class);

    private static final Uri URI_RULE = DBHelperManager.getUri(MarketActivityRule.class);

    private static final Uri URI_DYNAMIC = DBHelperManager.getUri(MarketDynamicCondition.class);

    private static final Uri URI_ACTIVITY = DBHelperManager.getUri(MarketActivity.class);

    private static final Uri URI_TEMPLET = DBHelperManager.getUri(MemberPriceTemplet.class);

    private static final Uri URI_TEMPLET_DETAIL = DBHelperManager.getUri(MemberPriceTempletDetail.class);

    private static final Uri URI_ACTIVITY_DiSh = DBHelperManager.getUri(MarketActivityDish.class);

    private static final Uri URI_CUSTOMER_RIGHTS_CONFIG = DBHelperManager.getUri(CrmCustomerRightsConfig.class);


    private class MarketRuleCacheObserver implements DatabaseHelper.DataChangeObserver {

        @Override
        public void onChange(Collection<Uri> uris) {
            if (uris.contains(URI_PLAN) || uris.contains(URI_RULE) || uris.contains(URI_DYNAMIC)
                    || uris.contains(URI_ACTIVITY) || uris.contains(URI_TEMPLET) || uris.contains(URI_TEMPLET_DETAIL)
                    || uris.contains(URI_ACTIVITY_DiSh)) {
                backgroundRefreshMarket();            } else if (uris.contains(URI_CUSTOMER_RIGHTS_CONFIG)) {
                refreshCustomerRights();            }
        }
    }

    private void refreshCustomerRights() {        MarketDal dal = OperatesFactory.create(MarketDal.class);
        CrmCustomerRightsConfig config = dal.queryCrmCustomerRightsConfig();
        if (config != null && config.priceLimit != null) {
            this.isPriceLimit = config.priceLimit == 2 ? true : false;
        }
    }
}
