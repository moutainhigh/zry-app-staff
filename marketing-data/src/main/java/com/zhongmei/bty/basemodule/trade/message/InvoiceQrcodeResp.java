package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.trade.entity.Invoice;



public class InvoiceQrcodeResp {
    private String qrcodeUrl;

    private Invoice invoice;

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
