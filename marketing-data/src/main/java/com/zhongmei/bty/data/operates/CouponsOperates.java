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

/**
 * Created by demo on 2018/12/15
 */
public interface CouponsOperates extends IOperates {


    /**
     * 微信卡券详情。此方法不阻塞调用线程
     *
     * @param req
     * @param listener
     */
    @Deprecated
    void getWeiXinCouponsDetail(WeiXinCouponsInfoReq weiXinCouponsInfoReq, ResponseListener<WeiXinCouponsInfoResp> listener);

    /**
     * 获取微信卡券详情
     *
     * @param wxCouponsNo 微信卡券卡号
     * @param listener    回调方法
     */
    void getWeiXinCouponsDetail(@NonNull String wxCouponsNo, ResponseListener<LoyaltyTransferResp<WxCouponsInfoResp>> listener);

    /**
     * 美团团购券详情
     */
    void getMeiTuanCouponsDetail(TuanGouCouponReq req, ResponseListener<TuanGouCouponDetail> listener);

    /**
     * 点评查询单张团购券
     *
     * @param serialNumber
     * @param listener
     */
    void ticketInfo(String serialNumber, ResponseListener<TicketInfo> listener);
}
