package com.zhongmei.bty.mobilepay.event;

import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.PayModelGroup;

/**
 * 发生编辑金额事件
 */
public class AmountEditedEvent {

    private PayModelGroup payModelType;// 支付方式分组id
    // private String payTypeName;// 支付方式名称
    private int payMethodId;// 支付方式菜单id

    private String methodName;// 支付方式菜单名称

    private double amountValue = 0.0f;// 支付金额

    private BusinessType businessType;//单据类型

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }


    public AmountEditedEvent(PayModelGroup type, String methodName) {
        this.setPayModelType(type);
        this.methodName = methodName;

    }

   /* public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }*/

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public double getAmountValue() {
        return amountValue;
    }

    public void setAmountValue(double amountValue) {
        this.amountValue = amountValue;
    }

    public PayModelGroup getPayModelType() {
        return payModelType;
    }

    public void setPayModelType(PayModelGroup payModelType) {
        this.payModelType = payModelType;
    }

    public int getPayMethodId() {
        return payMethodId;
    }

    public void setPayMethodId(int payMethodId) {
        this.payMethodId = payMethodId;
    }
}
