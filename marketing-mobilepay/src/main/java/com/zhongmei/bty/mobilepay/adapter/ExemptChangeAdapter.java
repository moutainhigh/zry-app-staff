package com.zhongmei.bty.mobilepay.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.popup.ErasePopupHelper;

import com.zhongmei.bty.basemodule.commonbusiness.adapter.BaseListAdapter;

public class ExemptChangeAdapter extends
        BaseListAdapter<ExemptChangeAdapter.ViewHolder, ErasePopupHelper.EraseItem> {

    public ExemptChangeAdapter(Context context) {

        super(context, R.layout.pay_exempt_change_list_item);
    }

    static class ViewHolder {
        TextView changename;
        RelativeLayout itemBg;
    }

    @Override
    public ViewHolder initViewHodler(View convertView) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.changename = (TextView) convertView.findViewById(R.id.name);
        viewHolder.itemBg = (RelativeLayout) convertView.findViewById(R.id.item_bg);
        return viewHolder;
    }

    @Override
    public void setViewHodler(ViewHolder viewHolder, int position) {
        ErasePopupHelper.EraseItem item = getItem(position);
        viewHolder.changename.setText(item.name);
        if (item.isSelected) {
            viewHolder.itemBg.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.pay_erase_selected_bg));
            viewHolder.changename.setTextColor(mContext.getResources().getColor(R.color.text_white));


        } else {
            viewHolder.itemBg.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.pay_erase_item_selector));
            viewHolder.changename.setTextColor(mContext.getResources().getColor(R.color.text_black));
        }
    }
}
