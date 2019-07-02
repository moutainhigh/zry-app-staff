package com.zhongmei.bty.basemodule.customer.bean;


import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;

/**
 * 添加会员档案请求参数
 */
public class TaskCreateOrEditReq {
    private Long taskId;
    private Long  customerId;
    private String customerName;
    private String customerMobile;
    private int type=1;
    private String title;
    private String content;
    private Long creatorId; //操作者Id
    private String creatorName; //操作者名称
    private Long updatorId;
    private String updatorName;
    private Long clientCreateTime;
    private Long clientUpdateTime;
    private Long shopIdenty;
    private Long brandIdenty;
    private Long remindTime;
    private Integer status;
    private Long customerDocId;
    private String taskResult;
    private Long userId;
    private String userName;


    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public Long getClientCreateTime() {
        return clientCreateTime;
    }

    public void setClientCreateTime(Long clientCreateTime) {
        this.clientCreateTime = clientCreateTime;
    }

    public Long getClientUpdateTime() {
        return clientUpdateTime;
    }

    public void setClientUpdateTime(Long clientUpdateTime) {
        this.clientUpdateTime = clientUpdateTime;
    }

    public Long getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Long remindTime) {
        this.remindTime = remindTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCustomerDocId() {
        return customerDocId;
    }

    public void setCustomerDocId(Long customerDocId) {
        this.customerDocId = customerDocId;
    }

    public String getTaskResult() {
        return taskResult;
    }

    public void setTaskResult(String taskResult) {
        this.taskResult = taskResult;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void validateCreate() {
        setBrandIdenty(ShopInfoManager.getInstance().getShopInfo().getBrandId());
        setShopIdenty(ShopInfoManager.getInstance().getShopInfo().getShopId());
        setCreatorId(ShopInfoCfg.getInstance().authUser.getId());
        setCreatorName(ShopInfoCfg.getInstance().authUser.getName());
        setClientCreateTime(System.currentTimeMillis());
    }

    public void validateUpdate() {
        setClientUpdateTime(System.currentTimeMillis());
        setUpdatorId(ShopInfoCfg.getInstance().authUser.getId());
        setUpdatorName(ShopInfoCfg.getInstance().authUser.getName());
    }
}
