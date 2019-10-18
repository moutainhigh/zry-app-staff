package com.zhongmei.bty.mobilepay.operate;

import com.zhongmei.bty.mobilepay.message.TuanGouCouponDetail;
import com.zhongmei.bty.mobilepay.message.TuanGouCouponReq;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.resp.ResponseListener;


public interface TuanGouOperates extends IOperates {



    void getTuanGouCouponsDetail(TuanGouCouponReq req, ResponseListener<TuanGouCouponDetail> listener);


}
