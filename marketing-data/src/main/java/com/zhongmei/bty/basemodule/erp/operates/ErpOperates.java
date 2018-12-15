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

/**
 * Created by demo on 2018/12/15
 */
public interface ErpOperates extends IOperates {

    /**
     * 获取当前设备配置的商户信息
     *
     * @param listener
     * @param errorListener
     */
    void getErp(ResponseStringListener<YFResponse<ShopInfo>> listener, Response.ErrorListener errorListener);

    /**
     * 获取指定门店信息
     *
     * @param shopId
     * @param listener
     */
    void getShopInfoById(Long shopId, ResponseListener<ShopInfoByIdResp> listener);

    /**
     * 获取验证码
     *
     * @param phoneNumber
     * @param listener
     */
    void createTokenByPhoneNumber(String phoneNumber, ResponseListener<ErpCreatTokenResp> listener);

    /**
     * 获取验证码
     * @param phoneNumber
     * @param token
     * @param listener
     */
    /* void checkPhoneToken(String phoneNumber,String token,ErpResponseListener<ShopInfoByIdResp> listener);*/


    /**
     * 激活设备
     *
     * @param brandId   品牌id
     * @param shopNo    商户编号
     * @param mac       mac地址
     * @param deviceNo  设备编号
     * @param isMainPos 是否为主pos
     * @param listener;
     */
    void doActiveDevice(String brandId, String shopNo, String mac, String deviceNo, Integer isMainPos, ResponseListener<SelfActivationResp> listener);

    /**
     * ERP消息点击量统计
     *
     * @param id        消息ID
     * @param listener;
     */
    void messagePushPosList(String id, ResponseListener<MessagePushPosListResp> listener);

}
