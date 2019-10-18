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


public interface MarketDal extends IOperates {


    public List<MarketActivityRule> queryAllRule();


    public List<MarketActivityRule> queryRuleByPlanId(Long planId);


    public MarketActivityRule queryRuleByRuleId(Long ruleId);


    public List<MarketActivityDish> queryDishByRuleId(Long ruleId);


    public List<MarketPlan> queryMarketPlanList();


    public List<MarketPlanVo> getMarketPlanvOList();

    public MarketTempletVo getMarketTempletVoById(Long ruleId);


    public MarketActivity queryActivityByPlanId(Long planId);


    public List<MarketDynamicCondition> queryConditionByPlanId(Long planId);


    public List<MarketActivityWeekday> queryWeekdayByRuleId(Long ruleId);


    public DishMemberPrice queryDishSpecailPrice(Long dishId);

        public CrmCustomerRightsConfig queryCrmCustomerRightsConfig();
}
