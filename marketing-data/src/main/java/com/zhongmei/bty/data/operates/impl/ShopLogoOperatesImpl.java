package com.zhongmei.bty.data.operates.impl;

import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.basemodule.shopmanager.shoplogo.ShopLogReq;
import com.zhongmei.bty.basemodule.shopmanager.shoplogo.ShopLogoOperates;
import com.zhongmei.bty.basemodule.shopmanager.shoplogo.ShopLogoResp;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.resp.data.TransferReq;
import com.zhongmei.yunfu.resp.ResponseListener;

/**
 * Created by demo on 2018/12/15
 */
public class ShopLogoOperatesImpl extends AbstractOpeartesImpl implements ShopLogoOperates {

    public ShopLogoOperatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void requestUrl(ShopLogReq req, ResponseListener<ShopLogoResp> listener) {
        String url = ServerAddressUtil.getInstance().mindTransfer();
        String mindUrl = ServerAddressUtil.getInstance().getLogoURLRequest();
        TransferReq<ShopLogReq> transferReq = new TransferReq<>(mindUrl, req);


        OpsRequest.Executor<TransferReq, ShopLogoResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq)
                .responseClass(ShopLogoResp.class)
                .execute(listener, mindUrl);
    }

}
