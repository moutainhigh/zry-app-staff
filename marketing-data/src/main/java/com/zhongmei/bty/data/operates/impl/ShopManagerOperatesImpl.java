package com.zhongmei.bty.data.operates.impl;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.bty.basemodule.shopmanager.paymentmanager.operates.ShopManagerOperates;
import com.zhongmei.bty.basemodule.shopmanager.paymentmanager.operators.PaymentsDetailReq;
import com.zhongmei.bty.basemodule.shopmanager.paymentmanager.operators.PaymentsDetailResp;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.bty.basemodule.pay.message.PaymentsInputReq;
import com.zhongmei.bty.basemodule.pay.message.PaymentsInputResp;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.data.TransferReq;

import java.util.List;

/**
 * @Date： 2016/7/21
 * @Description:门店管理网络请求实现类
 * @Version: 1.0
 */
public class ShopManagerOperatesImpl extends AbstractOpeartesImpl implements ShopManagerOperates {

    public ShopManagerOperatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void addPayments(List<PaymentsInputReq> req, ResponseListener<PaymentsInputResp> listener) {
        String url = ServerAddressUtil.getInstance().mindTransfer();
        String mindUrl = ServerAddressUtil.getInstance().getPaymentsInputUrl();
        TransferReq<List<PaymentsInputReq>> transferReq = new TransferReq<>(mindUrl, req);
        OpsRequest.Executor<TransferReq, PaymentsInputResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq)
                .responseClass(PaymentsInputResp.class)
                .execute(listener, mindUrl);//用mind的url作为请求的tag
    }

    @Override
    public void getPaymentsDetail(String startDate, String endDate, ResponseListener<PaymentsDetailResp> listener) {
        String url = ServerAddressUtil.getInstance().mindTransfer();

        String mindUrl = ServerAddressUtil.getInstance().getPaymentsDetailUrl();
        PaymentsDetailReq req = new PaymentsDetailReq();
        req.setBrandIdenty(BaseApplication.getInstance().getBrandIdenty());
        req.setShopIdenty(BaseApplication.getInstance().getShopIdenty());
        req.setStartDate(startDate);
        req.setEndDate(endDate);

        TransferReq<PaymentsDetailReq> transferReq = new TransferReq<>(mindUrl, req);
        OpsRequest.Executor<TransferReq, PaymentsDetailResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq)
                .responseClass(PaymentsDetailResp.class)
                .execute(listener, mindUrl);//用mind的url作为请求的tag
    }
}
