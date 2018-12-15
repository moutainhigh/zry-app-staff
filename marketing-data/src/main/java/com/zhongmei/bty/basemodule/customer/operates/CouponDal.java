package com.zhongmei.bty.basemodule.customer.operates;

import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;

import com.zhongmei.bty.basemodule.customer.db.CoupDish;
import com.zhongmei.bty.basemodule.customer.bean.coupon.CouponVo;
import com.zhongmei.yunfu.bean.req.CustomerCouponResp;
import com.zhongmei.yunfu.db.entity.discount.CoupRule;
import com.zhongmei.yunfu.db.entity.discount.Coupon;


/**
 * 操作coupon表(优惠券模板)及coup_rule(优惠券规则)表的接口
 *
 * @Date：2015-8-11 上午10:29:24
 * @Description: TODO
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public interface CouponDal extends IOperates {

    public Coupon findCouponById(Long couponId) throws Exception;

    public List<CoupRule> findCoupRuleById(Long couponId) throws Exception;

    public CouponVo findCouponVoByCouponInfo(CustomerCouponResp couponInfo) throws Exception;

    public List<CouponVo> findCouponVoListByCouponInfos(List<CustomerCouponResp> coupponInfoList) throws Exception;

    public List<CoupDish> findCoupDishById(Long couponId) throws Exception;
}
