package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.bty.basemodule.customer.operates.CouponDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.bean.req.CustomerCouponResp;
import com.zhongmei.yunfu.resp.data.LoyaltyTransferResp;
import com.zhongmei.bty.basemodule.customer.bean.coupon.CouponVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class CustomerDirectCouponListV2Resp extends LoyaltyTransferResp<List<CustomerDirectCouponListV2Resp.DirectCouponV2>> {

    class DirectCouponV2 {

        Long id;//优惠券模板id

        String couponName;//优惠券模板名称

        Integer couponType;//优惠券类型（2：折扣券，4：代金券）

        Integer currentStatus;//投放状态，2：投放中

        Integer validType;//券有效期类型(1:动态, 2:固定)

        Integer isCurDay;//是否当天有效(validType=1时指定)

        Integer validDayNum;//券有效期天数(validType=1时指定)

        String validStartDay;//券有效期开始时间(validType=2时指定)

        String validEndDay;//券有效期结束时间(validType=2时指定)

        Integer faceValue;//代金券面额

        Integer fullValue;//满减限制

        String discount;//折扣券折扣
    }

    public List<CouponVo> getCouponVoList() {
        List<CustomerCouponResp> couponInfoList = new ArrayList<>();

        if (getResult() != null && getResult().size() > 0) {
            DirectCouponV2 dc;
            CustomerCouponResp couponInfo;
            for (int i = 0; i < getResult().size(); i++) {
                dc = getResult().get(i);
                couponInfo = new CustomerCouponResp();
                couponInfo.setId(Long.valueOf(dc.id));
//                couponInfo.setValidStartDate(dc.validStartDay);
//                couponInfo.setValidEndDate(dc.validEndDay);
//                couponInfo.setIsCurDay(dc.isCurDay);
//                couponInfo.setValidType(dc.validType);
//                couponInfo.setValidDayNum(dc.validDayNum);
//                if(dc.faceValue != null)
//                    couponInfo.setFaceValue(dc.faceValue);
//                if(dc.discount != null)
//                couponInfo.setDiscount(dc.discount);
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
