package com.zhongmei.bty.customer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.devices.mispos.data.bean.CustomerSaleCardInfo;
import com.zhongmei.bty.customer.bean.CustomerOrdercenterDetailCommonBean;
import com.zhongmei.yunfu.R;

import java.util.List;


public class CustomerOrdercenterDetaillAdapter extends BaseAdapter {

    private Context mContext;

    private List<?> dataList;

    private Class beanClass;

    private boolean showCheckbox = false;
    public CustomerOrdercenterDetaillAdapter(Context context, List<?> dataList, Class beanClass) {
        mContext = context;
        this.dataList = dataList;
        this.beanClass = beanClass;
    }

    @Override
    public int getCount() {
        if (dataList == null)
            return 0;
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            if (beanClass == CustomerOrdercenterDetailCommonBean.class) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.customer_ordercenter_detail_listitem2, null);
                ViewHolderCommon viewHolder = new ViewHolderCommon();
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                viewHolder.value = (TextView) convertView.findViewById(R.id.value);
                convertView.setTag(viewHolder);
            }
        }

        if (beanClass == CustomerOrdercenterDetailCommonBean.class) {
            ViewHolderCommon viewHolder = (ViewHolderCommon) convertView.getTag();
            CustomerOrdercenterDetailCommonBean customerOrdercenterDetailCommonBean = (CustomerOrdercenterDetailCommonBean) dataList.get(position);
            viewHolder.name.setText(customerOrdercenterDetailCommonBean.getName());
            viewHolder.value.setText(customerOrdercenterDetailCommonBean.getValue());
        }
        return convertView;
    }

    public void resetData(List<?> dataList, Class beanClass) {
        this.dataList = dataList;
        this.beanClass = beanClass;
        notifyDataSetChanged();
    }

    private class ViewHolderCardList {
        ImageView checkIv;
        TextView serialNumber;
        TextView cardNumber;
        TextView cardCategory;
        TextView cardStatus;
        TextView sellPrice;
        TextView dealStatus;
    }

    private class ViewHolderCommon {
        TextView name;
        TextView value;

    }

    public void showCheckBox(boolean show) {
        this.showCheckbox = show;
        notifyDataSetChanged();
    }


    public void checkAll(boolean check) {

                notifyDataSetChanged();

    }


    private void updateCheckHeadBar() {
        Activity activity = (Activity) mContext;
        CheckBox allCb = (CheckBox) activity.findViewById(R.id.allcheck_cb);
                boolean isAllSelect = false;

            }

    private void resetSelectCount(int count) {
        Activity activity = (Activity) mContext;
        TextView checkNumberTv = (TextView) activity.findViewById(R.id.check_number_tv);
        checkNumberTv.setText(mContext.getString(R.string.customer_ordercenter_detail_checktip, count + ""));
    }


    public List<CustomerSaleCardInfo> getSelectItems() {

        return null;
    }


    public boolean isSelectAll() {
                boolean isAllSelect = false;

        return isAllSelect;
    }


    public byte getCanRefundNumber() {

        byte number = 0;

        return number;

    }


    public CustomerSaleCardInfo getRefundSingleCard() {


        return null;
    }


    public boolean isRefundAll(List<CustomerSaleCardInfo> selectedCards) {
        if (selectedCards.size() < dataList.size()) {
            return false;
        } else {
            return true;
        }
    }


    public void resetSelectStatus() {

    }
}
