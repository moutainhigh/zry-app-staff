package com.zhongmei.bty.basemodule.customer.message;

/**
 * 查询支付信息和会员信息请求数据
 *
 * @version: 1.0
 * @date 2015年5月13日
 */
public class PaymentAndMemberReq {
    private String paymentUuid;//支付uuid
    private long userId;//操作员id
    private long memberId;//会员id

    public String getPaymentUuid() {
        return paymentUuid;
    }

    public void setPaymentUuid(String paymentUuid) {
        this.paymentUuid = paymentUuid;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

}
