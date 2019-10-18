package com.zhongmei.bty.basemodule.reportcenter.message;

import java.util.List;


public class GoodsSellRankPrintReq {
    private String startDate;
    private String endDate;
    private Integer saleType;
    private List<Integer> source;
    private List<Integer> deliveryTypes;
    private Integer limitNo;
    private Long majorCategoryId;
    private Long categoryId;
    private Long cashierTicketId;     private Long shopId;
    private Long brandId;
    private Integer hasContainType;     private Integer hasContainMajorType;     private Integer queryType = 1;
    public Integer getHasContainType() {
        return hasContainType;
    }

    public void setHasContainType(Integer hasContainType) {
        this.hasContainType = hasContainType;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCashierTicketId() {
        return cashierTicketId;
    }

    public void setCashierTicketId(Long cashierTicketId) {
        this.cashierTicketId = cashierTicketId;
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

    public Integer getSaleType() {
        return saleType;
    }

    public void setSaleType(Integer saleType) {
        this.saleType = saleType;
    }

    public List<Integer> getSource() {
        return source;
    }

    public void setSource(List<Integer> source) {
        this.source = source;
    }

    public List<Integer> getDeliveryTypes() {
        return deliveryTypes;
    }

    public void setDeliveryTypes(List<Integer> deliveryTypes) {
        this.deliveryTypes = deliveryTypes;
    }

    public Integer getLimitNo() {
        return limitNo;
    }

    public void setLimitNo(Integer limitNo) {
        this.limitNo = limitNo;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getMajorCategoryId() {
        return majorCategoryId;
    }

    public void setMajorCategoryId(Long majorCategoryId) {
        this.majorCategoryId = majorCategoryId;
    }

    public Integer getHasContainMajorType() {
        return hasContainMajorType;
    }

    public void setHasContainMajorType(Integer hasContainMajorType) {
        this.hasContainMajorType = hasContainMajorType;
    }

    public void setQueryType(Integer queryType) {
        this.queryType = queryType;
    }

    public Integer getQueryType() {
        return queryType;
    }
}
