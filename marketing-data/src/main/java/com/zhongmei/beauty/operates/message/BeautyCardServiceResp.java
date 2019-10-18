package com.zhongmei.beauty.operates.message;

import com.zhongmei.bty.basemodule.beauty.BeautyCardServiceInfo;
import com.zhongmei.yunfu.resp.data.LoyaltyTransferResp;

import java.util.List;


public class BeautyCardServiceResp extends LoyaltyTransferResp<BeautyCardServiceResp.BeautyCardService> {

    public class BeautyCardService {
        public List<BeautyCardServiceInfo> simpleCardInstanceVoList;
    }
}
