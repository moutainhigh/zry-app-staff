package com.zhongmei.bty.basemodule.trade.message;



public class WriteoffResp {
    private Long tradeId;
    private String tradeNo;
    private String tradeUuid;
    private String payUrl;

    public Long getTradeId() {
        return tradeId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public String getPayUrl() {
        return payUrl;
    }

}
