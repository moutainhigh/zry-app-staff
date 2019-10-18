package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.resp.data.TransferReq;


public class BaseRequest {

    public String clientType = "pos";
    public Long brandId;     public Long commercialId;
    public BaseRequest() {
        clientType = "pos";
        brandId = BaseApplication.getInstance().getBrandIdenty();
        commercialId = BaseApplication.getInstance().getShopIdenty();
    }


    public static TransferReq newTransferReq(String apiKey, BaseRequest request) {
                        TransferReq<BaseRequest> transferReq = new TransferReq<>();
        transferReq.setUrl(apiKey);
        transferReq.setPostData(request);
        return transferReq;
    }
}
