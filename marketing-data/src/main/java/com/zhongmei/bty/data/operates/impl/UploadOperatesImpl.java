package com.zhongmei.bty.data.operates.impl;

import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.data.operates.UploadOperates;
import com.zhongmei.bty.data.operates.message.content.TokenReq;
import com.zhongmei.bty.data.operates.message.content.TokenResp;
import com.zhongmei.yunfu.resp.data.TransferReq;

/**
 * Created by demo on 2018/12/15
 */
public class UploadOperatesImpl extends AbstractOpeartesImpl implements UploadOperates {

    public UploadOperatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void requestToken(TokenReq req, ResponseListener<TokenResp> listener) {
        /*String url = ServerAddressUtil.getInstance().mindTransfer();
        String mindUrl = ServerAddressUtil.getInstance().getToken();

        TransferReq<TokenReq> transferReq = new TransferReq<>(mindUrl, req);
        OpsRequest.Executor<TransferReq, TokenResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq)
                .responseClass(TokenResp.class)
                .execute(listener, mindUrl);*/
    }
}
