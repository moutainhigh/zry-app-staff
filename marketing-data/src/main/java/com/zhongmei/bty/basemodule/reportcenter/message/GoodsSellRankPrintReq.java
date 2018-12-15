package com.zhongmei.bty.basemodule.reportcenter.message;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
public class GoodsSellRankPrintReq {
    private String startDate;
    private String endDate;
    private Integer saleType;
    private List<Integer> source;
    private List<Integer> deliveryTypes;
    private Integer limitNo;
    private Long majorCategoryId;
    private Long categoryId;
    private Long cashierTicketId; //收银点id,所有收银点传入null,没有收银点传入－1
    private Long shopId;
    private Long brandId;
    private Integer hasContainType; //是否查询中类 1 查询
    private Integer hasContainMajorType; //是否查询大类 1查询
    private Integer queryType = 1;//1 营业日  2自然日(默认营业日查询)

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
