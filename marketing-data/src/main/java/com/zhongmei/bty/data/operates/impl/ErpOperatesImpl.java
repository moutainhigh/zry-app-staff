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

/**
 * Created by demo on 2018/12/15
 */
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
        String url = ServerAddressUtil.getInstance().getErpShopInfo();
        url += shopId;
//        BaseApplication application = BaseApplication.sInstance;
//
//        OpsRequest.GetExecutor
//        CalmStringRequest req = new CalmStringRequest(application, Request.Method.GET,
//                url, listener, errorListener);
//        req.setErpReq(true);
//        req.executeRequest("getShopInfoById", "", fm);

      /*  ErpGetRequest.ErpGetExecutor<ShopInfoByIdResp> executor = ErpGetRequest.ErpGetExecutor.create(url);
        executor.responseClass(ShopInfoByIdResp.class).execute(listener, "getShopInfoById");*/

        OpsRequest.Executor<String, ShopInfoByIdResp> executor = OpsRequest.Executor.create(url);
        executor.setMethod(Request.Method.GET)
                .setHeaderType(HeaderType.HEADER_TYPE_ERP)
                .responseClass(ShopInfoByIdResp.class)
                .execute(listener, "getShopInfoById", true);
    }

    @Override
    public void createTokenByPhoneNumber(String phoneNumber, ResponseListener<ErpCreatTokenResp> listener) {
        String url = ServerAddressUtil.getInstance().createTokenByPhoneNumberUrl();
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phoneNumber);
        url = Utils.createGetUrl(url, params);
        /*ErpGetRequest.ErpGetExecutor<ErpCreatTokenResp> executor = ErpGetRequest.ErpGetExecutor.create(url);
        executor.responseClass(ErpCreatTokenResp.class).execute(listener, "getShopInfoById");*/
        OpsRequest.Executor<String, ErpCreatTokenResp> executor = OpsRequest.Executor.create(url);
        executor.setMethod(Request.Method.GET)
                .setHeaderType(HeaderType.HEADER_TYPE_ERP)
                .responseClass(ErpCreatTokenResp.class)
                .execute(listener, "createTokenByPhoneNumber", true);
    }

    @Override
    public void doActiveDevice(String brandId, String shopNo, String mac, String deviceNo, Integer isMainPos, ResponseListener<SelfActivationResp> listener) {
        String url = ServerAddressUtil.getInstance().activation();//接口还没有出
        Map<String, String> params = new HashMap<String, String>();
        params.put("brandId", brandId);
        params.put("commercialId", shopNo);
        params.put("mac", mac);
        params.put("padNo", deviceNo);
        params.put("isMainPos", isMainPos + "");
        params.put("isKDS", "2");
        params.put("applicationType", "5");

        url = Utils.createGetUrl(url, params);


       /* ErpPostRequest.ErpPostExecutor<SelfActivationResp> executor= ErpPostRequest.ErpPostExecutor.create(url);
//        ErpGetRequest.ErpGetExecutor<SelfActivationResp> executor = ErpGetRequest.ErpGetExecutor.create(url);
        executor.responseClass(SelfActivationResp.class).execute(listener, "activeDevice");
*/
        OpsRequest.Executor<String, SelfActivationResp> executor = OpsRequest.Executor.create(url);
        executor.setMethod(Request.Method.POST)
                .setHeaderType(HeaderType.HEADER_TYPE_ERP)
                .responseClass(SelfActivationResp.class)
                .execute(listener, "activeDevice", true);
    }

   /* @Override
    public void checkPhoneToken(String phoneNumber, String token, ErpResponseListener<ShopInfoByIdResp> listener) {
        String url = ServerAddressUtil.getInstance().getCheckPhoneTokenUrl();
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phoneNumber);
        params.put("token", token);
        url = Utils.createGetUrl(url, params);
        ErpGetRequest.ErpGetExecutor<ShopInfoByIdResp> executor = ErpGetRequest.ErpGetExecutor.create(url);
        executor.responseClass(ShopInfoByIdResp.class).execute(listener, "getShopInfoById");
    }*/

    @Override
    public void messagePushPosList(String id, ResponseListener<MessagePushPosListResp> listener) {
        String url = ServerAddressUtil.getInstance().getMessagePushPosListUrl();
        url += id;
       /* ErpGetRequest.ErpGetExecutor<MessagePushPosListResp> executor = ErpGetRequest.ErpGetExecutor.create(url);
        executor.responseClass(MessagePushPosListResp.class).execute(listener, "messagePushPosList");*/

        OpsRequest.Executor<String, MessagePushPosListResp> executor = OpsRequest.Executor.create(url);
        executor.setMethod(Request.Method.GET)
                .setHeaderType(HeaderType.HEADER_TYPE_ERP)
                .responseClass(MessagePushPosListResp.class)
                .execute(listener, "messagePushPosList", true);
    }

}
