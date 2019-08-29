package com.zhongmei.bty.customer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmei.beauty.operates.message.BeautyActivityBuyRecordResp;
import com.zhongmei.bty.commonmodule.util.DateUtil;
import com.zhongmei.yunfu.R;

import java.util.List;

/**
 * 微信小程序记录
 */
@SuppressLint("SimpleDateFormat")
public class CustomerWxAppRecordAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<BeautyActivityBuyRecordResp> mCouponList;

    public CustomerWxAppRecordAdapter(Context context, List<BeautyActivityBuyRecordResp> couponList) {
        this.mContext = context;
        this.mCouponList = couponList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (mCouponList != null) {
            return mCouponList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mCouponList != null) {
            return mCouponList.get(position);
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
            convertView = mInflater.inflate(R.layout.beauty_program_item, parent, false);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tv_use = (TextView) convertView.findViewById(R.id.tv_use);
            viewHolder.tv_projects=(TextView)convertView.findViewById(R.id.tv_projects);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mCouponList != null) {
            BeautyActivityBuyRecordResp item = mCouponList.get(position);
            viewHolder.tv_title.setText(item.marketingName);
            viewHolder.tv_projects.setText("可用项目:"+item.dishName);
            viewHolder.tv_content.setText("有效期:" + DateUtil.formatDate(item.validityPeriod));
            if (item.isUsed) {
                viewHolder.tv_use.setText(mContext.getString(R.string.beauty_used));
            } else {
                viewHolder.tv_use.setText(mContext.getString(R.string.beauty_not_used));
            }
        }

        return convertView;
    }

    class ViewHolder {
        TextView tv_title, tv_projects,tv_content, tv_use;
    }

}
