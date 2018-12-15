package com.zhongmei.bty.basemodule.shopmanager.paymentmanager.entitys;

import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.PayModeId;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Date： 2016/7/20
 * @Description:收支管理科目下的支付信息
 * @Version: 1.0
 */
public class PaymentsPayInfo implements Serializable {

    private Long payModeId;

    private BigDecimal mAmount;

    public PayModeId getPayModeId() {
        return ValueEnums.toEnum(PayModeId.class, payModeId);
    }

    public void setPayModeId(PayModeId payModeId) {
        this.payModeId = ValueEnums.toValue(payModeId);
    }

    public BigDecimal getmAmount() {
        return mAmount;
    }

    public void setmAmount(BigDecimal mAmount) {
        this.mAmount = mAmount;
    }
}
