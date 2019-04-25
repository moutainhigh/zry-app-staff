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

/**
 * 所有券的请求接口
 * Created by demo on 2018/12/15
 */
public class CouponsOperatesImpl extends AbstractOpeartesImpl implements CouponsOperates {

    public CouponsOperatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void getWeiXinCouponsDetail(WeiXinCouponsInfoReq weiXinCouponsInfoReq, ResponseListener<WeiXinCouponsInfoResp> listener) {
        /*String url = ServerAddressUtil.getInstance().getWeixinCouponsDetailUrl();
        OpsRequest.Executor<WeiXinCouponsInfoReq, WeiXinCouponsInfoResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(weiXinCouponsInfoReq).responseClass(WeiXinCouponsInfoResp.class).execute(listener, "weixincardDetail");*/
    }

    @Override
    public void getWeiXinCouponsDetail(@NonNull String wxCouponsNo, ResponseListener<LoyaltyTransferResp<WxCouponsInfoResp>> listener) {
        /*String url = ServerAddressUtil.getInstance().loyaltyTransfer();
        TransferReq<WxCouponsInfoReq> transferReq = new TransferReq<>();
        transferReq.setUrl(ServerAddressUtil.getInstance().getCoupInstanceByWxCardNumber());
        transferReq.setPostData(toWxCouponsInfoReq(wxCouponsNo));
        OpsRequest.Executor<TransferReq<WxCouponsInfoReq>, LoyaltyTransferResp<WxCouponsInfoResp>> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq)
                .responseType(OpsRequest.getContentResponseType(LoyaltyTransferResp.class, WxCouponsInfoResp.class))
                .execute(listener, "customerCoupons");*/
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
        /*String url = ServerAddressUtil.getInstance().getMeiTuanCouponsDetailUrl();
        OpsRequest.Executor<TuanGouCouponReq, TuanGouCouponDetail> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(TuanGouCouponDetail.class).execute(listener, "meituancouponDetail");*/

    }

    @Override
    public void ticketInfo(String serialNumber, ResponseListener<TicketInfo> listener) {
        /*String url = ServerAddressUtil.getInstance().ticketInfo();
        TicketInfoReq req = new TicketInfoReq(serialNumber);
        OpsRequest.Executor<TicketInfoReq, TicketInfo> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TicketInfo.class)
                .execute(listener, "ticketInfo");*/
    }
}
