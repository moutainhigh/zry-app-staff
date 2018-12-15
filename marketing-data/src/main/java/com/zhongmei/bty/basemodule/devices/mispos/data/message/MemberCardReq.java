package com.zhongmei.bty.basemodule.devices.mispos.data.message;

/**
 * 会员实体卡请求
 */
public class MemberCardReq {

    /**
     * 创建人
     */
    private Long createId;

    private Long customerId;

    /**
     * 手机
     */
    private String mobile;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }


}
