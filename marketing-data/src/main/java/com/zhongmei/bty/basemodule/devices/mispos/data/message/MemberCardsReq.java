package com.zhongmei.bty.basemodule.devices.mispos.data.message;

/**
 * Created by demo on 2018/12/15
 */

public class MemberCardsReq {
    private Long brandId;//品牌ID
    private Long commercialId;//门店ID
    private String clientType;//客户端请求来源
    private Long customerId;//顾客id
    private int currentPage;//当前也，非必需
    private int pageSize;//每页显示的条数

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(Long commercialId) {
        this.commercialId = commercialId;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
