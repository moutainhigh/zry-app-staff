package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.yunfu.resp.data.LoyaltyTransferResp;

public class CustomTimesResp extends LoyaltyTransferResp<CustomTimesResp.Resp> {


    public class Consume {
        public Long totalTradeCount;
    }

    public class Resp {
        public Consume consume;
    }
}
