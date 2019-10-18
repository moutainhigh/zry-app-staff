package com.zhongmei.bty.basemodule.customer.operates;

import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;

import com.zhongmei.bty.basemodule.customer.db.CoupDish;
import com.zhongmei.bty.basemodule.customer.bean.coupon.CouponVo;
import com.zhongmei.yunfu.bean.req.CustomerCouponResp;
import com.zhongmei.yunfu.db.entity.discount.CoupRule;
import com.zhongmei.yunfu.db.entity.discount.Coupon;



public interface CouponDal extends IOperates {

    public Coupon findCouponById(Long couponId) throws Exception;

    public List<CoupRule> findCoupRuleById(Long couponId) throws Exception;

    public CouponVo findCouponVoByCouponInfo(CustomerCouponResp couponInfo) throws Exception;

    public List<CouponVo> findCouponVoListByCouponInfos(List<CustomerCouponResp> coupponInfoList) throws Exception;

    public List<CoupDish> findCoupDishById(Long couponId) throws Exception;
}
