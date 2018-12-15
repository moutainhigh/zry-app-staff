package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

import java.util.List;

public class MemberCreditListReq extends BaseRequest {

    /**
     * 必填
     */
    private Long customerId;

    /**
     * 操作类型 1：挂账，2：销帐,3：挂账反结账，4：销帐反结账
     */
    private List<Integer> opType;

    private Long beginTime;

    private Long endTime;

    /**
     * 当前页（默认1）
     */
    private Integer currentPage;

    /**
     * 每页显示数据条数（默认10）
     */
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
