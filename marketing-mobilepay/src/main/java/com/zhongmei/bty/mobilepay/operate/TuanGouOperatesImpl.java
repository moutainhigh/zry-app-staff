package com.zhongmei.bty.mobilepay.operate;

import android.content.Context;

import com.zhongmei.bty.mobilepay.message.TuanGouCouponDetail;
import com.zhongmei.bty.mobilepay.message.TuanGouCouponReq;
import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.resp.ResponseListener;

/**
 * Created by demo on 2018/12/15
 */

public class TuanGouOperatesImpl implements TuanGouOperates, IOperates.ImplContext {
    private Context mContext;

    public TuanGouOperatesImpl(Context context) {
        mContext = context;
    }


    @Override
    public void getTuanGouCouponsDetail(TuanGouCouponReq req, ResponseListener<TuanGouCouponDetail> listener) {
        String url = ServerAddressUtil.getInstance().getMeiTuanCouponsDetailUrl();
        OpsRequest.Executor<TuanGouCouponReq, TuanGouCouponDetail> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(TuanGouCouponDetail.class).execute(listener, "meituancouponDetail");


    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
