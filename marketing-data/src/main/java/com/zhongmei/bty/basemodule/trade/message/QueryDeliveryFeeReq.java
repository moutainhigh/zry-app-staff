package com.zhongmei.bty.basemodule.trade.message;

/**
 * 查询配送费请求体
 */

public class QueryDeliveryFeeReq {
    //品牌Id
    private Long brandId;

    //商户Id
    private Long shopId;

    //订单Id
    private Long tradeId;

    //订单号
    private String tradeNo;

    //第三方订单号
    private String thirdTranNo;

    //配送平台
    private Integer deliveryPlatform;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getThirdTranNo() {
        return thirdTranNo;
    }

    public void setThirdTranNo(String thirdTranNo) {
        this.thirdTranNo = thirdTranNo;
    }

    public Integer getDeliveryPlatform() {
        return deliveryPlatform;
    }

    public void setDeliveryPlatform(Integer deliveryPlatform) {
        this.deliveryPlatform = deliveryPlatform;
    }
}
