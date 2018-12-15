package com.zhongmei.bty.basemodule.devices.mispos.data.message;


/**
 * @Date：2016年3月16日
 * @Description:售卡订单详情查询
 * @Version: 1.0
 */
public class CustomerSellcardDetailReq {
    private Long tradeId;
    //操作人id
    private Long userId;

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


}
