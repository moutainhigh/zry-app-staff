package com.zhongmei.bty.basemodule.customer.bean;

import com.zhongmei.yunfu.context.util.GsonUtil;

public class CustomerStatistic {
    public Double memberBalance;
    public String memberExpenditure;
    public Long memberIntegral;
    public Integer memberCoupons;
    public String bookingCount;
    public String realConsumeCount;
    public String arriveCount;
    public String phoneCount;
    public String consumPerCount;
    public String messageCount;

    public static CustomerStatistic initFromJson(String response) {
        return GsonUtil.jsonToObject(response, CustomerStatistic.class);
    }
}
