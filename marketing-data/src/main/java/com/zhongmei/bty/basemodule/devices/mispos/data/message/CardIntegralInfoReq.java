package com.zhongmei.bty.basemodule.devices.mispos.data.message;

/**
 * 实体卡积分记录请求
 */
public class CardIntegralInfoReq {
    private String entityCardNo;

    private Integer pageSize;

    private Long userId;

    private Integer source;


    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getEntityCardNo() {
        return entityCardNo;
    }

    public void setEntityCardNo(String entityCardNo) {
        this.entityCardNo = entityCardNo;
    }

}
