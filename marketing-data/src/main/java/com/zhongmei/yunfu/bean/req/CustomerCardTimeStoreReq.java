package com.zhongmei.yunfu.bean.req;

import com.zhongmei.yunfu.bean.YFPageReq;

/**
 * 会员登录的请求数据
 *
 * @version: 1.0
 * @date 2015年5月13日
 */
public class CustomerCardTimeStoreReq extends YFPageReq {

    private Integer tradeStatus; //订单状态,3:未结账,4:已支付,5:已退货,6:已作废
    private String cardNum; //客户手机号或实体卡号

    public CustomerCardTimeStoreReq(Integer tradeStatus, String cardNum, Integer page, Integer pageSize) {
        super(page, pageSize);
        this.tradeStatus = tradeStatus;
        this.cardNum = cardNum;
    }

    public Integer getTradeStatus() {
        return tradeStatus;
    }

    public String getCardNum() {
        return cardNum;
    }
}
