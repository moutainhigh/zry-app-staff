package com.zhongmei.bty.mobilepay.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.basemodule.pay.entity.InvoiceTaxRate;
import com.zhongmei.bty.mobilepay.bean.Invoice;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.bty.mobilepay.popup.InvoicePopupHelper;
import com.zhongmei.yunfu.context.util.ArgsUtils;
import com.zhongmei.yunfu.util.MathDecimal;

import java.math.BigDecimal;
import java.util.List;

/**
 * 主要是用户编辑、展示电子发票的视图(与具体业务有关，该视图不具有通用性)
 * Created by demo on 2018/12/15
 */

public class InvoiceView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = InvoiceView.class.getSimpleName();

    private TextView nameTxv;
    private TextView unitTxv;
    private TextView modelTxv;
    private TextView numberTxv;
    private TextView taxRateTxv;
    private TextView countTxv;
    private EditText unitPriceTxv;

    private Invoice invoice;

    private boolean isEdited;

    public InvoiceView(Context context) {
        this(context, null);
    }

    public InvoiceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InvoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.invoice_view_invoice, this);
        nameTxv = (TextView) findViewById(R.id.view_invoice_product_name);
        unitTxv = (TextView) findViewById(R.id.view_invoice_product_unit);
        modelTxv = (TextView) findViewById(R.id.view_invoice_product_model);
        numberTxv = (TextView) findViewById(R.id.view_invoice_product_number);
        taxRateTxv = (TextView) findViewById(R.id.view_invoice_product_tax_rate);
        countTxv = (TextView) findViewById(R.id.view_invoice_product_count);
        unitPriceTxv = (EditText) findViewById(R.id.view_invoice_product_unit_price);

        nameTxv.setOnClickListener(this);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        private String textBeforeTmp = "";

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            textBeforeTmp = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            isEdited = true;
            String matchStr = "^" + ShopInfoCfg.getInstance().getCurrencySymbol() + "([0-9]*)?+(.[0-9]{0,2})?$";
            if (matchStr.startsWith("^$")) {
                matchStr = "^\\$([0-9]*)?+(.[0-9]{0,2})?$";
            }
            String etText = s.toString();

            if (TextUtils.isEmpty(etText) || !etText.matches(matchStr)) {
                etText = textBeforeTmp;
                setChangedPrice(etText);
                unitPriceTxv.setText(textBeforeTmp);
            } else {
                setChangedPrice(etText);
            }
            unitPriceTxv.setSelection(unitPriceTxv.getText().length());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        private void setChangedPrice(String text) {
            if (invoice != null) {
                try {
                    text = text.replace(ShopInfoCfg.getInstance().getCurrencySymbol(), "0");
                    text.replaceAll(" ", "");
                    double result = Double.parseDouble(text);
                    invoice.setUnitPrice(BigDecimal.valueOf(result));
                } catch (NumberFormatException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
    };

    public void setInvoice(Invoice invoice) {
        ArgsUtils.notNull(invoice, "Invoice");
        this.invoice = invoice;
        unitPriceTxv.removeTextChangedListener(textWatcher);
        update();
        unitPriceTxv.addTextChangedListener(textWatcher);
    }

    private void update() {
        if (invoice == null) {
            return;
        }

        String taxRateName = null;
        String productUnit = null;
        String productModel = null;
        String productNumber = null;
        String taxRate = null;
        String count = null;

        InvoiceTaxRate invoiceTaxRate = invoice.getInvoiceTaxRate();
        if (invoiceTaxRate != null) {
            taxRateName = invoiceTaxRate.getInvoiceName();
            BigDecimal taxRateL = invoiceTaxRate.getTaxRate();
            taxRate = taxRateL == null ? "" : taxRateL.toString();
            productNumber = invoiceTaxRate.getInvoiceCode();
        }
        count = invoice.getCount() + "";

        nameTxv.setText(ArgsUtils.returnIfEmpty(taxRateName, "--"));
        unitTxv.setText(ArgsUtils.returnIfEmpty(productUnit, "--"));
        modelTxv.setText(ArgsUtils.returnIfEmpty(productModel, "--"));
        numberTxv.setText(ArgsUtils.returnIfEmpty(productNumber, "--"));
        taxRateTxv.setText(ArgsUtils.returnIfEmpty(taxRate, "--"));
        countTxv.setText(ArgsUtils.returnIfEmpty(count, "1"));

        if (!isEdited) {
            unitPriceTxv.setText(ShopInfoCfg.formatCurrencySymbol(
                    ArgsUtils.returnIfEmpty(MathDecimal.toDecimalFormatString(invoice.getUnitPrice()), "0.00")));
        }

        List<InvoiceTaxRate> taxRates = invoice.getElectronicInvoiceVo().getInvoiceTaxRates();
        Drawable[] drawables = nameTxv.getCompoundDrawables();
        if (taxRates.size() > 1) {
            nameTxv.setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1],
                    getResources().getDrawable(R.drawable.ic_invoice_pull_label), drawables[3]);
        } else {
            nameTxv.setCompoundDrawables(drawables[0], drawables[1], null, drawables[3]);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.view_invoice_product_name) {
            pickInvoiceTaxRate();
        }
    }

    private void pickInvoiceTaxRate() {
        if (invoice == null) {
            return;
        }
        List<InvoiceTaxRate> taxRates = invoice.getElectronicInvoiceVo().getInvoiceTaxRates();
        if (taxRates.size() > 1) {
            InvoicePopupHelper.showInvoiceItems(getContext(), invoice, nameTxv,
                    new InvoicePopupHelper.OnSelectedListener() {
                        @Override
                        public void onSelected(InvoiceTaxRate invoiceTaxRate) {
                            invoice.setInvoiceTaxRate(invoiceTaxRate);
                            update();
                        }
                    });
        }
    }

    public Invoice getInvoice() {
        return invoice;
    }

}