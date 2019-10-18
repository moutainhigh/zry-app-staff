package com.zhongmei.beauty.operates.message;

import com.zhongmei.yunfu.resp.ResponseObject;

import java.math.BigDecimal;
import java.util.List;


public class BeautyActivityBuyRecordResp extends ResponseObject<List<BeautyActivityBuyRecordResp>> {



    public String marketingName;


    public BigDecimal costPrice;


    public BigDecimal activityPrice;


    public Integer type;


    public Long planStartDate;


    public Long planEndDate;

    public Long useStartDate;

    public Long useEndDate;

    public String describe;

    public String remark;


    public Long dishId;


    public String dishName;


    public Integer goodsNum = new Integer(1);

    public Long id;


    public Long validityPeriod;
        public boolean isUsed = false;
}
