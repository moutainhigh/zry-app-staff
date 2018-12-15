package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

/**
 * 验证会员密码
 * 2.customerId和mobile不能同时为空，若customerId和mobile都有值，以cusotmerId为准
 */
public class MemberVerifyPwdReq extends BaseRequest {

    private Integer appType = 1;
    /**
     * 操作人ID
     * 必
     */
    private Long userId;

    /**
     * 手机号
     * 必填
     */
    private String mobile;

    /**
     * 必填
     */
    private Long customerId;

    /**
     * 会员密码（明文6位数字或者MD5）或者 免密token
     * 必填
     */
    private String password;

    /**
     * 实体卡号
     * 不是必填
     */
    private String entityCardNo;

    /**
     * 密码类型，1-明文密码，2-MD5密码，3-免密码临时token
     * 非必填，默认值1
     */
    private Integer type;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEntityCardNo(String entityCardNo) {
        this.entityCardNo = entityCardNo;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "?appType=" + appType
                + "&brandId=" + brandIdenty
                + "&commercialId=" + shopIdenty
                + "&userId=" + userId
                + "&mobile=" + mobile
                + "&customerId=" + customerId
                + "&password=" + password
                + "&entityCardNo=" + entityCardNo
                + "&type=" + type;
    }
}
