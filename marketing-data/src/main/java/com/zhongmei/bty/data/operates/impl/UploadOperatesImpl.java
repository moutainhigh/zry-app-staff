package com.zhongmei.bty.data.operates.impl;

import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.data.operates.UploadOperates;
import com.zhongmei.bty.data.operates.message.content.TokenReq;
import com.zhongmei.bty.data.operates.message.content.TokenResp;
import com.zhongmei.yunfu.resp.data.TransferReq;


public class UploadOperatesImpl extends AbstractOpeartesImpl implements UploadOperates {

    public UploadOperatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void requestToken(TokenReq req, ResponseListener<TokenResp> listener) {

    }
}
