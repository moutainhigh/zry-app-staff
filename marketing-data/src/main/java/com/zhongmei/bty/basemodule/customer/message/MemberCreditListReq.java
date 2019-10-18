package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

import java.util.List;

public class MemberCreditListReq extends BaseRequest {


    private Long customerId;


    private List<Integer> opType;

    private Long beginTime;

    private Long endTime;


    private Integer currentPage;


    private Integer pageSize;

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setOpType(List<Integer> opType) {
        this.opType = opType;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
