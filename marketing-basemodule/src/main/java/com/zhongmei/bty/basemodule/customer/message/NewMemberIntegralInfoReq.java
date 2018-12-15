package com.zhongmei.bty.basemodule.customer.message;


import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

public class NewMemberIntegralInfoReq extends BaseRequest {

    private Long customerId;

    private Integer pageSize;

    private Integer currentPage;

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }
}
