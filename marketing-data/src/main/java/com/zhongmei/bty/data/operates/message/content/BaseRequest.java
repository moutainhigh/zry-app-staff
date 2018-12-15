package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.resp.data.TransferReq;

/**
 * Created by demo on 2018/12/15
 */
public class BaseRequest {

    public String clientType = "pos";
    public Long brandId; //品牌id
    public Long commercialId; //门店id

    public BaseRequest() {
        clientType = "pos";
        brandId = BaseApplication.getInstance().getBrandIdenty();
        commercialId = BaseApplication.getInstance().getShopIdenty();
    }


    public static TransferReq newTransferReq(String apiKey, BaseRequest request) {
        //CustomerRequest req = new CustomerRequest(phone);
        //String url = ServerAddressUtil.getInstance().loyaltyTransfer();
        TransferReq<BaseRequest> transferReq = new TransferReq<>();
        transferReq.setUrl(apiKey);
        transferReq.setPostData(request);
        return transferReq;
    }
}
