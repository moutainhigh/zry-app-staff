package com.zhongmei.bty.basemodule.shopmanager.paymentmanager.operators;

/**
 * @Date： 2016/7/21
 * @Description:收支详情请求对象
 * @Version: 1.0
 */
public class PaymentsDetailReq {

    private Long brandIdenty;

    private Long shopIdenty;

    private String startDate;//格式yyyy-mm-dd

    private String endDate;//格式yyyy-mm-dd

    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
