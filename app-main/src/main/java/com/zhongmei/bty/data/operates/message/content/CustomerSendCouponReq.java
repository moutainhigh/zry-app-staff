package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

import java.util.List;



public class CustomerSendCouponReq extends BaseRequest {

    private Long customerId;


    private List<CouponNum> couponInfoList;

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setCouponInfoList(List<CouponNum> couponInfoList) {
        this.couponInfoList = couponInfoList;
    }

    public class CouponNum {
        public CouponNum(Long couponId, Integer num) {
            this.couponId = couponId;
            this.num = num;
        }

        Long couponId;
        Integer num;    }
}
