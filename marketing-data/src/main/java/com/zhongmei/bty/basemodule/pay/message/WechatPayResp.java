package com.zhongmei.bty.basemodule.pay.message;

/**
 * @Date：2015-7-8 下午3:41:01
 * @Description: TODO
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class WechatPayResp {
    /**
     * 状态码（ 0： 成功， -1： 失败）
     */
    private Integer status;

    /**
     * 支付状态 (2:已支付，1，未支付)
     */
    private Integer payState;

    /**
     * 状态码描述
     */
    private String message;

    /**
     * 商户网站唯一订单号
     */
    private String tradeNumber;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTradeNumber() {
        return tradeNumber;
    }

    public void setTradeNumber(String tradeNumber) {
        this.tradeNumber = tradeNumber;
    }
}
