package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.bty.basemodule.customer.operates.CouponDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.bean.req.CustomerCouponResp;
import com.zhongmei.yunfu.resp.data.LoyaltyTransferResp;
import com.zhongmei.bty.basemodule.customer.bean.coupon.CouponVo;

import java.util.ArrayList;
import java.util.List;



public class CustomerDirectCouponListV2Resp extends LoyaltyTransferResp<List<CustomerDirectCouponListV2Resp.DirectCouponV2>> {

    class DirectCouponV2 {

        Long id;
        String couponName;
        Integer couponType;
        Integer currentStatus;
        Integer validType;
        Integer isCurDay;
        Integer validDayNum;
        String validStartDay;
        String validEndDay;
        Integer faceValue;
        Integer fullValue;
        String discount;    }

    public List<CouponVo> getCouponVoList() {
        List<CustomerCouponResp> couponInfoList = new ArrayList<>();

        if (getResult() != null && getResult().size() > 0) {
            DirectCouponV2 dc;
            CustomerCouponResp couponInfo;
            for (int i = 0; i < getResult().size(); i++) {
                dc = getResult().get(i);
                couponInfo = new CustomerCouponResp();
                couponInfo.setId(Long.valueOf(dc.id));
                couponInfoList.add(couponInfo);
            }
        }
        try {
            return OperatesFactory.create(CouponDal.class).findCouponVoListByCouponInfos(couponInfoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
