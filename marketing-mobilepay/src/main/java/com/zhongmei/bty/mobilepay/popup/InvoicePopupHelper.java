package com.zhongmei.bty.mobilepay.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.bty.basemodule.pay.entity.InvoiceTaxRate;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.mobilepay.bean.Invoice;
import com.zhongmei.bty.basemodule.pay.bean.ElectronicInvoiceVo;

import java.util.ArrayList;
import java.util.List;



public class InvoicePopupHelper {



    public interface OnSelectedListener {

        void onSelected(InvoiceTaxRate invoiceTaxRate);
    }


    public static void showInvoiceItems(final Context context,
                                        final Invoice invoice,
                                        View anchorView,
                                        final OnSelectedListener onSelectedListener) {
        ElectronicInvoiceVo electronicInvoiceVo;
        List<InvoiceTaxRate> invoiceTaxRates;
        if (invoice == null || (electronicInvoiceVo = invoice.getElectronicInvoiceVo()) == null
                || (invoiceTaxRates = electronicInvoiceVo.getInvoiceTaxRates()) == null
                || invoiceTaxRates.isEmpty() || anchorView == null) {
            return;
        }
        ListView listView = new ListView(context);
        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        listView.setPadding(10, 10, 10, 10);
        listView.setDivider(new ColorDrawable(Color.parseColor("#DBE0E6")));
        listView.setDividerHeight(1);
        listView.setFadingEdgeLength(0);
        listView.setCacheColorHint(Color.parseColor("#00000000"));
        listView.setVerticalScrollBarEnabled(false);

        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        if (invoiceTaxRates.size() > 3) {
            height = DensityUtil.dip2px(context, 40) * 5 + 20;
        }

        final PopupWindow popupWindow = new PopupWindow(listView,
                DensityUtil.dip2px(context, 180), height);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_shadow_circular));
        final List<InvoiceTaxRate> finalInvoiceTaxRates = invoiceTaxRates;
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return finalInvoiceTaxRates.size();
            }

            @Override
            public Object getItem(int position) {
                return finalInvoiceTaxRates.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                TextView textView;
                if (convertView == null) {
                    textView = new TextView(context);
                    textView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                            DensityUtil.dip2px(context, 40)));
                    textView.setTextSize(DensityUtil.px2sp(context, 18));
                    textView.setBackgroundResource(R.drawable.selector_invoice_tax_bg);
                    textView.setTextColor(context.getResources().getColorStateList(R.color.selector_invoice_tax_text_color));
                    textView.setGravity(Gravity.CENTER);
                    textView.setClickable(true);

                    convertView = textView;
                }
                textView = (TextView) convertView;

                final InvoiceTaxRate invoiceTaxRate = finalInvoiceTaxRates.get(position);

                textView.setText(invoiceTaxRate.getInvoiceName());
                if (invoiceTaxRate.equals(invoice.getInvoiceTaxRate())) {
                    textView.setSelected(true);
                } else {
                    textView.setSelected(false);
                }
                textView.setSelected(true);

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSelectedListener != null) {
                            onSelectedListener.onSelected(invoiceTaxRate);
                        }
                        popupWindow.dismiss();
                    }
                });

                return convertView;
            }
        });

        popupWindow.showAsDropDown(anchorView,
                (anchorView.getWidth() - popupWindow.getWidth()) / 2,
                0);
    }

        public interface OnPaymentItemCallback {
        boolean onDelete(PaymentItem paymentItem);
    }



    public static void showPaymentItemList(final Context context, final IPaymentInfo paymentInfo, List<PaymentItem> paymentItems,
                                           View anchorView,
                                           final OnPaymentItemCallback onPaymentItemCallback, final PopupWindow.OnDismissListener listener) {
        if (paymentItems == null || paymentItems.isEmpty() || anchorView == null) {
            return;
        }
        final List<PaymentItem> innerPaymentItems = new ArrayList<>(paymentItems);
        ListView listView = new ListView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        listView.setLayoutParams(params);
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setSelector(android.R.color.transparent);
        listView.setPadding(10, 0, 10, 0);
        listView.setDivider(context.getResources().getDrawable(R.drawable.shape_pay_ment_list_divider));
        listView.setBackgroundColor(Color.WHITE);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.pay_ment_item);
        listView.setLayoutAnimation(new LayoutAnimationController(animation, 0.05f));
        final PopupWindow popupWindow = new PopupWindow(listView,
                DensityUtil.dip2px(context, 220), ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_shadow_circular));
        popupWindow.setOnDismissListener(listener);

        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return innerPaymentItems.size();
            }

            @Override
            public Object getItem(int position) {
                return innerPaymentItems.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder;
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = View.inflate(context, R.layout.pay_adapter_payment_item, null);
                    viewHolder.nameTxv = (TextView) convertView.findViewById(R.id.adapter_pay_ment_item_name);
                    viewHolder.deleteImg = (ImageView) convertView.findViewById(R.id.adapter_pay_ment_item_delete);
                    viewHolder.privilegeTxv = (TextView) convertView.findViewById(R.id.adapter_pay_ment_item_privilege);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                final PaymentItem paymentItem = (PaymentItem) getItem(position);
                if (paymentItem != null) {
                    String modeName = paymentItem.getPayModeName();
                                        if (paymentInfo.getTradeBusinessType() == BusinessType.BUFFET && paymentInfo.getTradeVo().getTradeDepositPaymentItem() != null) {
                        if (paymentItem.equals(paymentInfo.getTradeVo().getTradeDepositPaymentItem())) {
                            modeName = modeName + "(" + context.getString(R.string.pay_deposit_text) + ")";
                        }
                    }
                    viewHolder.nameTxv.setText(modeName + " " + ShopInfoCfg.formatCurrencySymbol(" " + paymentItem.getUsefulAmount()));
                                        if (PayModeId.MEITUAN_FASTPAY.value().equals(paymentItem.getPayModeId())) {
                        double youhuiAmount = paymentItem.getUsefulAmount().subtract(paymentItem.getFaceAmount()).doubleValue();
                        StringBuilder stringBuild = new StringBuilder();
                        stringBuild.append(context.getString(R.string.pay_paid_privilege));
                        stringBuild.append(CashInfoManager.formatCash(youhuiAmount));
                        viewHolder.privilegeTxv.setText(stringBuild.toString());
                        viewHolder.privilegeTxv.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.privilegeTxv.setVisibility(View.GONE);
                    }
                    viewHolder.deleteImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onPaymentItemCallback != null) {
                                if (onPaymentItemCallback.onDelete(paymentItem)) {

                                    if (innerPaymentItems.isEmpty()) {
                                        popupWindow.dismiss();
                                    }
                                }
                            }
                        }
                    });
                }

                return convertView;
            }

            class ViewHolder {
                TextView nameTxv;
                TextView privilegeTxv;
                ImageView deleteImg;
            }
        });

        popupWindow.showAsDropDown(anchorView,
                (anchorView.getWidth() - popupWindow.getWidth()) / 2,
                0);
    }
}
