package com.zhongmei.bty.basemodule.discount.bean;


import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 微信卡券信息
 * Created by demo on 2018/12/15
 */
public class WeiXinCashCouponsInfo implements Serializable {

    private BigDecimal least_cost;//起用金额（单位为分）

    private BigDecimal reduce_cost;//减免金额（单位为分）

    private WeiXinCouponsBaseInfo base_info;//基本信息

    private String title;//卡券名

    public BigDecimal getLeast_cost() {
        return least_cost.divide(new BigDecimal(100));
    }

    public void setLeast_cost(BigDecimal least_cost) {
        this.least_cost = least_cost;
    }

    public BigDecimal getReduce_cost() {
        return reduce_cost.divide(new BigDecimal(100));
    }

    public void setReduce_cost(BigDecimal reduce_cost) {
        this.reduce_cost = reduce_cost;
    }

    public String getTitle() {
        return getBase_info().title;
    }

    public WeiXinCouponsBaseInfo getBase_info() {
        return base_info;
    }

    public void setBase_info(WeiXinCouponsBaseInfo base_info) {
        this.base_info = base_info;
    }

    public static class WeiXinCouponsBaseInfo implements Serializable {

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        private String title;//卡券名

    }


}
