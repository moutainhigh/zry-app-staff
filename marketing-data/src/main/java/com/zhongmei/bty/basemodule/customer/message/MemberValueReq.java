package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.pay.message.PaymentItemReq;

public class MemberValueReq extends PaymentItemReq {
    private String consumePassword;//会员密码
    private Long customerId;//会员账号ID

    public MemberValueReq(PaymentItem paymentItem) {
        super(paymentItem);
        customerId = paymentItem.getPaymentItemExtra().getCustomerId();
        consumePassword = paymentItem.getConsumePassword();
    }

    public String getConsumePassword() {
        return consumePassword;
    }

    public void setConsumePassword(String consumePassword) {
        this.consumePassword = consumePassword;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
