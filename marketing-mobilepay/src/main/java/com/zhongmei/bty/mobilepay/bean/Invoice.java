package com.zhongmei.bty.mobilepay.bean;

import android.database.DataSetObservable;
import android.database.DataSetObserver;

import com.zhongmei.bty.basemodule.pay.entity.InvoiceTaxRate;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.bty.basemodule.pay.bean.ElectronicInvoiceVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import java.math.BigDecimal;
import java.util.List;



public class Invoice {

    private DataSetObservable dataSetObservable = new DataSetObservable();

    private final TradeVo tradeVo;
    private final ElectronicInvoiceVo electronicInvoiceVo;

    private InvoiceTaxRate invoiceTaxRate;
        private BigDecimal unitPrice;

    private int count = 1;

    public Invoice(TradeVo tradeVo, ElectronicInvoiceVo electronicInvoiceVo, BigDecimal actualAmount) {
        this.tradeVo = tradeVo;
        this.electronicInvoiceVo = electronicInvoiceVo;

        List<InvoiceTaxRate> invoiceTaxRates = electronicInvoiceVo.getInvoiceTaxRates();
        InvoiceTaxRate selectedInvoiceTaxRate = invoiceTaxRates.get(0);
        for (InvoiceTaxRate invoiceTaxRate : invoiceTaxRates) {
            if (invoiceTaxRate.getIsDefault() == Bool.YES) {
                selectedInvoiceTaxRate = invoiceTaxRate;
            }
        }

        this.invoiceTaxRate = selectedInvoiceTaxRate;
        this.unitPrice = actualAmount == null ? tradeVo.getTrade().getTradeAmount() : actualAmount;
    }

    public TradeVo getTradeVo() {
        return tradeVo;
    }

    public ElectronicInvoiceVo getElectronicInvoiceVo() {
        return electronicInvoiceVo;
    }

    public InvoiceTaxRate getInvoiceTaxRate() {
        return invoiceTaxRate;
    }

    public void setInvoiceTaxRate(InvoiceTaxRate invoiceTaxRate) {
        this.invoiceTaxRate = invoiceTaxRate;
        notifyDataChanged();
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        notifyDataChanged();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public BigDecimal getAmount() {
        return unitPrice.multiply(BigDecimal.valueOf(count));
    }

    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        if (dataSetObserver != null) {
            dataSetObservable.registerObserver(dataSetObserver);
        }
    }

    public void unRegisterDataSetObserver(DataSetObserver dataSetObserver) {
        dataSetObservable.unregisterObserver(dataSetObserver);
    }

    public void notifyDataChanged() {
        dataSetObservable.notifyChanged();
    }
}
