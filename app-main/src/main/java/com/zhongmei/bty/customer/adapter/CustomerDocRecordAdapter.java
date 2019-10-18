package com.zhongmei.bty.customer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.customer.bean.CustomerDocRecordResp;
import com.zhongmei.bty.basemodule.customer.bean.CustomerExpenseRecordResp;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.ui.H5WebViewActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CustomerDocRecordAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<CustomerDocRecordResp> mRecordList;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    private OnItemCheckListener mCheckedListener;

    View preCheckedView=null;

    public CustomerDocRecordAdapter(Context context, List<CustomerDocRecordResp> recordList) {
        this.mContext = context;
        this.mRecordList = recordList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setCheckedListener(OnItemCheckListener listener){
        this.mCheckedListener=listener;
    }

    @Override
    public int getCount() {
        if (mRecordList != null) {
            return mRecordList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mRecordList != null) {
            return mRecordList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.customer_doc_record_item, parent, false);
            viewHolder.itemView=(LinearLayout) convertView.findViewById(R.id.itemView);
            viewHolder.tv_createDate = (TextView) convertView.findViewById(R.id.tv_create_date);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.typeIcon = (ImageView) convertView.findViewById(R.id.typeIcon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mRecordList != null) {


            final CustomerDocRecordResp record = mRecordList.get(position);
            viewHolder.tv_createDate.setText(formatter.format(new Date(record.getServerCreateTime())));
            viewHolder.tv_title.setText(record.getTitle());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCheckedListener!=null){
                        mCheckedListener.onItemChecked(record);
                    }

                    v.setBackgroundColor(Color.parseColor("#F1F1F1"));
                    if(preCheckedView!=null){
                        preCheckedView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                    preCheckedView=v;
                }
            });
        }
        return convertView;
    }

    private void setupTextCenter(TextView view) {
        view.setPadding(0, 0, 0, 0);
        view.setGravity(Gravity.CENTER);
    }

    class ViewHolder {
        TextView tv_createDate, tv_title;
        ImageView typeIcon;
        LinearLayout itemView;
    }

    private String getPersonName(String userId) {
        String personName = "";
        personName = TextUtils.isEmpty(personName) ? userId : personName;

        return personName;
    }

    public interface OnItemCheckListener{
        public void onItemChecked(CustomerDocRecordResp docRecordItem);
    }

}
