package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.bty.commonmodule.util.DateUtil;
import com.zhongmei.yunfu.bean.req.CustomerCouponResp;
import com.zhongmei.yunfu.resp.data.LoyaltyTransferResp;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.entity.discount.Coupon;
import com.zhongmei.yunfu.db.enums.CouponType;
import com.zhongmei.bty.basemodule.customer.bean.coupon.CouponVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class CustomerDirectCouponListResp extends LoyaltyTransferResp<List<CustomerDirectCouponListResp.DirectCoupon>> {

    class DirectCoupon {

        Long id;//优惠券模板id

        String couponName;//优惠券模板名称

        Integer couponType;//优惠券类型（2：折扣券，4：代金券）

        String currentStatus;//投放状态，2：投放中

        String activityStartDay;//有效期开始时间

        String activityEndDay;//有效期结束时间

        Integer faceValue;//代金券面额

        Integer fullValue;//满减限制

        String discount;//折扣券折扣
    }

    public List<CouponVo> getCouponVoList() {
        List<CouponVo> couponVoList = new ArrayList<>();

        if (getResult() != null && getResult().size() > 0) {
            DirectCoupon dc;
            CouponVo vo;
            CustomerCouponResp couponInfo;
            Coupon coupon;
            for (int i = 0; i < getResult().size(); i++) {
                dc = getResult().get(i);
                vo = new CouponVo();
                couponInfo = new CustomerCouponResp();
                coupon = new Coupon();
                couponInfo.setId(Long.valueOf(dc.id));
                coupon.setName(dc.couponName);
                coupon.setCouponType(ValueEnums.toEnum(CouponType.class, dc.couponType));
                couponInfo.setEndTime(DateUtil.getCurrentTimeMillis());
                couponInfo.setEndTime(DateUtil.getCurrentTimeMillis());
                if (dc.faceValue != null)
                    couponInfo.setFullValue(new BigDecimal(dc.faceValue));
                if (dc.discount != null)
                    couponInfo.setDiscountValue(new BigDecimal(dc.discount));
                if (dc.fullValue != null) {
                    coupon.setFullValue(BigDecimal.valueOf(dc.fullValue));
                }
//                vo.setCoupon(coupon);
                vo.setCouponInfo(couponInfo);
                vo.setSelected(false);
                couponVoList.add(vo);
            }
        }
        return couponVoList;
    }
}
