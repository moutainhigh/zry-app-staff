package com.zhongmei.beauty.operates.message;

import com.zhongmei.bty.basemodule.beauty.BeautyCardServiceInfo;
import com.zhongmei.yunfu.resp.data.LoyaltyTransferResp;

import java.util.List;

/**
 * 次卡服务信息
 *
 * @date 2018/6/14
 */
public class BeautyCardServiceResp extends LoyaltyTransferResp<BeautyCardServiceResp.BeautyCardService> {

    public class BeautyCardService {
        public List<BeautyCardServiceInfo> simpleCardInstanceVoList;
    }
}
