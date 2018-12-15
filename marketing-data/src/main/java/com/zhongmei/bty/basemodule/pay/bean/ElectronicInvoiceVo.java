package com.zhongmei.bty.basemodule.pay.bean;

import com.zhongmei.bty.basemodule.pay.entity.ElectronicInvoice;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.pay.entity.InvoiceTaxRate;

import java.util.List;

/**
 * 电子发票设置数据
 */

public class ElectronicInvoiceVo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private boolean mIsSwitchOn;

    private ElectronicInvoice mElectronicInvoice;

    private List<InvoiceTaxRate> mInvoiceTaxRates;

    public boolean isSwitchOn() {
        return mIsSwitchOn;
    }

    public void setSwitchOn(boolean switchOn) {
        mIsSwitchOn = switchOn;
    }

    public ElectronicInvoice getElectronicInvoice() {
        return mElectronicInvoice;
    }

    public void setElectronicInvoice(ElectronicInvoice electronicInvoice) {
        mElectronicInvoice = electronicInvoice;
    }

    public List<InvoiceTaxRate> getInvoiceTaxRates() {
        return mInvoiceTaxRates;
    }

    public void setInvoiceTaxRates(List<InvoiceTaxRate> invoiceTaxRates) {
        mInvoiceTaxRates = invoiceTaxRates;
    }

    public boolean isOpen() {
        return mIsSwitchOn && mElectronicInvoice != null && Utils.isNotEmpty(mInvoiceTaxRates);
    }
}
