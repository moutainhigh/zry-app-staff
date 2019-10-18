package com.zhongmei.bty.mobilepay.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.bean.Invoice;
import com.zhongmei.bty.mobilepay.views.InvoiceView;
import com.zhongmei.yunfu.util.ViewFinder;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.bty.commonmodule.view.ExpandPlusListView;

import java.util.ArrayList;
import java.util.List;



public class InvoiceAdapter extends ExpandPlusListView.ExpandPlusListAdapter {

    public interface GroupCallback {
        void onDelete(Invoice invoice);

        void onCloseOpen(Invoice invoice);
    }

    private Context context;
    private ExpandPlusListView expandPlusListView;
    private InvoiceView invoiceView;
    private LayoutInflater inflater;
    private DataSetObserver outerDataSetObserver;
    private GroupCallback groupCallback;

    private List<Invoice> invoiceList;

    public InvoiceAdapter(Context context, ExpandPlusListView listView,
                          InvoiceView invoiceView, final DataSetObserver dataSetObserver) {
        this.context = context;
        this.expandPlusListView = listView;
        this.invoiceView = invoiceView;
        this.inflater = LayoutInflater.from(context);
        this.outerDataSetObserver = dataSetObserver;

        this.groupCallback = new GroupCallback() {
            @Override
            public void onDelete(Invoice invoice) {
                if (invoice != null) {
                    invoiceList.remove(invoice);
                    outerDataSetObserver.onChanged();
                }
            }

            @Override
            public void onCloseOpen(Invoice invoice) {
                int position = findGroupPosition(invoice);
                expandPlusListView.onGroupClick(expandPlusListView, null, position, 0);
            }
        };

        this.invoiceList = new ArrayList<>();
    }

    private int findGroupPosition(Invoice destInvoice) {
        int count = getGroupCount();
        for (int i = 0; i < count; i++) {
            Invoice invoice = (Invoice) getGroup(i);
            if (invoice.equals(destInvoice)) {
                return i;
            }
        }
        return -1;
    }

    public void addInvoice(Invoice invoice) {
        if (invoice != null) {
            this.invoiceList.add(invoice);
            notifyDataSetChanged();
        }
    }

    public void addInovice(List<Invoice> invoiceList) {
        if (invoiceList != null && !invoiceList.isEmpty()) {
            for (Invoice invoice : invoiceList) {
            }
            this.invoiceList.addAll(invoiceList);
            notifyDataSetChanged();
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (getGroupCount() == 1) {
            invoiceView.setVisibility(View.VISIBLE);
            invoiceView.setInvoice((Invoice) getGroup(0));
            expandPlusListView.setVisibility(View.GONE);

        } else {
            invoiceView.setVisibility(View.GONE);
            expandPlusListView.setVisibility(View.VISIBLE);

        }
    }

    public List<Invoice> getInvoiceList() {
        return invoiceList;
    }

    @Override
    public int getGroupCount() {
        return invoiceList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return invoiceList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return invoiceList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    @Override
    public View getGroupHeaderView(ViewGroup parent) {
        return inflater.inflate(R.layout.invoice_adapter_invoice_group, null);
    }

    @Override
    public void configureGroupHeaderView(View header, int groupPosition, int childPosition, int alpha) {
        GroupViewHolder groupViewHolder = new GroupViewHolder(context, groupCallback);
        groupViewHolder.initView(header);
        Invoice invoice = (Invoice) getGroup(groupPosition);
        groupViewHolder.bindData(invoice, groupPosition, getGroupCount());
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            groupViewHolder = new GroupViewHolder(context, groupCallback);
            convertView = ViewFinder.inflate(context, R.layout.invoice_adapter_invoice_group, null);
            groupViewHolder.initView(convertView);

            convertView.setTag(groupViewHolder);
        }
        groupViewHolder = (GroupViewHolder) convertView.getTag();

        Invoice invoice = (Invoice) getGroup(groupPosition);
        groupViewHolder.bindData(invoice, groupPosition, getGroupCount());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = ViewFinder.inflate(context, R.layout.invoice_adapter_invoice_child, null);
            childViewHolder = new ChildViewHolder();
            childViewHolder.initViews(convertView);

            convertView.setTag(childViewHolder);
        }
        childViewHolder = (ChildViewHolder) convertView.getTag();

        Invoice invoice = (Invoice) getChild(groupPosition, childPosition);
        childViewHolder.bindData(invoice);

        return convertView;
    }

    static class ChildViewHolder {
        InvoiceView invoiceView;

        void initViews(View view) {
            invoiceView = ViewFinder.findViewById(view, R.id.adapter_invoice_child_invoice);
        }

        void bindData(Invoice invoice) {
            invoiceView.setInvoice(invoice);
        }
    }

    static class GroupViewHolder implements View.OnClickListener {

        final Context context;
        final GroupCallback groupCallback;

        ImageView deleteIBtn;
        TextView nameTxv;
        TextView expandTxv;

        Invoice invoice;

        GroupViewHolder(Context context, GroupCallback groupCallback) {
            this.context = context;
            this.groupCallback = groupCallback;
        }

        void initView(View view) {
            deleteIBtn = ViewFinder.findViewById(view, R.id.adapter_invoice_group_delete);
            nameTxv = ViewFinder.findViewById(view, R.id.adapter_invoice_group_name);
            expandTxv = ViewFinder.findViewById(view, R.id.adapter_invoice_group_expand);

            expandTxv.setOnClickListener(this);
            deleteIBtn.setOnClickListener(this);
        }

        void bindData(Invoice invoice, int position, int groupCount) {
            if (invoice == null) {
                return;
            }
            this.invoice = invoice;

            String invoiceName = invoice.getInvoiceTaxRate().getInvoiceName();
            String invoiceAmount = invoice.getUnitPrice().toString();
            nameTxv.setText(context.getString(R.string.invoice_name_amount_format, invoiceName, ShopInfoCfg.formatCurrencySymbol(invoiceAmount)));

            if (groupCount <= 1) {
                deleteIBtn.setVisibility(View.INVISIBLE);
            } else {
                deleteIBtn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.adapter_invoice_group_delete) {
                if (groupCallback != null) {
                    groupCallback.onDelete(invoice);
                }
            } else if (v.getId() == R.id.adapter_invoice_group_expand) {
                if (groupCallback != null) {
                    groupCallback.onCloseOpen(invoice);
                }
            }
        }
    }
}
