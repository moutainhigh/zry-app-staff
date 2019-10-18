package com.zhongmei.bty.data.operates.impl;

import android.support.annotation.NonNull;

import com.zhongmei.bty.mobilepay.message.TuanGouCouponDetail;
import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.mobilepay.message.TicketInfo;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.resp.data.LoyaltyTransferResp;
import com.zhongmei.yunfu.resp.data.TransferReq;
import com.zhongmei.bty.data.operates.CouponsOperates;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.data.operates.message.content.TicketInfoReq;
import com.zhongmei.bty.mobilepay.message.TuanGouCouponReq;
import com.zhongmei.bty.data.operates.message.content.WeiXinCouponsInfoReq;
import com.zhongmei.bty.data.operates.message.content.WeiXinCouponsInfoResp;
import com.zhongmei.bty.data.operates.message.content.WxCouponsInfoReq;
import com.zhongmei.bty.data.operates.message.content.WxCouponsInfoResp;


public class CouponsOperatesImpl extends AbstractOpeartesImpl implements CouponsOperates {

    public CouponsOperatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void getWeiXinCouponsDetail(WeiXinCouponsInfoReq weiXinCouponsInfoReq, ResponseListener<WeiXinCouponsInfoResp> listener) {

    }

    @Override
    public void getWeiXinCouponsDetail(@NonNull String wxCouponsNo, ResponseListener<LoyaltyTransferResp<WxCouponsInfoResp>> listener) {

    }

    private WxCouponsInfoReq toWxCouponsInfoReq(String wxCouponsNo) {
        WxCouponsInfoReq req = new WxCouponsInfoReq();
        req.setBrandId(BaseApplication.getInstance().getBrandIdenty());
        req.setCommercialId(BaseApplication.getInstance().getShopIdenty());
        req.setCodeNumber(wxCouponsNo);

        return req;
    }

    @Override
    public void getMeiTuanCouponsDetail(TuanGouCouponReq req, ResponseListener<TuanGouCouponDetail> listener) {


    }

    @Override
    public void ticketInfo(String serialNumber, ResponseListener<TicketInfo> listener) {

    }
}
