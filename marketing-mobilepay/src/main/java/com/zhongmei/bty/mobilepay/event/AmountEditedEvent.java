package com.zhongmei.bty.mobilepay.event;

import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.PayModelGroup;


public class AmountEditedEvent {

    private PayModelGroup payModelType;        private int payMethodId;
    private String methodName;
    private double amountValue = 0.0f;
    private BusinessType businessType;
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
