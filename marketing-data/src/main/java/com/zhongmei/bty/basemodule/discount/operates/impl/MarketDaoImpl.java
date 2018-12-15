package com.zhongmei.bty.basemodule.discount.operates.impl;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.bty.basemodule.customer.bean.DishMemberPrice;
import com.zhongmei.bty.basemodule.customer.entity.MemberPriceTemplet;
import com.zhongmei.bty.basemodule.customer.entity.MemberPriceTempletDetail;
import com.zhongmei.bty.basemodule.discount.bean.MarketPlanVo;
import com.zhongmei.bty.basemodule.discount.bean.MarketTempletVo;
import com.zhongmei.bty.basemodule.discount.entity.MarketActivity;
import com.zhongmei.bty.basemodule.discount.entity.MarketActivityDish;
import com.zhongmei.yunfu.db.entity.discount.MarketActivityRule;
import com.zhongmei.bty.basemodule.discount.entity.MarketActivityWeekday;
import com.zhongmei.yunfu.db.entity.discount.MarketDynamicCondition;
import com.zhongmei.yunfu.db.entity.discount.MarketPlan;
import com.zhongmei.yunfu.db.entity.discount.MarketPlanCommercialRel;
import com.zhongmei.bty.basemodule.discount.enums.UserType;
import com.zhongmei.bty.basemodule.discount.operates.interfaces.MarketDal;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.entity.crm.CrmCustomerRightsConfig;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @date:2016年4月27日下午2:59:56
 */
public class MarketDaoImpl extends AbstractOpeartesImpl implements MarketDal {

    private static final String Tag = "MarketDaoImpl";

    public MarketDaoImpl(ImplContext context) {
        super(context);
    }

    @Override
    public List<MarketActivityRule> queryAllRule() {
        // TODO Auto-generated method stub
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            String date = DateTimeUtils.getCurrentDate();
            // 过滤门店
            Long shopIdenty = BaseApplication.sInstance.getShopIdenty() == null
                    ? 0L : BaseApplication.sInstance.getShopIdenty();

            Dao<MarketPlanCommercialRel, String> planRelDao = helper.getDao(MarketPlanCommercialRel.class);
            QueryBuilder<MarketPlanCommercialRel, String> planRelQB = planRelDao.queryBuilder();
            planRelQB.selectColumns(MarketPlanCommercialRel.$.planId);
            planRelQB.where()
                    .eq(MarketPlanCommercialRel.$.commercialId, shopIdenty)
                    .and()
                    .eq(MarketPlanCommercialRel.$.statusFlag, StatusFlag.VALID);

            // 过滤出pos终端conditionType(11),conditionValue(1)
            Dao<MarketDynamicCondition, String> conditionDao = helper.getDao(MarketDynamicCondition.class);
            QueryBuilder<MarketDynamicCondition, String> conditionQB = conditionDao.queryBuilder();
            conditionQB.selectColumns(MarketDynamicCondition.$.planId);
            conditionQB.where()
                    .eq(MarketDynamicCondition.$.conditionType, 11)
                    .and()
                    .eq(MarketDynamicCondition.$.conditionValue, 1)
                    .and()
                    .eq(MarketPlan.$.isDelete, 0)
                    .and()
                    .in(MarketDynamicCondition.$.planId, planRelQB);
            Dao<MarketPlan, Long> planDao = helper.getDao(MarketPlan.class);
            QueryBuilder<MarketPlan, Long> planBuilder = planDao.queryBuilder();

            // 过滤有效时间 eg:12月8号-12月25号
            planBuilder.orderBy(MarketPlan.$.serverUpdateTime, false)
                    .selectColumns(MarketPlan.$.id)
                    .where()
                    .le(MarketPlan.$.planStartDay, date)
                    .and()
                    .ge(MarketPlan.$.planEndDay, date)
                    .and()
                    .eq(MarketPlan.$.isDelete, 0)
                    .and()
                    .in(MarketPlan.$.id, conditionQB);
            Dao<MarketActivityRule, Long> ruleDao = helper.getDao(MarketActivityRule.class);
            List<MarketActivityRule> marketRuleList = null;
            // 通过builder外键关联查询
            QueryBuilder<MarketActivityRule, Long> ruleBuilder = ruleDao.queryBuilder();
            marketRuleList =
                    ruleBuilder.orderBy(MarketActivityRule.$.serverUpdateTime, false)
                            .where()
                            .ge(MarketActivityRule.$.statusFlag, StatusFlag.VALID)
                            .and()
                            .in(MarketActivityRule.$.planId, planBuilder)
                            .query();

            return marketRuleList;
        } catch (Exception e) {
            Log.e(Tag, e.getMessage());
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.zhongmei.bty.basemodule.discount.operates.interfaces.MarketDal#queryMarketPlanList
     * () 过滤出当前时间的pos终端有效的营销活动
     */
    @Override
    public List<MarketPlan> queryMarketPlanList() {
        List<MarketPlan> list = null;
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {

            String date = DateTimeUtils.getCurrentDate();
            // 过滤门店
            Long shopIdenty = BaseApplication.sInstance.getShopIdenty() == null
                    ? 0L : BaseApplication.sInstance.getShopIdenty();

            Dao<MarketPlanCommercialRel, String> planRelDao = helper.getDao(MarketPlanCommercialRel.class);
            QueryBuilder<MarketPlanCommercialRel, String> planRelQB = planRelDao.queryBuilder();
            planRelQB.selectColumns(MarketPlanCommercialRel.$.planId);
            planRelQB.where()
                    .eq(MarketPlanCommercialRel.$.commercialId, shopIdenty)
                    .and()
                    .eq(MarketPlanCommercialRel.$.statusFlag, StatusFlag.VALID);

            // 过滤出pos终端conditionType(11),conditionValue(1)
            Dao<MarketDynamicCondition, String> conditionDao = helper.getDao(MarketDynamicCondition.class);
            QueryBuilder<MarketDynamicCondition, String> conditionQB = conditionDao.queryBuilder();
            conditionQB.selectColumns(MarketDynamicCondition.$.planId);
            conditionQB.where()
                    .eq(MarketDynamicCondition.$.conditionType, 11)
                    .and()
                    .eq(MarketDynamicCondition.$.conditionValue, 1)
                    .and()
                    .eq(MarketPlan.$.isDelete, 0)
                    .and()
                    .in(MarketDynamicCondition.$.planId, planRelQB);

            Dao<MarketPlan, Long> planDao = helper.getDao(MarketPlan.class);
            QueryBuilder<MarketPlan, Long> planBuilder = planDao.queryBuilder();

            // 过滤有效时间 eg:12月8号-12月25号,planStatus=3 执行中的活动
            planBuilder.orderBy(MarketPlan.$.serverUpdateTime, false)
                    .where()
                    .le(MarketPlan.$.planStartDay, date)
                    .and()
                    .ge(MarketPlan.$.planEndDay, date)
                    .and()
                    .eq(MarketPlan.$.isDelete, 0)
                    .and()
                    .eq(MarketPlan.$.planStatus, 3)
                    .and()
                    .in(MarketPlan.$.id, conditionQB);
            list = planBuilder.query();
        } catch (Exception e) {
            Log.e(Tag, e.getMessage());
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return list;
    }

    @Override
    public List<MarketActivityDish> queryDishByRuleId(Long ruleId) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<MarketActivityDish, Long> dao = helper.getDao(MarketActivityDish.class);
            return dao.queryBuilder()
                    .selectColumns(MarketActivityDish.$.dishId,
                            MarketActivityDish.$.dishName,
                            MarketActivityDish.$.dishNum,
                            MarketActivityDish.$.type)
                    .where()
                    .eq(MarketActivityDish.$.ruleId, ruleId)
                    .and()
                    .eq(MarketActivityDish.$.statusFlag, StatusFlag.VALID)
                    .query();
        } catch (Exception e) {
            Log.e(Tag, e.getMessage());
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }

    @Override
    public MarketActivity queryActivityByPlanId(Long planId) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<MarketActivity, Long> dao = helper.getDao(MarketActivity.class);
            return dao.queryBuilder()
                    .selectColumns(MarketActivity.$.id, MarketActivity.$.promotionType, MarketActivity.$.activityType)
                    .where()
                    .eq(MarketActivity.$.planId, planId)
                    .and()
                    .eq(MarketActivity.$.statusFlag, StatusFlag.VALID)
                    .queryForFirst();

        } catch (Exception e) {
            Log.e(Tag, e.getMessage());
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }

    public List<MarketActivity> queryActivityListByPlanIds(List<Long> pIds) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        List<MarketActivity> list = null;
        try {
            Dao<MarketActivity, Long> dao = helper.getDao(MarketActivity.class);
            list =
                    dao.queryBuilder()
                            .selectColumns(MarketActivity.$.id,
                                    MarketActivity.$.planId,
                                    MarketActivity.$.promotionType,
                                    MarketActivity.$.activityType)
                            .where()
                            .in(MarketActivity.$.planId, pIds)
                            .and()
                            .eq(MarketActivity.$.statusFlag, StatusFlag.VALID)
                            .query();

        } catch (Exception e) {
            Log.e(Tag, e.getMessage());
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return list;
    }

    @Override
    public List<MarketDynamicCondition> queryConditionByPlanId(Long planId) {
        // TODO Auto-generated method stub

        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<MarketDynamicCondition, Long> dao = helper.getDao(MarketDynamicCondition.class);
            return dao.queryBuilder()
                    .where()
                    .eq(MarketDynamicCondition.$.planId, planId)
                    .and()
                    .eq(MarketDynamicCondition.$.isDelete, 0)
                    .query();
        } catch (Exception e) {
            Log.e(Tag, e.getMessage());
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }

    @Override
    public List<MarketActivityWeekday> queryWeekdayByRuleId(Long ruleId) {
        // TODO Auto-generated method stub
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<MarketActivityWeekday, Long> dao = helper.getDao(MarketActivityWeekday.class);
            return dao.queryBuilder()
                    .where()
                    .eq(MarketActivityWeekday.$.ruleId, ruleId)
                    .and()
                    .eq(MarketActivityWeekday.$.statusFlag, StatusFlag.VALID)
                    .query();

        } catch (Exception e) {
            Log.e(Tag, e.getMessage());
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }

    @Override
    public List<MarketActivityRule> queryRuleByPlanId(Long planId) {
        // TODO Auto-generated method stub
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<MarketActivityRule, Long> dao = helper.getDao(MarketActivityRule.class);
            return dao.queryBuilder()
                    .where()
                    .eq(MarketActivityRule.$.planId, planId)
                    .and()
                    .eq(MarketActivityRule.$.statusFlag, StatusFlag.VALID)
                    .query();
        } catch (Exception e) {
            Log.e(Tag, e.getMessage());
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }

    @Override
    public MarketActivityRule queryRuleByRuleId(Long ruleId) {
        // TODO Auto-generated method stub
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<MarketActivityRule, Long> dao = helper.getDao(MarketActivityRule.class);
            return dao.queryBuilder()
                    .where()
                    .eq(MarketActivityRule.$.planId, ruleId)
                    .and()
                    .eq(MarketActivityRule.$.statusFlag, StatusFlag.VALID)
                    .queryForFirst();
        } catch (Exception e) {
            Log.e(Tag, e.getMessage());
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }

    @Override
    public DishMemberPrice queryDishSpecailPrice(Long dishId) {
        // TODO Auto-generated method stub
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<MemberPriceTempletDetail, Long> priceTempletDetailDao = helper.getDao(MemberPriceTempletDetail.class);
            MemberPriceTempletDetail detail =
                    priceTempletDetailDao.queryBuilder()
                            .where()
                            .eq(MemberPriceTempletDetail.$.brandDishId, dishId)
                            .and()
                            .eq(MemberPriceTempletDetail.$.statusFlag, StatusFlag.VALID)
                            .queryForFirst();
            if (detail != null) {
                Dao<MemberPriceTemplet, Long> priceTempletDao = helper.getDao(MemberPriceTemplet.class);
                MemberPriceTemplet templet =
                        priceTempletDao.queryBuilder()
                                .where()
                                .eq(MemberPriceTemplet.$.id, detail.getPriceTempletId())
                                .and()
                                .eq(MemberPriceTemplet.$.statusFlag, StatusFlag.INVALID)
                                .queryForFirst();

                if (templet == null) {
                    return null;
                }
                DishMemberPrice dishMemberPrice = new DishMemberPrice();
                dishMemberPrice.setDiscount(detail.getDiscount());
                dishMemberPrice.setMemberPrice(detail.getMemberPrice());
                dishMemberPrice.setPriceType(templet.getPriceType());
                dishMemberPrice.setPriceName(templet.getName());
                return dishMemberPrice;
            }

        } catch (Exception e) {
            Log.e(Tag, e.getMessage());
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }

    @Override
    public CrmCustomerRightsConfig queryCrmCustomerRightsConfig() {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<CrmCustomerRightsConfig, Long> dao = helper.getDao(CrmCustomerRightsConfig.class);
            QueryBuilder<CrmCustomerRightsConfig, Long> qb = dao.queryBuilder();
            qb.selectColumns(CrmCustomerRightsConfig.$.priceLimit, CrmCustomerRightsConfig.$.commercialId, CrmCustomerRightsConfig.$.id)
                    .where().eq(CrmCustomerRightsConfig.$.statusFlag, StatusFlag.VALID);
            qb.orderBy(CrmCustomerRightsConfig.$.commercialId, false);
            qb.orderBy(CrmCustomerRightsConfig.$.serverUpdateTime, false);
            return qb.queryForFirst();
        } catch (Exception e) {
            Log.e(Tag, e.getMessage());
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.zhongmei.bty.basemodule.discount.operates.interfaces.MarketDal#getMarketPlanvOList
     * ()
     */
    @Override
    public List<MarketPlanVo> getMarketPlanvOList() {
        List<MarketPlanVo> voList = new ArrayList<MarketPlanVo>();
        List<MarketPlan> planList = queryMarketPlanList();
        if (planList != null && !planList.isEmpty()) {
            List<Long> pIds = new ArrayList<Long>();
            for (MarketPlan plan : planList) {
                pIds.add(plan.getId());
            }
            Map<Long, MarketActivity> maMap = new HashMap<Long, MarketActivity>();
            List<MarketActivity> maList = queryActivityListByPlanIds(pIds);
            if (maList != null && !maList.isEmpty()) {
                for (MarketActivity ma : maList) {
                    maMap.put(ma.getPlanId(), ma);
                }
            }
            MarketPlanVo vo = null;
            MarketActivity marketActivity = null;
            List<MarketDynamicCondition> conditionList = null;
            for (MarketPlan plan : planList) {
                marketActivity = maMap.get(plan.getId());

                if (marketActivity != null) {
                    vo = new MarketPlanVo(plan);
                    voList.add(vo);
                    vo.setActivityType(marketActivity.getActivityType());// 营销方式：1单商品
                    // 2多商品
                    vo.setPromotionType(marketActivity.getPromotionType());// 优惠类型

                    conditionList = this.queryConditionByPlanId(plan.getId());// 权限记录

                    if (conditionList != null && !conditionList.isEmpty()) {
                        for (MarketDynamicCondition condition : conditionList) {

                            switch (condition.getConditionType()) {

                                case CUSTOMER_TYPE:// 2参与人群
                                    if (condition.getConditionValue() == 1)// 会员
                                    {
                                        vo.setUserType(UserType.MEMBER);

                                    } else if (condition.getConditionValue() == 2) {
                                        vo.setUserType(UserType.MEMBERNON);// 非会员
                                    }
                                    break;

                                case TRADE_TYPE:// 10单据类型
                                    if (condition.getConditionValue() == 1)// 内用
                                    {
                                        vo.setDeliveryType(DeliveryType.HERE);

                                    } else if (condition.getConditionValue() == 2) {// 外送

                                        vo.setDeliveryType(DeliveryType.SEND);
                                    } else if (condition.getConditionValue() == 3) {// 自提

                                        vo.setDeliveryType(DeliveryType.TAKE);
                                    } else if (condition.getConditionValue() == 4) {// 打包（外带）

                                        vo.setDeliveryType(DeliveryType.CARRY);
                                    }
                                    break;
                                case ACTIVITY_POS:// 活动终端
                                    vo.setActivityPos(condition.getConditionValue());
                                    break;

                                case MEAL_BACKGROUND:// 12就餐场景
                                    if (condition.getConditionValue() == 1)// 正餐
                                    {
                                        vo.setBusinessType(BusinessType.DINNER);

                                    } else if (condition.getConditionValue() == 2) {// 快餐
                                        vo.setBusinessType(BusinessType.SNACK);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        }
        return voList;
    }

    @Override
    public MarketTempletVo getMarketTempletVoById(Long id) {
        MarketTempletVo vo = null;
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<MemberPriceTemplet, Long> priceTempletDetailDao = helper.getDao(MemberPriceTemplet.class);
            MemberPriceTemplet templet =
                    priceTempletDetailDao.queryBuilder()
                            .selectColumns(MemberPriceTemplet.$.id, MemberPriceTemplet.$.name, MemberPriceTemplet.$.priceType)
                            .where()
                            .eq(MemberPriceTemplet.$.id, id)
                            .and()
                            .eq(MemberPriceTemplet.$.statusFlag, StatusFlag.VALID)
                            .queryForFirst();
            if (templet != null) {
                Dao<MemberPriceTempletDetail, Long> priceTempletDao = helper.getDao(MemberPriceTempletDetail.class);
                List<MemberPriceTempletDetail> detailList =
                        priceTempletDao.queryBuilder()
                                .selectColumns(MemberPriceTempletDetail.$.id,
                                        MemberPriceTempletDetail.$.memberPrice,
                                        MemberPriceTempletDetail.$.discount,
                                        MemberPriceTempletDetail.$.brandDishId)
                                .where()
                                .eq(MemberPriceTempletDetail.$.priceTempletId, templet.getId())
                                .and()
                                .eq(MemberPriceTempletDetail.$.statusFlag, StatusFlag.VALID)
                                .query();
                vo = new MarketTempletVo(templet, detailList);

            }

        } catch (Exception e) {
            Log.e(Tag, e.getMessage());
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return vo;
    }
}
