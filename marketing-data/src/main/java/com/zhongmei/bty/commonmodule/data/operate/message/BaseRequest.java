package com.zhongmei.bty.commonmodule.data.operate.message;

import com.zhongmei.yunfu.context.base.BaseApplication;

/**
 * Created by demo on 2018/12/15
 */
public class BaseRequest {

    public String clientType = "pos";
    public Long brandIdenty; //品牌id
    public Long shopIdenty; //门店id

    public BaseRequest() {
        clientType = "pos";
        brandIdenty = BaseApplication.sInstance.getBrandIdenty();
        shopIdenty = BaseApplication.sInstance.getShopIdenty();
    }


    /*public static TransferReq newTransferReq(String apiKey, BaseRequest request) {
        //CustomerRequest req = new CustomerRequest(phone);
        //String url = ServerAddressUtil.getInstance().loyaltyTransfer();
        TransferReq<BaseRequest> transferReq = new TransferReq<>();
        transferReq.setUrl(apiKey);
        transferReq.setPostData(request);
        return transferReq;
    }*/
}
