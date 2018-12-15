package com.zhongmei.bty.basemodule.pay.message;

/**
 * 支付二维码码串接口的回复数据
 *
 * @version: 1.0
 * @date 2015年6月4日
 */
public class WechatPayUrlResp {

    /**
     * 状态码（ 0： 成功， -1： 失败）
     */
    private Integer status;
    /**
     * 状态码描述
     */
    private String message;
    /**
     * 商户网站唯一订单号
     */
    private String tradeNumber;
    /**
     * 二维码码串（ URL）
     */
    private String qrCode;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

}
