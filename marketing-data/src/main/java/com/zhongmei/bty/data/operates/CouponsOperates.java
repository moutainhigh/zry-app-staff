package com.zhongmei.bty.data.operates;

import android.support.annotation.NonNull;

import com.zhongmei.bty.mobilepay.message.TicketInfo;
import com.zhongmei.bty.mobilepay.message.TuanGouCouponDetail;
import com.zhongmei.yunfu.resp.data.LoyaltyTransferResp;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.mobilepay.message.TuanGouCouponReq;
import com.zhongmei.bty.data.operates.message.content.WeiXinCouponsInfoReq;
import com.zhongmei.bty.data.operates.message.content.WeiXinCouponsInfoResp;
import com.zhongmei.bty.data.operates.message.content.WxCouponsInfoResp;


public interface CouponsOperates extends IOperates {



    @Deprecated
    void getWeiXinCouponsDetail(WeiXinCouponsInfoReq weiXinCouponsInfoReq, ResponseListener<WeiXinCouponsInfoResp> listener);


    void getWeiXinCouponsDetail(@NonNull String wxCouponsNo, ResponseListener<LoyaltyTransferResp<WxCouponsInfoResp>> listener);


    void getMeiTuanCouponsDetail(TuanGouCouponReq req, ResponseListener<TuanGouCouponDetail> listener);


    void ticketInfo(String serialNumber, ResponseListener<TicketInfo> listener);
}
