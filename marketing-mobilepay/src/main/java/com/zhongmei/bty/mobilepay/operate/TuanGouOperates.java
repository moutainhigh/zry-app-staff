package com.zhongmei.bty.mobilepay.operate;

import com.zhongmei.bty.mobilepay.message.TuanGouCouponDetail;
import com.zhongmei.bty.mobilepay.message.TuanGouCouponReq;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.resp.ResponseListener;

/**
 * Created by demo on 2018/12/15
 */
public interface TuanGouOperates extends IOperates {


    /**
     * 美团团购券，百度糯米，口碑券详情
     */
    void getTuanGouCouponsDetail(TuanGouCouponReq req, ResponseListener<TuanGouCouponDetail> listener);


}
