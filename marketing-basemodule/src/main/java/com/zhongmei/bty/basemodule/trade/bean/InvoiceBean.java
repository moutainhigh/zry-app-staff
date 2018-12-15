package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

/**
 * 打印电子发票申请单需要的参数
 */
public class InvoiceBean {
    private String deskNo;//桌台号或者取餐号
    private String tradeNo;//订单号
    private String amount;//实付金额
    private String url;//二维码URL
    private String operator;//操作员
    private TradeVo tradeVo;


    public String getDeskNo() {
        return deskNo;
    }

    public void setDeskNo(String deskNo) {
        this.deskNo = deskNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public TradeVo getTradeVo() {
        return tradeVo;
    }

    public void setTradeVo(TradeVo tradeVo) {
        this.tradeVo = tradeVo;
    }

    @Override
    public String toString() {
        return "[deskNo:" + deskNo + ",tradeNo:" + tradeNo + ",amount" + amount + ",url:" + url + ",operator:" + operator + "]";
    }


}
