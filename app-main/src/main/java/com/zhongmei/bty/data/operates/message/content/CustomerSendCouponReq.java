package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class CustomerSendCouponReq extends BaseRequest {

    private Long customerId;

//    private List<CouponNum> couponInfoList;

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

        Long couponId;//发券模板id

        Integer num;//每个模板发券张数，本迭代都是发一张券
    }
}
