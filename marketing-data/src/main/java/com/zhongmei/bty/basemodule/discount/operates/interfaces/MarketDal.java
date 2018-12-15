package com.zhongmei.bty.basemodule.discount.operates.interfaces;

import com.zhongmei.bty.basemodule.customer.bean.DishMemberPrice;
import com.zhongmei.bty.basemodule.discount.bean.MarketPlanVo;
import com.zhongmei.bty.basemodule.discount.bean.MarketTempletVo;
import com.zhongmei.bty.basemodule.discount.entity.MarketActivity;
import com.zhongmei.bty.basemodule.discount.entity.MarketActivityDish;
import com.zhongmei.yunfu.db.entity.discount.MarketActivityRule;
import com.zhongmei.bty.basemodule.discount.entity.MarketActivityWeekday;
import com.zhongmei.yunfu.db.entity.discount.MarketDynamicCondition;
import com.zhongmei.yunfu.db.entity.discount.MarketPlan;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.db.entity.crm.CrmCustomerRightsConfig;

import java.util.List;

/**
 * 营销活动数据库查询
 *
 * @date:2016年4月27日上午10:11:50
 */
public interface MarketDal extends IOperates {

    /**
     * @Title: queryAllRule
     * @Description: 查询符合时间的营销活动
     * @Param @param date
     * @Param @return TODO
     * @Return List<MarketActivityRule> 返回类型
     */
    public List<MarketActivityRule> queryAllRule();

    /**
     * @Title: queryRuleByPlanId
     * @Description: 通过planid查询rule
     * @Param @param planId
     * @Param @return TODO
     * @Return List<MarketActivityRule> 返回类型
     */
    public List<MarketActivityRule> queryRuleByPlanId(Long planId);

    /**
     * @Title: queryRuleByRuleId
     * @Description: 根据ruleId查询rule
     * @Param @param ruleId
     * @Param @return TODO
     * @Return MarketActivityRule 返回类型
     */
    public MarketActivityRule queryRuleByRuleId(Long ruleId);

    /**
     * @Title: queryDishByRuleId
     * @Description: TODO
     * @Param @param ruleId
     * @Param @return TODO
     * @Return List<MarketActivityDish> 返回类型
     */
    public List<MarketActivityDish> queryDishByRuleId(Long ruleId);

    /**
     * @Title: queryMarketPlanList
     * @Description: 查询所有满足当前时段的方案
     * @Param @return TODO
     * @Return List<MarketPlan> 返回类型
     */
    public List<MarketPlan> queryMarketPlanList();

    /**
     * @Title: getMarketPlanvOList
     * @Description: 查询所有满足当前时段的方案数据集合
     * @Return List<MarketPlanVo> 返回类型
     */
    public List<MarketPlanVo> getMarketPlanvOList();

    public MarketTempletVo getMarketTempletVoById(Long ruleId);

    /**
     * @Title: queryActivityByPlanId
     * @Description: TODO
     * @Param @param planId
     * @Param @return TODO
     * @Return List<MarketActivity> 返回类型
     */
    public MarketActivity queryActivityByPlanId(Long planId);

    /**
     * @Title: queryConditionByPlanId
     * @Description: 通过方案id查询动态情形
     * @Param @param planId
     * @Param @return TODO
     * @Return List<MarketDynamicCondition> 返回类型
     */
    public List<MarketDynamicCondition> queryConditionByPlanId(Long planId);

    /**
     * @Title: queryWeekdayByRuleId
     * @Description: 通过marketActivityRule id查询 活动的星期
     * @Param @param ruleId
     * @Param @return TODO
     * @Return List<MarketActivityWeekday> 返回类型
     */
    public List<MarketActivityWeekday> queryWeekdayByRuleId(Long ruleId);

    /**
     * @Title: queryDishSpecailPrice
     * @Description: 查询菜品的特价
     * @Param @param dishId
     * @Param @return TODO
     * @Return DishMemberPrice 返回类型
     */
    public DishMemberPrice queryDishSpecailPrice(Long dishId);

    //查询虚拟会员价限制开关
    public CrmCustomerRightsConfig queryCrmCustomerRightsConfig();
}
