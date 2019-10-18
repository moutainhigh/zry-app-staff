package com.zhongmei.beauty.operates.message;

import com.zhongmei.bty.basemodule.inventory.message.InventoryChangeReq;



public class BeautyBookingTradeReq extends BeautyModifyReq {

    public TradeBookingRequesst bookingInfo;

    public static class TradeBookingRequesst {
        public Long bookingId;
        public String bookingUuid;
                public Long bookingServerUpdateTime;
    }

}
