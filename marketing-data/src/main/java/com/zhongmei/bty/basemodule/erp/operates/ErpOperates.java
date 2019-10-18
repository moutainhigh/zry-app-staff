package com.zhongmei.bty.basemodule.erp.operates;

import com.zhongmei.bty.basemodule.erp.message.ErpCreatTokenResp;
import com.zhongmei.bty.basemodule.erp.message.SelfActivationResp;
import com.zhongmei.bty.basemodule.erp.message.ShopInfoByIdResp;
import com.zhongmei.bty.basemodule.notifycenter.MessagePushPosListResp;
import com.zhongmei.yunfu.bean.YFResponse;
import com.zhongmei.yunfu.ShopInfo;
import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.monitor.ResponseStringListener;


public interface ErpOperates extends IOperates {


    void getErp(ResponseStringListener<YFResponse<ShopInfo>> listener, Response.ErrorListener errorListener);


    void getShopInfoById(Long shopId, ResponseListener<ShopInfoByIdResp> listener);


    void createTokenByPhoneNumber(String phoneNumber, ResponseListener<ErpCreatTokenResp> listener);






    void doActiveDevice(String brandId, String shopNo, String mac, String deviceNo, Integer isMainPos, ResponseListener<SelfActivationResp> listener);


    void messagePushPosList(String id, ResponseListener<MessagePushPosListResp> listener);

}
