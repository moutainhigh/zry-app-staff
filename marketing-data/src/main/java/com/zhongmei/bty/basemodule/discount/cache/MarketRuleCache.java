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

/**
 * @Date：2016-5-20 下午4:54:38
 * @Description: 营销活动设置缓存
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class MarketRuleCache {
    private static final String TAG = MarketRuleCache.class.getSimpleName();

    private static class LazySingletonHolder {
        private static final MarketRuleCache INSTANCE = new MarketRuleCache();
    }

    /**
     * @Description: 对外接口，开启缓存
     */
    public static void doStart() {
        LazySingletonHolder.INSTANCE.start();

    }

    /**
     * @Description: 对外接口，停止缓存
     */
    public static void doStop() {
        LazySingletonHolder.INSTANCE.stop();
    }


    /**
     * @Description:根据参数过滤营销活动
     * @Param UserType userType
     * @Param BusinessType businessType
     * @Param DeliveryType deliveryType
     * @Return List<MarketRuleVo> 返回类型
     */
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

    private final long EXCUTETIME = 1000 * 60 * 30;// 半小时刷新一次

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

    /**
     * @Description:开启缓存
     */
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
                refreshCustomerRights();// 刷新虚拟会员价限制开关
            }
        });
    }

    /**
     * @Description: 内部方法，停止并清空缓存
     */
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

    /**
     * @Title: backgroundRefresh
     * @Description: 开启新线程刷新缓存数据
     */
    private void backgroundRefreshMarket() {
        ThreadUtils.runOnWorkThread(new Runnable() {
            @Override
            public void run() {
                refreshMarketRules();
            }
        }); //modify v8.1 通用线程工具替换
    }

    /**
     * @Description: 刷新数据, 从数据库查询数据。 缓存到内存当中
     */
    private void refreshMarketRules() {
        Log.e(TAG, "refreshMarketRules...begin>" + System.currentTimeMillis());
        MarketDal dal = OperatesFactory.create(MarketDal.class);
        final Map<Long, MarketPlanVo> planVoMap = new HashMap<Long, MarketPlanVo>();
        // 查询MarketActivityRule
        final List<MarketActivityRule> ruleList = dal.queryAllRule();
        // 查询MarketPlanVo
        final List<MarketPlanVo> planVoList = dal.getMarketPlanvOList();
        // 活动信息保存到map
        if (!planVoList.isEmpty()) {
            for (MarketPlanVo planvo : planVoList) {
                planVoMap.put(planvo.getMarketPlan().getId(), planvo);
            }
        }
        Map<Long, MarketRuleVo> ruleVoMap = new LinkedHashMap<Long, MarketRuleVo>();

        // 生成MarketPlanVo
        if (ruleList != null && !ruleList.isEmpty()) {
            MarketPlanVo marketPlanVo = null;
            for (MarketActivityRule rule : ruleList) {
                if ((marketPlanVo = planVoMap.get(rule.getPlanId())) != null) {
                    MarketRuleVo ruleVo = new MarketRuleVo(marketPlanVo, rule);
                    ruleVoMap.put(ruleVo.getMarketActivityRule().getId(), ruleVo);
                    // 限制星期几
                    List<MarketActivityWeekday> weekDayList =
                            dal.queryWeekdayByRuleId(ruleVo.getMarketActivityRule().getId());
                    if (weekDayList != null) {
                        ruleVo.setWeekdayList(weekDayList);
                    }
                    // 如果特价
                    if (ruleVo.getPromotionType() == PromotionType.SPECAILPRICE) {
                        ruleVo.setTempletVo(dal.getMarketTempletVoById(rule.getMenuId()));
                    } else {
                        // 如果是部分菜品参与
                        if (!ruleVo.isAllDish()) {
                            ruleVo.setMarketActivityDishList(dal.queryDishByRuleId(rule.getId()));
                        }
                    }
                }
            }
        }
        Log.e(TAG, "refreshMarketRules.....end>" + System.currentTimeMillis());
        synchronized (this) {
            // 更新缓存数据
            marketRuleVoMap = ruleVoMap;
        }
    }

    /**
     * @Description: 根据活动类型查找营销活动规则信息
     */
    private synchronized List<MarketRuleVo> getMarketRuleVoListByType(ActivityType type) {
        List<MarketRuleVo> list = new ArrayList<MarketRuleVo>();
        if (marketRuleVoMap != null && !marketRuleVoMap.isEmpty()) {
            for (MarketRuleVo ruleVo : marketRuleVoMap.values()) {
                // 单商品、多商品过滤
                if (type == null || type.equals(ruleVo.getActivityType())) {
                    list.add(ruleVo);
                }
            }
        }
        return list;
    }

    /**
     * @Description: 根据规则Id查找营销活动规则信息
     */
    private synchronized MarketRuleVo getMarketRuleVoByRuleId(Long ruleId) {
        if (marketRuleVoMap != null) {
            return marketRuleVoMap.get(ruleId);
        } else {
            return null;
        }
    }

    /**
     * @Description: 根据活动Id查找营销活动规则信息
     */
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

    /**
     * @Description: 根据条件过滤缓存中的使用规则数据
     */
    private synchronized List<MarketRuleVo> filterMarketRule(UserType userType, BusinessType businessType,
                                                             DeliveryType deliveryType) {
        List<MarketRuleVo> list = new ArrayList<MarketRuleVo>();
        if (marketRuleVoMap != null && !marketRuleVoMap.isEmpty()) {
            for (MarketRuleVo ruleVo : marketRuleVoMap.values()) {
                // 过滤参与人员
                if (userType != null && !ruleVo.isContainsUserType(userType)) {
                    continue;
                }
                // 过滤单据类型
                if (businessType != null && !ruleVo.isContainsBusinessType(businessType)) {
                    continue;
                }
                // 过滤就餐方式
                if (deliveryType != null && !ruleVo.isContainsDeliveryType(deliveryType)) {
                    continue;
                }
                // 使用时间验证
                if (!ruleVo.IsEnableCurrent()) {
                    continue;
                }
                // 6.9暂停特价菜
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

    /**
     * @Date：2016-5-20 下午4:54:38
     * @Description: 监听数据库变化，刷新缓存
     */
    private class MarketRuleCacheObserver implements DatabaseHelper.DataChangeObserver {

        @Override
        public void onChange(Collection<Uri> uris) {
            if (uris.contains(URI_PLAN) || uris.contains(URI_RULE) || uris.contains(URI_DYNAMIC)
                    || uris.contains(URI_ACTIVITY) || uris.contains(URI_TEMPLET) || uris.contains(URI_TEMPLET_DETAIL)
                    || uris.contains(URI_ACTIVITY_DiSh)) {
                backgroundRefreshMarket();// 刷新
            } else if (uris.contains(URI_CUSTOMER_RIGHTS_CONFIG)) {
                refreshCustomerRights();// 刷新虚拟会员价限制开关
            }
        }
    }

    private void refreshCustomerRights() {//add v8.2 刷新会员价限制开关
        MarketDal dal = OperatesFactory.create(MarketDal.class);
        CrmCustomerRightsConfig config = dal.queryCrmCustomerRightsConfig();
        if (config != null && config.priceLimit != null) {
            this.isPriceLimit = config.priceLimit == 2 ? true : false;
        }
    }
}
