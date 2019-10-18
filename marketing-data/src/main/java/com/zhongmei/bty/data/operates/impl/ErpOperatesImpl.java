package com.zhongmei.bty.data.operates.impl;

import com.zhongmei.yunfu.bean.YFResponse;
import com.zhongmei.yunfu.ShopInfo;
import com.zhongmei.yunfu.http.CalmStringRequest;
import com.zhongmei.yunfu.http.HeaderType;
import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.basemodule.notifycenter.MessagePushPosListResp;
import com.zhongmei.yunfu.net.volley.Request;
import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.monitor.ResponseStringListener;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.erp.operates.ErpOperates;
import com.zhongmei.bty.basemodule.erp.message.ErpCreatTokenResp;
import com.zhongmei.bty.basemodule.erp.message.SelfActivationResp;
import com.zhongmei.bty.basemodule.erp.message.ShopInfoByIdResp;

import java.util.HashMap;
import java.util.Map;


public class ErpOperatesImpl extends AbstractOpeartesImpl implements ErpOperates {

    public ErpOperatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void getErp(ResponseStringListener<YFResponse<ShopInfo>> listener, Response.ErrorListener errorListener) {
        String url = ServerAddressUtil.getInstance().getErpApi();
        CalmStringRequest req = new CalmStringRequest(getContext(), Request.Method.GET,
                url, listener, errorListener);
        req.setHeaderType(HeaderType.HEADER_TYPE_ERP);
        req.setErpReq(true);
        req.setInterceptEnable(true);
        req.executeRequest("get_erp");
    }

    @Override
    public void getShopInfoById(Long shopId, ResponseListener<ShopInfoByIdResp> listener) {

    }

    @Override
    public void createTokenByPhoneNumber(String phoneNumber, ResponseListener<ErpCreatTokenResp> listener) {

    }

    @Override
    public void doActiveDevice(String brandId, String shopNo, String mac, String deviceNo, Integer isMainPos, ResponseListener<SelfActivationResp> listener) {

    }



    @Override
    public void messagePushPosList(String id, ResponseListener<MessagePushPosListResp> listener) {

    }

}
