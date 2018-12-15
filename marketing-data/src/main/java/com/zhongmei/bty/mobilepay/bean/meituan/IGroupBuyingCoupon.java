package com.zhongmei.bty.mobilepay.bean.meituan;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 * 团购券属性相关接口
 */

public interface IGroupBuyingCoupon extends Serializable {
    public Long getGrouponId();

    public String getSerialNumber();

    public String getBuyDate();

    public String getMobile();

    public int getStatus();

    public String getStatusDesc();

    public int getMinConsume();

    public int getMaxConsume();

    public String getDealTitle();

    public Long getDealId();

    public int getCanCancel();

    public Long getBeginTime();

    public Long getEndTime();

    public BigDecimal getMarketPrice();

    public BigDecimal getPrice();

    public String getRemark();

    public int getCouponType();

    public List<CouponDishLimit> getLimitDish();

}
