package com.zhongmei.bty.basemodule.devices.mispos.data.message;

/**
 * 会员登录的请求数据
 *
 * @version: 1.0
 * @date 2015年5月13日
 */
public class CustomerSellcardReq {
    private String cardNo;//卡号
    private long pageSize;//返回数量
    private int tradeStatus;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public int getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(int tradeStatus) {
        this.tradeStatus = tradeStatus;
    }


}
