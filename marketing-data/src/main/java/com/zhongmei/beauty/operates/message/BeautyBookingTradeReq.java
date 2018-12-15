package com.zhongmei.beauty.operates.message;

import com.zhongmei.bty.basemodule.inventory.message.InventoryChangeReq;

/**
 * Created by demo on 2018/12/15
 * 预定单转开单的时候也要用
 */

public class BeautyBookingTradeReq extends BeautyModifyReq {

    public TradeBookingRequesst bookingInfo;

    public static class TradeBookingRequesst {
        public Long bookingId;
        public String bookingUuid;
        //        预定最后更新时间
        public Long bookingServerUpdateTime;
    }

}
