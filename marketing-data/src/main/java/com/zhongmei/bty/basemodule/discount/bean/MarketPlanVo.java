package com.zhongmei.bty.basemodule.discount.bean;

import com.zhongmei.yunfu.db.entity.discount.MarketPlan;
import com.zhongmei.bty.basemodule.discount.enums.ActivityType;
import com.zhongmei.bty.basemodule.discount.enums.PromotionType;
import com.zhongmei.bty.basemodule.discount.enums.UserType;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Date：2016-5-20 下午1:22:24
 * @Description: TODO
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class MarketPlanVo implements java.io.Serializable {
    /**
     * @date：2016-5-20 下午1:21:07
     * @Description:营销活动信息
     */
    private static final long serialVersionUID = 1L;

    private MarketPlan marketPlan;

    // 优惠类型
    private PromotionType promotionType;

    // 营销方式：1单商品 2多商品
    private ActivityType activityType;

    // 用户类型
    private Set<UserType> userTypes = new HashSet<UserType>();

    // 订单类型
    private Set<BusinessType> businessTypes = new HashSet<BusinessType>();

    // 单据类型
    private Set<DeliveryType> deliveryTypes = new HashSet<DeliveryType>();
    //参与终端
    private List<Integer> activityPos = new ArrayList<Integer>();

    public MarketPlanVo(MarketPlan plan) {
        marketPlan = plan;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((marketPlan == null) ? 0 : marketPlan.getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MarketPlanVo other = (MarketPlanVo) obj;
        if (marketPlan == null) {
            if (other.marketPlan != null)
                return false;
        } else if (!marketPlan.equals(other.marketPlan))
            return false;
        return true;
    }

    public PromotionType getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(PromotionType promotionType) {
        this.promotionType = promotionType;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public Set<UserType> getUserTypes() {
        return userTypes;
    }

    public void setUserType(UserType userType) {
        this.userTypes.add(userType);
    }

    public Set<BusinessType> getBusinessTypes() {
        return businessTypes;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessTypes.add(businessType);
    }

    public MarketPlan getMarketPlan() {
        return marketPlan;
    }

    public Set<DeliveryType> getDeliveryTypes() {
        return deliveryTypes;
    }

    public void setDeliveryType(DeliveryType deliveryType) {

        this.deliveryTypes.add(deliveryType);
    }

    public List<Integer> getActivityPos() {
        return activityPos;
    }

    public void setActivityPos(Integer activityPosType) {
        this.activityPos.add(activityPosType);
    }
}
