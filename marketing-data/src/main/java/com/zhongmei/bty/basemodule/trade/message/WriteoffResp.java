package com.zhongmei.bty.basemodule.trade.message;

/**
 * Created by demo on 2018/12/15
 * 销账接口下行处理
 */

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
