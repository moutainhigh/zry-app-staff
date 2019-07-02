package com.zhongmei.bty.basemodule.customer.bean;

import com.zhongmei.yunfu.ShopInfoManager;

/**
 * Created by dingzb on 2019/6/25.
 */

public class TaskQueryReq {

    private int pageNo = 0;
    private int  pageSize = 100;
    private String title;
    private Long remindTime;
    private int type;
    private int status;
    private Long customerId;
    private Long userId;
    private Long shopIdenty;
    private Long brandIdenty;

    public TaskQueryReq(){
        shopIdenty= ShopInfoManager.getInstance().getShopInfo().getShopId();
        brandIdenty = ShopInfoManager.getInstance().getShopInfo().getBrandId();
    }


    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Long remindTime) {
        this.remindTime = remindTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }
}
